package sprihgfreamwork.mvcthymeleaf.com.app.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.AuthorService;
import sprihgfreamwork.mvcthymeleaf.domain.dao.AuthorRepository;
import sprihgfreamwork.mvcthymeleaf.domain.model.Author;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sprihgfreamwork.mvcthymeleaf.domain.model.Country.RUSSIA;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceImplTest {

    private AuthorService authorService;
    private Author author = new Author("id", "name", "surname", RUSSIA);

    @Mock
    private AuthorRepository authorRepository;

    @Before
    public void setUp() throws Exception {
        authorService = new AuthorServiceImpl(authorRepository);
    }

    @Test
    public void authorServiceShouldReturnAllAuthors() {
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(author));

        List<Author> authors = authorService.getAllAuthors();

        assertThat(authors)
                .as("Checking searching all authors")
                .isNotEmpty()
                .contains(author);
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void authorServiceShouldReturnCountOfAuthors() {
        when(authorRepository.count()).thenReturn(1L);

        long count = authorService.countAuthors();

        assertThat(count)
                .as("Checking counting authors")
                .isEqualTo(1L);
        verify(authorRepository, times(1)).count();
    }

    @Test
    public void authorServiceShouldCreateAuthorWithRepo() {
        when(authorRepository.findByNameAndSurname(anyString(), anyString())).thenReturn(Optional.empty());

        try {
            authorService.createAuthor(author);
        } catch (EntityExistsException e) {
            fail("author service should not throw ex cause author already in db");
        }

        verify(authorRepository, times(1)).findByNameAndSurname(eq(author.getName()), eq(author.getSurname()));
    }

    @Test(expected = EntityExistsException.class)
    public void authorServiceShouldThrowExCauseAuthorAlreadyInDb() throws EntityExistsException {
        when(authorRepository.findByNameAndSurname(anyString(), anyString())).thenReturn(Optional.of(author));

        authorService.createAuthor(author);
    }

    @Test
    public void authorServiceShouldReturnAuthorById() {
        when(authorRepository.findById(eq(author.getId()))).thenReturn(Optional.of(author));

        try {
            Author author = authorService.getAuthorById(this.author.getId());

            assertThat(author)
                    .as("Check searching author by id")
                    .isEqualTo(this.author);
        } catch (NotFoundException e) {
            fail("author service should not throw ex cause author not in db");
        }

        verify(authorRepository, times(1)).findById(eq(author.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void authorServiceShouldThrowExCauseNoSuchAuthorInDb() throws NotFoundException {
        when(authorRepository.findById(anyString())).thenReturn(Optional.empty());

        authorService.getAuthorById(author.getId());
    }

    @Test
    public void authorServiceShouldUpdateAuthor() {
        when(authorRepository.findById(eq(author.getId()))).thenReturn(Optional.of(author));

        try {
            authorService.updateAuthor(author);
        } catch (NotFoundException e) {
            fail("author service should not throw ex cause author in db");
        }

        verify(authorRepository, times(1)).findById(eq(author.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void authorServiceShouldThrowExCauseNoAuthorToUpdate() throws NotFoundException {
        when(authorRepository.findById(eq(author.getId()))).thenReturn(Optional.empty());

        authorService.updateAuthor(author);
    }

    @Test
    public void authorServiceShouldDeleteAuthorById() {
        authorService.deleteAuthor(author.getId());

        verify(authorRepository, times(1)).deleteById(eq(author.getId()));
    }
}