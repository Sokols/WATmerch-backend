package com.qsspy.watmerchbackend.controller.rest;

import com.jayway.jsonpath.InvalidJsonException;
import com.qsspy.watmerchbackend.entity.Address;
import com.qsspy.watmerchbackend.entity.ShopUser;
import com.qsspy.watmerchbackend.entity.ShopUserDetails;
import com.qsspy.watmerchbackend.exception.login.UserNotFoundException;
import com.qsspy.watmerchbackend.exception.login.WrongPasswordException;
import com.qsspy.watmerchbackend.exception.register.RegisterException;
import com.qsspy.watmerchbackend.model.UserAndPasswordModel;
import com.qsspy.watmerchbackend.service.IUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Zapisuje nowego użytkownika (register)",
                  notes = "Zapisuje nowego użytkownika. Nie wymaga autoryzacji")
    @PostMapping("/register")
    public ShopUser register(
            @ApiParam(name = "Nowy użytkownik") @RequestBody ShopUser user
    ) throws RegisterException {

        return userService.register(user);
    }

    @ApiOperation(value = "Pobranie danych użytkownika (logowanie)")
    @PostMapping("/loginUser")
    public ShopUser attemptLogin(
            @ApiParam(name = "Dane logowania") @RequestBody UserAndPasswordModel credsModel
    ) throws UserNotFoundException, WrongPasswordException {

        if(credsModel == null || credsModel.getPassword() == null || credsModel.getUsername() == null) {
            throw new InvalidJsonException("Data is not correct.");
        }
        return userService.getUser(credsModel.getUsername(), credsModel.getPassword());
    }

    @ApiOperation(value = "Edycja szczegółowych danych użytkownika",
                  notes = "Edycja szczegółowych danych użytkownika identyfikowanego z nagłowka 'Authorization'")
    @PutMapping("/editUserDetails")
    public ShopUserDetails editUserDetails(
            @RequestHeader(name = "Authorization") String authString,
            @ApiParam(name = "Szcegóły użytkownika") @RequestBody ShopUserDetails details
    ) {
        return userService.editUserDetails(details, authString);
    }

    @ApiOperation(value = "Edycja adresu dostawy użytkownika",
            notes = "Edycja adresu dostawy użytkownika identyfikowanego z nagłowka 'Authorization'")
    @PutMapping("/editUserAddress/shipping")
    public Address editUserShippingAddress(
            @RequestHeader(name = "Authorization") String authString,
            @ApiParam(name = "Adres") @RequestBody Address address
    ) {
        return userService.editUserAddress(true,address,authString);
    }

    @ApiOperation(value = "Edycja adresu rozliczeniowy użytkownika",
            notes = "Edycja adresu rozliczeniowego użytkownika identyfikowanego z nagłowka 'Authorization'")
    @PutMapping("/editUserAddress/billing")
    public Address editUserBillingAddress(
            @RequestHeader(name = "Authorization") String authString,
            @ApiParam(name = "Adres") @RequestBody Address address
    ) {
        return userService.editUserAddress(false,address,authString);
    }
}
