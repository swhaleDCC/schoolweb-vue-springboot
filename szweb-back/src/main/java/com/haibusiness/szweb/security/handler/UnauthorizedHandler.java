package com.haibusiness.szweb.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Jinyu
 */
@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}