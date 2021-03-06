package club.qqtim.diff.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: TinySnake
 * @Author lijie78
 * @Date: 2021/2/19
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Snake {

    private SnakePoint start;

    private SnakePoint middle;

    private SnakePoint end;

}
