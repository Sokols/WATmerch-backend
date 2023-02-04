package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.entity.CreditCard;
import com.qsspy.watmerchbackend.exception.login.UserNotFoundException;

import java.util.List;

public interface ICreditCardService {

    CreditCard postCreditCard(CreditCard creditCard, String authString) throws UserNotFoundException;
    List<CreditCard> getCreditCards(String authString) throws UserNotFoundException;
    void deleteCreditCard(long creditCardId);
}
