package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.dto.member.MemberInfoDto;
import com.example.issuetracker_server.dto.member.MemberLoginRequestDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;
import com.example.issuetracker_server.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    public void testLoginSuccess() {
        // Given
        MemberLoginRequestDto loginDto = new MemberLoginRequestDto();
        loginDto.setUser_id("testUser");
        loginDto.setPassword("testPass");

        when(memberService.login(loginDto.getUser_id(), loginDto.getPassword())).thenReturn(true);

        // When
        ResponseEntity<Void> response = memberController.login(loginDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testLoginFailure() {
        // Given
        MemberLoginRequestDto loginDto = new MemberLoginRequestDto();
        loginDto.setUser_id("testUser");
        loginDto.setPassword("wrongPass");

        when(memberService.login(loginDto.getUser_id(), loginDto.getPassword())).thenReturn(false);

        // When
        ResponseEntity<Void> response = memberController.login(loginDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testSignUpSuccess() {
        // Given
        MemberSignUpRequestDto signUpDto = new MemberSignUpRequestDto();
        signUpDto.setUser_id("newUser");
        signUpDto.setPassword("newPass");
        signUpDto.setName("New User");
        signUpDto.setEmail("newuser@example.com");

        when(memberService.signUp(signUpDto)).thenReturn(true);

        // When
        ResponseEntity<Void> response = memberController.signUp(signUpDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testSignUpFailure() {
        // Given
        MemberSignUpRequestDto signUpDto = new MemberSignUpRequestDto();
        signUpDto.setUser_id("newUser");
        signUpDto.setPassword("newPass");
        signUpDto.setName("New User");
        signUpDto.setEmail("newuser@example.com");

        when(memberService.signUp(signUpDto)).thenReturn(false);

        // When
        ResponseEntity<Void> response = memberController.signUp(signUpDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";

        List<MemberInfoDto> members = new ArrayList<>();
        members.add(MemberInfoDto.builder()
                .user_id("minseok128")
                .name("Minseok")
                .email("minseok128@example.com").build());
        members.add(MemberInfoDto.builder()
                .user_id("lucete012")
                .name("Lucete")
                .email("lucete012@example.com").build());

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberService.getAllMembers()).thenReturn(members);

        // When
        mockMvc.perform(get("/user")
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].user_id").value("minseok128"))
                .andExpect(jsonPath("$[0].name").value("Minseok"))
                .andExpect(jsonPath("$[0].email").value("minseok128@example.com"))
                .andExpect(jsonPath("$[1].user_id").value("lucete012"))
                .andExpect(jsonPath("$[1].name").value("Lucete"))
                .andExpect(jsonPath("$[1].email").value("lucete012@example.com"));
    }

    @Test
    public void testGetUsersUnauthorized() throws Exception {
        // Given
        String id = "testuser";
        String pw = "wrongpassword";

        when(memberService.login(id, pw)).thenReturn(false);

        // When
        mockMvc.perform(get("/user")
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testGetUserInfoAuthorizedAndUserFound() {
        // Given
        String userId = "testUser";
        when(memberService.login("admin", "adminPass")).thenReturn(true);
        Optional<MemberInfoDto> optionalMemberInfo = Optional.of(new MemberInfoDto("testUser", "Test User", "testuser@example.com"));
        when(memberService.getUserInfo(userId)).thenReturn(optionalMemberInfo);

        // When
        ResponseEntity<?> response = memberController.getUserInfo(userId, "admin", "adminPass");

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(optionalMemberInfo.get());
    }

    @Test
    public void testGetUserInfoUnauthorized() {
        // Given
        String userId = "testUser";
        when(memberService.login("admin", "wrongPass")).thenReturn(false);

        // When
        ResponseEntity<?> response = memberController.getUserInfo(userId, "admin", "wrongPass");

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testGetUserInfoAuthorizedButUserNotFound() {
        // Given
        String userId = "missingUser";
        when(memberService.login("admin", "adminPass")).thenReturn(true);
        when(memberService.getUserInfo(userId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> response = memberController.getUserInfo(userId, "admin", "adminPass");

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
