package battleshiplld.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
public class Board {
    private final List<Player> players;
    private final int size;

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void assignCells(Player player, boolean left) {
        int xStart = left ? 0 : size/2;
        int xEnd = left ? size/2 : size;
        for (int i = xStart; i < xEnd; i++) {
            for (int j = 0; j < size; j++) {
                player.addCell(new Cell(j, i));
            }
        }
    }

    public void viewBoard() {
        String[][] cells = new String[size][size];
        for (Player player : players) {
            markCells(cells, player.getAvailableCells(), player.getName());
            markCells(cells, player.getDiscardedCells(), player.getName() + "-L");
            for (Ship ship : player.getShips()) {
                String value = ship.isDestroyed() ? "X" : player.getName() + "-" + ship.getName();
                for (Cell cell : ship.getOccupiedCells()) {
                    cells[cell.getX()][cell.getY()] = value;
                }
            }
        }
        for (int i = size - 1; i >= 0; i--) {
            for (int j = 0; j < size; j++) {
                System.out.print(cells[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private void markCells(String[][] cells, Set<Cell> playerCells, String value) {
        for (Cell cell : playerCells) {
            cells[cell.getX()][cell.getY()] = value;
        }
    }
}
