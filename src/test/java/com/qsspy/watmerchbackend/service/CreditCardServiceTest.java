package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.entity.CreditCard;
import com.qsspy.watmerchbackend.entity.ShopUser;
import com.qsspy.watmerchbackend.exception.login.UserNotFoundException;
import com.qsspy.watmerchbackend.repository.CreditCardRepository;
import com.qsspy.watmerchbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private CreditCardRepository creditCardRepository;
    private ICreditCardService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CreditCardService(userRepository, creditCardRepository);
    }

    @Test
    void willPostCreditCard() throws UserNotFoundException {
        //given
        CreditCard creditCard = new CreditCard("1234", new Date(), "1234");
        String authString = "Basic dXNlcjp1c2Vy";
        ShopUser user = new ShopUser("TestUser","TestPass","mail@mail.com",true);
        user.setId(10);

        given(userRepository.findByUsername(any())).willReturn(user);

        //when
        underTest.postCreditCard(creditCard, authString);

        //then
        ArgumentCaptor<CreditCard> argumentCaptor = ArgumentCaptor.forClass(CreditCard.class);

        verify(creditCardRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(creditCard);
    }

    @Test
    void itShouldGetUserCreditCards() throws UserNotFoundException {
        //given
        String authString = "Basic dXNlcjp1c2Vy";
        ShopUser user = new ShopUser("TestUser","TestPass","mail@mail.com",true);
        user.setId(10);

        given(userRepository.findByUsername(any())).willReturn(user);

        //when
        underTest.getCreditCards(authString);
        //then
        verify(creditCardRepository).findByUser(user);
    }

    @Test
    void itShouldDeleteCreditCardWithGivenId() {
        //given
        long id = 10L;

        //when
        underTest.deleteCreditCard(id);
        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(creditCardRepository).deleteById(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(id);
    }
}