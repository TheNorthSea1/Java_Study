# 一、MyBatis基础概念

1. **什么是MyBatis？**

   MyBatis是一个半ORM（对象关系映射）框架，它内部封装了JDBC，加载驱动、创建连接、创建statement等繁杂的过程，开发者开发时只需要关注如何编写SQL语句，可以严格控制sql执行性能，灵活度高。MyBatis可以使用XML或注解来配置和映射原生信息，将POJO映射成数据库中的记录，避免了几乎所有的JDBC代码和手动设置参数以及获取结果集。

2. **MyBatis的优缺点是什么？**

   * **优点**：

	 * 基于SQL语句编程，相当灵活，不会对应用程序或者数据库的现有设计造成任何影响，SQL写在XML里，解除sql与程序代码的耦合，便于统一管理。
	 * 提供XML标签，支持编写动态SQL语句，并可重用。
	 * 与JDBC相比，减少了50%以上的代码量，消除了JDBC大量冗余的代码，不需要手动开关连接。
	 * 很好的与各种数据库兼容（因为MyBatis使用JDBC来连接数据库，所以只要JDBC支持的数据库MyBatis都支持）。
	 * 提供映射标签，支持对象与数据库的ORM字段关系映射；提供对象关系映射标签，支持对象关系组件维护。

   * **缺点**：

	 * SQL语句的编写工作量较大，尤其当字段多、关联表多时，对开发人员编写SQL语句的功底有一定要求。
	 * SQL语句依赖于数据库，导致数据库移植性差，不能随意更换数据库。

# 二、MyBatis核心对象与工作原理

1. **MyBatis有哪些核心对象？**

   MyBatis也有四大核心对象，包括：

   * **SqlSession对象**：该对象中包含了执行SQL语句的所有方法，类似于JDBC里面的Connection。
   * **Executor接口**：它将根据SqlSession传递的参数动态地生成需要执行的SQL语句，同时负责查询缓存的维护，类似于JDBC里面的Statement/PrepareStatement。
   * **MappedStatement对象**：该对象是对映射SQL的封装，用于存储要映射的SQL语句的id、参数等信息。
   * **ResultHandler对象**：用于对返回的结果进行处理，最终得到自己想要的数据格式或类型，可以自定义返回类型。

2. **MyBatis中Dao接口的工作原理是什么？**

   Dao接口即Mapper接口。接口的全限名就是映射文件中的namespace的值；接口的方法名就是映射文件中Mapper的Statement的id值；接口方法内的参数就是传递给sql的参数。

   Mapper接口是没有实现类的，当调用接口方法时，接口全限名+方法名拼接字符串作为key值，可唯一定位一个MapperStatement。在Mybatis中，每一个`<select>`、`<insert>`、`<update>`、`<delete>`标签，都会被解析为一个MapperStatement对象。

   Mapper接口的工作原理是JDK动态代理，Mybatis运行时会使用JDK动态代理为Mapper接口生成代理对象proxy，代理对象会拦截接口方法，转而执行MapperStatement所代表的sql，然后将sql执行结果返回。

# 三、MyBatis的映射与动态SQL

1. **MyBatis如何将sql执行结果封装为目标对象并返回？**

   MyBatis可以通过以下两种方式将sql执行结果封装为目标对象并返回：

   * 使用标签，逐一定义数据库列名和对象属性名之间的映射关系。
   * 使用sql列的别名功能，将列的别名书写为对象属性名。

   有了列名与属性名的映射关系后，MyBatis通过反射创建对象，同时使用反射给对象的属性逐一赋值并返回，那些找不到映射关系的属性，是无法完成赋值的。

2. **MyBatis动态SQL有什么用？执行原理是什么？有哪些动态SQL？**

   MyBatis动态SQL可以在Xml映射文件内，以标签的形式编写动态SQL，执行原理是根据表达式的值完成逻辑判断并动态拼接SQL的功能。MyBatis提供了9种动态SQL标签：`trim`、`where`、`set`、`foreach`、`if`、`choose`、`when`、`otherwise`、`bind`。

# 四、MyBatis的缓存与分页

