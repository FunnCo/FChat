package org.example.fchat.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*

@Component
class AuthorizationHeaderFilter() : OncePerRequestFilter() {

    private val secretKey = "9C74A653F12251258034023FF386CE931A2F60BA2247E85442259971951216D3"

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Пропуск, если запрос можно пройти без авторизации
        if(canBeSkipped(request)){
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Auth")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header")
            return
        }

        if(validateToken(authHeader.substringAfter("Bearer "))){
            filterChain.doFilter(request, response)
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header")
            return
        }

    }

    private val permittedURIKeys = listOf("auth", "v3", "swagger")

    private fun canBeSkipped(request: HttpServletRequest): Boolean {
        return permittedURIKeys.any { key -> request.requestURI.contains(key) }
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.encodeToByteArray())).build().parseClaimsJws(token)
            return !isTokenExpired(token)
        } catch (any: Exception){
            return false
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.encodeToByteArray())).build().parseClaimsJws(token).body
    }
}


/*
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzI5MTA4ODA3LCJleHAiOjE3MjkxOTUyMDd9.QpeyhfXpgiBQMmjjdceMCVH9wYBwH_k-Y0-RMCFIIAZdS1ktmuUjMfUTiAE0TrLI0f_HE_oBejg1pSGNVKSkog
 */