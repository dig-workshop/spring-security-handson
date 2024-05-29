package com.security.server.auth.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse

class SpyFilterChain: FilterChain {
    var doFilter_argument_request: ServletRequest? = null
    var doFilter_argument_response: ServletResponse? = null
    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?) {
        doFilter_argument_request = p0
        doFilter_argument_response = p1
    }
}

class DummyFilterChain: FilterChain {
    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?) {}
}