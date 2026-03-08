package battleshiplld;

import battleshiplld.service.GameService;

public class GameCoordinator {
    public static void main(String[] args) throws InterruptedException {
        GameService gameService = new GameService();
        gameService.initGame(6);
        gameService.viewBattleField();
        System.out.println();
        System.out.println();
        gameService.addShip("sh1", 2, 1, 5, 4, 4);
        gameService.viewBattleField();
        gameService.startGame();
        gameService.viewBattleField();
    }
}
