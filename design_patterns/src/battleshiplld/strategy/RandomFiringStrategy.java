package battleshiplld.strategy;

import battleshiplld.entity.Cell;
import battleshiplld.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomFiringStrategy implements FiringStrategy {

    @Override
    public Cell fire(Player player) {
        List<Cell> availableCells = new ArrayList<>(player.getAvailableCells());
        Random random = new Random();
        return availableCells.get(random.nextInt(availableCells.size()));
    }
}
