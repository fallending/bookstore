package pl.jojczykp.bookstore.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.jojczykp.bookstore.commands.books.DisplayBooksCommand;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/books-command-factory-test-context.xml")
public class DisplayBooksCommandFactoryUnitTest {

	@Value("${view.books.defaultPageNumber}") private int defaultPageNumber;
	@Value("${view.books.defaultPageSize}") private int defaultPageSize;
	@Value("${view.books.defaultSortColumn}") private PageSorterColumn defaultSortColumn;
	@Value("${view.books.defaultSortDirection}") private PageSorterDirection defaultSortDirection;

	@Autowired private BooksCommandFactory testee;

	@Test
	public void shouldCreateDefaultCommandWithDefaultPageNumber() {
		DisplayBooksCommand command = testee.create();

		assertThat(command.getPager().getPageNumber(), is(defaultPageNumber));
	}

	@Test
	public void shouldCreateDefaultCommandWithDefaultPageSize() {
		DisplayBooksCommand command = testee.create();

		assertThat(command.getPager().getPageSize(), is(defaultPageSize));
	}

	@Test
	public void shouldCreateDefaultCommandWithDefaultSortColumn() {
		DisplayBooksCommand command = testee.create();

		assertThat(command.getPager().getSorter().getColumn(), is(defaultSortColumn));
	}

	@Test
	public void shouldCreateDefaultCommandWithDefaultSortDirection() {
		DisplayBooksCommand command = testee.create();

		assertThat(command.getPager().getSorter().getDirection(), is(defaultSortDirection));
	}

}
