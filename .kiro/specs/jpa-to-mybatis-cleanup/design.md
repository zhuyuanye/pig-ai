# JPA 残留清理 Bugfix Design

## Overview

pig-monitor 项目已大部分从 JPA 迁移到 MyBatis Plus，但仍残留大量 JPA 代码（注解、依赖、EntityManager 注入、AttributeConverter 文件等），导致移除 JPA 依赖后编译失败和运行时异常。本次修复将彻底清除所有 JPA 残留代码，完成向 MyBatis Plus 的完整迁移，同时将编译版本对齐到 Java 21。

修复策略：逐一替换 JPA 注解为 MyBatis Plus 注解、移除 JPA 依赖和注入、删除废弃的 AttributeConverter 文件、清理启动类和配置类中的 JPA 引用、对齐编译器版本。

## Glossary

- **Bug_Condition (C)**: 项目中存在 JPA 残留代码（注解、依赖、EntityManager 注入、AttributeConverter 文件），在移除 JPA 运行时后触发编译失败或运行时异常
- **Property (P)**: 所有数据访问层代码统一使用 MyBatis Plus，无 JPA 依赖，编译和运行正常
- **Preservation**: 现有 MyBatis Plus Mapper CRUD 操作、TypeHandler 类型转换、JdbcTemplate SQL 执行、Flyway 迁移、应用启动注册等行为保持不变
- **JpaDatabaseDataStorage**: `pig-common-warehouse` 中的历史数据存储类，已使用 HistoryMapper（MyBatis Plus），但类名仍以 "Jpa" 为前缀
- **DataStorageDispatch**: `pig-common-warehouse` 中的数据存储调度类，通过 `@PersistenceContext` 注入了 JPA EntityManager
- **FlywayConfiguration**: `pig-monitor` 中的 Flyway 配置类，通过 `@DependsOn("entityManagerFactory")` 依赖 JPA bean
- **AttributeConverter**: JPA 的类型转换接口，已被 MyBatis Plus TypeHandler 替代

## Bug Details

### Bug Condition

当项目移除 JPA 依赖（spring-boot-starter-data-jpa、org.eclipse.persistence.jpa）后，残留的 JPA 代码导致编译失败或运行时 bean 创建异常。具体表现为：实体类上的 JPA 注解无法解析、启动类上的 JPA 注解找不到类、EntityManager 注入失败、AttributeConverter 文件编译失败、FlywayConfiguration 依赖的 bean 不存在。

**Formal Specification:**
```
FUNCTION isBugCondition(codeElement)
  INPUT: codeElement of type SourceCodeElement
  OUTPUT: boolean

  RETURN codeElement.hasAnnotation IN ['@Entity', '@Table', '@Column', '@Id', '@GeneratedValue',
         '@EntityListeners', '@EnableJpaAuditing', '@EnableJpaRepositories', '@EntityScan',
         '@PersistenceContext', '@DependsOn("entityManagerFactory")']
         OR codeElement.imports('jakarta.persistence.*')
         OR codeElement.implements('jakarta.persistence.AttributeConverter')
         OR codeElement.dependsOn('spring-boot-starter-data-jpa')
         OR codeElement.dependsOn('org.eclipse.persistence.jpa')
         OR codeElement.compilerVersion != projectTargetVersion
END FUNCTION
```

### Examples

- `GeneralConfig.java` 使用 `@Entity`, `@Table(name = "hzb_config")`, `@Id`, `@Column`, `@EntityListeners(AuditingEntityListener.class)` → 移除 JPA 后编译失败，期望替换为 `@TableName("hzb_config")`, `@TableId`, `@TableField`
- `PigMonitorApplication.java` 包含 `@EnableJpaAuditing`, `@EnableJpaRepositories`, `@EntityScan` → 移除 JPA 后启动类加载失败，期望移除这三个注解
- `DataStorageDispatch.java` 通过 `@PersistenceContext` 注入 `EntityManager` 并调用 `entityManager.getEntityManagerFactory().getCache().evict()` → 移除 JPA 后注入失败，期望移除 EntityManager 相关代码
- `FlywayConfiguration.java` 中 `@DependsOn("entityManagerFactory")` → 移除 JPA 后 bean 不存在导致初始化失败，期望移除该注解
- `JsonMapAttributeConverter.java` 等 7 个文件实现 `jakarta.persistence.AttributeConverter` → 移除 JPA 后编译失败，期望删除这些文件（已有 TypeHandler 替代）
- `pig-common/pom.xml` 中 `maven-compiler-plugin` 配置 `<source>17</source><target>17</target>` 而项目 `java.version=21` → 编译版本不一致，期望改为 21

