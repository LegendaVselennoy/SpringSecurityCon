package org.example.javauspring.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.example.javauspring.dto.PersonalInfo;
import org.example.javauspring.entity.Role;
import org.example.javauspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.firstname like %:firstname% and u.lastname like %:lastname%")
    List<User> findAllBy(String firstname, String lastname);
//    List<User> findAllByFirstnameContainingAndLastnameContaining(String firstname, String lastname);

    @Query(value = "SELECT u.* FROM test.public.users u WHERE u.username = :username", // к базе SQL
          nativeQuery = true)
    List<User>findAllByUsername(String username);

    // @Modifying(clearAutomatically = true, flushAutomatically = true) lazy exception возможен
    @Modifying
    @Query("update User u set u.role = :role where u.users_id in (:ids)")
    int updateRole(Role role,Integer...ids);

    // перенастройка Lazy, Eager
//    @EntityGraph("User.company")
//    @EntityGraph(attributePaths = {"company"})
    @EntityGraph(attributePaths = {"company", "company.locales"})  // проблемы с page (пагинацией)
    @Query(value = "select u from User u",
    countQuery = "select count(distinct u.firstname) from User u")
    Page<User> findAllByPage(Pageable pageable);

    // @Lock(LockModeType.NONE)  блокировки
//    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "50"))
    List<User> findTop3ByBirthDateBefore(LocalDate birthDate, Sort sort);

//    List<PersonalInfo> findAllByCompanyId(Integer userId);
}
