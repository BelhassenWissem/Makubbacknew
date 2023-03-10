package com.projet.maktub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
      final CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOriginPatterns(ImmutableList.of("*"));
      configuration.setAllowedMethods(ImmutableList.of("HEAD",
              "GET", "POST", "PUT", "DELETE", "PATCH"));
      // setAllowCredentials(true) is important, otherwise:
      // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
      configuration.setAllowCredentials(true);
      // setAllowedHeaders is important! Without it, OPTIONS preflight request
      // will fail with 403 Invalid CORS request
      configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
      final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }
  
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // Disable CSRF (cross site request forgery)
      http.csrf().disable();
	  http.cors();
    // No session will be created or used by spring security
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // Entry points
    http.authorizeRequests()//
        .antMatchers("/persons/signin").permitAll()//
        .antMatchers("/persons/signup").permitAll()
        .antMatchers("/persons/getUser").permitAll()
        .antMatchers("/persons/checkMail").permitAll()//
        .antMatchers("/persons/addFriendToUser").permitAll()//
        .antMatchers("/products/create").permitAll()//
        .antMatchers("/products").permitAll()//
        .antMatchers("/productsdone/addProductDoneToUser").permitAll()//
        .antMatchers("/productsdone/deleteDoneProductFromUser").permitAll()//
        .antMatchers("/links").permitAll()//
        .antMatchers("/links/{email}").permitAll()//
        .antMatchers("/addproduct").permitAll()//
        .antMatchers("/persons").permitAll()//
        .antMatchers("/products/create").permitAll()//
        .antMatchers("/persons/addlink").permitAll()//
        .antMatchers("/persons/{idperson}").permitAll()//
        .antMatchers("/persons/following").permitAll()//
        .antMatchers("/persons/deleteFriendFromUser").permitAll()//
        .antMatchers("/persons/friends").permitAll()//
        .antMatchers("/persons/addFollowing").permitAll()//
        .antMatchers("/getData").permitAll()//
        


        
        


        .antMatchers("/productsdone").permitAll()//        

        .antMatchers("/products/{idpro}").permitAll()//
        
        

        
        
        //.antMatchers("/**").permitAll()//

        //.antMatchers("/livres").permitAll()//

       //  Disallow everything else..
        .anyRequest().authenticated();

    // If a user try to access a resource without having enough permissions
    //http.exceptionHandling().accessDeniedPage("/login");

    // Apply JWT
    http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

    // Optional, if you want to test the API from a browser
    // http.httpBasic();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
  
    web.ignoring().antMatchers("/v2/api-docs")//
        .antMatchers("/swagger-resources/**")//
        .antMatchers("/swagger-ui.html")//
        .antMatchers("/configuration/**")//
        .antMatchers("/webjars/**")//
        .antMatchers("/public");;
        
	  web.ignoring().antMatchers("/files/**");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
  
  @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
  }
  


}
