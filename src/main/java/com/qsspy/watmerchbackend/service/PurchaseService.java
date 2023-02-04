package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.entity.OrderProduct;
import com.qsspy.watmerchbackend.entity.Purchase;
import com.qsspy.watmerchbackend.entity.ShopUser;
import com.qsspy.watmerchbackend.exception.login.LoginException;
import com.qsspy.watmerchbackend.model.UserAndPasswordModel;
import com.qsspy.watmerchbackend.repository.PurchaseRepository;
import com.qsspy.watmerchbackend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PurchaseService implements IPurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PurchaseService(PurchaseRepository purchaseRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Purchase makePurchase(Purchase purchase, String base64EncodedAuthString) throws LoginException {

        if(base64EncodedAuthString == null) {
            purchase.setUser(null);
        } else {
            UserAndPasswordModel userCreds = UserAndPasswordModel.basicAuthBase64Decode(base64EncodedAuthString);
            ShopUser user = userRepository.findByUsername(userCreds.getUsername());


            if (user == null || !passwordEncoder.matches(userCreds.getPassword(), user.getPassword())) {
                throw new LoginException("Incorect credentials!");
            }

            purchase.setUser(user);
        }

        return purchaseRepository.save(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Purchase> getPurchases(String authString, Integer pageSize, Integer pageNumber) {

        Sort sort = Sort.by(Sort.Direction.DESC,"purchaseDate");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        String username = UserAndPasswordModel.basicAuthBase64Decode(authString).getUsername();
        ShopUser user = userRepository.findByUsername(username);

        Page<Purchase> temp = purchaseRepository.findByUser(user,pageable);
        for(Purchase purchase : temp.getContent()) {
            purchase.setUser(null);
            for(OrderProduct orderProduct : purchase.getOrderProducts()) {
                orderProduct.getProduct().setDetails(null);
            }
        }
        return temp;
    }
}
