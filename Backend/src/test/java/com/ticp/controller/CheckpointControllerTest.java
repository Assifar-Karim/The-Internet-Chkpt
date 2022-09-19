package com.ticp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ticp.dto.CheckpointDTO;
import com.ticp.error.handler.RestResponseEntityExceptionHandler;
import com.ticp.model.User;
import com.ticp.service.CheckpointService;
import com.ticp.service.PublishingService;
import com.ticp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CheckpointControllerTest
{
    private MockMvc mockMvc;
    @Mock
    private CheckpointService checkpointService;
    @Mock
    private UserService userService;
    @Mock
    private PublishingService publishingService;
    @InjectMocks
    private CheckpointController checkpointController;

    @BeforeEach
    void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(checkpointController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }
    @Test
    void shouldCreateCheckpoint() throws Exception
    {
        //GIVEN
        CheckpointDTO checkpointDTO = new CheckpointDTO();
        checkpointDTO.setContent("content");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String checkpointDTOJson = objectWriter.writeValueAsString(checkpointDTO);
        CheckpointDTO expectedResult = new CheckpointDTO("id", "main-character", "content", "now");
        String expectedResultJson = objectWriter.writeValueAsString(expectedResult);

        when(checkpointService.createCheckpoint(any(CheckpointDTO.class))).thenReturn(expectedResult);
        doNothing().when(publishingService).publish(any(CheckpointDTO.class));

        //WHEN
        mockMvc.perform(post("/api/checkpoints").principal(expectedResult::getUsername).contentType("application/json").content(checkpointDTOJson).characterEncoding(StandardCharsets.UTF_8).accept("*/*"))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResultJson));
    }
    @Test
    public void shouldReturnCheckpoint() throws Exception
    {
        //GIVEN
        String id = "id";
        CheckpointDTO checkpointDTO = new CheckpointDTO(id, "main-character", "content", "now");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String expectedResponse = objectWriter.writeValueAsString(checkpointDTO);
        when(checkpointService.getCheckpointById(id)).thenReturn(checkpointDTO);

        //WHEN
        mockMvc.perform(get("/checkpoints/".concat(id)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void shouldReturnNotFoundCheckpoint() throws Exception
    {
        //GIVEN
        String id = "id";
        when(checkpointService.getCheckpointById(id)).thenThrow(new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Checkpoint with id = %s not found", id)
        ));

        //WHEN
        mockMvc.perform(get("/checkpoints/".concat(id)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Checkpoint with id = id not found"));
    }

    @Test
    public void shouldReturnUserCheckpoints() throws Exception
    {
        //GIVEN
        String username = "main-character";
        User expectedUser = new User(username, "mc1@provider.com", "password");
        expectedUser.setId("id");
        List<CheckpointDTO> expectedCheckpoints = List.of(
                new CheckpointDTO("chkpt-1", "main-character", "content", "now"),
                new CheckpointDTO("chkpt-2", "main-character", "content 2", "1 hour ago")
        );
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String expectedResponse = objectWriter.writeValueAsString(expectedCheckpoints);

        when(userService.findUserByUsername(username)).thenReturn(expectedUser);
        when(checkpointService.getCheckpointsByUser(expectedUser.getId())).thenReturn(expectedCheckpoints);

        //WHEN
        mockMvc.perform(get("/checkpoints/user/".concat(username)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void shouldReturnUserNotFound() throws Exception
    {
        //GIVEN
        String username = "main-character";
        when(userService.findUserByUsername(username)).thenReturn(null);

        //WHEN
        mockMvc.perform(get("/checkpoints/user/".concat(username)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with username = main-character not found"));
    }
}