package com.ticp.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ticp.dto.CheckpointDTO;
import com.ticp.model.User;
import com.ticp.service.CheckpointService;
import com.ticp.service.UserService;
import com.ticp.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CheckpointController {

    @Autowired
    private CheckpointService checkpointService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public CheckpointController(CheckpointService checkpointService) {
        this.checkpointService = checkpointService;
    }

    @PostMapping("/api/checkpoints")
    public CheckpointDTO createCheckpoint(@RequestBody CheckpointDTO checkpointDTO,
                                          @RequestHeader("Authorization") String authHeader){
        // get the user username
        DecodedJWT decodedJWT = jwtTokenUtil.verifyToken(authHeader);
        String username = decodedJWT.getSubject();

        checkpointDTO.setUsername(username);

        return checkpointService.createCheckpoint(checkpointDTO);
    }

    @GetMapping("/checkpoints")
    public List<CheckpointDTO> fetchAllCheckpoints(){
        return checkpointService.getAllCheckpoints();
    }

    //@GetMapping("/checkpoints")
    public Map<String, Object> fetchAllCheckpoints(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size){
        return checkpointService.getAllCheckpointsSortedByDateDesc(page, size);
    }

    @GetMapping("/checkpoints/{id}")
    public CheckpointDTO fetchOneCheckpointById(@PathVariable String id){
        return checkpointService.getCheckpointById(id);
    }

    @GetMapping("/api/checkpoints/my-checkpoints")
    public List<CheckpointDTO> fetchLoggedUserCheckpoints(@RequestHeader("Authorization") String authHeader){
        DecodedJWT decodedJWT = jwtTokenUtil.verifyToken(authHeader);
        String username = decodedJWT.getSubject();

        User user = userService.findUserByUsername(username);

        return checkpointService.getCheckpointsByUser(user.getId());
    }

    @DeleteMapping("/api/checkpoints/{id}")
    public ResponseEntity<String> deleteCheckPoint(@PathVariable String id,
                                               @RequestHeader("Authorization") String authHeader){

        DecodedJWT decodedJWT = jwtTokenUtil.verifyToken(authHeader);
        String username = decodedJWT.getSubject();

        checkpointService.deleteUserCheckpointById(id, username);
        return ResponseEntity.ok("Checkpoint deleted !");
    }

    @GetMapping("/checkpoints/user/{username}")
    public List<CheckpointDTO> getUserCheckpoints(@PathVariable String username){
        User user = userService.findUserByUsername(username);
        return checkpointService.getCheckpointsByUser(user.getId());
    }

}
