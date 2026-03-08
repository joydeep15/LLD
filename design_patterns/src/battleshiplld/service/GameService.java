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
        Player player1 = new Player("Player1", new ArrayList<>());
        Player player2 = new Player("Player2", new ArrayList<>());
        player1.setFiringStrategy(new RandomFiringStrategy());
        player2.setFiringStrategy(new RandomFiringStrategy());
        board.addPlayer(player1);
        board.addPlayer(player2);
        board.assignCells(player1, true);
        board.assignCells(player2, false);
        this.game = new Game(board, null, GameState.GAME_INITIALIZED);
    }

    public void addShip(String shipName, int size, int xa, int ya, int xb, int yb){
        Ship s1 = ShipFactory.createShip(shipName, xa, ya, size);
        Ship s2 = ShipFactory.createShip(shipName, xb, yb, size);
        game.getBoard().getPlayers().get(0).addShip(s1);
        game.getBoard().getPlayers().get(1).addShip(s2);
    }

    public void viewBattleField() {
        game.getBoard().viewBoard();
    }


    public void startGame() throws InterruptedException {
        int turn = 0;
        Player attacker = null;
        Player defender = null;
        while(game.getBoard().getPlayers().get(0).remainingShips() != 0 && game.getBoard().getPlayers().get(1).remainingShips() != 0){
            attacker = game.getBoard().getPlayers().get(turn);
            defender = game.getBoard().getPlayers().get((turn+1)%2);
            System.out.println("Attacker: " + attacker.getName() + " Defender: " + defender.getName());
            Thread.sleep(2000);
            Cell fired = attacker.fire(defender);
            System.out.printf("%s fired missile at %s at (%d, %d)\n", attacker.getName(), defender.getName(), fired.getX(), fired.getY());
            defender.registerFire(fired);
            List<Ship> destroyed = defender.fetchDestroyedShips();
            if(!destroyed.isEmpty()){
                for(Ship ship : destroyed){
                    System.out.println("HIT! Destroyed: " + defender.getName() + "-" +ship.getName());
                }
            }else {
                System.out.println("no destroyed ships");
            }
            turn = (turn+1)%2;
            Thread.sleep(2000);
            System.out.println();
            System.out.println();
        }
        if(game.getBoard().getPlayers().get(0).remainingShips() == 0){
            System.out.printf("Game over, player: %s wins\n", game.getBoard().getPlayers().get(1).getName());
        }else {
            System.out.printf("Game over, player: %s wins\n", game.getBoard().getPlayers().get(0).getName());
        }
    }
}
