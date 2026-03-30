package battleshiplld.factory;

import battleshiplld.entity.Cell;
import battleshiplld.entity.Ship;

import java.util.ArrayList;
import java.util.List;

public class ShipFactory {
    public static Ship createShip(String id, int center_x, int center_y, int size) {
        List<Cell> cells = new ArrayList<>();
        for (int i = center_x - size / 2; i < center_x + size / 2; i++) {
            for (int j = center_y - size / 2; j < center_y + size / 2; j++) {
                cells.add(new Cell(j, i));
            }
        }
        return new Ship(id, cells, false);
    }
}
