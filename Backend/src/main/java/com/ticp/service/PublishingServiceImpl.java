package com.ticp.service;

import com.ticp.dto.CheckpointDTO;
import com.ticp.event.publisher.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublishingServiceImpl implements PublishingService
{
    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @Override
    public void publish(CheckpointDTO checkpointDTO)
    {
        redisMessagePublisher.publish(checkpointDTO);
    }
}
