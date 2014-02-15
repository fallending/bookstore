package pl.jojczykp.bookstore.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.jojczykp.bookstore.command.BooksCommand;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
		"classpath:spring/config-test-context.xml",
		"classpath:spring/beans-context.xml"
})
public class BooksCommandFactoryTest {

	@Value("${view.books.defaultOffset}") private int defaultOffset;
	@Value("${view.books.defaultSize}") private int defaultSize;
	@Value("${view.books.defaultSortColumn}") private PageSorterColumn defaultSortColumn;
	@Value("${view.books.defaultSortDirection}") private PageSorterDirection defaultSortDirection;

	@Autowired private BooksCommandFactory testee;

	@Test
	public void shouldCreateDefaultCommandWithDefaultOffset() {
		BooksCommand command = testee.create();

		assertThat(command.getPager().getCurrent().getOffset(), is(defaultOffset));
	}

	@Test
	public void shouldCreateDefaultCommandWithDefaultSize() {
		BooksCommand command = testee.create();

		assertThat(command.getPager().getCurrent().getSize(), is(defaultSize));
	}

	@Test
	public void shouldCreateDefaultCommandWithSefaultSortColumn() {
		BooksCommand command = testee.create();

		assertThat(command.getPager().getSorter().getColumn(), is(defaultSortColumn));
	}

	@Test
	public void shouldCreateDefaultCommandWithDefaultSortDirection() {
		BooksCommand command = testee.create();

		assertThat(command.getPager().getSorter().getDirection(), is(defaultSortDirection));
	}

}