## Expected Behavior

### Preservation Requirements

**Unchanged Behaviors:**
- MyBatis Plus Mapper（HistoryMapper、GeneralConfigMapper 等）的 CRUD 操作正常执行
- TypeHandler（JsonMapTypeHandler、ZonedDateTimeTypeHandler、JsonStringListTypeHandler、JsonByteListTypeHandler、JsonLongListTypeHandler）正确处理类型转换
- DataStorageDispatch 通过 JdbcTemplate 更新监控状态的 SQL 逻辑不变
- DatabaseDataStorage（原 JpaDatabaseDataStorage）通过 HistoryMapper 保存和查询历史数据
- Flyway 在应用启动时正确执行数据库 schema 迁移
- pig-monitor 正常注册到 Nacos、启用 Spring Boot Admin、加载所有 Controller 和 Service

**Scope:**
所有不涉及 JPA 注解替换、JPA 依赖移除、JPA 注入清理的代码路径不受影响。包括：
- 所有 MyBatis Plus Mapper 接口和 XML 映射文件
- 所有 TypeHandler 实现
- 所有 Controller、Service 业务逻辑
- 所有配置文件（application.yml 等）

## Hypothesized Root Cause

基于代码分析，JPA 残留问题的根因如下：

1. **实体类 JPA 注解未替换**: `GeneralConfig`、`History`、`GrafanaDashboard`、`HzbUser`、`PushMetrics`、`SnmpConfigInfo`、`NetworkTopologyInfo`、`DataFragmentConfig` 等实体类仍使用 `@Entity`、`@Table`、`@Column`、`@Id`、`@GeneratedValue`、`@EntityListeners` 等 JPA 注解，这些注解来自 `jakarta.persistence` 包

2. **启动类 JPA 注解未移除**: `PigMonitorApplication` 仍包含 `@EnableJpaAuditing`、`@EnableJpaRepositories`、`@EntityScan`，这些注解依赖 `spring-data-jpa` 运行时

3. **EntityManager 注入未清理**: `DataStorageDispatch` 通过 `@PersistenceContext` 注入 `EntityManager`，并在 `calculateMonitorStatus` 方法中调用 `entityManager.getEntityManagerFactory().getCache().evict(Monitor.class, id)` 来清除 JPA 二级缓存，这在纯 MyBatis Plus 环境下无意义且会失败

4. **JPA 依赖未移除**: `pig-common-core/pom.xml` 仍声明 `spring-boot-starter-data-jpa` 依赖

5. **废弃 AttributeConverter 文件未删除**: 7 个 `AttributeConverter` 实现文件依赖 `jakarta.persistence.AttributeConverter`，已被对应的 TypeHandler 替代

6. **FlywayConfiguration JPA 依赖**: `@DependsOn("entityManagerFactory")` 引用了 JPA 自动配置创建的 bean

7. **编译版本不一致**: `pig-common/pom.xml` 中 `maven-compiler-plugin` 硬编码 `source/target` 为 17，而根 `pom.xml` 已设置 `java.version=21`

## Correctness Properties

Property 1: Bug Condition - JPA 残留代码清除后编译和启动正常

_For any_ 源代码元素 where isBugCondition 返回 true（包含 JPA 注解、JPA 依赖、JPA 注入、AttributeConverter 实现），修复后的代码 SHALL 不包含任何 `jakarta.persistence` 引用，实体类使用 MyBatis Plus 注解（`@TableName`、`@TableId`、`@TableField`），项目编译成功且应用正常启动。

**Validates: Requirements 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8**

Property 2: Preservation - 现有 MyBatis Plus 数据访问和业务逻辑不变

_For any_ 代码路径 where isBugCondition 返回 false（MyBatis Plus Mapper CRUD、TypeHandler 类型转换、JdbcTemplate SQL 执行、Flyway 迁移、应用启动注册），修复后的代码 SHALL 产生与修复前完全相同的行为，保持所有现有功能正常运行。

**Validates: Requirements 3.1, 3.2, 3.3, 3.4, 3.5, 3.6**

## Fix Implementation

### Changes Required

假设根因分析正确：

