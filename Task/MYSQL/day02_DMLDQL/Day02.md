# Day02

## DML

定义：Data Manipulation Language、数据操作语言（增删改）

1. 添加数据（INSERT）
2. 修改数据（UPDATE）
3. 删除数据（DELETE）



### 添加数据（INSERT）

1. 给指定的数据添加数据

   ```sql
   语法：INSERT INTO 表名 (字段名1, 字段名2, ...) VALUES (值1, 值2, ...);
   ```

   - 案例：给employee表所有的字段添加数据。

     ```SQL
     INSERT INTO employee (id,username,gender) VALUES(1,'sy','男');
     ```

2. 给全部字段添加数据

   ```sql
   语法：INSERT INTO 表名 VALUES (值1, 值2, ...);
   ```

   - 案例：给employee表的所有字段添加数据。

     ```sql
     INSERT INTO employee VALUES(2,'zs','001','男','111','2022-02-01');
     ```

3. 批量添加数据

   ```sql
   语法：
   	指定字段：INSERT INTO 表名 (字段名1, 字段名2, ...) VALUES (值1, 值2, ...), (值1, 值2, ...);
   	全部字段：INSERT INTO 表名 VALUES (值1, 值2, ...), (值1, 值2, ...), (值1, 值2, ...) ;
   ```

   - 案例：批量插入数据到employee表；

     ```sql
     #插入id 用户名 入职时间的
     	INSERT INTO employee (id,username,entry_date) values(3,'ls','2022-06-01'),(4,'ww','2015-03-10'),(5,'zl','2008-4-01');
     
     #插入全部字段
     	INSERT INTO employee values(6,'小明','002','男','123','2022-03-01'),(7,'小张','003','男','345','2022-04-01');
     ```

     ![image-20220618104520907](day02图片\image-20220618104520907.png)

     ![image-20220618104555667](day02图片\image-20220618104555667.png)

### 修改(UPDATE)

1. 语法：UPDATE 表名 SET 字段名1 = 值1 , 字段名2 = 值2 , .... [ WHERE 条件 ] ;

   ```sql
   语法：UPDATE 表名 SET 字段名1 = 值1 , 字段名2 = 值2 , .... [ WHERE 条件 ] ;
   ```

   - 案例：修改id为1的数据，将username修改为 syy

     ```sql
     UPDATE employee SET username = 'syy' WHERE id = 1;
     ```

   - 案例：修改id为1的数据, 将username修改为小明, gender修改为 男

     ```sql
     UPDATE employee SET username = '小明',gender = '男' WHERE id = 1;
     ```

   - 将所有的员工入职日期修改为 2018-02-01

     ```sql
     UPDATE employee SET entry_date = '2018-02-01';
     ```

   - **注意点：当你不加 WHERE 条件的时候，默认是改整张表**

### 删除（DELETE）

1. 语法：DELETE FROM 表名 [ WHERE 条件 ] 

   ```sql
   DELETE FROM 表名 [ WHERE 条件 ] 
   ```

   - 案例：删除gender为男的员工

     ```sql
     DELETE FROM employee WHERE gender = '男';
     ```

   - 删除所有员工

     ```sql
     DELETE FROM employee;
     ```

   - 注意事项：删除条件可以没有，没有默认是删除整张表数据，删除语句不能够去删除某一个字段的值，只能用修改语句实现

## DQL

1. 定义：Data Query Language(数据查询语言)，数据查询语言，用来查询数据库中表的记录。
2. 查询的关键字：SELECT
   - 淘宝
   - 京东
3. 具体的业务使用中
   - 分页
   - 搜索
   - 排序
   - 条件

### 基础查询

#### 不带条件的查询查询多个字段

1. 语法：

   ```sql
   #查询指定字段的数据
   SELECT 字段1, 字段2, 字段3 ... FROM 表名 ;	
   #查询表中全部字段的数据
   SELECT * FROM 表名 ;
   ```

   - 案例：查询表中所有信息数据

     ```sql
     SELECT * FROM employee;
     ```

   - 案例：查询表中姓名和性别这两个字段的信息

     ```sql
     SELECT name,gender from employee;
     ```

#### 查询字段设置别名

