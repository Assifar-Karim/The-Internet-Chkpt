package com.ticp.controller;

import com.ticp.dto.CheckpointDTO;
import com.ticp.event.subscriber.RedisMessageSubscriber;
import com.ticp.model.User;
import com.ticp.service.CheckpointService;
import com.ticp.service.UserService;
import com.ticp.service.PublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
public class CheckpointController {

    @Autowired
    private CheckpointService checkpointService;
    @Autowired
    private UserService userService;
    @Autowired
    private PublishingService publishingService;

    @PostMapping("/api/checkpoints")
    public CheckpointDTO createCheckpoint(@RequestBody CheckpointDTO checkpointDTO, Principal principal)
    {
        checkpointDTO.setUsername(principal.getName());
        CheckpointDTO savedCheckpoint = checkpointService.createCheckpoint(checkpointDTO);
        publishingService.publish(savedCheckpoint);
        return savedCheckpoint;
    }

    @GetMapping("/checkpoints")
    public List<CheckpointDTO> fetchAllCheckpoints(){
        return checkpointService.getAllCheckpoints();
    }

    @GetMapping("/checkpoints/{id}")
    public CheckpointDTO fetchOneCheckpointById(@PathVariable String id){
        return checkpointService.getCheckpointById(id);
    }

    @GetMapping("/api/checkpoints/my-checkpoints")
    public List<CheckpointDTO> fetchLoggedUserCheckpoints(Principal principal)
    {
        User user = userService.findUserByUsername(principal.getName());
        if(user == null)
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with username = %s not found", principal.getName())
            );
        }
        return checkpointService.getCheckpointsByUser(user.getId());
    }

    @DeleteMapping("/api/checkpoints/{id}")
    public ResponseEntity<String> deleteCheckPoint(@PathVariable String id, Principal principal)
    {
        checkpointService.deleteUserCheckpointById(id, principal.getName());
        return ResponseEntity.ok("Checkpoint deleted !");
    }

    @GetMapping("/checkpoints/user/{username}")
    public List<CheckpointDTO> getUserCheckpoints(@PathVariable String username){
        User user = userService.findUserByUsername(username);
        if(user == null)
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with username = %s not found", username)
            );
        }
        return checkpointService.getCheckpointsByUser(user.getId());
    }

}
