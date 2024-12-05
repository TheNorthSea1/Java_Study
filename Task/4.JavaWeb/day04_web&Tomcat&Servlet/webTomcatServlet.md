# 一、WEB 概述

- Web是全球广域网，也称为万维网(www)，能够通过浏览器访问的网站

- Web 项目广泛的应用在我们生活中，能够通过浏览器去访问的，比如有京东，淘宝，天猫，自己的域名下的项目统称为 Web网站。

  <img src="webTomcatServlet.assets/image-20221006190928470.png" alt="image-20221006190928470" style="zoom:33%;" />

  <img src="webTomcatServlet.assets/image-20221006190937579.png" alt="image-20221006190937579" style="zoom:33%;" />

## 1.JavaWEB 概述

- JavaWEB 就是使用 JAVA 技术来完成 web 网页的技术栈。
- 市面上使用 javaweb 的公司，比如阿里，腾讯，百度，滴滴
- javaweb技术栈 
  - 静态资源（固定的网页）
  - 动态资源（java 动态生成的，js 动态获取的）
  - 数据库（mysql）
  - web 服务器（tomcat）

### 1.1B/S 架构

- B (Browser) 浏览器

- S（Server）服务器

  <img src="webTomcatServlet.assets/image-20221006192049021.png" alt="image-20221006192049021" style="zoom:25%;" />

  - 后端的登录操作具体流程分析
    - 第一打开网站，输入用户名和密码
    - 请求后端服务器
    - 后端服务器响应
    - 将响应数据加载进静态页面里面，展示给用户

### 1.2静态资源

- 静态资源：
  - HTML
  - CSS
  - 图片
  - 音频

- 动态数据
  - 每个人登录时候的用户名
  - ![image-20221006192707529](webTomcatServlet.assets/image-20221006192707529.png)

### 1.3动态资源

- 动态资源：Servlet/jsp 用来填充数据，也可以说是处理逻辑的

- 处理流程：

  - 查询数据库

  - 根据业务逻辑处理数据

  - 响应给静态资源，通过静态资源展示处理

    ![image-20221006192956141](webTomcatServlet.assets/image-20221006192956141.png)

### 1.4数据库

- 注意：使用 mysql 操作

- 访问流程

  <img src="webTomcatServlet.assets/image-20221006193309409.png" alt="image-20221006193309409" style="zoom: 67%;" />

### 1.5HTTP 协议

- TCP:最常用的就是 tcp
- UDP

### 1.6WEB 服务器

- **服务器**用来解析 http 协议，处理请求数据的，响应结果数据
- 使用 tomcat 服务器

## 2.HTTP

- HTTP : 超文本传输协议，规定了 b/s 之间的数据传输规则

  <img src="webTomcatServlet.assets/image-20221006194728946.png" alt="image-20221006194728946" style="zoom: 80%;" />

- 数据传输：请求数据去请求后端服务器，服务器按照指定的格式返回给浏览器

### 2.1HTTP 协议特点

- 基于TCP的（面向连接的，连接之前需要**三次握手**）
- 基于请求、响应模型的（一次请求对应一次响应，一一对应的关系）
- HTTP 是无状态的，没有去存储响应数据，响应的数据是独立的
  - 优点：速度快
  - 缺点：上一次请求和下一次请求的数据不能共享

### 2.2请求数据格式

