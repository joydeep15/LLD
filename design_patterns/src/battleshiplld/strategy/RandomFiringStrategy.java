package battleshiplld.strategy;

import battleshiplld.entity.Cell;
import battleshiplld.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFiringStrategy implements FiringStrategy {
    @Override
    public Cell fire(Player player) {
        List<Cell> availableCells = new ArrayList<>(player.getAvailableCells());
        return availableCells.get(ThreadLocalRandom.current().nextInt(availableCells.size()));
    }
}
