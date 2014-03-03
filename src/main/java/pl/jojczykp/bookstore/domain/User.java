package pl.jojczykp.bookstore.domain;


import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import java.util.EnumSet;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
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

	@ElementCollection(fetch = EAGER)
	@JoinTable(name = "PERMISSIONS", joinColumns = @JoinColumn(name = "USER_ID"))
	@Column(name = "PERMISSION")
	@Enumerated(STRING)
	private Set<Permission> permissions;

	public User() {
		this.id = 0;
		this.name = "";
		this.password = "";
		this.notExpired = false;
		this.notLocked = false;
		this.credentialsNotExpired = false;
		this.enabled = false;
		this.permissions = EnumSet.noneOf(Permission.class);
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

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

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
				+ ", permissions=" + permissions + "}";
	}

}
