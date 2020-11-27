package com.haibusiness.szweb.security;


import com.haibusiness.szweb.security.handler.ForbiddenHandler;
import com.haibusiness.szweb.security.handler.UnauthorizedHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Jinyu
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final UnauthorizedHandler unauthorizedHandler;
    private final ForbiddenHandler forbiddenHandler;
	private final AuthenticationFilter authenticationFilter;
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			//重写父类提供的跨域请求处理的接口
			public void addCorsMappings(CorsRegistry registry) {
				//添加映射路径
				registry.addMapping("/**")
						//放行哪些原始域
						.allowedOrigins("*")
						//是否发送Cookie信息
						.allowCredentials(true)
						//放行哪些原始域(请求方式)
						.allowedMethods("GET","POST", "PUT", "DELETE")
						//放行哪些原始域(头部信息)
						.allowedHeaders("*")
						//暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
						.exposedHeaders("Header1", "Header2");
			}
		};
	}
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				// 授权异常
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.exceptionHandling().accessDeniedHandler(forbiddenHandler).and()
				// 不创建会话
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers("/auth/**").permitAll()
				.antMatchers("/home/**").permitAll()
				.antMatchers("/public/**").permitAll()
				.antMatchers("/upload/**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").anonymous()
				// 所有请求都需要认证
				.anyRequest().authenticated();
		httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.headers().cacheControl();
	}
}
