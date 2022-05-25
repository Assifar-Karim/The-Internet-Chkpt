package com.ticp.repository;

import com.ticp.model.Checkpoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CheckpointRepository extends MongoRepository<Checkpoint, String> {
    List<Checkpoint> findAllByUserId(String userId);
}
