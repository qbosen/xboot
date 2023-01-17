package top.abosen.xboot.objectdiffer;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2023/1/11
 */
interface DiffFormatter {

    String format(List<Difference> differences);
}


