package com.ticp.controller;

import com.ticp.dto.CheckpointDTO;
import com.ticp.service.CheckpointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CheckpointController {

    private final CheckpointService checkpointService;

    public CheckpointController(CheckpointService checkpointService) {
        this.checkpointService = checkpointService;
    }

    @PostMapping("/api/checkpoints")
    public CheckpointDTO createCheckpoint(@RequestBody CheckpointDTO checkpointDTO){
        return checkpointService.createCheckpoint(checkpointDTO);
    }

    @GetMapping("/checkpoints")
    public List<CheckpointDTO> fetchAllCheckpoints(){
        return checkpointService.getAllCheckpoints();
    }

    @GetMapping("/checkpoints/{id}")
    public CheckpointDTO fetchOneCheckpointById(@PathVariable String id){
        return checkpointService.getCheckpointById(id);
    }

    @DeleteMapping("/api/checkpoints/{id}")
    public ResponseEntity<String> deleteCheckPoint(@PathVariable String id){
        checkpointService.deleteCheckpointById(id);
        return ResponseEntity.ok("Checkpoint deleted !");
    }

    @GetMapping("/checkpoint/user/{userId}")
    public List<CheckpointDTO> getUserCheckpoints(@PathVariable String userId){
        return checkpointService.getCheckpointsByUser(userId);
    }

}
