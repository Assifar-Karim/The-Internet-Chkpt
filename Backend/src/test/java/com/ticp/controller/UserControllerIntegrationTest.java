package com.ticp.controller;

import com.ticp.dto.PasswordDTO;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;
import com.ticp.service.EmailSenderService;
import com.ticp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest
{
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private EmailSenderService emailSenderService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void given_valid_token_verifyUser_should_return_complete() throws Exception
    {
        //GIVEN
        String token = "token";
        //WHEN
        when(userService.validateVerificationToken(token)).thenReturn("Valid");
        //THEN
        mockMvc.perform(get("/verify?token=token"))
                .andDo(print())
                .andExpect(content().string(equalTo("Verification Complete")));
        verify(userService).validateVerificationToken(token);
    }
    @Test
    void given_expired_token_verifyUser_should_return_expired() throws Exception
    {
        //GIVEN
        String token = "token";
        //WHEN
        when(userService.validateVerificationToken(token)).thenReturn("Expired token");
        //THEN
        mockMvc.perform(get("/verify?token=token"))
                .andDo(print())
                .andExpect(content().string(equalTo("Expired token")));
        verify(userService).validateVerificationToken(token);
    }
    @Test
    void given_invalid_token_verifyUser_should_return_invalid() throws Exception
    {
        //GIVEN
        String token = "token";
        //WHEN
        when(userService.validateVerificationToken(token)).thenReturn("Invalid token");
        //THEN
        mockMvc.perform(get("/verify?token=token"))
                .andDo(print())
                .andExpect(content().string(equalTo("Invalid token")));
        verify(userService).validateVerificationToken(token);

    }

    @Test
    void given_non_existing_old_token_resendVerificationToken_should_return_non_existing() throws Exception
    {
        //GIVEN
        String old_token = "old_token";
        //WHEN
        when(userService.regenerateVerificationToken(old_token)).thenReturn(null);
        //THEN
        mockMvc.perform(get("/regenerateVerification?token=old_token"))
                .andDo(print())
                .andExpect(content().string(equalTo("Non existing verification token")));
    }
    @Test
    void given_existing_old_token_resendVerificationToken_should_return_regenerated() throws Exception
    {
        //GIVEN
        String old_token = "old_token";
        User user = new User("username","username@mail-provider.com","password");
        VerificationToken verificationToken = new VerificationToken(old_token);
        verificationToken.setUser(user);
        //WHEN
        when(userService.regenerateVerificationToken(old_token)).thenReturn(verificationToken);
        doNothing().when(emailSenderService).sendSimpleMail(verificationToken.getUser().getEmail(),"",
                "The Internet Checkpoint Password Reset");
        //THEN
        mockMvc.perform(get("/regenerateVerification?token=old_token"))
                .andDo(print())
                .andExpect(content().string(equalTo("Token Regenerated")));
        verify(userService).regenerateVerificationToken(old_token);
    }

    @Test
    void given_non_existing_email_resetPassword_return_incorrect_email() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO("username@mail-provider.com",
                "oldPassword,","newPassword");
        //WHEN
        when(userService.generatePasswordToken(passwordDTO)).thenReturn(null);
        //THEN
        mockMvc.perform(post("/resetPassword")
                        .contentType("application/json")
                        .content("{ " +
                                "\"email\": \"username@mail-provider.com\"," +
                                "\"oldPassword\": \"oldPassword\"," +
                                "\"newPassword\": \"newPassword\" " +
                                "}").accept("*/*"))
                .andDo(print())
                .andExpect(content().string(equalTo("Incorrect email")));
    }

    @Test
    void given_non_existing_email_changePassword_should_return_not_found() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO("username@mail-provider.com",
                "oldPassword,","newPassword");
        //WHEN
        when(userService.findUserByEmail(passwordDTO.getEmail())).thenReturn(null);
        //THEN
        mockMvc.perform(post("/changePassword")
                        .contentType("application/json")
                        .content("{ " +
                                "\"email\": \"username@mail-provider.com\"," +
                                "\"oldPassword\": \"oldPassword\"," +
                                "\"newPassword\": \"newPassword\" " +
                                "}").accept("*/*"))
                .andDo(print())
                .andExpect(content().string(equalTo("User not found")));
    }

}