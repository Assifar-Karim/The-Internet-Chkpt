package com.ticp.controller;

import com.ticp.dto.CheckpointDTO;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController
{
    @SendTo("/topic/checkpoints")
    public CheckpointDTO getCheckpoint(CheckpointDTO checkpointDTO)
    {
        return checkpointDTO;
    }
}
