## 原理

通过 BeanPostProcessor 扫描所有 ConfigurationProperties Bean 属性,
并根据规则,修改bean属性

## 场景

比如 解决 [spring yaml无法注入null的问题](https://github.com/spring-projects/spring-framework/issues/19986)

> 使用 redis/es 等中间件, 并从 remote config/environment variable 中获取密码配置,
> 其密码可能不存在, yaml中配置值为`null` 会在spring中识别为`''` 导致密码错误.

## 使用

应用此程序,进行如下配置

```yaml
property-adaptor:
  enabled: true
  null-string: "@null"    # 当属性值为此值时,设置为null
```

则 ConfigurationProperties bean 中`string`类型且值为`@null`的值会修改为`null`

### 使用条件 condition

```yaml
property-adaptor:
  enabled: true
  null-string: "@null"
  condition:
    - "RedisProperties#password"
    - "data.redis.RedisProperties#password"
    - "org.springframework.boot.autoconfigure.data.redis.RedisProperties#password"
```

`condition` 可选,默认无条件,即所有属性; 属性条件配置,满足才进行修改;
规则为 `[className]#prop1.prop2`, className可取后缀部分

## 扩展

实现 `PropertyHandler` 接口,并注册为bean
参考 `top.abosen.xboot.propertyadaptor.NullStringPropertyHandler`
或 `top.abosen.xboot.propertyadaptor.StringIntHandlerTest.intMustPositiveOrZero`