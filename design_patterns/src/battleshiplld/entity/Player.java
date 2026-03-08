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
public class Player {
    @Getter final String name;
    @Getter private final List<Ship> ships;

    @Getter
    Set<Cell> availableCells = new HashSet<>();

    @Getter
    Set<Cell> discardedCells = new HashSet<>();

    @Setter
    private FiringStrategy firingStrategy;
    List<Ship> shipsHitInRound = new ArrayList<>();

    public void addShip(Ship ship) {
        for (Cell cell : ship.getOccupiedCells()){
            if (!availableCells.contains(cell)){
                throw new RuntimeException("cannot accomodate ship");
            }
        }
        ships.add(ship);
    }

    void addCell(Cell cell) {
        availableCells.add(cell);
    }

    public int remainingShips() {
        int available = 0;
        for (Ship ship : ships) {
            if(!ship.isDestroyed()){
                available++;
            }
        }
        return available;
    }

    public Cell fire(Player opponent) {
        Cell fired = firingStrategy.fire(opponent);
        return fired;
    }

    public void registerFire(Cell cell) {
        this.shipsHitInRound.clear();
        for (Ship ship : ships) {
            if(ship.isHit(cell)) {
                System.out.println("aiyo!");
                availableCells.removeAll(ship.getOccupiedCells());
                discardedCells.addAll(ship.getOccupiedCells());
                shipsHitInRound.add(ship);
            }
        }
        availableCells.remove(cell);
        discardedCells.add(cell);
    }

    public List<Ship> fetchDestroyedShips() {
        List<Ship> toReturn = new ArrayList<>(this.shipsHitInRound);
        this.shipsHitInRound.clear();
        return toReturn;
    }



}
