package org.example.javauspring.repository;

import org.example.javauspring.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

//    @Test
//    void checkProjections() {
//        var users = userRepository.findAllByCompanyId(1);
//        System.out.println(users);
//    }

    @Test
    void checkQueries() {
       var users = userRepository.findAllBy("a","ov");
       assertThat(users).hasSize(0);
    }

    @Test
    void checkPageable() {
        var pageable = PageRequest.of(1, 2, Sort.by("users_id"));
        var users = userRepository.findAllByPage(pageable);
        assertThat(users).hasSize(1);
    }

    @Test
    void checkUpdate() {
       var resultCount = userRepository.updateRole(Role.ADMIN,1,3);
       assertEquals(2, resultCount);
    }
}
