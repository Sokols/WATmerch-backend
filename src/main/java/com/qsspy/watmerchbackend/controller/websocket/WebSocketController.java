package com.qsspy.watmerchbackend.controller.websocket;

import com.qsspy.watmerchbackend.model.WebSocketMessageModel;
import com.qsspy.watmerchbackend.service.ISupportBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ISupportBotService supportBotService;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, ISupportBotService supportBotService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.supportBotService = supportBotService;
    }

    @MessageMapping("/support")
    public void reply(
            @Payload WebSocketMessageModel message) {
        simpMessagingTemplate.convertAndSend("/queue/support-" + message.getSenderId(), message);
        simpMessagingTemplate.convertAndSend("/queue/support-" + message.getSenderId(),
                supportBotService.getBotAnswer(message.getMessage()));

    }
}
