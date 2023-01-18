package top.abosen.xboot.objectdiffer;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2023/1/11
 */
public interface DiffFormatter {

    String format(List<Difference> differences);
}


