package top.abosen.xboot.objectdiffer;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/1/13
 */
@Slf4j
abstract class DiffFieldParser {

    private static final Map<Class<? extends DiffFieldParser>, DiffFieldParser> instanceMap = new HashMap<>();


    public static String parse(Class<? extends DiffFieldParser> parserClass, Object value) {
        return pickParser(parserClass).parse(value);
    }

    private static DiffFieldParser pickParser(Class<? extends DiffFieldParser> parserClass) {
        DiffFieldParser diffFieldParser;
        if (instanceMap.containsKey(parserClass)) {
            diffFieldParser = instanceMap.get(parserClass);
        } else {
            try {
                diffFieldParser = parserClass.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                log.error("无效的字段解析器类型: {}", parserClass);
                diffFieldParser = new NONE();
            }
            instanceMap.put(parserClass, diffFieldParser);
        }
        return diffFieldParser;
    }

    abstract String parse(Object value);

    static class NONE extends DiffFieldParser {
        @Override
        String parse(Object value) {
            return String.valueOf(value);
        }

    }
}
