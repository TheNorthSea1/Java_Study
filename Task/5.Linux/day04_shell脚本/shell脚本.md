# 一、Shell 概述

- **Shell** 是一个**命令行解释器**，用于**接收用户命令**，**调度操作系统内核**，同时还是一个**脚本程序设计语言**

  <img src="shell脚本.assets/image-20221020093437196.png" alt="image-20221020093437196" style="zoom:33%;" />

  

- Shell 功能特点：

  - 易编写
  - 易调试
  - 高灵活性

## 1.Linux 提供的 Shell 解析器

- 解析器

  ```shell
  cat /etc/shells
  ```

  ![image-20221020093758682](shell脚本.assets/image-20221020093758682.png)

- 分类

  - sh
  - bash

## 2.sh 和 bash 关系

- sh和bash都是Linux系统shell的一种，其中bash命令是sh命令的超集，大多数sh脚本都可以在bash下运行

  <img src="shell脚本.assets/image-20221020093437196.png" alt="image-20221020093437196" style="zoom:33%;" />

## 3.centos 默认的 shell

```shell
echo $SHELL
```

![image-20221020094118283](shell脚本.assets/image-20221020094118283.png)

# 二、Shell 脚本快速入门

## 1.脚本格式

- 指定解析器：必须以 #!/bin/bash 开头

  ```shell
  #!/bin/bash
  ```

## 2.第一个 Shell 脚本

- 需求：第一个 Shell 脚本：HelloWorld.sh ，打印HelloWord Shell

- 代码

  ```shell
  #!/bin/bash
  echo "HelloWord Shell"
  ```

  ![image-20221020095143818](shell脚本.assets/image-20221020095143818.png)

## 3.常用运行方式

- 目的是运行 shell 代码

### 3.1.以sh/bash运行

- 绝对路径执行（从/ 目录往下执行）

  ![image-20221020095404632](shell脚本.assets/image-20221020095404632.png)

  

- 相对路径执行

  ![image-20221020095531289](shell脚本.assets/image-20221020095531289.png)

  

### 3.2赋予脚本可执行权限

- 给脚本赋予可执行权限 +x

  ```shell
  chmod +x HelloWord.sh
  ```

  ![image-20221020095933125](shell脚本.assets/image-20221020095933125.png)

- 直接运行脚本

  - 绝对路径

    ```shell
    /home/shell/HelloWord.sh
    ```

    ![image-20221020100116163](shell脚本.assets/image-20221020100116163.png)

  - 相对路径

    ```shell
    ./HelloWord.sh
    ```

    ![image-20221020100147060](shell脚本.assets/image-20221020100147060.png)

### 3.3两种方式的区别

- 第一种本质上是 **bash 解析器去完成我们脚本的执行**
- 第二种，脚本**自己执行**，所以**需要可执行权限**







# 三、Shell 语法

## 1.变量

### 1.1系统预定义变量

- $HOME

  ![image-20221020101113341](shell脚本.assets/image-20221020101113341.png)

- $PWD

  ![image-20221020101126971](shell脚本.assets/image-20221020101126971.png)

- $SHELL

  ![image-20221020101137881](shell脚本.assets/image-20221020101137881.png)

- $USER

  ![image-20221020101147820](shell脚本.assets/image-20221020101147820.png)

- 显示当前 Shell 中所有变量：set

  ![image-20221020101347622](shell脚本.assets/image-20221020101347622.png)

### 1.2自定义变量

- 定义变量语法

  ```shell
  变量名=变量值，注意，=号前后不能有空格
  ```

  - 定义变量 age=18

  ![image-20221020101709015](shell脚本.assets/image-20221020101709015.png)

  ![image-20221020101714398](shell脚本.assets/image-20221020101714398.png)

  

- 撤销变量

  ```shell
  unset 变量名
  ```

  - 撤销刚才定义的 age

    ![image-20221020101846058](shell脚本.assets/image-20221020101846058.png)

- 声明静态变量（一旦声明不可撤销）

  ```shell
  readonly 变量名
  ```

  - 声明sy的静态变量

    ![image-20221020102109593](shell脚本.assets/image-20221020102109593.png)

