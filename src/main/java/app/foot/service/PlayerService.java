package app.foot.service;

import app.foot.model.Player;
import app.foot.model.UpdatePlayer;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.mapper.PlayerMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository repository;
    private final PlayerMapper mapper;

    public List<Player> getPlayers() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Player> createPlayers(List<Player> toCreate) {
        return repository.saveAll(toCreate.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toUnmodifiableList())).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }
    public PlayerEntity updateNamePlayer(Integer id, String playerName ){
        PlayerEntity player= repository.findById(id).get();
        player.setName(playerName);
        return repository.save(player);
    }
    public PlayerEntity updateIsGuardian(Integer id, Boolean guardian ){
        PlayerEntity player= repository.findById(id).get();
        player.setGuardian(guardian);
        return repository.save(player);
    }
    public List<Player> updatePlayer(List<UpdatePlayer> toUpdate) {
        return repository.saveAll(toUpdate.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toUnmodifiableList())).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

}
