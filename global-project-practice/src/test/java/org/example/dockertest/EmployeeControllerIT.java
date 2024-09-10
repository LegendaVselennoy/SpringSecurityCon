package org.example.dockertest;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@Testcontainers
public class EmployeeControllerIT extends AbstractionBaseTest{

//    @Container
//    private static final MySQLContainer<?> mySQLContainer
//            = new MySQLContainer<>("mysql:latest")
//            .withDatabaseName("test")
//            .withUsername("root")
//            .withPassword("pass");
//
//    @DynamicPropertySource
//    public static void configureMySQL(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasourse.url", mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasourse.username", mySQLContainer::getUsername);
//        registry.add("spring.datasourse.password", mySQLContainer::getPassword);
//    }

//    @Test
//    void test() {
//        assertTrue(mySQLContainer.isRunning());
//    }
}
