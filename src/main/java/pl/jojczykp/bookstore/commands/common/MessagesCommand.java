package pl.jojczykp.bookstore.commands.common;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class MessagesCommand {

	private List<String> infos;
	private List<String> warns;
	private List<String> errors;

	public MessagesCommand() {
		infos = new ArrayList<>();
		warns = new ArrayList<>();
		errors = new ArrayList<>();
	}

	public void addInfos(String... messages) {
		infos.addAll(asList(messages));
	}

	public List<String> getInfos() {
		return infos;
	}

	public void addWarns(String... messages) {
		warns.addAll(asList(messages));
	}

	public List<String> getWarns() {
		return warns;
	}

	public void addErrors(String... messages) {
		errors.addAll(asList(messages));
	}

	public List<String> getErrors() {
		return errors;
	}

}