**File**: `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/manager/GeneralConfig.java`
**File**: `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/warehouse/History.java`
**File**: `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/grafana/GrafanaDashboard.java`
**File**: `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/user/HzbUser.java`
**File**: `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/push/PushMetrics.java`
**File**: `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/network/SnmpConfigInfo.java`
**File**: `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/network/NetworkTopologyInfo.java`
**File**: `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/fragment/DataFragmentConfig.java`

**Specific Changes**:
1. **实体类 JPA 注解替换为 MyBatis Plus 注解**:
   - 移除 `@Entity` 注解
   - `@Table(name = "xxx")` → `@TableName("xxx")`
   - `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` → `@TableId(value = "id", type = IdType.AUTO)`
   - `@Column(...)` → `@TableField(...)` 或移除（MyBatis Plus 默认驼峰映射）
   - `@EntityListeners(AuditingEntityListener.class)` → 移除（使用 MyBatis Plus MetaObjectHandler 替代）
   - `@Transient` → `@TableField(exist = false)`
   - 移除所有 `jakarta.persistence.*` import，添加 `com.baomidou.mybatisplus.annotation.*` import
   - 保留 `@CreatedBy`、`@CreatedDate`、`@LastModifiedBy`、`@LastModifiedDate` 等 Spring Data 审计注解，改用 MyBatis Plus `@TableField(fill = FieldFill.INSERT)` / `@TableField(fill = FieldFill.INSERT_UPDATE)` 替代

2. **启动类 JPA 注解移除**:
   - **File**: `pig-monitor/src/main/java/com/pig4cloud/pig/monitor/PigMonitorApplication.java`
   - 移除 `@EnableJpaAuditing` 注解及其 import
   - 移除 `@EnableJpaRepositories(...)` 注解及其 import
   - 移除 `@EntityScan(...)` 注解及其 import
   - 扩展 `@MapperScan` 扫描范围为 `{"com.pig4cloud"}` 以覆盖所有模块的 Mapper

3. **DataStorageDispatch EntityManager 清理**:
   - **File**: `pig-common/pig-common-warehouse/src/main/java/com/pig4cloud/pig/common/warehouse/store/DataStorageDispatch.java`
   - 移除 `@PersistenceContext` 注解和 `private EntityManager entityManager` 字段
   - 移除 `import jakarta.persistence.EntityManager` 和 `import jakarta.persistence.PersistenceContext`
   - 移除 `calculateMonitorStatus` 方法中 `entityManager.getEntityManagerFactory().getCache().evict(Monitor.class, id)` 调用（JPA 二级缓存清除在纯 MyBatis Plus 环境下无意义）

4. **JpaDatabaseDataStorage 类名重命名（可选）**:
   - **File**: `pig-common/pig-common-warehouse/src/main/java/com/pig4cloud/pig/common/warehouse/store/history/jpa/JpaDatabaseDataStorage.java`
   - 重命名类为 `DatabaseDataStorage`，反映实际使用 MyBatis Plus 的实现
   - 更新所有引用该类的地方

5. **pig-common-core JPA 依赖移除**:
   - **File**: `pig-common/pig-common-core/pom.xml`
   - 移除 `spring-boot-starter-data-jpa` 依赖声明
   - 移除 `org.eclipse.persistence.jpa` 依赖声明（如存在）

6. **废弃 AttributeConverter 文件删除**:
   - 删除 `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/alerter/JsonMapAttributeConverter.java`
   - 删除 `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/manager/JsonLongListAttributeConverter.java`
   - 删除 `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/manager/JsonOptionListAttributeConverter.java`
   - 删除 `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/manager/ZonedDateTimeAttributeConverter.java`
   - 删除 `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/manager/JsonStringListAttributeConverter.java`
   - 删除 `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/manager/JsonMapListAttributeConverter.java`
   - 删除 `pig-common/pig-common-core/src/main/java/com/pig4cloud/pig/common/core/entity/manager/JsonByteListAttributeConverter.java`

7. **编译版本对齐**:
   - **File**: `pig-common/pom.xml`
   - `<source>17</source>` → `<source>21</source>`
   - `<target>17</target>` → `<target>21</target>`

8. **FlywayConfiguration JPA 依赖清理**:
   - **File**: `pig-monitor/src/main/java/com/pig4cloud/pig/monitor/config/FlywayConfiguration.java`
   - 移除 `@DependsOn("entityManagerFactory")` 注解
   - Flyway 已通过 `FlywayMigrationInitializer` bean 正确初始化，`delayedFlywayInitializer` 方法中的 `@DependsOn` 可直接移除或改为依赖 `dataSource`

