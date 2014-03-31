package pl.jojczykp.bookstore.commands;

public class HttpErrorCommand {

	private int id;
	private String originalUrl;
	private String description;

		public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
