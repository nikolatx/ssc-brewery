package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerParamFilter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03")
                        .queryParam("Api-Key", "spring").queryParam("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerParamFilterBadCreds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03")
                        .queryParam("Api-Key", "spring").queryParam("Api-Secret", "guruxxx"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerBadCreds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03")
                        .header("Api-Key", "spring").header("Api-Secret", "guruXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03")
                .header("Api-Key", "spring").header("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/5bc9789f-a22b-40bf-a556-8744bd891c03")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
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