## Testing Strategy

### Validation Approach

测试策略分两阶段：首先在未修复代码上验证 bug 存在（编译失败），然后在修复后验证编译通过、功能正常、现有行为不变。

### Exploratory Bug Condition Checking

**Goal**: 在实施修复前，确认移除 JPA 依赖后的编译失败和运行时异常。

**Test Plan**: 尝试移除 `spring-boot-starter-data-jpa` 依赖后编译项目，观察编译错误和启动异常。

**Test Cases**:
1. **编译失败验证**: 移除 JPA 依赖后执行 `mvn compile`，观察 `jakarta.persistence` 相关编译错误（将在未修复代码上失败）
2. **启动类注解验证**: 检查 `PigMonitorApplication` 中 `@EnableJpaAuditing` 等注解是否导致 `ClassNotFoundException`（将在未修复代码上失败）
3. **EntityManager 注入验证**: 检查 `DataStorageDispatch` 中 `@PersistenceContext` 是否导致 `NoSuchBeanDefinitionException`（将在未修复代码上失败）
4. **FlywayConfiguration 验证**: 检查 `@DependsOn("entityManagerFactory")` 是否导致 bean 创建失败（将在未修复代码上失败）

**Expected Counterexamples**:
- `mvn compile` 输出大量 `package jakarta.persistence does not exist` 错误
- 应用启动时抛出 `ClassNotFoundException: org.springframework.data.jpa.repository.config.EnableJpaRepositories`
- 可能原因：JPA 注解未替换、JPA 依赖未移除、EntityManager 注入未清理

### Fix Checking

**Goal**: 验证所有 JPA 残留代码清除后，项目编译通过且应用正常启动。

**Pseudocode:**
```
FOR ALL codeElement WHERE isBugCondition(codeElement) DO
  result := compileAndStart(fixedProject)
  ASSERT result.compilesSuccessfully()
  ASSERT result.startsWithoutException()
  ASSERT NOT result.containsImport('jakarta.persistence.*')
END FOR
```

### Preservation Checking

**Goal**: 验证修复后所有现有功能保持不变。

**Pseudocode:**
```
FOR ALL codeElement WHERE NOT isBugCondition(codeElement) DO
  ASSERT originalBehavior(codeElement) = fixedBehavior(codeElement)
END FOR
```

**Testing Approach**: 属性基测试（Property-Based Testing）适用于验证保持不变的行为，因为：
- 可自动生成大量测试用例覆盖各种输入组合
- 能捕获手动单元测试可能遗漏的边界情况
- 对 Mapper CRUD、TypeHandler 转换等提供强保证

**Test Plan**: 先在未修复代码上观察 Mapper CRUD、TypeHandler 转换、JdbcTemplate 执行的正确行为，然后编写属性基测试确保修复后行为一致。

**Test Cases**:
1. **Mapper CRUD 保持**: 验证 HistoryMapper、GeneralConfigMapper 的增删改查操作在修复后继续正常工作
2. **TypeHandler 保持**: 验证 JsonMapTypeHandler 等 TypeHandler 在修复后正确序列化/反序列化
3. **JdbcTemplate 保持**: 验证 DataStorageDispatch 中 `calculateMonitorStatus` 的 SQL 更新逻辑在移除 EntityManager 后仍正确执行
4. **Flyway 迁移保持**: 验证 Flyway 在移除 `@DependsOn("entityManagerFactory")` 后仍能正确执行迁移

### Unit Tests

- 测试每个实体类的 MyBatis Plus 注解映射是否正确（`@TableName`、`@TableId`、`@TableField`）
- 测试 DataStorageDispatch 在无 EntityManager 情况下 `calculateMonitorStatus` 方法正常执行
- 测试 FlywayConfiguration 在无 `entityManagerFactory` 依赖时正常初始化

### Property-Based Tests

- 生成随机实体数据，验证 MyBatis Plus Mapper 的 CRUD 操作正确性
- 生成随机 JSON 字符串，验证 TypeHandler 的序列化/反序列化一致性
- 生成随机监控状态数据，验证 JdbcTemplate 更新逻辑的正确性

### Integration Tests

- 完整应用启动测试：验证 pig-monitor 在清除所有 JPA 残留后正常启动
- 数据库迁移集成测试：验证 Flyway 迁移脚本在新配置下正确执行
- 端到端数据流测试：验证从数据采集到历史存储的完整链路正常工作
