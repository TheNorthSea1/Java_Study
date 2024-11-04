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
