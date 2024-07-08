package com.sample.bookstore1.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final long THROTTLE_TIME = 60000; // Time period for rate limiting in milliseconds (60 seconds)
    private static final int MAX_REQUESTS = 60; // Maximum number of requests allowed within the throttle time
    // Map to track request counts per client IP
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    // Map to track the last request time per client IP
    private final Map<String, Long> lastRequestTime = new ConcurrentHashMap<>();

    // Pre-handle method to check and enforce rate limiting
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String clientIp = request.getRemoteAddr(); // Get client IP address
        long currentTime = System.currentTimeMillis(); // Get current system time

        // Reset count and time if the throttle time has passed
        if (!lastRequestTime.containsKey(clientIp) || (currentTime - lastRequestTime.get(clientIp) > THROTTLE_TIME)) {
            lastRequestTime.put(clientIp, currentTime); // Update last request time for the client IP
            requestCounts.put(clientIp, new AtomicInteger(0)); // Reset request count for the client IP
        }

        // If the client has exceeded the maximum number of requests, return an error
        // response
        if (requestCounts.get(clientIp).incrementAndGet() > MAX_REQUESTS) {
            response.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT); // Set response status to 504 Gateway Timeout
            response.getWriter().write("Too many requests. Please try again later.");
            return false; // Reject the request
        }

        return true; // Allow the request to proceed
    }
}