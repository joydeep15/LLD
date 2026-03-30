package battleshiplld.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Cell {
    @Getter private final int x;
    @Getter private final int y;
}
