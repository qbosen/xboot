package com.github.qbosen.dddboot.shared;

import com.github.qbosen.dddboot.shared.model.TestModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author qiubaisen
 * @date 2021/3/28
 */
public class JacocoTest {
    @Test
    public void test_jacoco() {
        TestModel testModel = new TestModel();
        Assertions.assertFalse(testModel.isPositive());
    }
}
