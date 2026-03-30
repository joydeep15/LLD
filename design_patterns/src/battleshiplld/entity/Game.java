package battleshiplld.entity;

import battleshiplld.enums.GameState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Game {
    private final Board board;
    private final Player winner;
    private final GameState gameState;
}
