package com.ticp.mapper;

import com.ticp.dto.CheckpointDTO;
import com.ticp.model.Checkpoint;
import com.ticp.model.User;
import com.ticp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CheckpointMapper implements Mapper<Checkpoint, CheckpointDTO>{

    private static Logger logger = LogManager.getLogger(CheckpointMapper.class);

    @Value("${ticp.configs.date_format:yyyy.MM.dd 'at' HH:mm:ss}")
    private String DATE_FORMAT;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Checkpoint toModel(CheckpointDTO checkpointDTO) {

        User fetchedUser = userRepository.findByUsername(checkpointDTO.getUsername());
        if(fetchedUser == null)
        {
            logger.error("User with username = {} was not found in the db", checkpointDTO.getUsername());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with username=%s not found", checkpointDTO.getUsername())
            );
        }

        return new Checkpoint(
                checkpointDTO.getContent(),
                fetchedUser.getId(),
                new Date()
        );
    }

    @Override
    public CheckpointDTO toDTO(Checkpoint model) {

        SimpleDateFormat sdf = new SimpleDateFormat(this.DATE_FORMAT);

        User fetchedUser = userRepository
                .findById(model.getUserId())
                .orElseThrow(() -> {
                    logger.error("User with id = {} was not found in the db", model.getUserId());
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            String.format("User with id=%s not found", model.getUserId()));
                });

        return new CheckpointDTO(
                model.getId(),
                fetchedUser.getUsername(),
                model.getContent(),
                sdf.format(model.getCheckpointDate())
        );
    }
}
