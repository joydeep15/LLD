package battleshiplld.service;

import battleshiplld.entity.*;
import battleshiplld.enums.GameState;
import battleshiplld.factory.ShipFactory;
import battleshiplld.strategy.RandomFiringStrategy;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class GameService {
    Game game;

    public void initGame(int size){
        Board board = Board.builder().players(new ArrayList<>()).size(size).build();
        for (String name : List.of("Player1", "Player2")) {
            Player player = new Player(name, new ArrayList<>());
            player.setFiringStrategy(new RandomFiringStrategy());
            board.addPlayer(player);
        }
        board.assignCells(board.getPlayers().get(0), true);
        board.assignCells(board.getPlayers().get(1), false);
        game = new Game(board, null, GameState.GAME_INITIALIZED);
    }

    public void addShip(String shipName, int size, int xa, int ya, int xb, int yb){
        List<Player> players = game.getBoard().getPlayers();
        players.get(0).addShip(ShipFactory.createShip(shipName, xa, ya, size));
        players.get(1).addShip(ShipFactory.createShip(shipName, xb, yb, size));
    }

    public void viewBattleField() {
        game.getBoard().viewBoard();
    }

    public void startGame() throws InterruptedException {
        int turn = 0;
        List<Player> players = game.getBoard().getPlayers();
        while (players.get(0).remainingShips() != 0 && players.get(1).remainingShips() != 0) {
            Player attacker = players.get(turn);
            Player defender = players.get((turn + 1) % 2);
            System.out.println("Attacker: " + attacker.getName() + " Defender: " + defender.getName());
            Thread.sleep(2000);
            Cell fired = attacker.fire(defender);
            System.out.printf("%s fired missile at %s at (%d, %d)\n", attacker.getName(), defender.getName(), fired.getX(), fired.getY());
            defender.registerFire(fired);
            List<Ship> destroyedShips = defender.fetchDestroyedShips();
            if (destroyedShips.isEmpty()) {
                System.out.println("no destroyed ships");
            } else {
                destroyedShips.forEach(ship -> System.out.println("HIT! Destroyed: " + defender.getName() + "-" + ship.getName()));
            }
            turn = (turn + 1) % 2;
            Thread.sleep(2000);
            System.out.println("\n");
        }
        System.out.printf("Game over, player: %s wins\n", players.get(players.get(0).remainingShips() == 0 ? 1 : 0).getName());
    }
}
