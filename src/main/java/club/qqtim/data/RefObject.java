package club.qqtim.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @title: RefObject
 * @Author rezeros.github.io
 * @Date: 2020/12/8
 * @Version 1.0.0
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RefObject {

    private String refName;

    private RefValue refValue;

}
