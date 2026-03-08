package battleshiplld.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Ship {
    String name;
    List<Cell> occupiedCells;
    boolean isDestroyed;

    public boolean isHit(Cell cell) {
        if(isDestroyed) {
            throw new IllegalStateException("Ship is already destroyed");
        }
        if (occupiedCells.contains(cell)) {
            isDestroyed = true;
            return true;
        }else return false;
    }

}
