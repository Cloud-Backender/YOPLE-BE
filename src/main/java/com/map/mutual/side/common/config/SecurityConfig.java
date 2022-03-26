package com.map.mutual.side.common.config;

import com.map.mutual.side.common.JwtTokenProvider;
import com.map.mutual.side.common.filter.AuthorizationCheckFilter;
import com.map.mutual.side.common.exception.handler.AuthenticationExceptionHandler;
import com.map.mutual.side.common.exception.handler.AuthorizationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Class       : SecurityConfig
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-03-11] - 조 준희 - Class Create
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 3. provider
    private final JwtTokenProvider jwtTokenProvider;
    // 4. 401,403 Handler
    private final AuthenticationExceptionHandler authenticationExceptionHandler;
    private final AuthorizationExceptionHandler authorizationExceptionHandler;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public SecurityConfig( JwtTokenProvider jwtTokenProvider
            , AuthenticationExceptionHandler authenticationExceptionHandler
            , AuthorizationExceptionHandler authorizationExceptionHandler
            , AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationExceptionHandler = authenticationExceptionHandler;
        this.authorizationExceptionHandler = authorizationExceptionHandler;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    /**
     * 전반적인 Spring Security의 설정
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()
                .formLogin() .disable()
                //  예외 처리 지정
                .exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionHandler)       //401 Error Handler
                .accessDeniedHandler(authorizationExceptionHandler)                //403 Error Handler

                // enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()       // 동일 도메인에서는 iframe 접근 가능

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().configurationSource(corsConfigurationSource())
                /**
                 * URI별 인가 정보 셋팅.
                 */
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.POST,"/user/signUp/**").permitAll()  // 회원 가입
                .antMatchers(HttpMethod.POST,"/user/**").authenticated()
                .antMatchers(HttpMethod.POST,"/world/**").authenticated()
                .antMatchers(HttpMethod.GET,"/world/**").authenticated()
                .antMatchers(HttpMethod.POST,"/review/**").authenticated()
                .antMatchers(HttpMethod.GET,"/review/**").authenticated()
                .antMatchers(HttpMethod.POST,"/auth/**").permitAll()
                .antMatchers(HttpMethod.POST,"/auth/refresh-refresh/**").authenticated()
                .anyRequest().authenticated()       // 그 외 나머지 리소스들은 무조건 인증을 완료해야 접근 가능
                .and()
                //AuthenticationFilterChain- UsernamePasswordAuthenticationFilter 전에 실행될 커스텀 필터 등록
                .addFilterBefore(new AuthorizationCheckFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        //.apply(new JwtSecurityConfig(jwtTokenProvider));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // - (3)
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
