package battleshiplld.factory;

import battleshiplld.entity.Cell;
import battleshiplld.entity.Ship;

import java.util.ArrayList;
import java.util.List;

public class ShipFactory {
    public static Ship createShip(String id, int center_x, int center_y, int size) {
        int minX = center_x - size/2;
        int maxX = center_x + size/2;
        int minY = center_y - size/2;
        int maxY = center_y + size/2;
        List<Cell> cells = new ArrayList<>();
        for(int i = minX; i < maxX; i++){
            for(int j = minY; j < maxY; j++){
                Cell cell = new Cell(j, i);
                cells.add(cell);
            }
        }
        return new Ship(id, cells, false);
    }
}