1. 语法：

   ```sql
   SELECT 字段1 [ AS 别名1 ] , 字段2 [ AS 别名2 ] ... FROM 表名;
   SELECT 字段1 [ 别名1 ] , 字段2 [ 别名2 ] ... FROM 表名;
   ```

   - 案例：查询表中姓名和性别这两个字段的信息，并且给中文别名

     ```sql
     SELECT name AS '姓名',gender AS '性别' FROM employee;
     
     SELECT name '姓名1',gender '性别1' FROM employee;
     ```

   - 注意：AS 是可以省略的



#### 去除重复记录

1. 语法：使用一个关键字 DISTINCT

   ```sql
   SELECT DISTINCT 字段列表 FROM 表名;
   ```

   - 案例：查询员工的家庭住址（不要重复）

     ```sql
     SELECT DISTINCT address FROM employee; 
     ```

#### 基础查询的案例

1. 查询指定字段 name, age并返回

   ```sql
   SELECT name,age FROM employee;
   ```

2. 查询返回所有字段

   ```sql
   SELECT * FROM employee;
   ```

3.  查询所有员工的年龄,起别名

   ```sql
   SELECT age '年龄' FROM employee;
   ```

4. 查询公司员工的家庭地址有哪些(不要重复)

   ```sql
   SELECT DISTINCT address FROM employee;
   ```



### 条件查询

1. 语法：使用到 where 之后

   ```sql
   SELECT 字段列表 FROM 表名 WHERE 条件列表 ;
   ```

#### 运算符

##### 比较运算符

![image-20220618152540797](day02图片\image-20220618152540797.png)

注意：

- java 中的等于是使用 == 而 mysql 中的等于直接使用 =
- BETWEEN ... AND ... 范围包含最小值和最大值
- IN(...) 属于括号后的子集
- LIKE % 表示通配符
- is null 表示空，非空 is not null



##### 逻辑运算符

![image-20220618153756148](day02图片\image-20220618153756148.png)

- 案例：查询年龄小于20并且idcard 非空的

  ```sql
  SELECT * FROM employee where age < 20 and idcard is not null;
  ```



#### 条件查询案例

-  查询年龄等于 18 的员工

  ```sql
  SELECT * FROM employee WHERE age = 18;
  ```

- 查询年龄小于 20 的员工信息

  ```sql
  SELECT * FROM employee WHERE age < 20;
  ```

- 查询年龄大于等于 20 的员工信息

  ```sql
  SELECT * FROM employee WHERE age >= 20;
  ```

- 查询没有身份证号的员工信息

  ```sql
  SELECT * FROM employee WHERE idcard is null;
  ```

- 查询有身份证号的员工信息

  ```sql
  SELECT * FROM employee WHERE idcard is not null;
  ```

- 查询年龄不等于 18 的员工信息

  ```sql
  SELECT * FROM employee WHERE age != 18;
  ```

- 查询年龄在15岁(包含) 到 20岁(包含)之间的员工信息

  ```sql
  SELECT * FROM employee WHERE age BETWEEN 15 AND 20;
  
  SELECT * FROM employee WHERE age >= 15 and age <= 20;
  
  SELECT * FROM employee WHERE age >= 15 && age <= 20;
  ```

- 查询性别为女且年龄小于 23岁的员工信息

  ```sql
  SELECT * FROM employee WHERE age < 23 AND gender = '女';
  ```

- 查询年龄等于18 或 20 或 40 的员工信息

  ```sql
  SELECT * FROM employee WHERE age in(18,20,40);
  
  SELECT * FROM employee WHERE age  = 18 or age = 20 or age = 40;
  ```

- 查询姓名为两个字的员工信息 _ %

  ```sql
  SELECT * FROM employee WHERE name like '__';
  ```

- 查询身份证号最后一位是X的员工信息

  ```sql
  SELECT * FROM employee WHERE idcard like '%X';
  SELECT * FROM employee WHERE idcard like '_________________X';
  ```

### 常用聚合函数

1. 语法：SELECT 聚合函数(字段列表) FROM 表名 ;将一列数据作为一个整体，进行纵向运算。

![image-20220618155556994](day02图片\image-20220618155556994.png)



#### 案例

- 统计企业员工数量

  ```sql
  SELECT COUNT(id) FROM employee
  ```

- 统计企业员工的平均年龄

  ```sql
  SELECT AVG(age) FROM employee;
  ```

