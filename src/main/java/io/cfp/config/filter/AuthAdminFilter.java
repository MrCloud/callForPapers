/*
 * Copyright (c) 2016 BreizhCamp
 * [http://breizhcamp.org]
 *
 * This file is part of CFP.io.
 *
 * CFP.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.cfp.config.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import io.cfp.entity.User;
import io.cfp.service.admin.user.AdminUserService;
import io.cfp.service.auth.AuthUtils;
import io.cfp.service.auth.AuthUtils.InvalidTokenException;

/**
 * Filter reading auth token and check if user is admin
 */
public class AuthAdminFilter extends AuthFilter {

	private AuthUtils authUtils;
    private AdminUserService adminUserService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        authUtils = webApplicationContext.getBean(AuthUtils.class);
        adminUserService = webApplicationContext.getBean(AdminUserService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
        	User user = authUtils.getAuthUser(httpRequest);
            User admin = adminUserService.findFromEmail(user.getEmail());
            if (admin == null) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Resource require Administrator role");
                return;
            };

            MDC.put(USER, user);
            adminUserService.setCurrentAdmin(admin);
            chain.doFilter(request, response);

        } catch (InvalidTokenException e) {
            httpResponse.sendError(e.getResponseCode(), e.getMessage());

        } finally {
            MDC.remove(USER);
        }
    }
}