- 变量命名规则

  - 变量名称可以由字母、数字和下划线组成，但是不能以数字开头，环境变量名建议大写

  - **等号两侧不能有空格**

  - 变量的值如果有空格，需要使用双引号或单引号括起来

    ![image-20221020102334413](shell脚本.assets/image-20221020102334413.png)

    

### 1.3特殊变量

- $n

  - n 为数字，$0 代表该脚本名称，$1-$9 代表第一到第九个参数，十以上的参数，十以上的参数需要用大括号包含，如${20}

  - 演示，写一个脚本打印输入参数

    ![image-20221020102628457](shell脚本.assets/image-20221020102628457.png)

    ![image-20221020102717940](shell脚本.assets/image-20221020102717940.png)

    

- $#

  - 获取所有输入参数个数，常用于循环,判断参数的个数是否正确以及加强脚本的健壮性

    > Note : Don't include $0.
    
    ![image-20221020102912795](shell脚本.assets/image-20221020102912795.png)

- $*

  - 代表命令行中所有的参数，$*把所有的参数看成一个整体
  - ![image-20221020103137134](shell脚本.assets/image-20221020103137134.png)

- $@

  - 代表命令行中所有的参数，不过$@把每个参数区分对待
  - ![image-20221020103140808](shell脚本.assets/image-20221020103140808.png)

- $？

  - 最后一次执行的命令的返回状态。**如果这个变量的值为 0**，**证明上一个命令正确执行**；如果这个变量的值为非 0（具体是哪个数，由命令自己来决定），则证明上一个命令执行出错了

    ![image-20221020103234375](shell脚本.assets/image-20221020103234375.png)

## 2.运算符

- 语法

  ```shell
  $((运算式))” 或 “$[运算式]
  ```

- 计算 （3+3）/2

  ```shell
  $[(3+3)/2]
  ```

  ![image-20221020111020456](shell脚本.assets/image-20221020111020456.png)

## 3.条件语句

- 语法**（注意 condition 前后要有空格）**

  ```shell
  [ condition ]
  ```

  ![image-20221020111348136](shell脚本.assets/image-20221020111348136.png)

### 3.1两个整数之间比较

| condition | 说明     | 英文          |
| --------- | -------- | ------------- |
| -eq       | 等于     | equal         |
| -ne       | 不等于   | not equal     |
| -lt       | 小于     | less than     |
| -gt       | 大于     | greater than  |
| -le       | 小于等于 | less equal    |
| -ge       | 大于等于 | greater equal |

- 需求

  - 判断10是否大于5

    ![image-20221020113010854](shell脚本.assets/image-20221020113010854.png)

### 3.2字符串比较

- “=” 相等

- “!=” 不等

- 需求

  - 判断 sy 和 sy 是否相等

    ![image-20221020113104861](shell脚本.assets/image-20221020113104861.png)

### 3.3文件权限判断

- -r 有读的权限（read）

- -w 有写的权限（write）

  - 需求：判断sy.sh 脚本是否有写的权限

    ![image-20221020113303168](shell脚本.assets/image-20221020113303168.png)

- -x 有执行的权限（execute）

  - 需求：判断 sy.sh 是否有执行权

    ![image-20221020113345614](shell脚本.assets/image-20221020113345614.png)

### 3.4以文件类型判断

- -e 文件存在（existence）

  - 需求：判断 sy.sh 是否存在

    ![image-20221020113550517](shell脚本.assets/image-20221020113550517.png)

- -f 文件存在并且是一个常规的文件（file）

  - 判断 sy.sh 是否是一个常规存在文件

    ![image-20221020113653568](shell脚本.assets/image-20221020113653568.png)

- -d 文件存在并且是一个目录（directory）

  - 判断 shell 目录是否存在

    ![image-20221020113737400](shell脚本.assets/image-20221020113737400.png)

### 3.5&&  ||

- &&  满足前才执行后

  ![image-20221020113418057](shell脚本.assets/image-20221020113418057.png)

