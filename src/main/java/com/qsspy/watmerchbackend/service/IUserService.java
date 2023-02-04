package com.qsspy.watmerchbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qsspy.watmerchbackend.entity.Address;
import com.qsspy.watmerchbackend.entity.ShopUser;
import com.qsspy.watmerchbackend.entity.ShopUserDetails;
import com.qsspy.watmerchbackend.exception.login.UserNotFoundException;
import com.qsspy.watmerchbackend.exception.login.WrongPasswordException;
import com.qsspy.watmerchbackend.exception.register.RegisterException;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IUserService {

    Page<ShopUser> getUsers(int page, int size, String keyword, int roleId);
    ShopUser register(ShopUser user) throws RegisterException;

    ShopUser getUser(String username, String password) throws UserNotFoundException, WrongPasswordException;
    ShopUser getUser(long Id);

    ShopUserDetails editUserDetails(ShopUserDetails details, String authString);
    ShopUser editUser(Map<String,String> parameters, long Id) throws JsonProcessingException;

    Address editUserAddress(boolean isAddressShipping, Address address, String authString);
    void deleteUser(long id);
}
