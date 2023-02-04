package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.entity.OrderProduct;
import com.qsspy.watmerchbackend.entity.Product;
import com.qsspy.watmerchbackend.entity.Purchase;
import com.qsspy.watmerchbackend.entity.ShopUser;
import com.qsspy.watmerchbackend.exception.login.LoginException;
import com.qsspy.watmerchbackend.repository.PurchaseRepository;
import com.qsspy.watmerchbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock private PurchaseRepository purchaseRepository;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    private IPurchaseService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PurchaseService(purchaseRepository, userRepository, passwordEncoder);
    }

    @Test
    void canMakePurchaseNotLoggedIn() throws LoginException {
        //given
        Purchase purchase = new Purchase(new Date(),false, false);
        purchase.setId(1);
        String base64EncodedAuthString = null;

        given(purchaseRepository.save(any(Purchase.class))).willReturn(purchase);
        //when
        Purchase outputPurchase = underTest.makePurchase(purchase, base64EncodedAuthString);
        //then
        ArgumentCaptor<Purchase> argumentCaptor = ArgumentCaptor.forClass(Purchase.class);
        verify(purchaseRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(purchase);
        assertThat(outputPurchase).isEqualTo(purchase);
    }

    @Test
    void canMakePurchaseIfLoggedIdAndCredentialsCorrect() throws LoginException {
        //given
        Purchase purchase = new Purchase(new Date(),false, false);
        purchase.setId(1);
        String base64EncodedAuthString = "Basic dXNlcjp1c2Vy";
        ShopUser user = new ShopUser("TestUser","pass","mail@mail.com",true);

        given(purchaseRepository.save(any(Purchase.class))).willReturn(purchase);
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        //when
        Purchase outputPurchase = underTest.makePurchase(purchase, base64EncodedAuthString);
        //then
        ArgumentCaptor<Purchase> argumentCaptor = ArgumentCaptor.forClass(Purchase.class);
        verify(purchaseRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(purchase);
        assertThat(outputPurchase).isEqualTo(purchase);
    }

    @Test
    void throwMakePurchaseIfLoggedIdAndUsernameIncorrect(){
        //given
        Purchase purchase = new Purchase(new Date(),false, false);
        purchase.setId(1);
        String base64EncodedAuthString = "Basic dXNlcjp1c2Vy";

        given(userRepository.findByUsername(anyString())).willReturn(null);
        //when
        //then
        assertThatThrownBy(()->underTest.makePurchase(purchase,base64EncodedAuthString))
                .isInstanceOf(LoginException.class)
                .hasMessage("Incorect credentials!");
    }

    @Test
    void throwMakePurchaseIfLoggedIdAndPasswordIncorrect(){
        //given
        Purchase purchase = new Purchase(new Date(),false, false);
        purchase.setId(1);
        String base64EncodedAuthString = "Basic dXNlcjp1c2Vy";
        ShopUser user = new ShopUser("TestUser","pass","mail@mail.com",true);

        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
        //when
        //then
        assertThatThrownBy(()->underTest.makePurchase(purchase,base64EncodedAuthString))
                .isInstanceOf(LoginException.class)
                .hasMessage("Incorect credentials!");
    }

    @Test
    void getPurchases() {
        //given
        int pageNumber = 1;
        int pageSize = 10;
        Page<Purchase> purchasePage = Mockito.mock(Page.class);
        Sort sort = Sort.by(Sort.Direction.DESC,"purchaseDate");
        String base64EncodedAuthString = "Basic dXNlcjp1c2Vy";

        Purchase purchase1 = new Purchase(new Date(),false, false);
        purchase1.setOrderProducts(new ArrayList<>(Arrays.asList(
                new OrderProduct(1, new Product()),
                new OrderProduct(2, new Product()),
                new OrderProduct(3, new Product()))));

        Purchase purchase2 = new Purchase(new Date(),false, false);
        purchase2.setOrderProducts(new ArrayList<>(Arrays.asList(
                new OrderProduct(4, new Product()),
                new OrderProduct(5, new Product()))));


        given(purchasePage.getContent()).willReturn(Arrays.asList(purchase1, purchase2));
        given(userRepository.findByUsername(anyString())).willReturn(new ShopUser());
        given(purchaseRepository.findByUser(any(ShopUser.class),any(Pageable.class))).willReturn(purchasePage);
        //when
        underTest.getPurchases(base64EncodedAuthString,pageSize,pageNumber);
        //then
        verify(userRepository).findByUsername(anyString());
        verify(purchaseRepository).findByUser(any(ShopUser.class), any(Pageable.class));
    }
}