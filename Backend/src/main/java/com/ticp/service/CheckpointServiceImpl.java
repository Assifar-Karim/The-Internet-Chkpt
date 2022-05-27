package com.ticp.service;

import com.ticp.dto.CheckpointDTO;
import com.ticp.mapper.CheckpointMapper;
import com.ticp.mapper.ConcreteMapperFactory;
import com.ticp.model.Checkpoint;
import com.ticp.repository.CheckpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CheckpointServiceImpl implements CheckpointService{

    @Autowired
    private CheckpointRepository checkpointRepository;
    @Autowired
    private ConcreteMapperFactory mapperFactory;

    public CheckpointDTO toDTO(Checkpoint model){
        CheckpointMapper checkpointMapper = (CheckpointMapper) mapperFactory.getMapper(Checkpoint.class);
        return checkpointMapper.toDTO(model);
    }

    public Checkpoint toModel(CheckpointDTO DTO){
        CheckpointMapper checkpointMapper = (CheckpointMapper) mapperFactory.getMapper(Checkpoint.class);
        return checkpointMapper.toModel(DTO);
    }

    @Override
    public CheckpointDTO createCheckpoint(CheckpointDTO checkpointDTO) {
        Checkpoint checkpoint = toModel(checkpointDTO);
        return toDTO(checkpointRepository.save(checkpoint));
    }

    @Override
    public List<CheckpointDTO> getAllCheckpoints() {
        return checkpointRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckpointDTO> getCheckpointsByUser(String userId) {
        return checkpointRepository
                .findAllByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CheckpointDTO getCheckpointById(String id) {
        return toDTO(checkpointRepository
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
