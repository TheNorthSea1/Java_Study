# 基础知识

1. **什么是 Spring Boot？**
   - Spring Boot 是 Spring 开源组织下的子项目，旨在简化 Spring 应用的初始搭建和开发过程。它提供了自动配置、内嵌 Servlet 容器、起步依赖管理等功能，使得开发者可以快速创建独立运行的 Spring 应用。

2. **Spring Boot 的主要优点是什么？**
   - 独立运行：内嵌了 Tomcat、Jetty 等 Servlet 容器，无需部署到外部容器。
   - 简化配置：自动配置功能减少了大量的配置工作。
   - 起步依赖：提供了各种起步依赖，简化了 Maven 或 Gradle 的依赖管理。
   - 自动配置：根据类路径上的 jar 包和类自动配置 Bean。
   - 生产就绪：提供了生产环境的监控和管理功能。
   - 云原生支持：与云计算平台（如 Heroku、Cloud Foundry）天然集成。

3. **Spring Boot 的核心注解有哪些？**
   - `@SpringBootApplication`：组合注解，包含 `@SpringBootConfiguration`、`@EnableAutoConfiguration` 和 `@ComponentScan`。
   - `@SpringBootConfiguration`：标识这是一个 Spring Boot 配置类。
   - `@EnableAutoConfiguration`：启用自动配置功能。
   - `@ComponentScan`：扫描指定包及其子包下的组件。

自动配置

4. **Spring Boot 的自动配置原理是什么？**
   - Spring Boot 通过 `@EnableAutoConfiguration` 注解启用自动配置功能。它会从类路径下的 `META-INF/spring.factories` 文件中读取 `EnableAutoConfiguration` 指定的值，并将这些值作为自动配置类导入到容器中。自动配置类会根据类路径上的 jar 包和类自动配置 Bean。

5. **如何关闭某个自动配置？**
   - 可以通过 `@SpringBootApplication(exclude = { SomeAutoConfiguration.class })` 注解来排除某个自动配置类。

# 启动过程

6. **Spring Boot 的启动过程是怎样的？**
   - 解析 `@SpringBootApplication` 注解。
   - 初始化 `ApplicationContext`。
   - 加载配置文件（如 `application.properties` 或 `application.yml`）。
   - 执行自动配置。
   - 扫描并注册组件。
   - 初始化和启动嵌入式 Servlet 容器（如 Tomcat）。
   - 启动应用。

# 配置文件

7. **Spring Boot 支持哪些配置文件格式？**
   - `application.properties` 和 `application.yml`。

8. **如何在 Spring Boot 中使用多个配置文件？**
   - 可以通过 `spring.profiles.active` 属性激活某个配置文件，例如 `application-dev.properties` 或 `application-prod.properties`。

# 第三方库集成

9. **如何在 Spring Boot 中集成 MyBatis？**
   - 添加 MyBatis 的起步依赖 `spring-boot-starter-mybatis`。
   - 在配置文件中配置数据源信息。
   - 使用 `@Mapper` 注解标记 MyBatis 的 Mapper 接口。

10. **如何在 Spring Boot 中集成 Redis？**
    - 添加 Redis 的起步依赖 `spring-boot-starter-data-redis`。
    - 在配置文件中配置 Redis 连接信息。
    - 使用 `@Autowired` 注入 `RedisTemplate` 或 `StringRedisTemplate`。

# 热部署

11. **如何在 Spring Boot 中实现热部署？**
    - 使用 DevTools 模块，添加 `spring-boot-devtools` 依赖。
    - 在配置文件中设置 `spring.devtools.restart.enabled=true`。

# 监控

12. **Spring Boot Actuator 是什么？**
    - Spring Boot Actuator 是 Spring Boot 的一个模块，提供了生产环境中监控和管理应用的功能。它暴露了一系列的端点，可以通过 HTTP 或 JMX 访问。

13. **如何使用 Spring Boot Actuator？**
    - 添加 `spring-boot-starter-actuator` 依赖。
    - 在配置文件中启用需要的端点，例如 `management.endpoints.web.exposure.include=health,info`。

# 其他

14. **Spring Boot 中如何处理异常？**
    - 可以使用 `@ControllerAdvice` 注解创建全局异常处理器，捕获并处理控制器抛出的异常。

