# JustReader 数据访问层配置

## 🗄️ 数据源配置

### 1. 数据库连接配置 (application.yml)

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:35432/els-just-reader-local
    username: root
    password: 123456
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 600000
      max-lifetime: 1800000
      connection-test-query: SELECT 1
  
  jpa:
    hibernate:
      ddl-auto: validate  # 生产环境使用 validate，开发环境可用 update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        show_sql: true
    open-in-view: false
  
  data:
    redis:
      host: localhost
      port: 36379
      password: 
      database: 0
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 20
          max-idle: 10
          min-idle: 5
          max-wait: -1ms
```

### 2. MyBatis Plus 配置

```java
// MyBatisPlusConfig.java
@Configuration
@MapperScan("com.yearsalso.**.mapper")
public class MyBatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        
        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        
        return interceptor;
    }
    
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // 开启驼峰命名转换
            configuration.setMapUnderscoreToCamelCase(true);
            // 设置日志实现
            configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        };
    }
}
```

### 3. Redis 配置

```java
// RedisConfig.java
@Configuration
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        
        // 使用 Jackson2JsonRedisSerializer 序列化
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), 
            ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        
        // 设置 key 和 value 的序列化规则
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(2))  // 默认缓存2小时
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .disableCachingNullValues();
        
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .transactionAware()
            .build();
    }
}
```

### 4. 数据源健康检查

```java
// DataSourceHealthIndicator.java
@Component
public class DataSourceHealthIndicator implements HealthIndicator {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return Health.up()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("connection", "valid")
                    .build();
            } else {
                return Health.down()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("connection", "invalid")
                    .build();
            }
        } catch (SQLException e) {
            return Health.down(e)
                .withDetail("database", "PostgreSQL")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

## 📊 数据库监控配置

### 1. Prometheus 监控配置

```yaml
# prometheus.yml
scrape_configs:
  - job_name: 'just-reader-db'
    static_configs:
      - targets: ['localhost:35432']
    metrics_path: '/metrics'
    
  - job_name: 'just-reader-app'
    static_configs:
      - targets: ['localhost:8401']
    metrics_path: '/actuator/prometheus'
```

### 2. 应用监控端点

```yaml
# application.yml 中的监控配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
      probes:
        enabled: true
    metrics:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: just-reader
```

## 🔄 数据库迁移管理

### 1. Flyway 配置

```yaml
# application.yml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    baseline-version: 1
    validate-on-migrate: true
    out-of-order: false
    clean-disabled: true
    table: flyway_schema_history
```

### 2. 迁移脚本目录结构

```
src/main/resources/db/migration/
├── V1__Initial_schema.sql
├── V2__Add_SQ3R_tables.sql
├── V3__Add_AI_tables.sql
├── V4__Add_indexes.sql
└── V5__Seed_data.sql
```

## 🧪 测试数据配置

### 1. 测试环境配置

```yaml
# application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: 
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    database-platform: org.hibernate.dialect.H2Dialect
  sql:
    init:
      mode: always
      schema-locations: classpath:sql/schema.sql
      data-locations: classpath:sql/data.sql
```

### 2. 测试数据脚本

```sql
-- sql/data.sql
INSERT INTO users (username, email, password, real_name, role_name) VALUES
('test1', 'test1@example.com', 'hashed_password_1', '测试用户1', 'user'),
('test2', 'test2@example.com', 'hashed_password_2', '测试用户2', 'user');

INSERT INTO books (title, author, description, status) VALUES
('测试书籍1', '作者1', '这是一本测试书籍', 'ready'),
('测试书籍2', '作者2', '这是另一本测试书籍', 'ready');
```

## 🔒 数据安全配置

### 1. 数据加密配置

```java
// DataEncryptionConfig.java
@Configuration
public class DataEncryptionConfig {
    
    @Value("${app.encryption.key}")
    private String encryptionKey;
    
    @Bean
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        
        config.setPassword(encryptionKey);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        
        encryptor.setConfig(config);
        return encryptor;
    }
}
```

### 2. 敏感数据脱敏

```java
// SensitiveDataMaskingAspect.java
@Aspect
@Component
public class SensitiveDataMaskingAspect {
    
    @Around("@annotation(com.yearsalso.common.annotation.SensitiveData)")
    public Object maskSensitiveData(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        
        if (result instanceof User) {
            User user = (User) result;
            user.setPassword("***");
            user.setPasswordSalt("***");
            user.setMobile(maskMobile(user.getMobile()));
        }
        
        return result;
    }
    
    private String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 11) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }
}
```

## 📈 性能优化配置

### 1. 连接池监控

```java
// ConnectionPoolMonitor.java
@Component
public class ConnectionPoolMonitor {
    
    @Autowired
    private DataSource dataSource;
    
    @Scheduled(fixedDelay = 60000)  // 每分钟监控一次
    public void monitorConnectionPool() {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            
            log.info("连接池状态: " +
                "活跃连接: " + hikariDataSource.getHikariPoolMXBean().getActiveConnections() +
                ", 空闲连接: " + hikariDataSource.getHikariPoolMXBean().getIdleConnections() +
                ", 等待线程: " + hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
    }
}
```

### 2. 慢查询监控

```java
// SlowQueryInterceptor.java
@Interceptor
public class SlowQueryInterceptor {
    
    private static final long SLOW_QUERY_THRESHOLD = 1000;  // 1秒
    
    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object monitorQueryTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        if (duration > SLOW_QUERY_THRESHOLD) {
            log.warn("慢查询检测: " + joinPoint.getSignature().toShortString() + 
                " 耗时: " + duration + "ms");
        }
        
        return result;
    }
}
```

## 🚀 部署配置

### 1. Docker 数据库初始化

```dockerfile
# Dockerfile-db-init
FROM postgres:17.1

COPY database/migrations/ /docker-entrypoint-initdb.d/

ENV POSTGRES_USER=root
ENV POSTGRES_PASSWORD=123456
ENV POSTGRES_DB=els-just-reader-local

EXPOSE 5432
```

### 2. 数据库备份脚本

```bash
#!/bin/bash
# backup-database.sh

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/backup/database"
BACKUP_FILE="$BACKUP_DIR/justreader_backup_$TIMESTAMP.sql"

mkdir -p $BACKUP_DIR

# 备份数据库
pg_dump -h localhost -p 35432 -U root -d els-just-reader-local -F c -b -v -f $BACKUP_FILE

# 压缩备份
gzip $BACKUP_FILE

# 清理旧备份（保留最近7天）
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete

echo "数据库备份完成: $BACKUP_FILE.gz"
```

## 📝 使用说明

### 1. 本地开发环境启动

```bash
# 启动数据库
docker-compose up -d just-reader-db

# 运行数据库迁移
./mvnw flyway:migrate

# 启动应用
./mvnw spring-boot:run
```

### 2. 生产环境部署

```bash
# 1. 备份现有数据库
./scripts/backup-database.sh

# 2. 运行数据库迁移
./mvnw flyway:migrate -Dspring.profiles.active=prod

# 3. 重启应用
systemctl restart just-reader.service
```

### 3. 监控数据库

```bash
# 查看数据库连接
psql -h localhost -p 35432 -U root -d els-just-reader-local -c "SELECT * FROM pg_stat_activity;"

# 查看表大小
psql -h localhost -p 35432 -U root -d els-just-reader-local -c "SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname || '.' || tablename)) FROM pg_tables ORDER BY pg_total_relation_size(schemaname || '.' || tablename) DESC;"
```

---

**配置完成时间**: 2026-02-13 22:35  
**下一步**: 更新 Obsidian 文档，记录任务完成情况