package com.ticp.service;

import com.ticp.dto.CheckpointDTO;
import com.ticp.mapper.CheckpointMapper;
import com.ticp.mapper.ConcreteMapperFactory;
import com.ticp.model.Checkpoint;
import com.ticp.model.User;
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
class CheckpointServiceImplTest
{

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

    @Test
    void shouldReturnCorrectCheckpoint()
    {

        //GIVEN
        String id = "checkpoint-id";
        Checkpoint checkpoint = new Checkpoint(id, "user id", new Date());
        CheckpointDTO expectedCheckpointDTO = new CheckpointDTO(id, "main-character", "content", "now");

        when(checkpointRepository.findById(id)).thenReturn(Optional.of(checkpoint));
        doReturn(checkpointMapper).when(mapperFactory).getMapper(Checkpoint.class);
        when(checkpointMapper.toDTO(checkpoint)).thenReturn(expectedCheckpointDTO);

        //WHEN
        CheckpointDTO actualCheckpointDTO = checkpointServiceImpl.getCheckpointById(id);
        //THEN
        assertEquals(id, actualCheckpointDTO.getId());
    }

    @Test
    void shouldThrowResponseStatusException()
    {
        //GIVEN
        String id = "checkpoint-id";
        String expectedResponse = String.format("Checkpoint with id = %s not found", id);
        int expectedStatusCode = 404;
        when(checkpointRepository.findById(id)).thenReturn(Optional.empty());

        //WHEN
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class, () -> {
            checkpointServiceImpl.getCheckpointById(id);
        });
        //THEN
        assertEquals(expectedResponse,actualException.getReason());
        assertEquals(expectedStatusCode, actualException.getRawStatusCode());
    }

    @Test
    void shouldCreateCheckpoint()
    {

        //GIVEN
        CheckpointDTO checkpointDTO = new CheckpointDTO(null, "username", "content-checkpoint", null);
        Checkpoint checkpoint = new Checkpoint("content-checkpoint", "username-id", new Date());
        Checkpoint persistedCheckpoint = new Checkpoint("content-checkpoint", "username-id", new Date());
        persistedCheckpoint.setId("checkpoint-id");
        CheckpointDTO persistedCheckpointDTO = new CheckpointDTO("checkpoint-id", "username", "content-checkpoint", "now");

        when(checkpointRepository.save(checkpoint)).thenReturn(persistedCheckpoint);
        doReturn(checkpointMapper).when(mapperFactory).getMapper(Checkpoint.class);
        when(checkpointMapper.toModel(checkpointDTO)).thenReturn(checkpoint);
        when(checkpointMapper.toDTO(persistedCheckpoint)).thenReturn(persistedCheckpointDTO);
        when(userRepository.existsByUsername(checkpointDTO.getUsername())).thenReturn(true);

        //WHEN
        CheckpointDTO actualCheckpointDTO = checkpointServiceImpl.createCheckpoint(checkpointDTO);
        //THEN
        assertEquals(persistedCheckpointDTO.getId(), actualCheckpointDTO.getId());
    }

    @Test
    void shouldThrowNotFoundResponseStatus()
    {
        //GIVEN
        String username = "username";
        String id = "id";
        int expectedStatusCode = 404;
        String expectedErrorReason = String.format("User with username = %s not found", username);
        when(userRepository.findByUsername(username)).thenReturn(null);

        //WHEN
        var actualException = assertThrows(ResponseStatusException.class, () -> {
            checkpointServiceImpl.deleteUserCheckpointById(id, username);
        });
        //THEN
        assertEquals(expectedStatusCode, actualException.getRawStatusCode());
        assertEquals(expectedErrorReason, actualException.getReason());
    }

    @Test
    void shouldThrowForbiddenResponseStatus()
    {
        //GIVEN
        String username = "username";
        String id = "id";
        User persistedUser = new User(username, "username@provider.com", "password");
        persistedUser.setId("user-id-1");
        Checkpoint persistedCheckpoint = new Checkpoint("content", "user-id-2", new Date());
        int expectedStatusCode = 403;

        when(userRepository.findByUsername(username)).thenReturn(persistedUser);
        when(checkpointRepository.findById(id)).thenReturn(Optional.of(persistedCheckpoint));

        //WHEN
        var actualException = assertThrows(ResponseStatusException.class, () ->{
            checkpointServiceImpl.deleteUserCheckpointById(id, username);
        });
        //THEN
        assertEquals(expectedStatusCode, actualException.getRawStatusCode());
    }
}