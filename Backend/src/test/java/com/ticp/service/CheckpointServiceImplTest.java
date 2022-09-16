package com.ticp.service;

import com.ticp.dto.CheckpointDTO;
import com.ticp.mapper.CheckpointMapper;
import com.ticp.mapper.ConcreteMapperFactory;
import com.ticp.model.Checkpoint;
import com.ticp.repository.CheckpointRepository;
import com.ticp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckpointServiceImplTest {

    @Mock
    private CheckpointRepository checkpointRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConcreteMapperFactory mapperFactory;

    @Mock
    private CheckpointMapper checkpointMapper;

    @InjectMocks
    private CheckpointServiceImpl checkpointServiceImpl;

    /*@Test
    void giving_existing_checkpoint_id_should_return_correct_checkpoint(){

        // Giving
        String id = "checkpoint-id";
        Checkpoint checkpoint = new Checkpoint(id, null, null, null);
        CheckpointDTO checkpointDTO = new CheckpointDTO(id, null, null, null, null);

        when(checkpointRepository.findById(id)).thenReturn(Optional.of(checkpoint));
        doReturn(checkpointMapper).when(mapperFactory).getMapper(Checkpoint.class);
        when(checkpointMapper.toDTO(checkpoint)).thenReturn(checkpointDTO);

        // When
        CheckpointDTO returnedCheckpoint = checkpointServiceImpl.getCheckpointById(id);

        //Then
        verify(mapperFactory).getMapper(Checkpoint.class);
        verify(checkpointRepository).findById(id);
        assertEquals(id, returnedCheckpoint.getId());

    }

    @Test
    void giving_non_existing_checkpoint_id_should_throw_response_status_exception(){
        // Giving
        String id = "checkpoint-id";
        String expectedResponse = String.format("Checkpoint with id = %s not found", id);
        int expectedStatusCode = 404;
        when(checkpointRepository.findById(id)).thenReturn(Optional.empty());

        // When

        // Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            checkpointServiceImpl.getCheckpointById(id);
        });
        assertEquals(exception.getReason(), expectedResponse);
        assertEquals(exception.getRawStatusCode(), expectedStatusCode);
    }

    @Test
    void giving_null_id_should_throw_bad_argument_exception(){
        // Giving
        String id = null;
        String expectedResponse = String.format("Checkpoint with id = %s not found", id);
        int expectedStatusCode = 404;
        when(checkpointRepository.findById(id)).thenReturn(Optional.empty());

        // When

        // Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            checkpointServiceImpl.getCheckpointById(id);
        });
        assertEquals(exception.getReason(), expectedResponse);
        assertEquals(exception.getRawStatusCode(), expectedStatusCode);
    }

    @Test
    void giving_a_valid_checkpoint_dto_object_should_create_a_checkpoint(){

        // Giving
        Date d = new Date();
        CheckpointDTO checkpointDTO = new CheckpointDTO(null, "username", "content-checkpoint", d, null);
        Checkpoint checkpoint = new Checkpoint(null, "content-checkpoint", "username-id", d);
        Checkpoint persistedCheckpoint = new Checkpoint("checkpoint-id", "content-checkpoint", "username-id", d);
        CheckpointDTO persistedCheckpointDTO = new CheckpointDTO("checkpoint-id", "username", "content-checkpoint", d, null);

        when(checkpointRepository.save(checkpoint)).thenReturn(persistedCheckpoint);
        doReturn(checkpointMapper).when(mapperFactory).getMapper(Checkpoint.class);
        when(checkpointMapper.toModel(checkpointDTO)).thenReturn(checkpoint);
        when(checkpointMapper.toDTO(persistedCheckpoint)).thenReturn(persistedCheckpointDTO);
        when(userRepository.existsByUsername(checkpointDTO.getUsername())).thenReturn(true);

        // When
        CheckpointDTO createdCheckpointDTO = checkpointServiceImpl.createCheckpoint(checkpointDTO);

        // Then
        verify(checkpointRepository).save(checkpoint);
        assertEquals(createdCheckpointDTO.getId(), persistedCheckpointDTO.getId());
    }

    @Test
    void giving_checkpoint_id_should_call_repository_delete(){
        // Giving
        String id = "checkpoint-id";
        // When
        checkpointRepository.deleteById(id);
        // Then
        verify(checkpointRepository).deleteById(id);
    }

    @Test
    void giving_non_existing_user_id_should_return_empty_checkpoints_list(){

        //Giving
        String userId = "userId";
        List<Checkpoint> checkpoints = List.of();

        when(checkpointRepository.findAllByUserId(userId)).thenReturn(checkpoints);

        // When
        List<CheckpointDTO> returnedCheckpoints = checkpointServiceImpl.getCheckpointsByUser(userId);

        // Then
        verify(checkpointRepository).findAllByUserId(userId);
        assertEquals(returnedCheckpoints.size(), 0);
    }*/

}