- || 满足其一就行

  ![image-20221020113845963](shell脚本.assets/image-20221020113845963.png)

## 4.流程控制语句

### 4.1if 判断

- 单条件判断

  - 语法

    ```shell
    if [ 条件判断式 ];then
    	程序
    fi
    ```

    ```shell
    if [ 条件判断式 ]
    then
    	程序
    fi
    ```

- 多条件

  - 语法

    ```shell
    if [ 条件判断式 ]
    then
    	程序
    elif [ 条件判断式 ]
    then
    	程序
    else
    	程序
    fi
    ```

- 实操：传递一个参数，如果是 java 则打印java工程师，如果是 vue 打印前端工程师，否则打印其它工种

  ```shell
  #!/bin/bash
  if [ $1 = java  ]
  then
          echo 'java 工程师'
  elif [ $1 = vue ]
  then
          echo '前端工程师'
  else
          echo '其它工种'
  fi
  ```

  

### 4.2case 语句

- 语法

  ```shell
  case $变量名 in
  "值 1"）
  如果变量的值等于值 1，则执行程序 1
  ;;
  "值 2"）
  如果变量的值等于值 2，则执行程序 2
  ;;
  …省略其他分支…
  *）
  如果变量的值都不是以上的值，则执行此程序
  ;;
  esac
  ```

  - ;;双分号表示这一行结束了，相当于 break
  - *)表示其它的 相当于java default

- 实操：传递一个参数，如果是 java 则打印java工程师，如果是 vue 打印前端工程师，否则打印其它工种

  ```shell
  #!/bin/bash
  case $1 in
          'java')
          echo "java 工程师"
  ;;
          'vue')
          echo '前端工程师'
  ;;
          *)
          echo '其它工种'
  ;;
  esac
  
  ```

### 4.3for 循环

- 语法

  ```shell
  for (( 初始值;循环控制条件;变量变化 ))
  do
  	程序
  done
  ```

  ```shell
  for 变量 in 值 1 值 2 值 3…
  do
  	程序
  done
  ```

- 实操：从1一直加到10

  ```shell
  #!/bin/bash
  #先定义变量 sum
  sum=0
  for((i=0;i<11;i++))
  do
          echo "目前的 sum 值" $sum
          echo "$i i的值" $i
          sum=$[$sum+$i]
  done
  echo $sum
  ```

  ```shell
  #!/bin/bash
  #先定义变量 sum
  sum=0
  for((i=0;i<11;i++))
  do
          echo "目前的 sum 值" $sum
          echo "$i i的值" $i
          sum=$[$sum+$i]
  done
  echo $sum
  
  echo '===$*====='
  for ret in "$*"
  do
          echo "sy $ret"
  done
  
  echo '====$@=='
  for ret1 in "$@"
  do
          echo "sy $ret1"
  
  done
  
  ```

  ![image-20221020121524085](shell脚本.assets/image-20221020121524085.png)

### 4.4while 循环

- 语法

  ```shell
  while [ 条件判断式 ]
  do
  	程序
  done
  ```

- 实操：从1一直加到10

  ```shell
  #!/bin/bash
  sum=0
  i=0
  while [ $i -lt 11 ]
  do
          sum=$[$sum+$i]
          i=$[$i+1]
  done
  
  echo $sum
  
  ```

## 5函数

### 5.1系统函数

- basename

  ```shell
  basename [pathname] [suffix]
  	basename [pathname]  可以获取文件名称带后缀
  	basename [pathname] [suffix] 去掉后缀文件名
  ```

  ![image-20221020124704127](shell脚本.assets/image-20221020124704127.png)

- dirname

  ```shell
  dirname 文件绝对路径
  ```

  ![image-20221020124859937](shell脚本.assets/image-20221020124859937.png)

### 5.2自定义函数

- 语法

  ```shell
  [ function ] funname[()]
  {
  	Action;
  	[return int;]
  }
  ```

  - 定义函数必须在调用之前定义
  - 函数返回值可以自己设定，使用 $获取

