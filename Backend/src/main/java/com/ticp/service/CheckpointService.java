package com.ticp.service;

import com.ticp.dto.CheckpointDTO;

import java.util.List;
import java.util.Map;

public interface CheckpointService {

    CheckpointDTO createCheckpoint(CheckpointDTO checkpointDTO);
    List<CheckpointDTO> getAllCheckpoints();
    List<CheckpointDTO> getCheckpointsByUser(String userId);
    CheckpointDTO getCheckpointById(String id);
    void deleteCheckpointById(String id);
    void deleteUserCheckpointById(String id, String username);
    Map<String, Object> getAllCheckpointsSortedByDateDesc(int page, int size);

}
