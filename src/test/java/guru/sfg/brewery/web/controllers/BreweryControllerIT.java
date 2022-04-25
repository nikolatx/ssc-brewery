package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BreweryControllerIT extends BaseIT {



    @Test
    void testListBreweriesCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/brewery/breweries")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testListBreweriesUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/brewery/breweries")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testListBreweriesAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/brewery/breweries")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testListBreweriesNoAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/brewery/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListBreweriesJsonCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/brewery/api/v1/breweries")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testListBreweriesJsonUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/brewery/api/v1/breweries")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testListBreweriesJsonAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/brewery/api/v1/breweries")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testListBreweriesJsonNoAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }

}