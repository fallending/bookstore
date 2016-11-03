package pl.jojczykp.bookstore.commands.books;

public class DisplayBookCommand {

	private int id;
	private int version;
	private String title;
	private String iconName;

	public DisplayBookCommand() {
		id = 0;
		version = 0;
		title = "";
		iconName = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
}
