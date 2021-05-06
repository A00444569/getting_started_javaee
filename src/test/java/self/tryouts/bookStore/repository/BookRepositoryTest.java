package self.tryouts.bookStore.repository;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import self.tryouts.bookStore.model.Book;
import self.tryouts.bookStore.model.Language;

import javax.inject.Inject;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BookRepositoryTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }

    @Inject
    private BookRepository bookRepository;

    @Test
    public void create() {
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0,bookRepository.findAll().size());

        Book book = new Book(
                "Love will find a way",
                "Are you willing to face the past?\nMadhav is an aspiring writer who gets stuck in a dead-end " +
                        "corporate job that gives him no joy and no time to write his book. But there's more to him than " +
                        "meets the eye. He has been hiding a secret all his life---which, if revealed, may shatter " +
                        "his very existence.",
                199F,
                "9780143439936",
                new Date(),
                214,
                "https://shuttershock.null/love_will_find_a_way.png",
                Language.ENGLISH
        );

        book = bookRepository.create(book);
        Long bookId = book.getId();

        assertNotNull(bookId);

        Book bookFound = bookRepository.find(bookId);

        assertEquals("Love will find a way", bookFound.getTitle());

        assertEquals(Long.valueOf(1), bookRepository.countAll());
        assertEquals(1,bookRepository.findAll().size());

        bookRepository.delete(bookId);

        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0,bookRepository.findAll().size());
    }
}
