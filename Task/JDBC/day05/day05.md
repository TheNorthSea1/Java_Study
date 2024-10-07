# 一、JDBC 事务操作

## 1.事务回顾

### 1.1事务的ACID 属性

- 原子性（Atomicity）：事务是不可分割的，要么同时成功，要么同时失败。
- 一致性（Consistency）：事务必须使数据库从一个一致性状态变成另一个一致性状态。
- 隔离性（Isolation）：一个事务的执行不会被其它事务所干扰。
-  持久性（Durability）：事务一旦提交，内存中的内容持久化到文件，并且是永久性的。

### 1.2数据库并发问题

- 脏读：有事务A、B，事务A读到了事务B更新但是没有提交的数据。
- 不可重复读：有事务A、B，事务A先去读一个字段（name）,事务B去更新修改(name) 字段，事务A再去读，发现数据变了。
- 幻读：有事务A、B，事务A先去读整张表的数据，事务B去插入了新数据，事务A再去读的时候，数据变多了。

### 1.3数据库隔离级别

- 读未提交（READ UNCOMMITED）：允许事务读取未被其它事务提交的数据。
  - 脏读
  - 不可重复读
  - 幻读
- 读已提交（READ COMMITED）：只允许事务读取已经被其它事务所提交的数据。
  - 可以避免脏读
  - 不可避免不可重复读和幻读
- **可重复读（REPEATABLE_READ）:事务操作某一个字段时，禁止其它事务更新。**
  - 避免了脏读和不可重复读
  - 幻读还是可能出现
- 串行化(SERIALIZABLE)：确保事务操作的时候，禁止其它事务操作。
  - 解决掉所有的问题

## 2.JDBC 事务处理

### 1.事务特点

- 事务一旦提交，数据就不可以回滚，不可变了。
- 数据什么时候提交？
  - **当我们获取连接的时候，默认情况下是自动提交事务**；每次执行sql执行成功就会自动提交，不会回滚。
  - 关闭数据库连接的时候，自动提交事务。**只要谈事务，多个操作必须使用同一个连接才有意义。**

### 2.JDBC 操作多条SQL的事务

- 调用 Connection 对象的 setAutoCommit(false); 以取消自动提交事务
- 所有的SQL执行完之后做 commit()
- 出现异常回滚，调用 rollback()回滚事务
- 注意
  - 如果同一个连接没有被关闭，可能会存在重复使用，那需要手动的设置自动提交方式。setAutoCommit(true);
  - 特别是在连接池使用的过程中，调用close 方法先恢复自动提交状态。



## 3.JDBC 操作银行转账案例

### 1.需求：上云向张三转账1000块

```sql
update account set money = money -1000 where user_name = ?

update account set money = money + 1000 where user_name = ?
```



# 二、批量处理

## 1.使用前提

- 当需要成批插入或者更新记录时，可以采用Java的批量更新机制，这一机制允许多条语句一次性提交给数据库批量处理，通常情况下比单独提交处理更有效率。

## 2.JDBC 批量处理方法

- addBatch(String):添加需要批量处理的SQL语句或是参数
- executeBatch()：执行批量处理语句
- clearBatch():清空缓存的数据

## 3.业务场景

- 多条SQL语句的批量处理
- 一个SQL语句的批量传参

## 4.批量处理案例

- 向数据库中插入5万条数据

### 4.1实现方式1

- 使用Statement

  ```java
  public void test1() throws SQLException {
          Connection conn = DruidUtil.getConn();
          Statement statement = conn.createStatement();
          for (int i = 0; i < 50000; i++) {
              String sql = "insert into account(user_name) values("+i+")";
              statement.execute(sql);
          }
      }
  ```

  

### 4.2实现方式2

- 使用PreparedStatement

  ```java
   public void test2() throws SQLException {
          long begin = System.currentTimeMillis();
          String sql = "insert into account(user_name) values(?)";
          Connection conn = DruidUtil.getConn();
          PreparedStatement statement = conn.prepareStatement(sql);
          for (int i = 0; i < 20000; i++) {
              statement.setObject(1,i);
              statement.executeUpdate();
          }
          conn.close();
          System.out.println("耗时："+(System.currentTimeMillis() - begin));
      }
  ```

### 4.3实现方式3

- addBatch() / executeBatch() / clearBatch()

  ```java
  public void test3() throws SQLException {
          long begin = System.currentTimeMillis();
          String sql = "insert into account(user_name) values(?)";
          Connection conn = DruidUtil.getConn();
          PreparedStatement statement = conn.prepareStatement(sql);
          for (int i = 0; i < 20000; i++) {
              statement.setObject(1,i);
              //存sql
              statement.addBatch();
              if(i %1000 == 0){
                  statement.executeBatch();
                  //执行完还需要去清缓存
                  statement.clearBatch();
              }
          }
          conn.close();
          System.out.println("耗时："+(System.currentTimeMillis() - begin));
      }
  ```

### 4.4实现方式四

- 批处理+事务

  ```java
   public void test4() throws SQLException {
          long begin = System.currentTimeMillis();
          String sql = "insert into account(user_name) values(?)";
          Connection conn = DruidUtil.getConn();
          conn.setAutoCommit(false);
          PreparedStatement statement = conn.prepareStatement(sql);
          for (int i = 0; i < 20000; i++) {
              statement.setObject(1, i);
              //存sql
              statement.addBatch();
              if (i % 1000 == 0) {
                  statement.executeBatch();
                  //执行完还需要去清缓存
                  statement.clearBatch();
              }
          }
          conn.commit();
          conn.close();
          System.out.println("耗时：" + (System.currentTimeMillis() - begin));
      }
  ```

  