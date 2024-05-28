package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectSaveRequestDto;
import com.example.issuetracker_server.service.member.MemberServiceImpl;
import com.example.issuetracker_server.service.memberproject.MemberProjectServiceImpl;
import com.example.issuetracker_server.service.project.ProjectServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private ProjectSaveRequestDto requestDto;

    String url = "http://localhost:" + port + "/project/";
    @BeforeEach
    public void setUp() {
        String title = "project1";
        List<ProjectSaveRequestDto.Member> members = new ArrayList<>();

        members.add(new ProjectSaveRequestDto.Member("user1", Role.PL));
        members.add(new ProjectSaveRequestDto.Member("user2", Role.DEV));
        members.add(new ProjectSaveRequestDto.Member("user3", Role.TESTER));

        requestDto = ProjectSaveRequestDto.builder()
                .title(title)
                .members(members)
                .build();

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveProject_Success() throws Exception {
        // given

        Member adminMember = new Member();
        adminMember.setId("admin");
        adminMember.setPassword("password");
        adminMember.setName("admin");
        adminMember.setMail("aaa@aaa.com");

        when(memberRepository.findById("admin")).thenReturn(Optional.of(adminMember));
        when(projectsService.save(any(ProjectSaveRequestDto.class))).thenReturn(1L);

        //when
        mvc.perform(post(url)
                        .param("id", "admin")
                        .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        Mockito.verify(projectsService, Mockito.times(1)).save(any(ProjectSaveRequestDto.class));
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
        when(projectsService.save(any(ProjectSaveRequestDto.class))).thenReturn(1L);

        //when
        mvc.perform(post(url)
                        .param("id", "user")
                        .param("pw", "pw")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().is(400));

        //then

    }

    @Test
    public void FindByUserAdmin_Unauthorized() throws Exception {

        when(memberService.login(anyString(), anyString())).thenReturn(false);

        mvc.perform(get(url).param("id", "testUser").param("pw", "wrongPassword"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void FindByUserAdmin_Succecc() throws Exception {
        when(memberService.login(anyString(),anyString())).thenReturn(true);

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

}
