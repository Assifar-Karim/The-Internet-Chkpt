package com.ticp.service;

import com.ticp.dto.CheckpointDTO;

import java.util.List;

public interface CheckpointService {

    CheckpointDTO createCheckpoint(CheckpointDTO checkpointDTO);
    List<CheckpointDTO> getAllCheckpoints();
    List<CheckpointDTO> getCheckpointsByUser(String userId);
    CheckpointDTO getCheckpointById(String id);
    void deleteCheckpointById(String id);

}
