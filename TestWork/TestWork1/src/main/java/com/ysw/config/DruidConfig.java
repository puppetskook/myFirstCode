package com.ysw.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;

@Configuration
public class DruidConfig {
	
	//使其它数据源配置有效
	@ConfigurationProperties(prefix="spring.datasource")
	@Bean(destroyMethod="close",initMethod="init")
	public DataSource druidDataSource(){
		return new DruidDataSource();
	}
	
	//配置监控 服务器
	@Bean
	public ServletRegistrationBean<Servlet> statViewServlet(){
		ServletRegistrationBean<Servlet> srb=new ServletRegistrationBean<Servlet>(new StatViewServlet(),"/druid/*");
		//添加IP白名单-空表示默认允许所有访问
		srb.addInitParameter("allow",""); 
		//添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
		srb.addInitParameter("deny","192.168.11.18");
		//添加控制台管理用户
		srb.addInitParameter("loginUsername","admin");
		srb.addInitParameter("loginPassword","999999");
		//是否能够重置数据
		srb.addInitParameter("resetEnable","false"); 
		return srb;
	}
	//配置服务过滤器
	@Bean
	public FilterRegistrationBean<Filter> statFile(){
		FilterRegistrationBean<Filter> frb=new FilterRegistrationBean<Filter>(new WebStatFilter());
		//添加过滤规则
		frb.addUrlPatterns("/*");
		//忽略过滤格式
	frb.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return frb;
	}
}