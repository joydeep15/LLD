package battleshiplld.entity;

import battleshiplld.strategy.FiringStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public class Player {
    private final String name;
    private final List<Ship> ships;
    private final Set<Cell> availableCells = new HashSet<>();
    private final Set<Cell> discardedCells = new HashSet<>();
    @Setter private FiringStrategy firingStrategy;
    private final List<Ship> shipsHitInRound = new ArrayList<>();

    public void addShip(Ship ship) {
        if (!availableCells.containsAll(ship.getOccupiedCells())) {
            throw new RuntimeException("cannot accomodate ship");
        }
        ships.add(ship);
    }

    void addCell(Cell cell) {
        availableCells.add(cell);
    }

    public int remainingShips() {
        return (int) ships.stream().filter(ship -> !ship.isDestroyed()).count();
    }

    public Cell fire(Player opponent) {
        return firingStrategy.fire(opponent);
    }

    public void registerFire(Cell cell) {
        this.shipsHitInRound.clear();
        for (Ship ship : ships) {
            if(ship.isHit(cell)) {
                availableCells.removeAll(ship.getOccupiedCells());
                discardedCells.addAll(ship.getOccupiedCells());
                shipsHitInRound.add(ship);
            }
        }
        availableCells.remove(cell);
        discardedCells.add(cell);
    }

    public List<Ship> fetchDestroyedShips() {
        List<Ship> destroyedShips = new ArrayList<>(shipsHitInRound);
        shipsHitInRound.clear();
        return destroyedShips;
    }
}
