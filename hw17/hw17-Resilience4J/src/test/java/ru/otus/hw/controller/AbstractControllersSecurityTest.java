package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
public abstract class AbstractControllersSecurityTest {

    @Autowired
    protected MockMvc mockMvc;

    @DisplayName("Should return expected status")
    @ParameterizedTest(name = "{0} {1} {2} for user {3} should return {5} status")
    @MethodSource("getSecurityTestData")
    void shouldReturnExpectedStatus(String method, String url, String jsonContent,
                                    String userName, String[] roles,
                                    int status, boolean checkLoginRedirection) throws Exception {

        var request = method2RequestBuilder(method, url);

        if (nonNull(userName)) {
            request = request.with(user(userName).roles(roles));
        }

        if(nonNull(jsonContent)) {
            request = request.contentType(APPLICATION_JSON)
                    .content(jsonContent);
        }

        ResultActions resultActions = mockMvc.perform(request)
                .andExpect(status().is(status));

        if (checkLoginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }

    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", (str -> MockMvcRequestBuilders.post(str).with(csrf())),
                        "put", (str -> MockMvcRequestBuilders.put(str).with(csrf())),
                        "delete", (str -> MockMvcRequestBuilders.delete(str).with(csrf())));
        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getSecurityTestData() {
        System.out.println("ABSTRACT!!!!");
        return null;
    }

}