1. **MyBatis的一级、二级缓存是什么？**

   * **一级缓存**：基于PerpetualCache的HashMap本地缓存，其存储作用域为Session，当Session flush或close之后，该Session中的所有Cache就将清空，默认打开一级缓存。
   * **二级缓存**：与一级缓存其机制相同，默认也是采用PerpetualCache，HashMap存储，不同在于其存储作用域为Mapper（Namespace），并且可自定义存储源，如Ehcache。默认不打开二级缓存，要开启二级缓存，使用二级缓存属性类需要实现Serializable序列化接口（可用来保存对象的状态），可在它的映射文件中配置。

2. **MyBatis是如何进行分页的？分页插件的原理是什么？**

   MyBatis使用RowBounds对象进行分页，它是针对ResultSet结果集执行的内存分页，而非物理分页。可以在sql内直接书写带有物理分页的参数来完成物理分页功能，也可以使用分页插件来完成物理分页。分页插件的基本原理是使用MyBatis提供的插件接口，实现自定义插件，在插件的拦截方法内拦截待执行的sql，然后重写sql，根据dialect方言，添加对应的物理分页参数。

# 五、MyBatis的常见问题

1. **#{}和${}的区别是什么？**

   * `#{}`是预编译处理，MyBatis在处理#{}时，会对sql语句进行预处理，将sql中的#{}替换为占位符?，使用#{}可以有效的防止SQL注入，提高系统安全性。
   * `${}`是字符串替换，MyBatis将直接将替换变量的值进行拼接操作，如果是用户输入的内容，可能导致SQL注入安全问题。

2. **MyBatis的Xml映射文件中，不同的Xml映射文件，id是否可以重复？**

   不同的Xml映射文件，如果配置了namespace，那么id可以重复；如果没有配置namespace，那么id不能重复。原因就是namespace+id是作为Map的key使用的，如果没有namespace，就剩下id，那么id重复会导致数据互相覆盖。有了namespace，自然id就可以重复，namespace不同，namespace+id自然也就不同。

3. **MyBatis的Xml映射文件中有哪些标签？**

   - CRUD操作标签：包括select、insert、update、delete等，用于定义对数据库的增、删、改、查操作。
   - 结果集映射标签：包括<resultMap>和<result>，用于定义Java对象和数据库表之间的映射关系。
   - SQL片段标签：包括<sql>和<include>，用于定义可重用的SQL代码片段。
   - 动态SQL标签：包括<if>、<choose>、<when>、<otherwise>、<where>、<set>、<foreach>和<bind>等，用于动态生成SQL语句中的条件判断、循环、赋值等操作

4. **MyBatis的动态SQL是做什么的？都有哪些动态SQL？**

   MyBatis的动态SQL可以在Xml映射文件内以标签的形式编写动态SQL。它可以根据表达式的值完成逻辑判断并动态拼接SQL的功能。MyBatis提供了9种动态SQL标签：trim、where、set、foreach、if、choose、when、otherwise、bind。

5. **MyBatis如何执行批量插入？能返回主键ID吗？**

   MyBatis可以通过<insert>标签的useGeneratedKeys属性和keyProperty属性来执行批量插入并返回主键ID。useGeneratedKeys属性设置为true表示使用JDBC的getGeneratedKeys方法来获取数据库自动生成的主键。keyProperty属性指定了Java对象的哪个属性用来接收生成的主键值。

6. **MyBatis是否支持延迟加载？如果支持，它的实现原理是什么？**

   MyBatis支持延迟加载。延迟加载是一种优化策略，它允许在真正需要数据时才从数据库中加载数据。MyBatis的延迟加载是通过Mapper配置文件中的<association>和<collection>标签来实现的。当配置了这些标签后，MyBatis会在需要时才加载关联的数据，从而提高了性能。

7. **MyBatis如何处理数据库链接中断的问题？**

   数据库链接中断是一个常见的问题。MyBatis本身并不直接处理这个问题，但可以通过配置一些参数来减少因链接中断而导致的等待时间。例如可以设置connect_timeout参数来定义链接的超时时间，当链接超时后MyBatis会抛出异常而不是一直等待。此外，还可以使用连接池等技术来管理和维护数据库连接。

8. **MyBatis与Spring整合后为什么一级缓存会失效？**

   当MyBatis与Spring整合时，由于Spring会管理MyBatis的SqlSession对象，并且每次调用完一个dao方法后就会关闭SqlSession对象，这导致一级缓存失效。因为一级缓存是基于SqlSession对象的，当SqlSession对象关闭时，一级缓存就会被清空。如果开启了事务，一级缓存就会生效，因为开启了事务后SqlSession对象不会立即关闭，而是等到事务结束时才关闭。

