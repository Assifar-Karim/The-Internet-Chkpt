package com.ticp.event.publisher;

import com.ticp.dto.CheckpointDTO;

public interface MessagePublisher
{
    void publish(CheckpointDTO checkpointDTO);
}
