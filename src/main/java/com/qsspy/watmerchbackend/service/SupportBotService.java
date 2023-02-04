package com.qsspy.watmerchbackend.service;

import com.github.javafaker.Faker;
import com.qsspy.watmerchbackend.model.WebSocketMessageModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SupportBotService implements ISupportBotService{

    private final Faker faker = new Faker();

    @Override
    @Transactional(readOnly = true)
    public WebSocketMessageModel getBotAnswer(String question) {
        return new WebSocketMessageModel(null,"WATmerch",faker.chuckNorris().fact());
    }
}
