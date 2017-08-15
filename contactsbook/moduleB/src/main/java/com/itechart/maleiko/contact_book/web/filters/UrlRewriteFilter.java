package com.itechart.maleiko.contact_book.web.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName = "UrlRewriteFilter", value = "/*")
public class UrlRewriteFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestURI = request.getRequestURI();
        //if request URI includes either only context path or context path with /
        //or context path with front controller mapping (with or without /)
        if (requestURI != null && requestURI.matches(request.getContextPath() + "(/?|(/app/?))")) {
            response.sendRedirect(request.getContextPath() + "/app/contacts");
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }
}