package org.example.javauspring.repository;

import jakarta.persistence.EntityManager;
import org.example.javauspring.entity.Company;
import org.example.javauspring.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
//@Commit появится в базе
public class CompanyRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void checkSort() {
        var sortBy = Sort.sort(User.class);
        var sort = sortBy.by(User::getFirstname).and(sortBy.by(User::getLastname));

//        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "firstname");
//        Sort.by("firstname", "lastname");
    }

    @Test
    void findById() {
        var company = entityManager.find(Company.class, 1L);
        assertNotNull(company);
        assertThat(company.getLocales()).hasSize(1); // because company is null, исправим
        // lazy exception без транзакции. Должны зафиксироваться.
    }

    @Test
    void checkFindByQueries() {
        companyRepository.findByName("Google");
        companyRepository.findAllByNameContainingIgnoreCase("a");
    }

    @Test
    void delete() {
        var company = companyRepository.findCompanyByCompanyId(1);
        companyRepository.delete(company);
        // по умолчанию метод delete не выполнится до тех пор, пока не пройдет коммит транзакции
        // либо вызовем flush явно. После удаления можем сделать запрос
        entityManager.flush();
        assertTrue(companyRepository.findById(1).isEmpty());
    }

    @Test
    void save() {
        var company = Company.builder()
                .name("Apple1")
                .locales(Map.of(
                        "key1","Apple описание",
                        "key2","Apple description"
                ))
                .build();
        entityManager.persist(company);
        assertNotNull(company.getCompanyId());
    }
}
