package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectDto;
import com.example.issuetracker_server.dto.project.ProjectRequestDto;
import com.example.issuetracker_server.service.member.MemberServiceImpl;
import com.example.issuetracker_server.service.memberproject.MemberProjectServiceImpl;
import com.example.issuetracker_server.service.project.ProjectServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.issuetracker_server.domain.memberproject.Role.DEV;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectServiceImpl projectsService;

    @MockBean
    private MemberProjectServiceImpl memberProjectService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberServiceImpl memberService;

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private ProjectController projectController;

    private ProjectRequestDto requestDto;

    String url = "http://localhost:" + port + "/project";

    @BeforeEach
    public void setUp() {
        String title = "project1";
        List<ProjectRequestDto.Member> members = new ArrayList<>();

        members.add(new ProjectRequestDto.Member("user1", Role.PL));
        members.add(new ProjectRequestDto.Member("user2", DEV));
        members.add(new ProjectRequestDto.Member("user3", Role.TESTER));

        requestDto = new ProjectRequestDto();
        requestDto.setMembers(members);
        requestDto.setTitle(title);
        MockitoAnnotations.openMocks(this);


    }

    @Test
    public void saveProject_Success() throws Exception {
        // given
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(projectsService.saveDto(any(ProjectRequestDto.class))).thenReturn(1L);


        //when
        mvc.perform(post(url)
                        .param("id", "admin")
                        .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        verify(projectsService, times(1)).saveDto(any(ProjectRequestDto.class));
    }

    @Test
    public void saveProject_Fail() throws Exception {
        //given

        Member userMember = new Member();
        userMember.setId("user");
        userMember.setPassword("pw");
        userMember.setName("user");
        userMember.setMail("user@aaa.com");

        when(memberRepository.findById("user")).thenReturn(Optional.of(userMember));
        when(projectsService.saveDto(any(ProjectRequestDto.class))).thenReturn(1L);

        //when
        mvc.perform(post(url)
                        .param("id", "user")
                        .param("pw", "pw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                //then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void FindByUserAdmin_Unauthorized() throws Exception {

        when(memberService.login(anyString(), anyString())).thenReturn(false);

        mvc.perform(get(url).param("id", "testUser").param("pw", "wrongPassword"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void FindByUserAdmin_Success() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);

        Project project1 = new Project();
        project1.setId(1L);
        project1.setTitle("Project 1");
        project1.setCreatedDate(LocalDateTime.now());

        Project project2 = new Project();
        project2.setId(2L);
        project2.setTitle("Project 2");
        project2.setCreatedDate(LocalDateTime.now());

        List<Project> projects = Arrays.asList(project1, project2);

        when(memberProjectService.getProjectIdByMemberId(anyString())).thenReturn(projects);

        mvc.perform(get(url)
                        .param("id", "testUser")
                        .param("pw", "correctPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projects").isArray())
                .andExpect(jsonPath("$.projects[0].project_id").value(1L))
                .andExpect(jsonPath("$.projects[0].title").value("Project 1"))
                .andExpect(jsonPath("$.projects[1].project_id").value(2L))
                .andExpect(jsonPath("$.projects[1].title").value("Project 2"));

    }

    @Test
    public void updateProject_Success_TitleUpdate() throws Exception {
        ProjectRequestDto requestDto = new ProjectRequestDto();
        ProjectRequestDto.Member member = new ProjectRequestDto.Member();
        member.setUser_id("lucete012");
        member.setRole(DEV);
        requestDto.setMembers(List.of(member));

        Project project = new Project();
        project.setId(1L);

        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(projectsService.findById(anyLong())).thenReturn(Optional.of(project));

        mvc.perform(put("/project/{projectId}", 1L)
                        .param("id", "admin")
                        .param("pw", "adminPass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(projectsService, times(1)).update(anyLong(), any(ProjectRequestDto.class));
        verify(memberProjectService, times(1)).deleteAll(anyList());
        verify(memberProjectService, times(1)).save(anyLong(), any(ProjectRequestDto.Member.class));
    }

    @Test
    public void updateProject_Unauthorized() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(false);

        ProjectDto updateRequestDto = new ProjectDto();
        updateRequestDto.setTitle("Updated Project Title");

        mvc.perform(put(url + "/1")
                        .param("id", "user")
                        .param("pw", "wrongPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status().isUnauthorized());

        verify(projectsService, never()).update(anyLong(), any(ProjectRequestDto.class));
    }

    @Test
    public void updateProject_NotFound() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(projectsService.findById(anyLong())).thenReturn(Optional.empty());

        ProjectDto updateRequestDto = new ProjectDto();
        updateRequestDto.setTitle("Updated Project Title");

        mvc.perform(put(url + "/1")
                        .param("id", "admin")
                        .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status().isNotFound());

        verify(projectsService, never()).update(anyLong(), any(ProjectRequestDto.class));
    }

    @Test
    public void updateProject_Success_MembersUpdate() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(projectsService.findById(anyLong())).thenReturn(Optional.of(new Project()));
        when(memberProjectService.getMemberProjectByProjectId(anyLong())).thenReturn(new ArrayList<>());

        ProjectRequestDto updateRequestDto = new ProjectRequestDto();
        updateRequestDto.setMembers(Arrays.asList(
                new ProjectRequestDto.Member("minseok128", DEV),
                new ProjectRequestDto.Member("lucete012", Role.TESTER)
        ));

        mvc.perform(put(url + "/18")
                        .param("id", "admin")
                        .param("pw", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());

        verify(memberProjectService, times(1)).deleteAll(anyList());
        verify(memberProjectService, times(2)).save(anyLong(), any(ProjectRequestDto.Member.class));
    }

    @Test
    public void deleteProject_Unauthorized_NotPermission() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberProjectService.getRole(anyString(), anyLong())).thenReturn(Optional.of(Role.TESTER));

        mvc.perform(delete(url + "/1")
                        .param("id", "user")
                        .param("pw", "wrongpassword"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteProject_NotFound() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(projectsService.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(delete(url + "/1")
                        .param("id", "admin")
                        .param("pw", "password"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProject_Success() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(projectsService.findById(anyLong())).thenReturn(Optional.of(new Project()));
        doNothing().when(memberProjectService).deleteAll(anyList());
        doNothing().when(projectsService).delete(anyLong());

        mvc.perform(delete(url + "/1")
                        .param("id", "admin")
                        .param("pw", "password"))
                .andExpect(status().isOk());

    }

    @Test
    public void getProjectUserRoles_NotFound() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(projectsService.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(get(url + "/1/userRole")
                        .param("id", "user")
                        .param("pw", "password"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getProjectUserRoles_Success() throws Exception {
        Project project1 = new Project();
        project1.setTitle("project1");
        project1.setId(1L);

        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(projectsService.findById(anyLong())).thenReturn(Optional.of(project1));
        when(memberProjectService.getMemberProjectByProjectId(anyLong())).thenReturn(Arrays.asList(
                new MemberProject(new Member("user1", "user1", "user1", "user1"), project1, DEV),
                new MemberProject(new Member("user2", "user2", "user2", "user1"), project1, Role.TESTER)
        ));

        mvc.perform(get(url + "/1/userRole")
                        .param("id", "admin")
                        .param("pw", "password"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].user_id").value("user1"))
                .andExpect(jsonPath("$[0].role").value(DEV.name()))
                .andExpect(jsonPath("$[1].user_id").value("user2"))
                .andExpect(jsonPath("$[1].role").value(Role.TESTER.name()));
    }


}
