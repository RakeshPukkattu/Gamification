package com.zisbv.gamification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.zisbv.gamification.security.JwtAuthenticationEntryPoint;
import com.zisbv.gamification.security.JwtAuthenticationFilter;
import com.zisbv.gamification.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsServiceImpl userDetailsService;

  private final JwtAuthenticationEntryPoint jwtEntryPoint;

  @Bean
  public JwtAuthenticationFilter authenticationJwtTokenFilter() {
    return new JwtAuthenticationFilter();
  }

  @Autowired
  public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
      JwtAuthenticationEntryPoint jwtEntryPoint) {
    this.userDetailsService = userDetailsService;
    this.jwtEntryPoint = jwtEntryPoint;
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
        "/configuration/**", "/swagger-ui.html", "/webjars/**","/index.html");
  }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(jwtEntryPoint).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/index.html","/js/main.js","/css/main.css","/18.jpg","/favicon.ico",
						"/chat.register","/chat.send","/topic/public",
						"/topic","/app","/ws","/ws/*",
						"/userManagement/download/errorLog.xlsx", "/gamification/auth/signin",
						"/userManagement/getCredentials/{email}", "/userManagement/downloadFile/{fileName:.+}",
						"/adminGamesManagement/downloadFile/{fileName:.+}",
						"/Games/downloadFile/{fileName:.+}",
						"/Themes/downloadFile/{fileName:.+}",
						"/adminUIThemeManagement/downloadFile/{fileName:.+}", "/destroy", "/persistMessage", "/",
						"/userManagement/uploadAvathar", "/userManagement/avathars", "/userManagement/domains",
						"/userManagement/authenticate", "/userManagement/learnerRegistration", "/userManagement/verify",
						"/verify_success.html", "/verify_fail.html", "/userManagement/user_registration", "/home",
						"/user_registration", "/userManagement/superAdminRegistration", "/forgot_password", "/reset_password",
						"/forgot_password_form.html", "/reset_password_form.html")
				.permitAll().antMatchers("/**/gamification/auth/**").permitAll().anyRequest().authenticated();

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}


//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }
  
}
