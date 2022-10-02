package com.example.customerprofile;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldPassHealthCheck() {
        var responseEntity = restTemplate.getForEntity("/actuator/health", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldExposeOpenApiEndpoint() {
        var responseEntity = restTemplate.getForEntity("/api-docs", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldPersistCustomerProfileOnPostRequest() throws Exception {
        var response = restTemplate.postForEntity(
                "/api/customer-profiles",
                createCustomerProfileRequest(),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        Long profileId = extractCustomerProfileFromLocationHeader(response);

        response = restTemplate.getForEntity("/api/customer-profiles/" + profileId, String.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        var expectedJson = "{" +
                "\"id\": " + profileId + "," +
                "\"firstName\": \"Joe\"," +
                "\"lastName\": \"Doe\"," +
                "\"email\": \"joe.doe@test.org\"" +
                "}";
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    private HttpEntity<String> createCustomerProfileRequest() {
        var body = "{" +
                "\"firstName\": \"Joe\"," +
                "\"lastName\": \"Doe\"," +
                "\"email\": \"joe.doe@test.org\"" +
                "}";

        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private Long extractCustomerProfileFromLocationHeader(ResponseEntity<String> responseEntity) {
        var location = responseEntity.getHeaders().getLocation();
        return Long.decode(location.getPath().substring(location.getPath().lastIndexOf("/") + 1));
    }
}
