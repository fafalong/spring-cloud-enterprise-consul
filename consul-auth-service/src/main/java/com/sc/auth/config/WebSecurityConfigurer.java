package com.sc.auth.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

import com.sc.auth.service.AuthUserDetailsService;

@Configuration
//@Order(2)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {   
	
    //自定义UserDetailsService注入
    @Autowired
    private AuthUserDetailsService userDetailsService;
 
    
    //Notes: 配置匹配用户时密码规则
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //Notes: 配置全局设置
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //设置UserDetailsService以及密码规则
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
	
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable()
        .requestMatchers().anyRequest()
        .and()         
        .formLogin().permitAll() //Imp: 新增login form， 允许用户登录及授权         
        .and()
        .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/**","/login/**","/logout/**","/actuator/health").permitAll()
        .antMatchers("/actuator/**").permitAll() //EnableHystrixDashboard
        .anyRequest().authenticated();
    	 
    }
    
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/css/**", "/js/**", "/plugins/**", "/favicon.ico");
//    }

} 

