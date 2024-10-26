# Day03

## 函数

1. 回顾学过的函数
   - count
   - avg
   - sum
   - min
   - max

### 数值函数

1. 做数值运算的

   ![image-20220619135127065](day03图片\image-20220619135127065.png)

2. 演示

   ```sql
   #ABS(X)
   	SELECT ABS(0);
   #SIGN(X)
   	SELECT SIGN(-10);
   #SQRT(X)
   	SELECT SQRT(4);
   #LEAST(value1,value2,...)
   	SELECT LEAST(10,20,15);
   ```

### 字符串函数

1. 做字符串处理（CONCAT()）

![image-20220619135702928](day03图片\image-20220619135702928.png)

2. 演示

   ```sql
   #CONCAT(str1,str2,...)
       SELECT CONCAT('我','爱','你');
   #CONCAT_WS(separator,str1,str2,...)
       SELECT CONCAT_WS('s','我','爱','你');
   #REPLACE(,from_str,to_str)
       SELECT REPLACE("AABBAACDD",'A','x');
   ```



### 日期函数

1. 处理日期相关

   ![image-20220619140326988](day03图片\image-20220619140326988.png)

2. 演示

   ```sql
   #YEAR(date)
       SELECT YEAR(NOW());
   #MONTH(date)
       SELECT MONTH(NOW());
   #DAY(date)
       SELECT DAY(NOW());
   #QUARTER(date)
       SELECT QUARTER(NOW());
   ```

### 流程函数

1. 控制sql 执行顺序

   ![image-20220619140803810](day03图片\image-20220619140803810.png)

2. 演示

   ```sql
   #流程函数
   	#IF(expr1,expr2,expr3)
   		SELECT IF(TRUE,'T','F');
   	#IFNULL(expr1,expr2)
   		SELECT IFNULL('NIHAO',FALSE);
   ```

### 加密解密函数

1. 用来做加密解密的

   ![image-20220619141112032](day03图片\image-20220619141112032.png)

2. 演示

   ```sql
   #PASSWORD(str) // 已失效
       SELECT PASSWORD("123456");
   #MD5(str)
       SELECT MD5('123456');
   #SHA(STR)
       SELECT SHA('123456');
   ```



## 约束

1. 概念：就是用来作用表中字段的规则，用于限制存储在表中的数据。

2. 目的：保证数据库中数据的正确性，有效性和完整性。

   ![image-20220619143512465](day03图片\image-20220619143512465.png)

3. 约束演示

   ```sql
   #定义一个学生表，表中要求如下：
   #sn 表示学生学号，要求使用 int 类型，主键并且自动递增；
   #name 表示姓名，不为空；
   #age 表示年龄，18-30岁之间;
   #gender 表示性别;
   #study_status 表示学习状态，0表示挂科，1表示通过，默认是 1
   
   CREATE TABLE student(
   	sn int AUTO_INCREMENT  PRIMARY KEY,
   	name varchar(10) not null,
   	age int check(age >= 18 && age <=30),
   	gender char(1),
   	study_status TINYINT DEFAULT 1
   
   )COMMENT '学生表';
   
   SELECT * FROM student;
   
   #添加一点学生数据
   insert into student (name,age,gender,study_status) values('sy',18,'男',1);
   insert into student (name,age,gender,study_status) values('sy',18,'男',1);
   insert into student (name,age,gender,study_status) values(null,18,'男',1);
   insert into student (name,age,gender,study_status) values('sy',31,'男',1);
   insert into student (name,age,gender) values('werewr',30,'男');
   
   ```

### 外键约束

1. 概念：用户建立两张表之间的联系的，为了保证数据的一致性和完整性的。

   ![image-20220619150006995](day03图片\image-20220619150006995.png)

2. 注意上面创建的时候没有使用外键真正的来管理数据，可能会存在数据的丢失

3. 添加外键

   ```sql
   1.创建表创建外键
   CREATE TABLE 表名( 
   	字段名 数据类型, ... 
   	[CONSTRAINT] [外键名称] FOREIGN KEY (外键字段名) REFERENCES 主表 (主表列名)
    );
   
   2.添加外键
   	ALTER TABLE 表名 ADD CONSTRAINT 外键名称 FOREIGN KEY (外键字段名) REFERENCES 主表 (主表列名) ;
   ```

   - 案例：为emp表的dept_id字段添加外键约束,关联department表的主键id

     ```sql
     alter table emp add constraint fk_emp_dept_id FOREIGN key (dept_id) REFERENCES department(id);
     ```

4. 删除外键

   ```sql
   ALTER TABLE 表名 DROP FOREIGN KEY 外键名称; 
   ```

   - 案例：删除刚才添加的外键

     ```sql
     ALTER TABLE emp DROP FOREIGN KEY fk_emp_dept_id; 
     ```

5. 删除更新外键

   - 添加了外键之后尼，再删除父表数据时产生约束行为，就称为删除和更新行为。

     ![image-20220619151128905](day03图片\image-20220619151128905.png)

   - 语法

     ```sql
     ALTER TABLE 表名 ADD CONSTRAINT 外键名称 FOREIGN KEY (外键字段) REFERENCES 主表名 (主表字段名) ON UPDATE CASCADE ON DELETE CASCADE;
     ```

   - 案例：CASCADE（父表会删除或者更新子表中的外键数据）

     ```SQL
     ALTER TABLE emp add constraint fk_emp_dept_id FOREIGN KEY (dept_id) REFERENCES department(id)
     on update cascade on delete cascade;
     ```

   - 案例：SET NULL

     ```SQL
     ALTER TABLE emp add constraint fk_emp_dept_id FOREIGN KEY (dept_id) REFERENCES department(id)
     ON UPDATE SET NULL ON DELETE SET NULL;
     ```

   



## 多表关系

### 一对一关系

1. 用户和用户详情

2. 关系：一对一的关系

3. 用途：用于单表拆分，将一张表的基础字段放在一张表中，其它字段放在另一张表中，可以提升查询效率

4. 实现：在任意一张表里面添加外键，关联另一张表的主键

   ![image-20220619170331289](day03图片\image-20220619170331289.png)

   ```sql
   CREATE TABLE `user_detail` (
     `id` int NOT NULL AUTO_INCREMENT,
     `university` varchar(255) DEFAULT NULL,
     `car` varchar(255) DEFAULT NULL,
     `hourse` varchar(255) DEFAULT NULL,
     `user_id` int NOT NULL UNIQUE,
     PRIMARY KEY (`id`) USING BTREE,
     KEY `u_id` (`user_id`),
     CONSTRAINT `u_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
   ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
   ```



### 一对多关系

1. 案例：部门和员工关系

2. 关系：一个部门对应多个员工，一个员工对应一个部门

3. 实现：在**多的一方建立外键**，指向一的一方的主键

   ![image-20220619170838627](day03图片\image-20220619170838627.png)

   

### 多对多的关系

1. 案例：角色和菜单的关系，学生和老师

2. 关系：一个学生可以有多个老师，一个老师可以有多个学生

3. 实现：建立三方表，中间表来包含两个表的主键

   ![image-20220619171611848](day03图片\image-20220619171611848.png)

