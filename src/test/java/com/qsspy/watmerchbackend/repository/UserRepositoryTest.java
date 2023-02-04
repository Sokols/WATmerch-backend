package com.qsspy.watmerchbackend.repository;

import com.qsspy.watmerchbackend.entity.Role;
import com.qsspy.watmerchbackend.entity.ShopUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void itShouldFindByUsername() {
        //given
        String username = "BasicUser";

        ShopUser inputUser = underTest.save(new ShopUser(
                username, "pass", "asd@asd.com", true
        ));

        //when
        ShopUser outputUser = underTest.findByUsername(username);

        //then
        assertThat(outputUser).isEqualTo(inputUser);
    }

    @Test
    void itShouldNotFindUserByUsername() {
        //given
        String username = "BasicUser";
        String wrongUsername = "icUser";

        ShopUser inputUser = underTest.save(new ShopUser(
                username, "pass", "asd@asd.com", true
        ));

        //when
        ShopUser outputUser = underTest.findByUsername(wrongUsername);

        //then
        assertThat(outputUser).isNotEqualTo(inputUser);
    }

    @Test
    void itShouldFindUserByEmail() {
        //given
        String email = "email@mail.com";

        ShopUser inputUser = underTest.save(new ShopUser(
                "JustSomeUser", "pass", email, true
        ));

        //when
        ShopUser outputUser = underTest.findByEmail(email);

        //then
        assertThat(outputUser).isEqualTo(inputUser);
    }

    @Test
    void itShouldNotFindUserByEmail() {
        //given
        String email = "email@mail.com";
        String wrongEmail = "email123@mail.com";

        ShopUser inputUser = underTest.save(new ShopUser(
                "JustSomeUser", "pass", email, true
        ));

        //when
        ShopUser outputUser = underTest.findByEmail(wrongEmail);

        //then
        assertThat(outputUser).isNotEqualTo(inputUser);
    }

    @Test
    void itShouldFindUserByRoleIdAndUsernameContaining() {
        //given
        Role role = roleRepository.save(new Role(Role.RoleType.USER));
        String searchWord = "JustSomeUser";
        List<ShopUser> users = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ShopUser user = new ShopUser(searchWord + i, "pass", searchWord + i + "@mail.com", true);
            user.setRole(role);
            users.add(underTest.save(user));
        }

        System.out.println(underTest.findAll());

        //when
        List<ShopUser> outputUsers =
                underTest.findByRoleIdAndUsernameContaining(role.getId(), searchWord, PageRequest.of(0, 10)).getContent();
        //then
        assertThat(outputUsers).isEqualTo(users);
    }

    @Test
    void itShouldFindUserByUsernameContaining() {
        //given
        String username = "BasicUser";
        String stringContaining = "ser";

        ShopUser inputUser = underTest.save(new ShopUser(
                username, "pass", "asd@asd.com", true
        ));

        //when
        ShopUser outputUser = underTest
                .findByUsernameContaining(stringContaining, PageRequest.of(0,10)).getContent().get(0);

        //then
        assertThat(outputUser).isEqualTo(inputUser);
    }
}