package com.ticp.event.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticp.dto.CheckpointDTO;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener
{
    private final SimpMessagingTemplate messagingTemplate;

    public RedisMessageSubscriber(SimpMessagingTemplate messagingTemplate)
    {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            CheckpointDTO deserializedMsg = objectMapper.readValue(message.toString(), CheckpointDTO.class);
            messagingTemplate.convertAndSend("/topic/checkpoints", deserializedMsg);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
    }
}
