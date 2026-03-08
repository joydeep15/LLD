package battleshiplld.entity;

import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.util.Set;


@Builder
public class Board {
    @Getter private final List <Player> players;
    @Getter private final int size;

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void assignCells(Player player, boolean left) {
        //assign left half of all the cells
        // x -> 0 -> n/2-1, n/2 -> n-1
        //y -> 0 -> n-1
        int xStart = left ? 0 : size/2;
        int xEnd = left ? size/2 -1 : size-1;
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell(j, i);
                player.addCell(cell);
            }
        }
    }


    public void viewBoard() {
        String[][] cells = new String[size][size];
        for(Player player : players) {
            Set<Cell> scell = player.getAvailableCells();
            for (Cell cell : scell) {
                cells[cell.getX()][cell.getY()] = player.getName();
            }
            scell = player.getDiscardedCells();
            for (Cell cell : scell) {
                cells[cell.getX()][cell.getY()] = player.getName() + "-L";
            }
            for(Ship s : player.getShips()){
                for (Cell cell : s.getOccupiedCells()) {
                    cells[cell.getX()][cell.getY()] = player.getName() + "-" +  s.getName();
                    if(s.isDestroyed()){
                        cells[cell.getX()][cell.getY()] = "X";
                    }
                }
            }
        }
        for (int i = size-1; i >= 0; i--) {
            for (int j = 0; j < size; j++) {
                System.out.print(cells[i][j] + "\t");
            }
            System.out.println();
        }



    }
}
