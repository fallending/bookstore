package pl.jojczykp.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "NOT_EXPIRED", nullable = false)
	private boolean notExpired;

	@Column(name = "NOT_LOCKED", nullable = false)
	private boolean notLocked;

	@Column(name = "CREDENTIALS_NOT_EXPIRED", nullable = false)
	private boolean credentialsNotExpired;

	@Column(name = "ENABLED", nullable = false)
	private boolean enabled;

	@ManyToMany(fetch = EAGER, cascade = ALL)
	@JoinTable(name = "USERS_AUTHORITIES",
			joinColumns = @JoinColumn(name = "USER_ID"),
			inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID"))
	private Set<Authority> authorities;

	public User() {
		this.id = 0;
		this.name = "";
		this.password = "";
		this.notExpired = false;
		this.notLocked = false;
		this.credentialsNotExpired = false;
		this.enabled = false;
		this.authorities = new HashSet<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isNotExpired() {
		return notExpired;
	}

	public void setNotExpired(boolean notExpired) {
		this.notExpired = notExpired;
	}

	public boolean isNotLocked() {
		return notLocked;
	}

	public void setNotLocked(boolean notLocked) {
		this.notLocked = notLocked;
	}

	public boolean isCredentialsNotExpired() {
		return credentialsNotExpired;
	}

	public void setCredentialsNotExpired(boolean credentialsNotExpired) {
		this.credentialsNotExpired = credentialsNotExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User book = (User) o;

		return (id == book.id);
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public String toString() {
		return "User{id=" + id
				+ ", name='" + name
				+ "', password='" + password
				+ "', notExpired='" + notExpired
				+ "', notLocked='" + notLocked
				+ "', credentialsNotExpired='" + credentialsNotExpired
				+ "', enabled='" + enabled + "'"
				+ ", authorities=" + authorities + "}";
	}

}
