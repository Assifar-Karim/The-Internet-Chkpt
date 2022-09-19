package com.ticp.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
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
}