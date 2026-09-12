package com.pokearena.security;

import com.pokearena.entity.Trainer;
import com.pokearena.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TrainerRepository trainerRepository;

    @BeforeEach
    void setUp() {
        if (trainerRepository.findByName("AshKetchum").isEmpty()) {
            Trainer ash = new Trainer();
            ash.setName("AshKetchum");
            ash.setEmail("ash@pokearena.com");
            ash.setPassword("password123");
            ash.setRole("ROLE_TRAINER");
            trainerRepository.save(ash);
        }
    }

    @Test
    @DisplayName("The open endPoints can be freely accessed")
    void testPublicSpecisEndPoint() throws Exception {
        mockMvc.perform(get("/api/species"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("The Leaderboard should be publicly accessible")
    void testPublicLeaderboardEndPoint() throws Exception {
        mockMvc.perform(get("/api/leaderboard"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Swagger and OpenAPI documentation should be publicly accessible")
    void testPublicSwaggerEndpoints() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }



    @Test
    @DisplayName("Private Endpoints should not be accessible without token")
    void testPrivateTeamsEndPoint() throws Exception {
        String tempJson = """
                {
                    "trainerId": 1,
                    "teamName": "Kanto ka Gunda",
                    "members": []
                }
                """;

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tempJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should not be able to simulate without token")
    void testSimulationEndPoint() throws Exception {
        String tempJson = """
                {
                    "teamAId": 1,
                    "teamBId": 2
                }
                """;

        mockMvc.perform(post("/api/battles/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tempJson))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Request with valid token should pass through security filter")
    void testValidTokenToProtected() throws Exception {
        String validToken = jwtService.generateToken("AshKetchum");

        mockMvc.perform(post("/api/teams")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result -> {
                    int statusCode = result.getResponse().getStatus();
                    assertNotEquals(403, statusCode);
                });
    }
}