package pl.jojczykp.bookstore.command;

import java.util.ArrayList;
import java.util.List;

public class MessagesCommand {

	private List<String> infos;
	private List<String> warns;
	private List<String> errors;

	public MessagesCommand() {
		infos = new ArrayList<>();
		warns = new ArrayList<>();
		errors = new ArrayList<>();
	}

	public void addInfo(String message) {
		infos.add(message);
	}

	public List<String> getInfos() {
		return infos;
	}

	public void addWarn(String message) {
		warns.add(message);
	}

	public List<String> getWarns() {
		return warns;
	}

	public void addError(String message) {
		errors.add(message);
	}

	public List<String> getErrors() {
		return errors;
	}

}
