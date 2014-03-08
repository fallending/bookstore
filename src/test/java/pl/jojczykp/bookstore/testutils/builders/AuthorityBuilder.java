package pl.jojczykp.bookstore.testutils.builders;

import pl.jojczykp.bookstore.domain.Authority;

public abstract class AuthorityBuilder {

	public static Authority anAuthority(int id, String role) {
		Authority result = new Authority();
		result.setId(id);
		result.setRole(role);

		return result;
	}

}
