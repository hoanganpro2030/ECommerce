package vn.elca.training;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import vn.elca.training.constant.SecurityConstant;
import vn.elca.training.fiter.JwtAccessDeniedHandler;
import vn.elca.training.fiter.JwtAuthenticationEntryPoint;
import vn.elca.training.fiter.JwtAuthorizationFilter;
import vn.elca.training.service.ProjectService;
import vn.elca.training.util.JWTTokenProvider;
import vn.elca.training.web.ApplicationController;
import vn.elca.training.web.ProjectController;
import vn.elca.training.web.PurchaseOrderController;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author gtn
 */
@SpringBootApplication(scanBasePackages = "vn.elca.training")
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan(basePackageClasses = {ApplicationController.class, ProjectController.class, ProjectService.class,
        JwtAuthorizationFilter.class, JWTTokenProvider.class})
@PropertySource({"classpath:/application.properties", "classpath:/messages.properties"})
public class ApplicationWebConfig {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(ApplicationWebConfig.class);
//    }

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        // access to h2 console by
        // this link: http://localhost:8080/h2console
        // JDBC URL: jdbc:h2:mem:onboardingexercise
        // other fields left as default, this configuration will access on memory schema
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/h2console/*");
        return registrationBean;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Password encoder
        return new BCryptPasswordEncoder();
    }

    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        @Qualifier("UserDetailsService")
        UserDetailsService userDetailsService;

        @Autowired
        JwtAuthorizationFilter jwtAuthorizationFilter;

        @Autowired
        JwtAccessDeniedHandler jwtAccessDeniedHandler;

        @Autowired
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Autowired
        BCryptPasswordEncoder bCryptPasswordEncoder;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService) // Cung cáp userservice cho spring security
                    .passwordEncoder(bCryptPasswordEncoder); // cung cấp password encoder
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable().cors()
//                    .cors().configurationSource(configurationSource())
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().authorizeRequests().antMatchers(SecurityConstant.PULIC_URLS).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and()
                    .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception{
            return super.authenticationManagerBean();
        }

        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowCredentials(true);
//            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://127.0.0.1:4200"));
            corsConfiguration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:4200", "http://18.188.153.21"));
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                    "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                    "Access-Control-Request-Method", "Access-Control-Request-Headers"));
            corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type",
                    "Accept", "Jwt-Token", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Credentials"));
            urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
            return new CorsFilter(urlBasedCorsConfigurationSource);
        }

        private CorsConfigurationSource configurationSource() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("http://127.0.0.1:4200");
//        config.setAllowCredentials(true);
            config.addAllowedHeader("*");
//        config.addAllowedHeader("Content-Type");
//            config.addAllowedMethod(HttpMethod.GET);
            config.addAllowedMethod("*");
//            config.addAllowedMethod(HttpMethod.POST);
//            config.addAllowedMethod(HttpMethod.GET);
//            config.addAllowedMethod(HttpMethod.DELETE);
            source.registerCorsConfiguration("/**", config);
            return source;
        }
    }
}
