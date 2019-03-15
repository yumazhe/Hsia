# Hsia
a tool for db sharding

Introduction：
    1： 创建数据库：执行sharding.sql文件
        创建 sharding_0000\sharding_0001\sharding_0002\sharding_0003四个库
        每个库创建四个表 t_sharding_0000\t_sharding_0001\t_sharding_0002\t_sharding_0003
    



Use Rules：
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">
      
      <!-- aop -->
      <bean class="com.Hsia.sharding.aop.JDBCExecuterInterceptor" />
      
      <!-- 注入jdbc的执行引擎 -->
      <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSourceGroup" />
      </bean>
    
      <!-- 路由规则配置 -->
      <bean id="shardingRule" class="com.Hsia.sharding.route.ShardingRule">
        <property name="write_index" value="0" /><!-- 写库索引 -->
        <property name="read_index" value="0" /><!-- 读库索引 -->
        <property name="dbQuantity" value="4" /><!-- 数据库数量 -->
        <property name="tbQuantity" value="4" /><!-- 单库中表的数量 -->
        <property name="routeKey" value="id" /><!-- 指定路由主键，如果没有指定则全局扫描 -->
      </bean>
      
      <bean id="dataSourceGroup" class="com.Hsia.sharding.dataSource.DatasourceGroup">
        <property name="targetDataSources">
          <map key-type="java.lang.Integer">
            <entry key="0" value-ref="dataSource0" />
            <entry key="1" value-ref="dataSource1" />
            <entry key="2" value-ref="dataSource2" />
            <entry key="3" value-ref="dataSource3" />
          </map>
        </property>
      </bean>
      <!-- 以下配置数据源信息 -->
      <bean id="dataSource0" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close" init-method="init">
        <property name="driverClassName" value="${jdbc.driverClassName}" />
            <property name="url" value="${jdbc.url1}" />
            <property name="username" value="${jdbc.username}" />
            <property name="password" value="${jdbc.password}" />
            <property name="maxActive" value="100" />
            <property name="testOnBorrow" value="true" />
            <property name="validationQuery" value="select 0" />
      </bean>
      <bean id="dataSource1" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close" init-method="init">
        <property name="driverClassName" value="${jdbc.driverClassName}" />
            <property name="url" value="${jdbc.url2}" />
            <property name="username" value="${jdbc.username}" />
            <property name="password" value="${jdbc.password}" />
            <property name="maxActive" value="100" />
            <property name="testOnBorrow" value="true" />
            <property name="validationQuery" value="select 0" />
      </bean>
      <bean id="dataSource2" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close" init-method="init">
        <property name="driverClassName" value="${jdbc.driverClassName}" />
            <property name="url" value="${jdbc.url3}" />
            <property name="username" value="${jdbc.username}" />
            <property name="password" value="${jdbc.password}" />
            <property name="maxActive" value="100" />
            <property name="testOnBorrow" value="true" />
            <property name="validationQuery" value="select 0" />
      </bean>
      <bean id="dataSource3" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close" init-method="init">
        <property name="driverClassName" value="${jdbc.driverClassName}" />
            <property name="url" value="${jdbc.url4}" />
            <property name="username" value="${jdbc.username}" />
            <property name="password" value="${jdbc.password}" />
            <property name="maxActive" value="100" />
            <property name="testOnBorrow" value="true" />
            <property name="validationQuery" value="select 0" />
      </bean>
      <!-- 以上配置数据源信息 -->
    </beans>


Performance Testing：



Contact：