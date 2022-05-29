package com.ticp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticp.dto.CheckpointDTO;
import com.ticp.model.Checkpoint;
import com.ticp.service.CheckpointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CheckpointControllerIntegrationTest
{
    private MockMvc mockMvc;
    @Mock
    private CheckpointService checkpointService;
    @InjectMocks
    private CheckpointController checkpointController;
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(checkpointController).build();
    }
    @Test
    public void given_existing_username_createCheckpoint_should_return_ok() throws Exception
    {
        //GIVEN
        CheckpointDTO checkpointDTO = new CheckpointDTO();
        checkpointDTO.setUsername("username");
        checkpointDTO.setContent("content");
        //WHEN
        CheckpointDTO resultCheckpoint = new CheckpointDTO("id",
                checkpointDTO.getUsername(),
                checkpointDTO.getContent(),
                new Date(),
                "2022-05-29 at 14:40");
        when(checkpointService.createCheckpoint(any(CheckpointDTO.class))).thenReturn(
                resultCheckpoint);
        //THEN
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/checkpoints").contentType("application/json")
                        .content("{ " +
                                "\"content\": \"content\"," +
                                "\"username\": \"username\"" +
                                " }").accept("*/*"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(resultCheckpoint))));
        verify(checkpointService).createCheckpoint(any(CheckpointDTO.class));
    }
    @Test
    public void given_non_existing_username_createCheckpoint_should_throw_404() throws Exception
    {
        //GIVEN
        CheckpointDTO checkpointDTO = new CheckpointDTO();
        checkpointDTO.setUsername("username");
        checkpointDTO.setContent("content");
        //WHEN
        when(checkpointService.createCheckpoint(any(CheckpointDTO.class))).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with username = %s not found", checkpointDTO.getUsername())));
        //THEN
        mockMvc.perform(post("/api/checkpoints").contentType("application/json")
                        .content("{ " +
                                "\"username\": \"username\"," +
                                "\"content\": \"content\"" +
                                "}").accept("*/*"))
                .andDo(print())
                .andExpect(status().isNotFound());
        //verify(checkpointService).createCheckpoint(checkpointDTO);
    }

    @Test
    public void given_existing_id_fetchOneCheckpointById_should_return_dto() throws Exception
    {
        //GIVEN
        String id = "chkpt-id";
        //WHEN
        when(checkpointService.getCheckpointById(id)).thenReturn(new CheckpointDTO(id,
                "username",
                "chkpt-content",
                new Date(),
                "2022-05-29 at 12:45:32"));
        //THEN
        mockMvc.perform(get("/checkpoints/chkpt-id"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(checkpointService).getCheckpointById(id);
    }
    @Test
    public void given_non_existing_id_fetchOneCheckpointById_should_return_404() throws Exception
    {
        //GIVEN
        String id = "chkpt-id";
        //WHEN
        when(checkpointService.getCheckpointById(id))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Checkpoint with id = %s not found", id)
                ));
        //THEN
        mockMvc.perform(get("/checkpoints/chkpt-id"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(checkpointService).getCheckpointById(id);
    }

    @Test
    public void given_checkpoint_id_deleteCheckpoint_should_delete() throws Exception
    {
        //GIVEN
        String id = "chkpt-id";
        //WHEN
        doNothing().when(checkpointService).deleteCheckpointById(id);
        //THEN
        mockMvc.perform(delete("/api/checkpoints/chkpt-id"))
                .andDo(print()).andExpect(status().isOk());
        verify(checkpointService).deleteCheckpointById(id);
    }

    @Test
    public void given_non_existing_user_id_getUserCheckpoints_should_return_an_empty_list() throws Exception
    {
        //GIVEN
        String userId = "user-id";
        List<CheckpointDTO> checkpoints = List.of();
        //WHEN
        when(checkpointService.getCheckpointsByUser(userId)).thenReturn(checkpoints);
        //THEN
        mockMvc.perform(get("/checkpoint/user/user-id"))
                .andDo(print())
                .andExpect(content().string(equalTo("[]")));
        verify(checkpointService).getCheckpointsByUser(userId);
    }
}