- 请求数据分为三部分

  - **请求行**

    <img src="webTomcatServlet.assets/image-20221007100250619.png" alt="image-20221007100250619" style="zoom: 67%;" />

    - **请求方法**：描述了客户端希望服务器对资源执行的动作。HTTP/1.1协议中定义了八种请求方法，常用的有GET和POST两种。GET方法用于请求访问已被URI识别的资源，POST方法用于向指定资源提交数据，请求服务器进行处理（例如提交表单或者上传文件）。
    - **请求URL**：命名了所请求的资源，可以是完整URL，也可以是URL路径组件。通过URL可以定位到网络中的某个资源。
    - **HTTP协议版本**：指明了客户端使用的HTTP协议版本，如HTTP/1.1。

  - **请求头**

    <img src="webTomcatServlet.assets/image-20221007100352129.png" alt="image-20221007100352129" style="zoom: 67%;" />
  
    - **Host**：指明了请求的资源所在的服务器和端口号。
    - **User-Agent**：描述了客户端浏览器的版本信息，服务器可以根据这个头信息来解决浏览器的兼容性问题。
    - **Referer**：描述了当前请求的来源页面，可以用于防盗链和统计工作。
    - **Cookie**：包含了客户端发送给服务器的各种Cookie信息，用于维持会话状态等。
    - **Content-Type**：描述了请求体的数据格式以及编码格式，仅当请求方法包含请求体时才需要此头信息。
    - **Content-Length**：描述了请求体的长度，仅当请求方法包含请求体时才需要此头信息。

  - **请求体**

    ![image-20221007100509421](webTomcatServlet.assets/image-20221007100509421.png)
  
    ![image-20221007100524884](webTomcatServlet.assets/image-20221007100524884.png)
  
    - 请求体是请求数据的最后一部分，包含了要发送给服务器的数据。只有当请求方法需要时（如POST、PUT等），请求体才存在。请求体的数据格式由Content-Type头信息指定。
    - 如果是 Get 请求或者 delete 请求，请求体放置到请求行位置
    - POST 放置到请求体中
    - get 请求参数大小是有限制的，Post没有大小限制

### 2.3响应数据格式

- **响应行**

  <img src="webTomcatServlet.assets/image-20221007101819007.png" alt="image-20221007101819007" style="zoom:50%;" />

  - **HTTP协议版本**：指明了服务器使用的HTTP协议版本，如HTTP/1.1。
  - **状态码**：一个三位数的整数，用于表示请求的处理结果。状态码分为五类，分别以1xx、2xx、3xx、4xx和5xx开头，每类状态码都有特定的含义。例如，200表示请求成功，404表示未找到资源。
  - **状态信息**：对状态码的简短描述，如“OK”表示请求成功，“Not Found”表示未找到资源。

- **响应头**

  <img src="webTomcatServlet.assets/image-20221007101900668.png" alt="image-20221007101900668" style="zoom:50%;" />

  - **Date**：表示响应生成的时间。
  - **Server**：包含了服务器的信息，如服务器的名称和版本。
  - **Content-Type**：描述了响应体的数据格式以及编码格式，如text/html表示HTML格式的数据，application/json表示JSON格式的数据等。
  - **Content-Length**：描述了响应体的长度，以字节为单位。
  - **Set-Cookie**：包含了服务器发送给客户端的Cookie信息，用于维持会话状态等。
  - **Cache-Control**：用于指定缓存策略，如no-cache表示不允许缓存，max-age=3600表示缓存时间为3600秒等。

- **响应体**

  - 提供数据给前端进行数据展示的
  - 服务器返回给客户端的数据。响应体的数据格式由Content-Type头信息指定。根据请求的不同，响应体可以包含HTML页面、JSON数据、图片、视频等多种类型的数据。

  <img src="webTomcatServlet.assets/image-20221007101928076.png" alt="image-20221007101928076" style="zoom:50%;" />

- **状态码**:用来标识本次请求的状态成功与否

  - 200 请求成功

    ![image-20221007102512013](webTomcatServlet.assets/image-20221007102512013.png)

    

  - 404 找不到请求资源

    <img src="webTomcatServlet.assets/image-20221007102257754.png" alt="image-20221007102257754" style="zoom:50%;" />

    

  - 500 服务器报错
  
  - [服务端状态码](服务器状态码.md)

## 3.Tomcat

- web 服务器：是一个应用程序（**tomcat**）,是对 http 进行封装，能够简化程序员的开发，能够提供网上信息的交互和浏览。

- 访问项目服务器的流程

  - 写好前端静态页面，以及后端服务代码
  - 部署 web 服务器（tomcat）
  - 将写好的静态页面部署到 web 服务器上

