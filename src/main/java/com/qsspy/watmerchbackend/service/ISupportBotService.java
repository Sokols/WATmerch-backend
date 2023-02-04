package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.model.WebSocketMessageModel;

public interface ISupportBotService {

    WebSocketMessageModel getBotAnswer(String question);
}
