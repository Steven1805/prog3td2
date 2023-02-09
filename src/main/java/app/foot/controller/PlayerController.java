package app.foot.controller;

import app.foot.controller.rest.Player;
import app.foot.controller.rest.mapper.PlayerRestMapper;
import app.foot.model.UpdatePlayer;
import app.foot.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class PlayerController {
    private final PlayerRestMapper mapper;
    private final PlayerService service;

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return service.getPlayers().stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());
    }

    @PostMapping("/players")
    public List<Player> addPlayers(@RequestBody List<Player> toCreate) {
        List<app.foot.model.Player> domain = toCreate.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
        return service.createPlayers(domain).stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());
    }

    @PutMapping("/players")
    public List<Player> putPlayers(@RequestBody List<UpdatePlayer> toUpdate) {
        List<app.foot.model.UpdatePlayer> domain = toUpdate.stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());
        return service.updatePlayer(domain).stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());
    }
}
