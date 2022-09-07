用于在consul中读取服务或kv，并注册到环境中

```yaml
spring:
  application:
    name: demo
  cloud:
    consul:
      host: "${CONSUL_HOST:localhost}"
      port: "${CONSUL_PORT:8500}"
      config:
        ...
      discovery:
        ...
    consul-util:
      # 查询健康检测通过的服务
      query-passing: true
      # key-value 注册
      kv:
        "[redis.password]":               # key中存在特殊字符 . / *, 使用 "[..]"包围
          alias: redis-service.password   # 注册 ${redis-service.password}
          blank-to-null: true             # 空字符串时，是否设置为 null
          must-exist: false               # false且key不存在 设置为null，否则抛出异常
          null-to-value: @null            # 当值为null时,设置为@null, 可以与配置处理工具联用,达到null配置的效果
        "[deploy.status]":                # 注册 ${deploy.status}
        some-key:                         # 注册 ${some-key}
      # 配置的服务必须存在，形式为 {服务名:[服务别名]}，随机选取一个
      # 别名为注册到当前上下文的key，空时表示同服务名
      # 注册内容为 服务别名.host, 服务别名.port
      service:
        redis: 
          alias: redis-service            # 注册：${redis-service.host}, ${redis-service.port}
          action: RANDOM_HOST_PORT        # 默认选项, 从可用服务中随机获取一个,追加 alias.host/alias.port配置
        mysql:                            # 默认配置情况, 注册 ${mysql.host}, ${mysql.port}
        es:
          alias: es-cluster               # 注册 ${es-cluster}
          action: JOIN_ALL                # 连接多个服务
          join-with: ,                    # es1:port1,es2:port2
```