- 统计企业员工的最大年龄

  ```sql
  SELECT MAX(age) FROM employee;
  ```

- 统计企业员工的最小年龄

  ```sql
  SELECT MIN(age) FROM employee;
  ```

- 统计家庭是杭州员工的年龄之和

  ```sql
  SELECT SUM(age) FROM employee WHERE address = '杭州';
  ```

### 分组查询

1. 语法(GROUP BY )

   ```sql
   SELECT 字段列表 FROM 表名 [ WHERE 条件 	] GROUP BY 分组字段名 [ HAVING 分组后过滤条件 ];
   ```

2. WHERE 和 HAVING 的区别

   - 执行时间不同，WHERE 是分组之前执行，不参与分组，HAVING 是分组之后执行
   - 判断条件不同，WHERE 是不能对聚合函数做判断的，HAVING 是可以的。

3. 注意：

   - 分组之后，查询的字段一般为聚合函数和分组字段，查询其它其它字段没有意义；
   - 执行顺序：WHERE > 聚合函数 > HAVING
   - 可以支持多字段分组 GROUP BY COLUM1,COLUM2;

#### 案例

- 根据性别分组 , 统计男性员工 和 女性员工的数量

  ```sql
  SELECT gender,count(id) from employee GROUP BY gender;
  ```

- 根据性别分组 , 统计男性员工 和 女性员工的平均年龄

  ```sql
  SELECT gender,avg(age) from employee GROUP BY gender;
  ```

- 查询年龄小于45的员工 , 并根据家庭地址分组 , 获取员工数量大于等于3的家庭地址

  ```sql
  	SELECT address,count(*) num
  		FROM employee 
  	WHERE age < 45
  		GROUP BY address having num >= 3;
  ```

- 统计各个家庭地址上班的男性及女性员工的数量

  ```sql
  SELECT gender,count(*) '数量',address
  		FROM employee
  		GROUP BY gender,address; 
  ```

### 排序查询

1. 语法(ORDER BY)

   - 排序方式
     - 升序 ASC(默认就是升序)
     - 降序 DESC

   ```sql
   SELECT 字段列表 FROM 表名 ORDER BY 字段1 排序方式1 , 字段2 排序方式2 ;
   ```

#### 案例

-  根据年龄对公司的员工进行升序排序

  ```sql
  SELECT * FROM employee ORDER BY age ;
  SELECT * FROM employee ORDER BY age ASC;
  ```

- 根据入职时间, 对员工进行降序排序

  ```SQL
  SELECT * FROM employee ORDER BY entrydate DESC;
  ```

- 根据年龄对公司的员工进行升序排序 , 年龄相同 , 再按照入职时间进行降序排序

  ```SQL
  SELECT * FROM employee ORDER BY age ASC,entrydate DESC;
  ```




### 分页查询

1. 语法（LIMIT）

   ```SQL
   SELECT 字段列表 FROM 表名 LIMIT 起始索引, 查询记录数 ;
   ```

2. 注意：

   - **起始索引从0开始，起始索引 = （查询页码 -1 ）* 每页显示的记录数**
   - LIMIT 是MYSQL 中的实现
   - 如果只查第一页数据，起始索引是可以省略的，limit 5;

#### 案例

- 查询第1页员工数据, 每页展示 5 条记录

  ```sql
  SELECT * FROM employee limit 0,5;
  
  SELECT * FROM employee LIMIT 5;
  ```

- 查询第2页员工数据, 每页展示 5 条记录

  ```SQL
  #（查询页面 -1） * 页码显示数
  SELECT * FROM employee LIMIT 5,5;
  ```







### 执行优先级

```sql
		SELECT 
4			字段列表 							
		FROM
1			表名列表							
		WHERE
2			条件列表							
		GROUP BY
3			分组字段列表					       
		HAVING
			分组后条件列表				          
		ORDER BY
5			排序字段列表					      
		LIMIT
6			分页参数		
```



- 案例验证

  - 查询年龄大于15的员工姓名、年龄，并根据年龄进行升序排序。

    ```sql
    	SELECT emp.name eName,emp.age eAge from employee emp WHERE emp.age > 15 GROUP BY eAge HAVING eAge >20  order by eAge limit 2;
    ```

- 执行先后

  ```sql
  from ... where ... group by ... having ... select ... order by ... limit
  ```

  