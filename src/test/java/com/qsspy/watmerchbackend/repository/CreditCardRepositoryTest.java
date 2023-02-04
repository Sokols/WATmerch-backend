package com.qsspy.watmerchbackend.repository;

import com.qsspy.watmerchbackend.entity.CreditCard;
import com.qsspy.watmerchbackend.entity.ShopUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CreditCardRepositoryTest {

    @Autowired
    private CreditCardRepository underTest;
    @Autowired
    private UserRepository userRepository;


    @Test
    void itShouldFindCreditCardsByUserId() {

        //given
        ShopUser user = new ShopUser("TestUser", "TestPass", "mail@mail.com", true);
        List<CreditCard> inputCreditCards = Arrays.asList(
                new CreditCard("1234", new Date(), "1234"),
                new CreditCard("5678", new Date(), "5678"),
                new CreditCard("9999", new Date(), "9999")
        );
        user.setCreditCards(inputCreditCards);
        user.getCreditCards().forEach(card->card.setUser(user));
        ShopUser savedUser = userRepository.save(user);

        //when
        List<CreditCard> returnedCreditCards = underTest.findByUser(savedUser);

        assertThat(returnedCreditCards).isEqualTo(inputCreditCards);
    }
}