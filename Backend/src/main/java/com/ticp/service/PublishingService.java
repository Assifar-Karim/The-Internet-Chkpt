package com.ticp.service;

import com.ticp.dto.CheckpointDTO;

public interface PublishingService
{
    void publish(CheckpointDTO checkpointDTO);
}
