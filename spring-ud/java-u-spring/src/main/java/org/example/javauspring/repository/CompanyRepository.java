package org.example.javauspring.repository;

import org.example.javauspring.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findCompanyByCompanyId(Integer id);

    // Optional, Entity, Future
//    @Query(name = "Company.findByName")
    @Query("select c from Company c where c.name = :name") // HQL
    Optional<Company> findByName(@Param("name") String name);

    // Containing = like '% %', Collection, Stream
    List<Company> findAllByNameContainingIgnoreCase(String fragment);
}
