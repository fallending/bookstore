/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package pl.jojczykp.bookstore.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXPrincipal;
import javax.security.auth.Subject;

public class JmxSecurityAuthenticator implements JMXAuthenticator {

	private static final String ROLE_GRANTING_ACCESS = "ROLE_JMX";

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Override
	public Subject authenticate(Object credentials) {
		try {
			return authenticateOrException(credentials);
		} catch (Exception e) {
			throw new SecurityException(e);
		}
	}

	private Subject authenticateOrException(Object credentials) {
		Authentication authentication = authenticationManager.authenticate(getAuthentication(credentials));
		verifyJmxAuthority(authentication);

		return createSubjectFor(authentication);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(Object credentials) {
		String[] userPassword = (String[]) credentials;
		String user = userPassword[0];
		String password = userPassword[1];

		return new UsernamePasswordAuthenticationToken(user, password);
	}

	private void verifyJmxAuthority(Authentication authentication) {
		for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			if (ROLE_GRANTING_ACCESS.equals(grantedAuthority.getAuthority())) {
				return;
			}
		}
		throw new BadCredentialsException("Bad credentials or user has no JMX Authority");
	}

	private Subject createSubjectFor(Authentication authentication) {
		Subject subject = new Subject();

		for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			subject.getPrincipals().add(new JMXPrincipal(grantedAuthority.getAuthority()));
		}

		return subject;
	}

}
