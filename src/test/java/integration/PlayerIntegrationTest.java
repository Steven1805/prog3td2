package integration;

import app.foot.FootApi;
import app.foot.controller.rest.Player;
import app.foot.model.UpdatePlayer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
class PlayerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    Player player1() {
        return Player.builder()
                .id(1)
                .name("Joureur1")
                .teamName("E1")
                .isGuardian(false)
                .build();
    }

    Player player2() {
        return Player.builder()
                .id(2)
                .name("Joueur2")
                .teamName("E2")
                .isGuardian(false)
                .build();
    }

    Player player3() {
        return Player.builder()
                .id(3)
                .name("Joueur3")
                .teamName("E2")
                .isGuardian(false)
                .build();
    }

    @Test
    void read_players_ok() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/players"))
                .andReturn()
                .getResponse();
        List<Player> actual = convertFromHttpResponse(response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(10, actual.size());
        assertTrue(actual.containsAll(List.of(
                player1(),
                player2(),
                player3())));
    }

    @Test
    void create_players_ok() throws Exception {
        Player toCreate = Player.builder()
                .id(10)
                .name("Joe Doe")
                .isGuardian(false)
                .teamName("E1")
                .build();
        MockHttpServletResponse response = mockMvc
                .perform(post("/players")
                        .content(objectMapper.writeValueAsString(List.of(toCreate)))
                        .contentType("application/json")
                        .accept("application/json"))
                .andReturn()
                .getResponse();
        List<Player> actual = convertFromHttpResponse(response);

        assertEquals(1, actual.size());
        assertEquals(toCreate, actual.get(0).toBuilder().build());
    }

    @Test
    void update_players_ok() throws Exception {
        UpdatePlayer toUpdate = UpdatePlayer.builder()
                .id(9)
                .name("Paul")
                .isGuardian(false)
                .build();
        Player expected = Player.builder()
                .id(9)
                .name("Paul")
                .isGuardian(false)
                .teamName("E3")
                .build();
        MockHttpServletResponse response = mockMvc
                .perform(put("/players")
                        .content(objectMapper.writeValueAsString(List.of(toUpdate)))
                        .contentType("application/json")
                        .accept("application/json"))
                .andReturn()
                .getResponse();
        List<Player> actual = convertFromHttpResponse(response);

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0).toBuilder().build());
    }

    private List<Player> convertFromHttpResponse(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType playerListType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Player.class);
        return objectMapper.readValue(
                response.getContentAsString(),
                playerListType);
    }
}