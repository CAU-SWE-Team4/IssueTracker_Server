package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.dto.member.MemberInfoDto;
import com.example.issuetracker_server.dto.member.MemberLoginRequestDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;
import com.example.issuetracker_server.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @Test
    public void testLoginSuccess() {
        // Given
        MemberLoginRequestDto loginDto = new MemberLoginRequestDto();
        loginDto.setUserId("testUser");
        loginDto.setPassword("testPass");

        when(memberService.login(loginDto.getUserId(), loginDto.getPassword())).thenReturn(true);

        // When
        ResponseEntity<Void> response = memberController.login(loginDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testLoginFailure() {
        // Given
        MemberLoginRequestDto loginDto = new MemberLoginRequestDto();
        loginDto.setUserId("testUser");
        loginDto.setPassword("wrongPass");

        when(memberService.login(loginDto.getUserId(), loginDto.getPassword())).thenReturn(false);

        // When
        ResponseEntity<Void> response = memberController.login(loginDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testSignUpSuccess() {
        // Given
        MemberSignUpRequestDto signUpDto = new MemberSignUpRequestDto();
        signUpDto.setUserId("newUser");
        signUpDto.setPassword("newPass");
        signUpDto.setName("New User");
        signUpDto.setMail("newuser@example.com");

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
        signUpDto.setUserId("newUser");
        signUpDto.setPassword("newPass");
        signUpDto.setName("New User");
        signUpDto.setMail("newuser@example.com");

        when(memberService.signUp(signUpDto)).thenReturn(false);

        // When
        ResponseEntity<Void> response = memberController.signUp(signUpDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
