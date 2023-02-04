package com.qsspy.watmerchbackend.service;

import com.github.javafaker.Faker;
import com.qsspy.watmerchbackend.model.WebSocketMessageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SupportBotServiceTest {

    @Autowired private Faker faker;

    private ISupportBotService underTest;

    @BeforeEach
    void setUp() {
        underTest = new SupportBotService();
    }

    @Test
    void getBotAnswer() {
        //given
        String question = "Hello world?";
        String senderName = "WATmerch";
        //when
        WebSocketMessageModel model = underTest.getBotAnswer(question);
        //then
        assertThat(model.getName()).isEqualTo(senderName);
        assertThat(model.getMessage()).isNotEmpty();
    }
}