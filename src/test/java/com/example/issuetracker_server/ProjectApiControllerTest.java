package com.example.issuetracker_server;

import com.example.issuetracker_server.controller.ProjectController;
import com.example.issuetracker_server.controller.dto.ProjectsSaveRequestDto;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.service.MemberProjectService.MemberProjectService;
import com.example.issuetracker_server.service.ProjectsService.ProjectsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectApiControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectsService projectsService;

    @MockBean
    private MemberProjectService memberProjectService;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private ProjectController projectApiController;

    private ProjectsSaveRequestDto requestDto;

    @Before
    public void setUp() {
        String title = "project1";
        List<ProjectsSaveRequestDto.Member> members = new ArrayList<>();

        members.add(new ProjectsSaveRequestDto.Member("user1", Role.PL));
        members.add(new ProjectsSaveRequestDto.Member("user2", Role.DEV));
        members.add(new ProjectsSaveRequestDto.Member("user3", Role.TESTER));

        requestDto = ProjectsSaveRequestDto.builder()
                .title(title)
                .members(members)
                .build();


    }

    @Test
    public void saveProject_Success() throws Exception {
        // given
        String url = "http://localhost:" + port + "/project/";

        Member adminMember = new Member();
        adminMember.setId("admin");
        adminMember.setPassword("password");
        adminMember.setName("admin");
        adminMember.setMail("aaa@aaa.com");


        Mockito.when(memberRepository.findById("admin")).thenReturn(Optional.of(adminMember));
        Mockito.when(projectsService.save(any(ProjectsSaveRequestDto.class))).thenReturn(1L);

        //when
        mvc.perform(post(url)
                .param("id", "admin")
                .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        Mockito.verify(projectsService, Mockito.times(1)).save(any(ProjectsSaveRequestDto.class));
    }

    @Test
    public void saveProject_Fail() throws Exception {
        //given
        String url = "http://localhost:" + port + "/project/";

        Member userMember = new Member();
        userMember.setId("user");
        userMember.setPassword("pw");
        userMember.setName("user");
        userMember.setMail("user@aaa.com");

        Mockito.when(memberRepository.findById("user")).thenReturn(Optional.of(userMember));
        Mockito.when(projectsService.save(any(ProjectsSaveRequestDto.class))).thenReturn(1L);

        //when
        mvc.perform(post(url)
                        .param("id", "user")
                        .param("pw", "pw")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().is(400));

        //then

    }
}
