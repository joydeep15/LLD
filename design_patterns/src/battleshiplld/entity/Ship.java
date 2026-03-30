package battleshiplld.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Ship {
    private final String name;
    private final List<Cell> occupiedCells;
    private boolean isDestroyed;

    public boolean isHit(Cell cell) {
        if (isDestroyed) {
            throw new IllegalStateException("Ship is already destroyed");
        }
        if (!occupiedCells.contains(cell)) {
            return false;
        }
        isDestroyed = true;
        return true;
    }
}
