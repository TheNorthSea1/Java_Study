# 一、数据持久化回顾

## 1.持久化概述

1.1 **把内存中的数据保存到可掉电设备的一种方式，对于大多数的持久化来说，都是通过数据库（MySQL）进行持久化操作**

![image-20220707223646459](day01.assets/image-20220707223646459.png)

## 2. JAVA中的数据存储

2.1 序列化与反序列化

2.2 MySQL 存储数据 （通过JDBC 操作数据库（增、删、改、查...））

2.3 JAVA如何操作

- JDBC 可以直接访问数据库
- 第三方的 O/R 工具（Mybatis）
- JDBC 是java访问数据库的基础，后期要学习的Mybatis 等框架，只不过是封装了JDBC而已，JDBC 还是底层原理。

# 二、JDBC概述

## 1.概述

### 1.1概述：

JDBC( Java DataBase Connectivity)是java 操作数据库的一套API,是独立于数据管理系统的（MySQL）,定义了访问数据库的标准规范，通过SQL操作数据库

![image-20220707224506891](day01.assets/image-20220707224506891.png)

### 1.2 没有JDBC之前存在问题：

- 开发一套java代码不能操作不同的数据库，其实，不同的关系型数据库底层的实现细节都不一样

### 1.3 有JDBC之后

JDBC其实是 SUN 公司定义的一套标准接口，不同的数据库厂商需要去遵循这个规范，开发者只需要导入加载注册驱动，不用去管具体的实现细节，就可以操作数据库了。

### 1.4 JDBC本质：

- 官方（sun公司）定义的一套操作所有关系型数据库的规则，即接口
- 各个数据库厂商去实现这套接口，提供数据库驱动jar包
- 我们可以使用这套接口（JDBC）编程，真正执行的代码是驱动jar包中的实现类（传说中的面向接口编程）

### 1.5 优点

- 程序员不需要去写各数据库与java连接的代码，大大的减少了加班时间
- 随时可以去替换新数据库，访问数据库的java代码基本不变



# 三、JDBC API

![image-20220707230137580](day01.assets/image-20220707230137580.png)

- DriverManager（驱动管理类）

- Connection（数据库连接对象）

- Statement 执行SQL语句

- ResultSet（结果集对象）

  **![image-20220707230820961](day01.assets/image-20220707230820961.png)**

  

# 四、初试JDBC 操作数据库

![image-20220707231257758](day01.assets/image-20220707231257758.png)

- 第一步，编写java代码
- 第二步，java代码将SQL发送到MySQL服务器
- 第三步，MySQL服务端接收到SQL语句并且执行
- 第四步，将SQL语句执行的结果返回给java代码

## 1.实操步骤

### 1.1 导入MySQL驱动包

1.1.1 去maven 仓库下载（https://mvnrepository.com/）收索mysql-connector 点击第一个

![image-20220707231630998](day01.assets/image-20220707231630998.png)

![image-20220707231721934](day01.assets/image-20220707231721934.png)

![image-20220707231754416](day01.assets/image-20220707231754416.png)

### 1.2 注册驱动

```java
Class.forName("com.mysql.jdbc.Driver");
```

**注意，8.0版本数据库这样连接会出现问题，后面我们再解决**

### 1.3 获取连接

```sql
Connection conn = DriverManager.getConnection(url, username,password);
//通过驱动管理接口（DriverManager）获取到连接对象（Connection）
```

### 1.4 定义SQL

```sql
String sql = “insert…” ;
```

### 1.5 获取执行对象 Statement

```sql
Statement stmt = conn.createStatement();
```

### 1.6 执行SQL

```sql
stmt.executeUpdate(sql);
```

### 1.7 处理结果并且释放资源

```sql
//在 finally 里面关联Statement 对象和 Connection对象
```

## 2.代码实操

- 创建新的空项目

  ![image-20220707232952185](day01.assets/image-20220707232952185.png)

  ![image-20220707233027959](day01.assets/image-20220707233027959.png)

- 设置项目的jdk和编译版本

  ![image-20220707233232678](day01.assets/image-20220707233232678.png)

- 创建模块，设置模块名称

  ![image-20220707233420344](day01.assets/image-20220707233420344.png)

- 添加mysql 驱动包，将该jar 包添加为库文件。

  ![image-20220707233627260](day01.assets/image-20220707233627260.png)

- 添加为库文件的时候，有如下选择

  - Global Library : 全局有效

  - Project Library : 项目有效

  - Module Library : 模块有效

    ![image-20220707233833809](day01.assets/image-20220707233833809.png)

    

### 2.1实操问题

- SDK未添加jar包路径 

  [参考文档](https://blog.csdn.net/MMH1028/article/details/129367284?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522E37DF037-1ED6-4130-95C8-A2FA15EE0E3C%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=E37DF037-1ED6-4130-95C8-A2FA15EE0E3C&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-3-129367284-null-null.142^v100^pc_search_result_base5&utm_term=Exception%20in%20thread%20main%20java.lang.ClassNotFoundException%3A%20com.mysql.jdbc.Driver&spm=1018.2226.3001.4187)

  ![image-20241006203250836](./assets/image-20241006203250836.png)

- 驱动写错

  ![image-20220707235131075](day01.assets/image-20220707235131075.png)

- 列没写明确导致出错

  ![image-20220707235334974](day01.assets/image-20220707235334974.png)

# 五、安装DataGrip 工具

## 1.解压文件

![image-20220707235855107](day01.assets/image-20220707235855107.png)

## 2.设置软件安装位置

![image-20220708000007573](day01.assets/image-20220708000007573.png)

## 3.配置设置

![image-20220708000204606](day01.assets/image-20220708000204606.png)

## 	4.选择是否使用之前的配置信息

![image-20220708000336352](day01.assets/image-20220708000336352.png)

![image-20220708000419332](day01.assets/image-20220708000419332.png)

# 六、DataGrip 的使用

## 1.选择数据库源（MySQL）

![image-20220708000825785](day01.assets/image-20220708000825785.png)

## 2.填写连接信息

![image-20220708000951748](day01.assets/image-20220708000951748.png)

## 3.出现时区异常错误

![image-20220708001013987](day01.assets/image-20220708001013987.png)

![image-20220708001600889](day01.assets/image-20220708001600889.png)

## 4.连接进来后

![image-20220708001740471](day01.assets/image-20220708001740471.png)







