package com.ticp.event.publisher;

import com.ticp.dto.CheckpointDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher implements MessagePublisher
{
    @Autowired
    private RedisTemplate<String , Object> redisTemplate;
    @Autowired
    private ChannelTopic topic;

    public RedisMessagePublisher() {}

    public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic)
    {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(CheckpointDTO checkpointDTO)
    {
        redisTemplate.convertAndSend(topic.getTopic(), checkpointDTO);
    }
}
