package pl.jojczykp.bookstore.testutils.builders;

import pl.jojczykp.bookstore.domain.Authority;
import pl.jojczykp.bookstore.domain.User;

import java.util.Set;

public class UserBuilder {

	private int id;
	private String name;
	private String password;
	private boolean notExpired;
	private boolean notLocked;
	private boolean credentialsNotExpired;
	private boolean enabled;
	private Set<Authority> authorities;

	public static UserBuilder aUser() {
		return new UserBuilder();
	}

	public UserBuilder withId(int id) {
		this.id = id;
		return this;
	}

	public UserBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public UserBuilder withPassword(String password) {
		this.password = password;
		return this;
	}

	public UserBuilder withNotExpired(boolean notExpired) {
		this.notExpired = notExpired;
		return this;
	}

	public UserBuilder withNotLocked(boolean notLocked) {
		this.notLocked = notLocked;
		return this;
	}

	public UserBuilder withCredentialsNotExpired(boolean credentialsNotExpired) {
		this.credentialsNotExpired = credentialsNotExpired;
		return this;
	}

	public UserBuilder withEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public UserBuilder withAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
		return this;
	}

	public User build() {
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setPassword(password);
		user.setNotExpired(notExpired);
		user.setNotLocked(notLocked);
		user.setCredentialsNotExpired(credentialsNotExpired);
		user.setEnabled(enabled);
		user.setAuthorities(authorities);

		return user;
	}
}
