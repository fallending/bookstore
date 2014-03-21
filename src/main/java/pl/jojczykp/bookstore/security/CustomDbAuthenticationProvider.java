package pl.jojczykp.bookstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.jojczykp.bookstore.entities.Authority;
import pl.jojczykp.bookstore.entities.User;
import pl.jojczykp.bookstore.repositories.SecurityRepository;

import java.util.HashSet;
import java.util.Set;

public class CustomDbAuthenticationProvider implements UserDetailsService {

	@Autowired private SecurityRepository securityRepository;

	@Override
	public UserDetails loadUserByUsername(String name) {
		User user = securityRepository.findByName(name);

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
