package com.qsspy.watmerchbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsspy.watmerchbackend.entity.Address;
import com.qsspy.watmerchbackend.entity.Role;
import com.qsspy.watmerchbackend.entity.ShopUser;
import com.qsspy.watmerchbackend.entity.ShopUserDetails;
import com.qsspy.watmerchbackend.exception.login.UserNotFoundException;
import com.qsspy.watmerchbackend.exception.login.WrongPasswordException;
import com.qsspy.watmerchbackend.exception.register.EmailNotAvailableException;
import com.qsspy.watmerchbackend.exception.register.RegisterException;
import com.qsspy.watmerchbackend.exception.register.UserExistsException;
import com.qsspy.watmerchbackend.model.UserAndPasswordModel;
import com.qsspy.watmerchbackend.repository.AddressRepository;
import com.qsspy.watmerchbackend.repository.UserDetailsRepository;
import com.qsspy.watmerchbackend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsRepository userDetailsRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsRepository userDetailsRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsRepository = userDetailsRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShopUser> getUsers(int page, int size, String keyword, int roleId) {

        Sort sort = Sort.by(Sort.Direction.ASC, "username");
        Pageable pageable = PageRequest.of(page, size, sort);

        if (roleId == 0) {
            return userRepository.findByUsernameContaining(keyword, pageable);
        }

        return userRepository.findByRoleIdAndUsernameContaining(roleId, keyword, pageable);
    }

    @Override
    public ShopUser register(ShopUser user) throws RegisterException {

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserExistsException("User " + user.getUsername() + " already exists!");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailNotAvailableException("E-mail " + user.getEmail() + " is already taken!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            Role basicRole = new Role();
            basicRole.setId(1);
            basicRole.setName(Role.RoleType.USER);
            user.setRole(basicRole);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ShopUser getUser(String username, String password) throws UserNotFoundException, WrongPasswordException {

        ShopUser user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException(username + " not found.");
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException("Invalid password.");
        }

        user.setPassword(password);

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public ShopUser getUser(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public ShopUserDetails editUserDetails(ShopUserDetails details, String authString) {

        UserAndPasswordModel userCreds = UserAndPasswordModel.basicAuthBase64Decode(authString);

        ShopUser user = userRepository.findByUsername(userCreds.getUsername());
        details.setId(user.getUserDetails().getId());
        return userDetailsRepository.save(details);

    }

    @Override
    public Address editUserAddress(boolean isAddressShipping, Address address, String authString) {

        UserAndPasswordModel userCreds = UserAndPasswordModel.basicAuthBase64Decode(authString);

        ShopUser user = userRepository.findByUsername(userCreds.getUsername());
        if (isAddressShipping) {
            address.setId(user.getShippingAddress().getId());
        } else {
            address.setId(user.getBillingAddress().getId());
        }

        return addressRepository.save(address);
    }

    @Override
    public ShopUser editUser(Map<String, String> params, long id) throws JsonProcessingException {

        ShopUser oldUser = userRepository.findById(id).get();
        Map<String, Object> nestedMap = generateNestedMap(params, "\\.", ".");
        ObjectMapper jsonMapper = new ObjectMapper();

        String json = jsonMapper.writeValueAsString(nestedMap);
        System.out.println(json);
        jsonMapper.readerForUpdating(oldUser).readValue(json);
        ShopUser newUser = userRepository.save(oldUser);
        return newUser;
    }

    @Override
    public void deleteUser(long id) {

        userRepository.deleteById(id);
    }

    private Map<String, Object> generateNestedMap(Map<String, String> map, String separatorRegex, String separator) {

        Map<String, Map<String, String>> nestedMaps = new HashMap<>();
        Map<String, Object> outMap = new HashMap<>();

        for (String key : map.keySet()) {

            if (key.contains(separator)) {

                String[] parts = key.split(separatorRegex, 2);
                if (!nestedMaps.containsKey(parts[0])) {
                    nestedMaps.put(parts[0], new HashMap());
                }
                nestedMaps.get(parts[0]).put(parts[1], map.get(key));
            } else {
                outMap.put(key, map.get(key));
            }
        }

        for (String nestedKey : nestedMaps.keySet()) {
            outMap.put(nestedKey, generateNestedMap(nestedMaps.get(nestedKey), separatorRegex, separator));
        }

        return outMap;
    }
}
