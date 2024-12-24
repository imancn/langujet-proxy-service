package com.cn.langujet.application.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    private val modelMapper: ObjectMapper,
    @Value("\${langujet.proxy.client.secret}")
    private val langujetProxyClientSecret: String,
    private val unauthorizedHandler: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http.cors().and().csrf().disable()
            .authorizeHttpRequests().anyRequest().permitAll().and()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
            .addFilterBefore(AuthTokenFilter(modelMapper, langujetProxyClientSecret), UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement().disable()
            .build()
    }
    
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().also { cors ->
        CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("POST", "GET")
            allowedHeaders = listOf(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
            )
            exposedHeaders = listOf(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition"
            )
            maxAge = 3600
            cors.registerCorsConfiguration("/**", this)
        }
    }
    
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService {
            throw UsernameNotFoundException("User not found")
        }
    }
}