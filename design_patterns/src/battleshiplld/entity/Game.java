package battleshiplld.entity;

import battleshiplld.enums.GameState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Game {
    private Board board;
    private Player winner;
    private GameState gameState = GameState.GAME_NOT_STARTED;
}
