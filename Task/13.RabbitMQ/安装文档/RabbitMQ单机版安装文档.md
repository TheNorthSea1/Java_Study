# RabbitMQ安装文档

RabbitMQ官网下载地址：https://www.rabbitmq.com/download.html

## 1.安装依赖

- 在线安装依赖环境：

  ```shell
  yum install build-essential openssl openssl-devel unixODBC unixODBC-devel make gcc gcc-c++ kernel-devel m4 ncurses-devel tk tc xz
  ```

## 2.安装环境

- 上传安装包到linux 环境

  ![image-20221228130037676](picture/image-20221228130037676.png)

### 2.1安装erlang环境

```java
rpm -ivh erlang-23.3.4.5-1.el7.x86_64.rpm	
```

- 如果出现如下错误

  ![image-20221228125924544](picture/image-20221228125924544.png)

- 使用yum升级gblic 版本

  ```shell
  sudo yum install zlib-devel bzip2-devel openssl-devel ncurses-devel sqlite-devel readline-devel tk-devel gcc make -y
  ```

### 2.2安装rabbitMQ

- 安装socat

  ```shell
  yum install socat -y
  ```

- 安装rabbitMQ

  ```shell
  rpm -ivh rabbitmq-server-3.8.34-1.suse.noarch.rpm
  ```

- 开启管理界面

  ```shell
  rabbitmq-plugins enable rabbitmq_management
  ```

- 启动rabbitmq

  ```shell
  /bin/systemctl start rabbitmq-server.service
  ```

- 查看rabbitmq进程

  ```shell
  ps -ef|grep rabbitmq;
  ```

### 	2.3rabbitMQ操作

- 开放端口

  > Note:
  >
  > 如果是阿里云作为服务器，需要去开放端口。

  ```shell
  sudo systemctl start firewalld  // 开启防火墙。
  sudo systemctl enable firewalld // 设置 Firewalld 开机启动
  
  firewall-cmd --zone=public --add-port=15672/tcp --permanent
  firewall-cmd --zone=public --add-port=5672/tcp --permanent  // 用于AMQP
  firewall-cmd --reload
  ```

  > <img src="./assets/image-20241109215256364.png" alt="image-20241109215256364" style="zoom:67%;" />
  >
  > 使用http://118.31.104.65:15672 地址访问。（注意：ip地址根据你的）
  >
  > **发现只能通过本地登入，移步下面添加用户密码。**

- 查看服务状态

  ```shell
  /sbin/service rabbitmq-server status
  ```

- 停止服务

  ```shell
  /sbin/service rabbitmq-server stop
  ```

- 添加开机自启动

  ```shell
  chkconfig rabbitmq-server on
  ```

  

## 3.添加用户密码

- 添加账户密码

  ```shell
  rabbitmqctl add_user bwh 123456
  ```

- 设置角色

  ```java
  rabbitmqctl set_user_tags bwh administrator
  ```

- 设置用户权限

  ```shell
  rabbitmqctl set_permissions -p "/" bwh ".*" ".*" ".*"
  ```

- 查看用户和角色

  ```shell
  rabbitmqctl list_users
  ```

- 使用账号密码登入

  <img src="./assets/image-20241109215928397.png" alt="image-20241109215928397" style="zoom:67%;" />

## 4.重置命令

- 关闭rabbitMQ

  ```shell
  rabbitmqctl stop_app
  ```

- 重置命令

  > 需要先停止RabbitMQ

  ```shell
  rabbitmqctl reset
  ```

- 重新启动

  ```shell
  rabbitmqctl start_app
  ```

  

## 5.服务出问题

- 查看主机名称

  ```shell
  hostnamectl status
  ```

  ![image-20230108132217642](../配套资料/picture/image-20230108132217642.png)

- 重新设置一下主机名称

  ```shell
  hostnamectl set-hostname localhost.localdomain
  ```

  
