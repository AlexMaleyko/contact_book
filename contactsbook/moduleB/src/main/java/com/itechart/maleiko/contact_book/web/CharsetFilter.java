package com.itechart.maleiko.contact_book.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Alexey on 04.04.2017.
 */
@WebFilter(filterName = "com.itechart.maleiko.contact_book.web.CharsetFilter")
public class CharsetFilter implements Filter {

    private static final org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(CharsetFilter.class);

    private String encoding;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

            LOGGER.info("request character encoding: {}", req.getCharacterEncoding());

        if (null == req.getCharacterEncoding()) {
            req.setCharacterEncoding(encoding);

            LOGGER.info("character encoding is set to {}", encoding);
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null) encoding = "UTF-8";
    }

}
