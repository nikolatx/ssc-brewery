package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/b27876ab-de54-461c-88c5-3b68c4b4b1b4")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerHttpBasicUserRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerHttpBasicCustomerRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listBeers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void getBeerById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03"))
                .andExpect(status().isOk());
    }

    @Test
    void getBeerByUpc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }
}