- tomcat 是免费的，是 apache 基金会下的（已经学过 maven 了）[Apache Tomcat® - Welcome!](https://tomcat.apache.org/)

  ![image-20221007105356481](webTomcatServlet.assets/image-20221007105356481.png)

- 如何学习 tomcat 可以参考mysql

  - 下载
  - 安装 [参考csdn](https://blog.csdn.net/weixin_47700137/article/details/116055222?ops_request_misc=%257B%2522request%255Fid%2522%253A%25226D58964B-35A0-4552-ACD4-C9441A93E4F6%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=6D58964B-35A0-4552-ACD4-C9441A93E4F6&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-116055222-null-null.142^v100^pc_search_result_base5&utm_term=tomcat%E5%AE%89%E8%A3%85%E5%8F%8A%E9%85%8D%E7%BD%AE%E6%95%99%E7%A8%8B&spm=1018.2226.3001.4187)
  - 启动/关闭
  - 配置（环境配置）

- JAVAEE：java 企业版本，支持了技术规范：**JDBC**,**EJB**,**JPA**

### 3.1下载

<img src="webTomcatServlet.assets/image-20221007110313271.png" alt="image-20221007110313271" style="zoom:50%;" />

<img src="webTomcatServlet.assets/image-20221007110620359.png" alt="image-20221007110620359" style="zoom:50%;" />



### 3.2解压

![image-20221007111852837](webTomcatServlet.assets/image-20221007111852837.png)

<img src="webTomcatServlet.assets/image-20221007112235651.png" alt="image-20221007112235651" style="zoom:50%;" />

- .bat 结尾的是 windows 的可执行文件

- .sh 结尾的是linux 的可执行文件

  <img src="webTomcatServlet.assets/image-20221007112352024.png" alt="image-20221007112352024" style="zoom:50%;" />

  

- webapps 就是以后我们需要部署自己项目的位置

### 3.3启动tomcat

- 出现乱码问题

  ![image-20221007112643436](webTomcatServlet.assets/image-20221007112643436.png)

- 解决办法

  - 找到日志配置

    ![image-20221007112722135](webTomcatServlet.assets/image-20221007112722135.png)

    ![image-20221007112847991](webTomcatServlet.assets/image-20221007112847991.png)

    

- 启动成功会出现如下图案

  ![image-20221007112932743](webTomcatServlet.assets/image-20221007112932743.png)

### 3.4关闭tomcat

- 直接关掉运行窗口
- shutdown.bat
- **ctrl + c**

### 3.5配置

- 很容易遇到这个问题

![image-20221007114234878](webTomcatServlet.assets/image-20221007114234878.png)

- 解决办法：打开 conf/server.xml

  ![image-20221007114335036](webTomcatServlet.assets/image-20221007114335036.png)

  

![image-20221007114445841](webTomcatServlet.assets/image-20221007114445841.png)

### 3.6项目部署

- 手写一个 index.html 静态页面
- 将index.html 拷贝到 webapps 下面
- 后期通过maven 打包打成一个war 包来部署

## 4.maven 创建 web 项目



### 4.1使用普通的创建方式

<img src="webTomcatServlet.assets/image-20221007135054516.png" alt="image-20221007135054516" style="zoom: 67%;" />

- 使用普通的这种方式，项目结构是不完整的，需要补齐项目缺失的目录结构

![image-20221007135727939](webTomcatServlet.assets/image-20221007135727939.png)

<img src="webTomcatServlet.assets/image-20221007135738047.png" alt="image-20221007135738047" style="zoom:50%;" />

- 得到的web 项目结构

  <img src="webTomcatServlet.assets/image-20221007140253755.png" alt="image-20221007140253755" style="zoom: 67%;" />
  
- Web项目结构更改

  ![image-20241011220341798](./assets/image-20241011220341798.png)

### 4.2使用 maven 的骨架创建项目

- 选择 maven 的骨架

  <img src="./assets/image-20241011214909321.png" alt="image-20241011214909321"  />

  

  <img src="./assets/image-20241011215015059.png" alt="image-20241011215015059"  />

- 修改maven 配置（新版idea不需要）

  <img src="webTomcatServlet.assets/image-20221007140840914.png" alt="image-20221007140840914" style="zoom: 33%;" />

- 缺失了 java 源代码目录和 resources 目录（新版idea只缺少java原代码目录）

  ![image-20241011215334420](./assets/image-20241011215334420.png)

## 5.把 java 项目部署到 tomcat

- 将 java 代码打成一个 war 包

  ![image-20221007143935004](webTomcatServlet.assets/image-20221007143935004.png)

- 使用Maven的打包

  ![image-20241011223734632](./assets/image-20241011223734632.png)

- 将 war 包丢进tomcat webapps 下面

  ![image-20221007144008296](webTomcatServlet.assets/image-20221007144008296.png)

- 访问 ip + port / 文件夹名称

  ![image-20221007144038416](webTomcatServlet.assets/image-20221007144038416.png)

- 存在的问题：

  - 开发在开发工具里面开发，每次部署需要移到tomcat 中去，太麻烦了
  - 解决方式：使用 idea 集成 tomcat

## 6.idea 使用 tomcat

- 点击配置编辑

  ![image-20221007145150196](webTomcatServlet.assets/image-20221007145150196.png)

- 添加 tomcat

  ![image-20221007145204669](webTomcatServlet.assets/image-20221007145204669.png)

- 配置本地tomcat

  ![image-20221007145306426](webTomcatServlet.assets/image-20221007145306426.png)

- 部署项目

  ![image-20221007145344973](webTomcatServlet.assets/image-20221007145344973.png)

- 注意：部署的时候会出现两种不同的类型

  - **war 模式**：将WEB项目打成war 包，把war 包发布到 tomcat 服务器上
  - war exploded模式：把web 工程以文件夹的位置关系发布到 tomcat 服务器上

- 容易出现的端口占用的问题

  ![image-20221007150010583](webTomcatServlet.assets/image-20221007150010583.png)

  

- 配置总结

  <img src="webTomcatServlet.assets/image-20221007150154693.png" alt="image-20221007150154693" style="zoom:50%;" />

## 7.Servlet

- 概述：Java Servlet 是运行在 Web 服务器或应用服务器上的程序，它是作为来自 Web 浏览器或其他 HTTP 客户端的请求和 HTTP 服务器上的数据库或应用程序之间的中间层。

- Servlet 架构图

  <img src="webTomcatServlet.assets/image-20221007151523448.png" alt="image-20221007151523448" style="zoom:50%;" />

  

- Servlet :是 javaee 的规范，其实就是个接口

  

### 7.1quickstart 快速入门

- 需求：写一个自定义 servlet 类，通过浏览器去访问到我们自己写的类

#### 7.1.1 导包

```xml
<dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
<!--            提供provided 编译和测试有效，避免和 tomcat 中的 servlet-api 包冲突报错-->
</dependency>
```

#### 7.1.2 实现 servlet 接口

```java
public class MyServlet implements Servlet {
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * 需要写的就是业务的这个方法
     * @param servletRequest
     * @param servletResponse
     * @throws ServletException
     * @throws IOException
     */
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("测试 servlet 的联通性");
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
```



#### 7.1.3配置 servlet（注解的方式）

```java
@WebServlet("/myServlet")
```

- 注意：除了注解可以使用 web.xml 手动配置的

#### 7.1.4访问servlet

```java
http://localhost:8080/myServlet
```

### 7.2 执行流程

<img src="webTomcatServlet.assets/image-20221007154116370.png" alt="image-20221007154116370" style="zoom: 80%;" />

- 浏览器发起 http://localhost:8080/myServlet 请求
  - http://localhost:8080 找到需要访问的服务器（tomcat）
  - myServlet 使用 WebServlet 注解命名的类
- 找到 servlet 之后是怎么访问的
  - tomcat 服务器会为我们的MyServlet 创建一个对象，调用自己service 方法，也即调用了子类实现的service
  - service 方法有两个参数
    - ServletRequest
      - 封装了请求数据
      - 取值从这里面取
    - ServletResponse
      - 封装了响应数据
      - 响应值从这里面传

> 1. **浏览器发送请求**：用户在浏览器中输入URL，浏览器根据URL建立与Web服务器（如Tomcat）的连接，并发送HTTP请求。
> 2. **服务器解析请求**：Web服务器接收到请求后，解析请求数据，并根据请求的URL路径找到对应的Servlet。
> 3. **实例化Servlet对象**：如果Servlet尚未被实例化，Web服务器会创建Servlet实例，并调用其`init()`方法进行初始化。
> 4. **调用service方法**：Web服务器调用Servlet的`service()`方法来处理请求。`service()`方法根据请求的类型（如GET、POST等）调用相应的`doGet()`、`doPost()`等方法。
> 5. **处理请求并生成响应**：在`doGet()`、`doPost()`等方法中，程序员编写业务逻辑来处理请求，生成响应内容，并将其写入`HttpServletResponse`对象中。
> 6. **发送响应**：Web服务器将`HttpServletResponse`对象中的响应内容发送回浏览器。
> 7. **浏览器显示响应**：浏览器接收到响应后，解析并显示响应内容。

- 额外面试拓展
  - Servlet 是谁创建的对象
    - tomcat 服务器
  - 为什么一定能调用 service 方法
    - 因为必须实现servlet 接口必须实现里面的方法，通过多态能够调用到子类（实现类的实现）

### 7.3生命周期

- servlet 从创建到销毁的一个过程

  - 创建

    - 第一次访问servlet 的时候，服务器调用 init 方法创建servlet，init 只创建一次，使用了一种**懒加载机制**

      - > 懒加载机制:  为延迟加载或按需加载，是一种编程模式和优化策略，其核心思想在于推迟对象的初始化、数据的加载或资源的获取，直到它们真正被需要时才进行。

      - 优点：节约内存开销

      - 缺点：第一个用户的体验感不好

      - 把注解加上启动参数loadOnStartup 非负数的时候容器加载的时候就启动了
      
      - ```java
        @WebServlet(urlPatterns = "/myServlet",loadOnStartup = 0)
        ```

  - 业务操作

    - service方法处理业务逻辑，可以访问多次

  - 销毁
  
    - 关闭服务的时候
    - 或者手动调用 destroy 方法的时候，垃圾回收器就会不定时的回收Servlet 对象

### 7.4体系结构

利用这个可以清楚的看到类的体系结构图

![image-20241012183244026](./assets/image-20241012183244026.png)

![image-20221007160612816](webTomcatServlet.assets/image-20221007160612816.png)

#### 7.4.1 继承 HttpServlet

- 可以单独重写我们想实现的方法

  ```java
  @WebServlet("/extendsServlet")
  public class MyServletExtends extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          System.out.println("-------------访问我们自己的get 请求");
      }
  }
  ```

- **思考：Servlet如何判断从客户端发来的请求类型呢？**

  - 通过 查看 HttpServlet 类
  - service方法接收来自客户端的请求，进行判断请求类型，再调用对应的请求方法
  
  ```java
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          String method = req.getMethod();
          long lastModified;
          if (method.equals("GET")) {
              lastModified = this.getLastModified(req);
              if (lastModified == -1L) {
                  this.doGet(req, resp);
              } else {
                  long ifModifiedSince = req.getDateHeader("If-Modified-Since");
                  if (ifModifiedSince < lastModified) {
                      this.maybeSetLastModified(resp, lastModified);
                      this.doGet(req, resp);
                  } else {
                      resp.setStatus(304);
                  }
              }
          } else if (method.equals("HEAD")) {
              lastModified = this.getLastModified(req);
              this.maybeSetLastModified(resp, lastModified);
              this.doHead(req, resp);
          } else if (method.equals("POST")) {
              this.doPost(req, resp);
          } else if (method.equals("PUT")) {
              this.doPut(req, resp);
          } else if (method.equals("DELETE")) {
              this.doDelete(req, resp);
          } else if (method.equals("OPTIONS")) {
              this.doOptions(req, resp);
          } else if (method.equals("TRACE")) {
              this.doTrace(req, resp);
          } else {
              String errMsg = lStrings.getString("http.method_not_implemented");
              Object[] errArgs = new Object[]{method};
              errMsg = MessageFormat.format(errMsg, errArgs);
              resp.sendError(501, errMsg);
          }
  
      }
  ```

## 8.urlPattern 配置

- 想要访问servlet 就需要去配置访问路径

- 注意：必须使用 / 打头

- 配置规则
  - 可以做到精确配置 
    - "/myServlet"
    - /*
    - ".do"
  
- 建议：
  - 能使用精确配置就使用精确配置
  - **精确配置**>扩展名配置>模糊配置
  
- ```java
  @WebServlet(urlPatterns ={"/1","/2"})
  public class MyServletExtends extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          System.out.println("-------------访问我们自己的get 请求");
      }
  }
  ```

​	

## 9.XML配置 servlet

> 在web.xml 文件中增加配置

```xml
<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <servlet>
<!--    servlet 的名称-->
    <servlet-name>servlet</servlet-name>
<!--    类的权限定类名-->
    <servlet-class>cn.sycoder.XmlServlet</servlet-class>
  </servlet>
  <!--    urlpattern-->
  <servlet-mapping>
<!--    保持和上面一致-->
    <servlet-name>servlet</servlet-name>
<!--    url-pattern-->
    <url-pattern>/xml</url-pattern>
  </servlet-mapping>
</web-app>
```