15. **Spring Boot 中如何配置日志？**
    - 可以在 `application.properties` 或 `application.yml` 中配置日志级别和输出格式。Spring Boot 默认使用 Logback 作为日志框架。

16. **Spring Boot 中如何实现国际化？**
    - 使用 `MessageSource` 接口和 `ResourceBundleMessageSource` 实现。
    - 在配置文件中配置消息资源文件的位置。
    - 使用 `@Value` 注解或 `MessageSource` 接口获取国际化消息。

17. **Spring Boot 中如何实现事务管理？**
    - 使用 `@Transactional` 注解标记需要事务管理的方法或类。
    - 配置事务管理器，例如 `DataSourceTransactionManager`。

18. **Spring Boot 中如何实现定时任务？**
    - 使用 `@Scheduled` 注解标记定时任务方法。
    - 配置 `@EnableScheduling` 注解启用定时任务支持。

19. **Spring Boot 中如何实现安全认证？**
    - 使用 Spring Security 框架。
    - 添加 `spring-boot-starter-security` 依赖。
    - 配置安全策略，例如 `WebSecurityConfigurerAdapter`。

20. **Spring Boot 中如何实现 RESTful API？**
    - 使用 `@RestController` 注解标记控制器类。
    - 使用 `@RequestMapping`、`@GetMapping`、`@PostMapping` 等注解映射请求。
    - 返回 JSON 或 XML 格式的数据。

# SpringBoot一次能处理多少个请求

Spring Boot 应用程序能够同时处理的请求数量主要取决于其内嵌的 Web 容器的配置。对于默认使用的 Tomcat 容器，有几个关键的配置项影响了请求的处理能力：

1. **最大线程数（Maximum Threads）**：这是处理请求的工作线程的最大数量。默认情况下，Tomcat 的最大线程数为 200。这意味着在同一时刻，最多可以有 200 个线程来处理请求。

2. **最大连接数（Max Connections）**：这是 Tomcat 可以同时保持的连接数上限，默认值为 8192。这包括正在处理的请求和等待处理的请求。

3. **最大等待数（Accept Count）**：这是当所有可能的请求处理线程都在忙时，可以排队等待的请求的最大数量。默认值为 100。一旦超过这个数量，新的客户端连接尝试将被拒绝。

因此，在默认配置下，Spring Boot 应用程序理论上可以同时处理的最大请求数量为最大连接数加上最大等待数，即 8192 + 100 = 8292 个请求。但实际上，能够处理的最大请求数量还受到服务器硬件性能、网络状况、应用程序逻辑复杂度等多种因素的影响。

此外，如果您的应用需要处理更多的并发请求，您可以通过修改配置文件中的相关属性来调整这些值，例如：

```yaml
server:
  tomcat:
    max-threads: 400 # 修改最大线程数
    max-connections: 10000 # 修改最大连接数
    accept-count: 200 # 修改最大等待数
```

请注意，增加这些数值的同时也要考虑服务器资源的实际限制，确保服务器有足够的资源来支持更高的并发量。

# @SpringBootApplication

 是 Spring Boot 框架中的一个注解，它用于标记一个类作为 Spring Boot 应用的入口点。这个注解是三个核心注解的组合：

1. `@Configuration`: 允许在类中定义@Bean方法，以便将组件注册到Spring应用上下文中。
2. `@EnableAutoConfiguration`: 告诉Spring Boot根据你添加的jar依赖自动配置你的应用程序。例如，如果你的应用程序连接到一个嵌入式的H2数据库，并且classpath中有必要的驱动程序，那么Spring Boot会自动配置数据源和JPA设置等。
3. `@ComponentScan`: 自动扫描并加载所有带有`@Component`注解的类，以及它的衍生注解（如`@Service`, `@Repository`, `@Controller`等），并将它们注册为Spring应用上下文中的bean。

使用`@SpringBootApplication`注解通常可以简化配置，因为它是上述三个注解的便捷形式。此外，它还默认从该注解所在的类的包开始进行组件扫描。如果你的应用结构遵循一定的规范，即所有的组件都位于主应用程序类的子包下，那么你就不需要额外指定`@ComponentScan`的`basePackages`属性。
