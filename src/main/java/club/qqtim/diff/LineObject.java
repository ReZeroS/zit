package club.qqtim.diff;

import club.qqtim.common.ConstantVal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: LineObject
 * @Author rezeros.github.io
 * @Date: 2020/12/16
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class LineObject  {

    /**
     * sync, plus, minus
     */
    private String action;

    /**
     * row number, default from zero
     */
    private Integer index;

    /**
     * can not be null
     * line content which contains the whole content of the current line
     */
    private String lineContent;

    public LineObject(Integer index, String lineContent) {
        this.index = index;
        this.lineContent = lineContent;
    }

    @Override
    public String toString() {
        String actionTxt = ConstantVal.EMPTY;
        if (ConstantVal.SYNC.equals(this.action)) {
            actionTxt = ConstantVal.SINGLE_SPACE;
        }
        if (ConstantVal.PLUS.equals(this.action)) {
            actionTxt = ConstantVal.PLUS_SYMBOL;
        }
        if (ConstantVal.MINUS.equals(this.action)) {
            actionTxt = ConstantVal.MINUS_SYMBOL;
        }
        return actionTxt + ConstantVal.SINGLE_SPACE + index + ConstantVal.SINGLE_SPACE + lineContent;
    }

}
