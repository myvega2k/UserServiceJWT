package com.example.userservice.config;

import com.example.userservice.filter.MyAuthenticationFilter;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final Environment env;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/actuator/**").permitAll();
		//테스트 후에 permitAll() 주석처리 필요
		http.authorizeRequests().antMatchers("/users/**").permitAll();
		http.authorizeRequests().antMatchers("/**")
				//.hasIpAddress("192.168.0.100") // <- 각자의 IP로 변경필요
				.hasIpAddress(env.getProperty("gateway.ip"))
				.and()
				.addFilter(getAuthenticationFilter());
		http.headers().frameOptions().disable();
	}


	private MyAuthenticationFilter getAuthenticationFilter() throws Exception {
		MyAuthenticationFilter authenticationFilter =
				new MyAuthenticationFilter(userService, env, authenticationManager());
//		authenticationFilter.setAuthenticationManager(authenticationManager());
		return authenticationFilter;
	}


}
