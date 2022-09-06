package top.abosen.xboot.propertyadaptor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
class StringIntHandlerTest {
    PropertyHandler intMustPositiveOrZero = new PropertyHandler() {
        @Override
        public boolean shouldHandle(IHandlerContext context) {
            Class<?> propertyType = context.getCurrentDescriptor().getPropertyType();
            if (propertyType.equals(int.class) || propertyType.equals(Integer.class)) {
                return true;
            }
            if (propertyType.isPrimitive() || propertyType.isArray() || propertyType.isAnnotation() || propertyType.isEnum() || propertyType.isInterface()) {
                return false;
            }
            return !propertyType.getPackage().getName().startsWith("java.");
        }

        @Override
        public boolean handle(IHandlerContext context) {
            Object value = PropertyHandler.get(context.getCurrentDescriptor(), context.getCurrentParent());
            Class<?> type = context.getCurrentDescriptor().getPropertyType();
            if (type == int.class || type == Integer.class) {
                Integer integer = ((Integer) value);
                if (integer != null && integer < 0) {
                    System.out.printf("Set config property [%s] to 0 %n", context.currentPath());
                    PropertyHandler.set(context.getCurrentDescriptor(), context.getCurrentParent(), 0);
                    return true;
                }
            }
            return false;
        }
    };
    PropertyHandler stringMustCaps = new PropertyHandler() {
        @Override
        public boolean shouldHandle(IHandlerContext context) {
            Class<?> propertyType = context.getCurrentDescriptor().getPropertyType();
            if (propertyType.isPrimitive() || propertyType.isArray() || propertyType.isAnnotation() || propertyType.isEnum() || propertyType.isInterface()) {
                return false;
            }
            if (propertyType.isAssignableFrom(String.class)) {
                return true;
            }
            return !propertyType.getPackage().getName().startsWith("java.");
        }

        @Override
        public boolean handle(IHandlerContext context) {
            Object value = PropertyHandler.get(context.getCurrentDescriptor(), context.getCurrentParent());
            if (value == null) {
                return false;
            }

            if (String.class.equals(context.getCurrentDescriptor().getPropertyType())) {
                System.out.printf("Set config property [%s] CAPS%n", context.currentPath());
                PropertyHandler.set(context.getCurrentDescriptor(), context.getCurrentParent(), ((String) value).toUpperCase());
                return true;
            }
            return false;
        }
    };

    @Test
    public void should_be_caps() {
        ConfigurationPropertyBeanPostProcessor processor = YamlTest.postProcessor(true, "@null", stringMustCaps);
        TestConf bean = new TestConf("@null", -1, new TestConf("some thing", 1, null));

        processor.postProcessAfterInitialization(bean, "test_bean");
        assertEquals("SOME THING", bean.getNest().getStr());
    }

    @Test
    public void test_complex() {
        ConfigurationPropertyBeanPostProcessor processor = YamlTest.postProcessor(true, "@null",
                new NullStringPropertyHandler(), intMustPositiveOrZero, stringMustCaps);
        TestConf bean = new TestConf("@null", -1, new TestConf("some thing", 1, null));

        processor.postProcessAfterInitialization(bean, "test_bean");

        assertNull(bean.getStr());
        assertEquals(0, bean.getInteger());
        assertEquals("SOME THING", bean.getNest().getStr());
        assertEquals(1, bean.getNest().getInteger());
    }
}
