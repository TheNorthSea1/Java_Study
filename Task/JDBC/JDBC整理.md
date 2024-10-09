# 一、JDBC基础概念

1. **什么是JDBC？**

   JDBC的全称是Java DataBase Connection，即Java数据库连接。它是Java语言中用于访问关系型数据库的API，提供了一套标准的接口，使得Java应用程序能够与各种关系型数据库进行通信。

2. **JDBC接口及相关类在哪个包里？**

   JDBC接口及相关类在`java.sql`包和`javax.sql`包里。

3. **JDBC的主要作用是什么？**

   JDBC的主要作用是连接数据库，执行SQL查询、存储过程，并处理返回的结果。JDBC接口让Java程序和JDBC驱动实现了松耦合，使得切换不同的数据库变得更加简单。

# 二、JDBC操作数据库的步骤

1. **JDBC访问数据库的基本步骤是什么？**

   - 加载（注册）数据库驱动到JVM。
   - 建立（获取）数据库连接。
   - 创建（获取）数据库操作对象（如Statement、PreparedStatement等）。
   - 定义操作的SQL语句。
   - 执行数据库操作。
   - 获取并操作结果集（如ResultSet）。
   - 关闭对象，回收数据库资源（关闭ResultSet、Statement、Connection等）。

2. **如何理解`Class.forName(com.mysql.cj.jdbc.Driver)`？**

   `com.mysql.cj.jdbc.Driver`是MySQL JDBC驱动类的全限定名。`Class.forName()`方法用于动态加载类，并调用该类的静态代码块。在MySQL JDBC驱动的静态代码块中，会注册该驱动到`DriverManager`中。这样，后续就可以通过`DriverManager`来获取数据库连接了。

3. **`DriverManager`的作用是什么？**

   `DriverManager`是一个工厂类，用于创建数据库连接。当JDBC的Driver类被加载进来时，它会自己注册到`DriverManager`类里面。通过`DriverManager`的`getConnection()`方法，可以获取到数据库的连接对象。

# 三、Statement与PreparedStatement

1. **Statement、PreparedStatement和CallableStatement的区别是什么？**

   - **Statement**：用于执行固定的、没有参数的SQL语句。
   - **PreparedStatement**：继承于Statement，用于执行带参数的SQL语句。PreparedStatement可以防止SQL注入攻击，因为它会自动对特殊字符进行转义。此外，PreparedStatement还支持预编译和缓存机制，可以提高执行效率。（预编译需要手动开启）
   - **CallableStatement**：用于执行存储过程。

2. **execute、executeQuery、executeUpdate的区别是什么？**

   - **execute(String query)**：用于执行任意的SQL查询。如果查询的结果是一个ResultSet，则返回true；如果结果不是ResultSet（如insert或update查询），则返回false。可以使用`getResultSet()`方法来获取ResultSet，或者使用`getUpdateCount()`方法来获取更新的记录条数。
   - **executeQuery(String query)**：用于执行select查询，并返回ResultSet。即使查询不到记录，返回的ResultSet也不会为null。如果传入的是insert或update语句，则会抛出异常。
   - **executeUpdate(String query)**：用于执行insert、update/delete（DML）语句，或者返回DDL语句的执行结果。返回值是int类型，如果是DML语句，则表示更新的条数；如果是DDL语句，则返回0。

3. **相对于Statement，PreparedStatement的优点是什么？**

   PreparedStatement的优点包括：

   - 防止SQL注入攻击。
   - 可以进行动态查询。
   - 执行效率更高，尤其是当重用PreparedStatement或使用其批量查询接口执行多条语句时。
   - 使用setter方法更容易写出面向对象的代码。

# 四、事务与连接池

1. **什么是事务？事务有哪些特性？**

   事务是作为单个逻辑工作单元执行的一系列操作。一个逻辑工作单元必须具有四个特性，称为原子性（Atomicity）、一致性（Consistency）、隔离性（Isolation）和持久性（Durability），这四个特性合称为ACID特性。

2. **在JDBC编程中如何处理事务？**

   在JDBC编程中，可以通过以下步骤来处理事务：

   - 获取数据库连接对象Connection。
   - 关闭自动提交模式，即调用`setAutoCommit(false)`方法。
   - 执行SQL语句（可以是增删改操作）。
   - 根据执行结果决定是提交事务（调用`commit()`方法）还是回滚事务（调用`rollback()`方法）。
   - 关闭连接。

3. **什么是数据库连接池？为什么要使用连接池？**

   数据库连接池负责分配、管理和释放数据库连接。它允许应用程序重复使用一个现有的数据库连接，而不是重新建立一个。使用连接池的好处包括：

   - 提高数据库操作的性能。
   - 避免因为没有释放数据库连接而引起的数据库连接遗漏问题。



# 五、DBCP、C3P0和Druid

DBCP、C3P0和Druid都是Java中常用的数据库连接池，它们为数据库连接的管理提供了高效且方便的方式。以下是它们的共同点和不同点：

### 共同点

1. **连接池管理**：三者都实现了数据库连接池的管理，预先创建并维护一定数量的数据库连接，供应用程序在需要时快速获取和释放。
2. **提高性能**：通过连接池技术，减少了数据库连接的创建和销毁次数，从而提高了数据库访问的性能。
3. **配置灵活**：都支持通过配置文件或代码进行灵活的配置，以满足不同应用程序的需求。

### 不同点

1. **实现机制**：
   - **DBCP**：是Apache上的一个Java连接池项目，依赖于Jakarta commons-pool对象池机制。它直接可以在应用程序中使用，也是Tomcat使用的连接池组件。但DBCP没有自动回收空闲连接的功能。
   - **C3P0**：是一个开源的JDBC连接池，实现了数据源和JNDI绑定，支持JDBC3规范和JDBC2的标准扩展。C3P0是异步操作的，缓慢的JDBC操作可以通过帮助进程完成，以提升性能。它还具有自动回收空闲连接的功能。
   - **Druid**：是阿里巴巴开发的数据库连接池，不仅功能强大，而且在监控、可扩展性、稳定性和性能方面都有明显优势。Druid还提供了一个ProxyDriver、一系列内置的JDBC组件库和一个SQL Parser。它针对Oracle和MySQL做了特别优化，并提供了Filter-Chain模式的扩展API。
2. **使用场景**：
   - **DBCP**：由于其简单性和高效性，适用于中小型应用程序或需要快速部署的场景。
   - **C3P0**：由于其异步操作和自动回收空闲连接的功能，适用于需要处理大量数据库连接和长时间运行的应用程序。
   - **Druid**：由于其强大的功能和性能，以及针对特定数据库的优化，适用于大型应用程序、需要高性能和稳定性的场景，以及需要对数据库访问进行监控和日志记录的场景。
3. **配置和监控**：
   - **DBCP**：配置相对简单，但监控和日志记录功能相对较弱。
   - **C3P0**：提供了丰富的配置选项和监控功能，但可能需要额外的配置工作来实现。
   - **Druid**：提供了强大的监控和日志记录功能，以及灵活的扩展性，使得开发人员可以根据需要进行定制化的开发。

# 六、其他常见问题

1. **如何关闭JDBC资源以避免资源泄漏？**

   在JDBC编程中，应该养成在代码中显式关闭ResultSet、Statement和Connection等JDBC对象的习惯。可以在finally块中关闭这些资源，以确保即使出现异常也能正常关闭。

2. **java.util.Date和java.sql.Date有什么区别？**

   - `java.util.Date`包含日期和时间信息。
   - `java.sql.Date`只包含日期信息，没有具体的时间信息。