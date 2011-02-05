/*******************************************************************************
 * Copyright (c) 2011 Earth System Grid Federation
 * ALL RIGHTS RESERVED. 
 * U.S. Government sponsorship acknowledged.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package org.esgf.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Filter that stores the openid in a client-side cookie
 * if the flag "remember_openid" is set in the request.
 */
@Component("openidCookieFilter")
public class OpenidCookieFilter implements Filter {
	
	private FilterConfig filterConfig;
	
	public final static String PARAMETER_OPENID = "openid_identifier";
	public final static String PARAMETER_REMEMBERME = "remember_openid";
	public final static String OPENID_COOKIE_NAME = "esgf.idp.cookie";
	public final static int OPENID_COOKIE_LIFETIME = 86400*365*10; // ten years

			
	private final Log LOG = LogFactory.getLog(this.getClass());

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		
		final HttpServletRequest req = (HttpServletRequest)request;
		final HttpServletResponse resp = (HttpServletResponse)response;
		final String openid = req.getParameter(PARAMETER_OPENID);
		final String rememberme = req.getParameter(PARAMETER_REMEMBERME);		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Parameter name="+PARAMETER_OPENID+" value="+openid);
			LOG.debug("Parameter name="+PARAMETER_REMEMBERME+" value="+rememberme);
		}
		
		// remember openid identity
		if (StringUtils.hasText(openid)) {
			final Cookie cookie = new Cookie(OPENID_COOKIE_NAME, openid);
			if (StringUtils.hasText(rememberme) && rememberme.equals("on")) {
				cookie.setMaxAge(OPENID_COOKIE_LIFETIME);
				if (LOG.isDebugEnabled()) LOG.debug("Set cookie name="+cookie.getName()+" value="+cookie.getValue());
			} else {
				cookie.setMaxAge(0);
				if (LOG.isDebugEnabled()) LOG.debug("Removing cookie name="+cookie.getName()+" value="+cookie.getValue());
			}
			resp.addCookie(cookie);
		}
		
   		// keep processing
   		chain.doFilter(request, response);

	}

	public void init(FilterConfig filterConfig) throws ServletException { 
		this.filterConfig = filterConfig; 
	}
	   
	public void destroy() { this.filterConfig = null; }

}
