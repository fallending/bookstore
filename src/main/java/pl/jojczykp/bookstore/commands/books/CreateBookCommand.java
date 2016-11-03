package pl.jojczykp.bookstore.commands.books;

import org.springframework.web.multipart.MultipartFile;
import pl.jojczykp.bookstore.commands.common.PagerCommand;

public class CreateBookCommand {

	private PagerCommand pager;
	private String title;
	private MultipartFile file;

	public CreateBookCommand() {
		pager = new PagerCommand();
		title = "";
	}

	public PagerCommand getPager() {
		return pager;
	}

	public void setPager(PagerCommand pager) {
		this.pager = pager;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
