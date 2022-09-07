package top.abosen.xboot.propertyadaptor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
class NullStringTest {
    @Test
    public void processor_not_work_if_disabled() {
        ConfigurationPropertyBeanPostProcessor processor = YamlTest.postProcessor(false, "@null", new HashSet<>(), new NullStringPropertyHandler());

        TestConf bean = new TestConf("@null", new TestConf("@null", null));
        processor.postProcessAfterInitialization(bean, "test_bean");

        assertEquals("@null", bean.getStr());
        assertEquals("@null", bean.getNest().getStr());
        assertNull(bean.getNest().getNest());
    }

    @Test
    public void processor_not_work_if_null_string_not_match() {
        ConfigurationPropertyBeanPostProcessor processor = YamlTest.postProcessor(true, "@null_str", new HashSet<>(), new NullStringPropertyHandler());

        TestConf bean = new TestConf("@null", new TestConf("@null", null));

        processor.postProcessAfterInitialization(bean, "test_bean");

        assertEquals("@null", bean.getStr());
        assertEquals("@null", bean.getNest().getStr());
        assertNull(bean.getNest().getNest());
    }

    @Test
    public void processor_change_nullable_str() {
        ConfigurationPropertyBeanPostProcessor processor = YamlTest.postProcessor(true, "@null", new HashSet<>(), new NullStringPropertyHandler());
        TestConf bean = new TestConf("@null", new TestConf("@null", null));

        processor.postProcessAfterInitialization(bean, "test_bean");

        assertNull(bean.getStr());
        assertNull(bean.getNest().getStr());
        assertNull(bean.getNest().getNest());
    }

    @Test
    public void processor_ignore_limited() {
        ConfigurationPropertyBeanPostProcessor processor = YamlTest.postProcessor(true, "@null",
                new HashSet<>(Arrays.asList("TestConf#nest.str")), new NullStringPropertyHandler());

        TestConf bean = new TestConf("@null", new TestConf("@null", null));

        processor.postProcessAfterInitialization(bean, "test_bean");

        assertEquals("@null", bean.getStr());
        assertNull(bean.getNest().getStr());
        assertNull(bean.getNest().getNest());
    }
}
