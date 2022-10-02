package com.example.customerprofile.api;

import com.example.customerprofile.domain.CustomerProfileService;
import com.example.customerprofile.domain.NewCustomerProfile;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.example.customerprofile.domain.TestData.testCustomerProfile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerProfileController.class)
class CustomerProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerProfileService service;

    @Nested
    class Create {

        @Test
        void shouldDelegateToService() throws Exception {
            when(service.create(any())).thenReturn(testCustomerProfile());

            mockMvc.perform(post("/api/customer-profiles")
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
                            .content("{" +
                                    "\"firstName\": \"Joe\"," +
                                    "\"lastName\": \"Doe\"," +
                                    "\"email\": \"joe.doe@test.org\"" +
                                    "}"))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/api/customer-profiles/123"));

            var profileCaptor = ArgumentCaptor.forClass(NewCustomerProfile.class);
            verify(service).create(profileCaptor.capture());
            var profile = profileCaptor.getValue();
            assertThat(profile.firstName()).isEqualTo("Joe");
            assertThat(profile.lastName()).isEqualTo("Doe");
            assertThat(profile.email()).isEqualTo("joe.doe@test.org");
        }

        @Test
        void shouldReturnBadRequestWhenEmailIsNotProvided() throws Exception {
            mockMvc.perform(post("/api/customer-profiles")
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
                            .content("{" +
                                    "\"firstName\": \"Joe\"," +
                                    "\"lastName\": \"Doe\"" +
                                    "}"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(service);
        }
    }

    @Nested
    class Get {

        @Test
        void shouldDelegateToService() throws Exception {
            when(service.getById(any())).thenReturn(Optional.of(testCustomerProfile()));

            mockMvc.perform(get("/api/customer-profiles/123")
                            .accept(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{" +
                            "\"id\": 123," +
                            "\"firstName\": \"Joe\"," +
                            "\"lastName\": \"Doe\"," +
                            "\"email\": \"joe.doe@test.org\"" +
                            "}"));

            verify(service).getById(123L);
        }

        @Test
        void shouldReturnNotFoundWhenNotExists() throws Exception {
            when(service.getById(any())).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/customer-profiles/123")
                            .accept(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(""));

            verify(service).getById(123L);
        }
    }
}
