package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @DisplayName("Delete Tests")
    @org.junit.jupiter.api.Nested
    class DeleteTests {

        public Beer beerToDelete() {
            Random rand = new Random();
            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Delete Me")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }


        @Test
        void deleteBeerHttpBasic() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic("spring", "guru")))
                    .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void deleteBeerHttpBasicNotAdmin(String user, String pwd) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicNotAuth() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }

    }

    @DisplayName("List Beers")
    @Nested
    class ListBeers {
        @Test
        void findBeers() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeersAUTH(String user, String pwd) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/").with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }

    }

    @DisplayName("Get Beer by ID")
    @Nested
    class GetBeerById {
        @Test
        void findBeerById() throws Exception {
            Beer beer = beerRepository.findAll().get(0);
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }
        @ParameterizedTest
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerByIdAuth(String user, String pwd) throws Exception {
            Beer beer = beerRepository.findAll().get(0);
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + beer.getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Find by UPS")
    @Nested
    class FindByUps {
        @Test
        void findBeerByUpc() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beerUpc/0631234200036"))
                    .andExpect(status().isUnauthorized());
        }
        @ParameterizedTest
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerByUpcAuth(String user, String pwd) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beerUpc/0631234200036")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

}