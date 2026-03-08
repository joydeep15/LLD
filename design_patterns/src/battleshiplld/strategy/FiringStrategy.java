package battleshiplld.strategy;

import battleshiplld.entity.Cell;
import battleshiplld.entity.Player;

public interface FiringStrategy {
    Cell fire(Player player);
}
