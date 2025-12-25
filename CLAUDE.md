## 语言要求

- **主要输出语言**：请尽量使用中文进行解释和说明
- **代码注释**：所有代码注释请使用中文

## 常用命令

- Build project: `./gradlew build`
- Run application: `./gradlew bootRun`
- Run tests: `./gradlew test`
- Clean build: `./gradlew clean`
- Check style: `./gradlew check`

## 代码风格

优先使用声明式流处理（Java Stream API）和响应式编程（Project Reactor）。

### 1. 编程范式：声明式优先

- **拒绝常规循环**：除非在极少数性能敏感的底层算法中，否则禁止使用 `for`、`while` 和 `do-while` 循环。
- **优先使用 Stream**：使用 `java.util.stream` 处理集合数据。
- **响应式流**：对于异步或非阻塞场景，必须使用 Project Reactor 的 `Mono` 和 `Flux`。
- **无状态逻辑**：倾向于编写纯函数，避免修改外部变量（避免 Side Effects）。

### 2. Java Stream API 指南

- **链式调用**：每个操作占据一行，逻辑清晰。
- **方法引用**：优先使用 `Class::method` 而非 `(x) -> x.method()`。
- **常用技巧**：
    - 使用 `filter` 进行条件过滤。
    - 使用 `map` 或 `flatMap` 进行对象转换。
    - 使用 `Collectors.toMap` 或 `groupingBy` 进行高级聚合。
    - 使用 `Optional.ifPresent` 或 `orElseGet` 处理空值。

### 3. Project Reactor (WebFlux) 指南

- **严禁阻塞**：禁止在响应式链中使用 `.block()`、`.blockFirst()` 或 `.toIterable()`。
- **组合操作符**：
    - 使用 `flatMap` 处理嵌套的异步调用。
    - 使用 `zip` 或 `zipWith` 合并多个数据源。
    - 使用 `switchIfEmpty` 处理空流。
    - 使用 `onErrorResume` 进行函数式异常回退。
- **上下文**：利用 `defer` 确保逻辑在订阅时执行。

### 4. 异常处理

- **函数式异常**：避免在流中间抛出受检异常。使用 `try-catch` 包装或自定义函数式接口。
- **Result 模式**：考虑返回封装了成功/失败状态的对象，而非直接抛出异常。

## 单元测试标准 (Unit Testing Standards)

- **框架选择**: 使用 **JUnit 4**（通过 `@RunWith(MockitoJUnitRunner.class)`）和 **Mockito**。
- **断言库**: 必须使用 **AssertJ** (`org.assertj.core.api.Assertions.assertThat`)。
- **命名约定**: 测试方法使用 **蛇形命名法 (snake_case)**，模式为：  
  `should_[预期结果]_when_[动作]_given_[前提条件]`  
  *例如*: `should_return_order_details_when_get_order_given_order_is_exist`
- **代码结构**: 必须严格遵守 **Given-When-Then** 模式，并包含明确的代码注释：
    - `// given`: 初始化数据、设置 Mock 行为和前置条件。
    - `// when`: 调用被测的核心方法。
    - `// then`: 验证结果（断言）及 Mock 交互行为。
- **Mocking 风格**:
    - 使用 `@Mock` 注入依赖，使用 `@InjectMocks` 注入被测类。
    - 打桩使用 `when(...).thenReturn(...)`。
    - 行为验证使用 BDD 风格的 `then(mock).should(times(n)).method(...)`。
- **Controller 测试要求**:
    - 验证 `ResponseEntity` 的状态码 (`getStatusCode()`) 和 Body 内容。
    - 检查 Body 中的字段是否符合预期。