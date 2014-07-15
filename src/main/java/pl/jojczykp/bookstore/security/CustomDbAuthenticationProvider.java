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

package pl.jojczykp.bookstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.jojczykp.bookstore.entities.Authority;
import pl.jojczykp.bookstore.entities.User;
import pl.jojczykp.bookstore.repositories.AuthRepository;

import java.util.HashSet;
import java.util.Set;

public class CustomDbAuthenticationProvider implements UserDetailsService {

	@Autowired private AuthRepository authRepository;

	@Override
	public UserDetails loadUserByUsername(String name) {
		User user = authRepository.findByName(name);

		if (user == null) {
			throw new UsernameNotFoundException("User '" + name + "' not found.");
		}

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (Authority authority: user.getAuthorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getRole()));
		}

		return new org.springframework.security.core.userdetails.User(
				user.getName(), user.getPassword(), user.isEnabled(),
				user.isNotExpired(), user.isCredentialsNotExpired(), user.isNotLocked(),
				grantedAuthorities);
	}

}