- 实操：做一个加法计算器

  ```shell
  #!/bin/bash
  function sum(){
  
          echo ===============
          ret=0;
          ret=$[ $1+$2 ]
          echo $ret
          return $ret
  }
  num1=1
  num2=3
  sum $num1 $num2
  ```

  

# 四、正则匹配

- grep

  ![image-20221020134436154](shell脚本.assets/image-20221020134436154.png)

- ^ 匹配一行的开头

  ![image-20221020134555669](shell脚本.assets/image-20221020134555669.png)

- $ 匹配一行的结束

  ![image-20221020134802959](shell脚本.assets/image-20221020134802959.png)

- . 匹配一个任意字符

  ![image-20221020134855771](shell脚本.assets/image-20221020134855771.png)

- '*'不单独使用，他和上一个字符连用，表示匹配上一个字符 **0 次或多次**

  ![image-20221020135126440](shell脚本.assets/image-20221020135126440.png)



- [ ] 表示匹配某个范围内的一个字符

  - [1,3]------匹配 1 或者 3

  - [0-9]------匹配一个 0-9 的数字

  - [0-9]*------匹配任意长度的数字字符串

  - [a-z]------匹配一个 a-z 之间的字符

  - [a-z]* ------匹配任意长度的字母字符串

  - [a-c, e-f]-匹配 a-c 或者 e-f 之间的任意字符

    ![image-20221020135350406](shell脚本.assets/image-20221020135350406.png)

- \ 表示转义
  - \ $

# 五、文本处理工具

## 1.概述

- **awk**:文本分析工具，把**文件逐行的读入**，以**空格**为默认分隔符将每行切片，切开的部分再进行分析处理

## 2.语法

```shell
awk [参数] ‘/pattern1/{action1}...’ filename
	pattern：表示 awk 在数据中查找的内容，就是匹配模式
	action：在找到匹配内容时所执行的一系列命令

	参数
			-F 指定输入文件分隔符
			-v 赋值一个用户定义变量
```

- 拷贝数据为了避免出现问题

  ![image-20221020141510556](shell脚本.assets/image-20221020141510556.png)

- 查看以 root 开头的数据的第二列的数据

  ![image-20221020141814447](shell脚本.assets/image-20221020141814447.png)

- 查看以 root 开头的数据的第二列和第五列的数据

  ![image-20221020141923943](shell脚本.assets/image-20221020141923943.png)

- 找到 passwd 中第一列的值

  ![image-20221020142223190](shell脚本.assets/image-20221020142223190.png)

- 将用户信息id +1显示

  ![image-20221020142514266](shell脚本.assets/image-20221020142514266.png)

  

## 3.awk 的内置变量

- FILENAME 文件名
- NR 已读的记录数（行号）
- NF 浏览记录的域的个数（切割后，列的个数）

- 需求

  - 统计passwd 每行行号

    ![image-20221020142848785](shell脚本.assets/image-20221020142848785.png)

  - 查找ip 地址

    ![image-20221020143743608](shell脚本.assets/image-20221020143743608.png)

# 六、杀tomcat 进程重启

- 找到进程 pid
- kill -9 pid
- 执行tomcat sh 脚本

```shell
#!/bin/bash
pid=`ps -ef|grep apache-tomcat-9.0.68| awk 'NR==1{print $2}'`
echo "tomcat pid : $pid"
kill -9 $pid
echo "tomcat 已经停止工作"
echo "准备启动tomcat"
cd /home/sycoder/apache-tomcat-9.0.68/bin
sh ./startup.sh
tail -n 500 -f ../logs/catalina.out

```

- 注意：面试官问到你有没有写过脚本的时候，我不允许你说没有

# 七、任务布置

- 对于 vim/vi 的正常使用要会
- 查看日志
- 文件目录指令
- 查找路径指令
- 压缩和解压
- 文件权限指令
- 磁盘查看
- 查看进程
- 防火墙
  - **开放端口信息**
- 检查网络是否通
  - ping 查看ip 是否通
  - telnet 检查端口是否通
- linux 软件安装
