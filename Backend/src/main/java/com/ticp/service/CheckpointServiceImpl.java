package com.ticp.service;

import com.ticp.dto.CheckpointDTO;
import com.ticp.mapper.CheckpointMapper;
import com.ticp.mapper.ConcreteMapperFactory;
import com.ticp.model.Checkpoint;
import com.ticp.model.User;
import com.ticp.repository.CheckpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CheckpointServiceImpl implements CheckpointService{

    @Autowired
    private CheckpointRepository checkpointRepository;
    @Autowired
    private ConcreteMapperFactory mapperFactory;

    @Override
    public CheckpointDTO createCheckpoint(CheckpointDTO checkpointDTO) {
        CheckpointMapper checkpointMapper = (CheckpointMapper) mapperFactory.getMapper(Checkpoint.class);

        Checkpoint checkpoint = checkpointMapper.toModel(checkpointDTO);

        return checkpointMapper.toDTO(checkpointRepository.save(checkpoint));
    }

    @Override
    public List<CheckpointDTO> getAllCheckpoints() {

        CheckpointMapper checkpointMapper = (CheckpointMapper) mapperFactory.getMapper(Checkpoint.class);

        return checkpointRepository
                .findAll()
                .stream()
                .map(checkpointMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckpointDTO> getCheckpointsByUser(String userId) {
        CheckpointMapper checkpointMapper = (CheckpointMapper) mapperFactory.getMapper(Checkpoint.class);
        return checkpointRepository
                .findAllByUserId(userId)
                .stream()
                .map(checkpointMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CheckpointDTO getCheckpointById(String id) {
        CheckpointMapper checkpointMapper = (CheckpointMapper) mapperFactory.getMapper(Checkpoint.class);
        return checkpointMapper
                .toDTO(checkpointRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Checkpoint with id = %s not found", id))));
    }

    @Override
    public void deleteCheckpointById(String id) {
        checkpointRepository.deleteById(id);
    }
}
