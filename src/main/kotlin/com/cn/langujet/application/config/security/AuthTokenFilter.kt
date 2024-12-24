package com.cn.langujet.application.config.security

import com.cn.langujet.application.advice.ErrorMessageResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class AuthTokenFilter(
    private val modelMapper: ObjectMapper, private val langujetProxyClientSecret: String
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        if (request.getHeader(HttpHeaders.AUTHORIZATION) != langujetProxyClientSecret && request.requestURI.startsWith("/api")) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.print(
                modelMapper.writeValueAsString(
                    ErrorMessageResponse("Access Denied")
                )
            )
        } else {
            filterChain.doFilter(request, response)
        }
    }
}