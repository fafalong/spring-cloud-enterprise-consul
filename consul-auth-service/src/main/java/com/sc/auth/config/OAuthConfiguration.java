package com.sc.auth.config;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
//     org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.sc.auth.service.AuthUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager auth;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
    private AuthUserDetailsService userDetailsService;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Bean
	public JdbcTokenStore tokenStore() {
		// return new RedisTokenStore(connectionFactory);
		return new JdbcTokenStore(dataSource);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(auth)
		.tokenStore(tokenStore())
		//.userDetailsService(userDetailsService)
		.reuseRefreshTokens(true);  //开启刷新token;
		
		
		// 配置TokenServices参数
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds( (int) TimeUnit.DAYS.toSeconds(30)); // 30天
        endpoints.tokenServices(tokenServices); 
		
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		clients.withClientDetails(new JdbcClientDetailsService(dataSource));
		//data init script - oauth_client_details
		/**
		clients.jdbc(dataSource).passwordEncoder(passwordEncoder)
			.withClient("client").secret("secret")
				.authorizedGrantTypes("password", "refresh_token").scopes("read", "write")
				.accessTokenValiditySeconds(3600) // 1 hour
				.refreshTokenValiditySeconds(2592000) // 30 days
				.and()
				.withClient("service-a").secret("password")
				.authorizedGrantTypes("client_credentials", "refresh_token").scopes("server")
				.and()
				.withClient("service-b").secret("password")
				.authorizedGrantTypes("client_credentials", "refresh_token").scopes("server")
				.and()
				.withClient("client_code") // client_id
                .secret("secret_code") // client_secret
                .authorizedGrantTypes("authorization_code") // 该client允许的授权类型
                .scopes("app"); // 允许的授权范围 ;
		*/
	}

	@Configuration
	@Order(-20)
	protected static class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		private DataSource dataSource;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			// auth.jdbcAuthentication().dataSource(dataSource)
			// .withUser("dave").password("secret").roles("USER")
			// .and()
			// .withUser("anil").password("password").roles("ADMIN")
			// ;
		}
	}

	public static void main(String[] args) {
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		System.out.println(pe.encode("password"));
		// $2a$10$MKDtO00YG7tLVTl3vYoxJOQTRSLu3DZkW9VM6g8JY6c4IIUZHNWd6
		System.out.println(pe.matches("password", "$2a$10$/EbtUW5.qD1UpjZI4DHc/eg/RjJy/9zb0Tl6c/6r0a.slDtZ6R5iW"));
		
		/**
		 * 
		 [root@hadoopnode4 ~]# curl -X POST -vu client:secret http://192.168.1.63:5000/oauth/token -H "Accept: application/json" -d "password=password&username=admin&grant_type=password&scope=read%20write"
			{"access_token":"fd69ea49-0b8a-457f-ae7b-73cdd42bff4d","token_type":"bearer","refresh_token":"66906acf-f701-4542-b8ca-33213c7958cf","expires_in":3560,"scope":"read write"}
		 
		 curl -X POST -vu client:secret http://192.168.1.63:5000/oauth/token?grant_type=refresh_token&refresh_token=66906acf-f701-4542-b8ca-33213c7958cf
		 
		 on browsers: auto redirect to login page:
		 http://192.168.1.63:5000/oauth/authorize?response_type=code&client_id=client_code&redirect_uri=http://www.baidu.com&scope=app
		 
		 
		 [root@hadoopnode4 ~]# curl -X POST -vu service-a:password http://192.168.1.63:5000/oauth/token -d "client_id=service-a&scopes=server&grant_type=client_credentials&client_secret=password"
             {"access_token":"563b6ecf-271a-42d1-a2f8-0e59cf9e7746","token_type":"bearer","expires_in":2591908,"scope":"server"}
             
             

		 
		 * 
		 */

	}

}