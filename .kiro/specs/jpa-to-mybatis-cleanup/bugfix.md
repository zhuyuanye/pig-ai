# Bugfix Requirements Document

## Introduction

pig-monitor 项目已大部分从 JPA 迁移到 MyBatis Plus，但仍残留 JPA 代码（注解、依赖、EntityManager 注入等），导致编译失败和运行时错误。本次清理旨在彻底移除所有 JPA 残留，完成向 MyBatis Plus 的完整迁移。

## Bug Analysis

### Current Behavior (Defect)

1.1 WHEN pig-common-core 中的实体类（GeneralConfig、History、GrafanaDashboard、HzbUser、PushMetrics、SnmpConfigInfo、NetworkTopologyInfo、DataFragmentConfig）使用 JPA 注解（@Entity、@Table、@Column、@Id、@GeneratedValue、@EntityListeners 等）THEN 系统依赖 JPA 运行时环境，与已迁移的 MyBatis Plus 数据访问层不一致，移除 JPA 依赖后将编译失败

1.2 WHEN pig-monitor 启动类 PigMonitorApplication 包含 @EnableJpaAuditing、@EnableJpaRepositories、@EntityScan 注解 THEN 系统在移除 JPA 依赖后启动失败，抛出 ClassNotFoundException 或 BeanCreationException

1.3 WHEN DataStorageDispatch 通过 @PersistenceContext 注入 EntityManager 并调用 entityManager.getEntityManagerFactory().getCache().evict() THEN 系统在无 JPA 环境下运行时抛出注入失败异常（NoSuchBeanDefinitionException）

1.4 WHEN JpaDatabaseDataStorage 类名仍以 "Jpa" 为前缀 THEN 类名与实际使用 MyBatis Plus HistoryMapper 的实现不一致，造成代码可读性和维护性问题

1.5 WHEN pig-common-core 的 pom.xml 包含 spring-boot-starter-data-jpa 和 org.eclipse.persistence.jpa 依赖 THEN 系统引入不必要的 JPA 运行时类路径，增加包体积并可能引发类冲突

1.6 WHEN pig-common-core 中存在已被 TypeHandler 替代的 JPA AttributeConverter 文件（JsonLongListAttributeConverter、JsonOptionListAttributeConverter、ZonedDateTimeAttributeConverter、JsonStringListAttributeConverter、JsonMapListAttributeConverter、JsonByteListAttributeConverter、JsonMapAttributeConverter）THEN 这些文件依赖 jakarta.persistence.AttributeConverter，移除 JPA 依赖后编译失败

1.7 WHEN pig-common 的 maven-compiler-plugin 配置 source/target 为 17 而项目 java.version 属性已设为 21 THEN 编译器版本与项目目标 Java 版本不一致，无法使用 Java 21 语言特性

1.8 WHEN FlywayConfiguration 中 @DependsOn("entityManagerFactory") 引用 JPA 的 entityManagerFactory bean THEN 移除 JPA 后该 bean 不存在，导致 Flyway 迁移初始化失败

### Expected Behavior (Correct)

2.1 WHEN 实体类（GeneralConfig、History、GrafanaDashboard、HzbUser、PushMetrics、SnmpConfigInfo、NetworkTopologyInfo、DataFragmentConfig）被访问 THEN 系统 SHALL 使用 MyBatis Plus 注解（@TableName、@TableId、@TableField）替代 JPA 注解，且编译和运行正常

2.2 WHEN pig-monitor 启动类 PigMonitorApplication 启动 THEN 系统 SHALL 不包含任何 JPA 相关注解（@EnableJpaAuditing、@EnableJpaRepositories、@EntityScan），仅保留 @MapperScan 和 @ComponentScan 等 MyBatis Plus/Spring 注解

2.3 WHEN DataStorageDispatch 执行监控状态更新 THEN 系统 SHALL 不依赖 EntityManager，移除 @PersistenceContext 注入和 entityManager 缓存清除逻辑，仅使用 JdbcTemplate 完成状态更新

2.4 WHEN JpaDatabaseDataStorage 类被引用 THEN 系统 SHALL 将类名重命名为 DatabaseDataStorage，反映其实际使用 MyBatis Plus 的实现（可选）

2.5 WHEN pig-common-core 模块编译 THEN 系统 SHALL 不包含 spring-boot-starter-data-jpa 和 org.eclipse.persistence.jpa 依赖

2.6 WHEN pig-common-core 模块编译 THEN 系统 SHALL 不包含已废弃的 JPA AttributeConverter 文件，所有类型转换由 MyBatis Plus TypeHandler 处理

2.7 WHEN pig-common 模块编译 THEN 系统 SHALL 使用 Java 21 作为 maven-compiler-plugin 的 source 和 target 版本

2.8 WHEN FlywayConfiguration 初始化 Flyway 迁移 THEN 系统 SHALL 不依赖 entityManagerFactory bean，改为依赖 dataSource 或移除 @DependsOn 约束

### Unchanged Behavior (Regression Prevention)

3.1 WHEN MyBatis Plus Mapper（如 HistoryMapper、GeneralConfigMapper）执行数据库 CRUD 操作 THEN 系统 SHALL CONTINUE TO 正常执行查询、插入、更新、删除操作

3.2 WHEN pig-common-core 中的 TypeHandler（JsonMapTypeHandler、ZonedDateTimeTypeHandler、JsonStringListTypeHandler、JsonByteListTypeHandler、JsonLongListTypeHandler）处理数据库类型转换 THEN 系统 SHALL CONTINUE TO 正确序列化和反序列化 JSON/时间类型数据

3.3 WHEN DataStorageDispatch 通过 JdbcTemplate 更新监控状态 THEN 系统 SHALL CONTINUE TO 正确执行 SQL 更新语句并返回匹配行数

3.4 WHEN DatabaseDataStorage（原 JpaDatabaseDataStorage）存储历史指标数据 THEN 系统 SHALL CONTINUE TO 通过 HistoryMapper 正确保存和查询历史数据

3.5 WHEN Flyway 执行数据库迁移脚本 THEN 系统 SHALL CONTINUE TO 在应用启动时正确执行数据库 schema 迁移

3.6 WHEN pig-monitor 应用启动 THEN 系统 SHALL CONTINUE TO 正常注册到 Nacos、启用 Spring Boot Admin、加载所有 Controller 和 Service
