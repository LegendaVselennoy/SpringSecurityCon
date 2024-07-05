package org.example.reactivesecurity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.opaqueToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Делает Spring Boot ответственным за управление контекстом Spring для тестов
@SpringBootTest
// Позволяет Spring Boot автоматически настраивать MockMvc. Как следствие, в контекст Spring добавляется объект типа MockMvc.
//@AutoConfigureMockMvc
// Запрашивает Spring Boot для автоматической настройки WebTestClient, который мы используем для тестов
@AutoConfigureWebTestClient
public class MainTests {

    // Внедряет объект MockMvc, который мы используем для тестирования конечной точки
    // Для нереактивного
//    @Autowired
    private MockMvc mvc;
   // Внедряет экземпляр WebTestClient, сконфигурированный Spring Boot, из контекста Spring
    @Autowired
    private WebTestClient client;



    // При выполнении GET-запроса по пути /hello мы ожидаем получить ответ со статусом Unauthorized
    @Test
    // Вызывает метод с имитацией пользователя, прошедшего проверку подлинности.
    // Устанавливает имя пользователя для фиктивного пользователя
    @WithMockUser(username = "mary")
    // Загружает пользователя John с помощью UserDetailsService для выполнения тестового сценария
//    @WithUserDetails("user")
    public void helloUnauthenticated() throws Exception {
        // В этом случае при выполнении запроса GET по пути /hello мы ожидаем, что состояние ответа будет OK
        mvc.perform(get("/hello")
                        .with(user("mary")))
//                .andExpect(status().isUnauthorized());
                .andExpect(content().string("Hello, mary!"))
                .andExpect(status().isOk());
    }

    // Проверка подлинности с неправильными учетными данными
    @Test
    public void helloAuthenticatingWithInvalidUser() throws Exception {
        mvc.perform(
                        get("/hello")
                                .with(httpBasic("mary","12345")))
                .andExpect(status().isUnauthorized());
    }

    // Проверка подлинности с использованием правильных учетных данных
    @Test
    public void helloAuthenticatingWithValidUser() throws Exception {
        mvc.perform(
                        get("/hello")
                                .with(httpBasic("john","12345")))
                .andExpect(status().isOk());
    }

    // Аутентификация с помощью входа в форму с недопустимым набором учетных данных
    @Test
    public void loggingInWithWrongUser() throws Exception {
        mvc.perform(formLogin()
                        .user("joey").password("12345"))
                .andExpect(header().exists("failed"))
                .andExpect(unauthenticated());
    }

    // При проверке подлинности с помощью пользователя, у которого нет прав на чтение, приложение перенаправляет пользователя по пути /error
    @Test
    public void loggingInWithWrongAuthority() throws Exception {
        mvc.perform(formLogin()
                        .user("bill").password("12345")
                )
                .andExpect(redirectedUrl("/error"))
                .andExpect(status().isFound())
                .andExpect(authenticated());
    }

    // При проверке подлинности с помощью пользователя, имеющего права на чтение, приложение перенаправляет пользователя по пути /home.
    @Test
    public void loggingInWithCorrectAuthority() throws Exception {
        mvc.perform(formLogin()
                        .user("john").password("12345")
                )
                .andExpect(redirectedUrl("/home"))
                .andExpect(status().isFound())
                .andExpect(authenticated());
    }


    // Настраивает макет маркера JWT для проверки подлинности сервера ресурсов с использованием непрозрачных маркеров
    @Test
    void demoEndpointSuccessfulAuthenticationTest() throws Exception {
        mvc.perform(
                        get("/demo").with(jwt()))
                .andExpect(status().isOk());
    }

    // При вызове конечной точки без CSRF-токена состояние ответа HTTP — 403 Forbidden.
    @Test
    public void testHelloPOST() throws Exception {
        mvc.perform(post("/hello"))
                .andExpect(status().isForbidden());
    }

    // При вызове конечной точки с маркером CSRF состояние ответа HTTP — 200 OK.
    @Test
    public void testHelloPOSTWithCSRF() throws Exception {
        mvc.perform(post("/hello").with(csrf()))
                .andExpect(status().isOk());
    }


    // Использование макета непрозрачного маркера для проверки подлинности сервера ресурсов
    @Test
    void demoEndpointSuccessfulAuthenticationTest1() throws Exception {
        mvc.perform(get("/demo").with(opaqueToken()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCORSForTestEndpoint() throws Exception {
    // Выполняет запрос HTTP OPTIONS к конечной точке, запрашивая значение для заголовков CORS.
        mvc.perform(options("/test")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "https://www.example.com")
                )  // Проверяет значения заголовков в соответствии с конфигурацией, которую мы сделали в приложении
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST"))
                .andExpect(status().isOk());
    }


//    @Test
//    void testNameServiceWithNoUser() {
//        assertThrows(AuthenticationException.class,
//                () -> nameService.getName());
//    }
//    @Test
//    @WithMockUser(authorities = "read")
//    void testNameServiceWithUserButWrongAuthority() {
//        assertThrows(AccessDeniedException.class,
//                () -> nameService.getName());
//    }
//    @Test
//    @WithMockUser(authorities = "write")
//    void testNameServiceWithUserButCorrectAuthority() {
//        var result = nameService.getName();
//        assertEquals("Fantastico", result);
//    }


    @Test
    // Использует аннотацию @WithMockUser для определения фиктивного пользователя для теста.
    @WithMockUser
    void testCallHelloWithValidUser() {
        // Производит обмен и проверяет результат
        client.get()
                .uri("/hello")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testCallHelloWithValidUserWithMockUser() {
        // Перед выполнением GET-запроса изменяет вызов для использования фиктивного пользователя
        client.mutateWith(mockUser())
                .get()
                .uri("/hello")
                .exchange()
                .expectStatus().isOk();
    }
}
