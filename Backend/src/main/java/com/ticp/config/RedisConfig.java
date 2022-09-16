package com.ticp.config;

import com.ticp.event.publisher.MessagePublisher;
import com.ticp.event.publisher.RedisMessagePublisher;
import com.ticp.event.subscriber.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig
{
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter)
    {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, topic());
        return redisMessageListenerContainer;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter()
    {
        return new MessageListenerAdapter(new RedisMessageSubscriber(messagingTemplate), "onMessage");
    }

    @Bean
    public ChannelTopic topic()
    {
        return new ChannelTopic("checkpointsQueue");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
    {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    public MessagePublisher messagePublisher()
    {
        return new RedisMessagePublisher(redisTemplate(redisConnectionFactory), topic());
    }


}
