package top.abosen.xboot.spring.validator;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiubaisen
 * @date 2021/5/6
 */

@UtilityClass
public class NumberSignHelper {

    final short SHORT_ZERO = (short) 0;

    final byte BYTE_ZERO = (byte) 0;

    int signum(Long number) {
        return Long.signum(number);
    }

    int signum(Integer number) {
        return Integer.signum(number);
    }

    int signum(Short number) {
        return number.compareTo(SHORT_ZERO);
    }

    int signum(Byte number) {
        return number.compareTo(BYTE_ZERO);
    }

    int signum(BigInteger number) {
        return number.signum();
    }

    int signum(BigDecimal number) {
        return number.signum();
    }

    int signum(Number value) {
        return Double.compare(value.doubleValue(), 0D);
    }

}
