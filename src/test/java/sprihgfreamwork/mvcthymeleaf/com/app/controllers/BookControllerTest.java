package sprihgfreamwork.mvcthymeleaf.com.app.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.AuthorService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.BookService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.GenreService;
import sprihgfreamwork.mvcthymeleaf.domain.model.Author;
import sprihgfreamwork.mvcthymeleaf.domain.model.Book;
import sprihgfreamwork.mvcthymeleaf.domain.model.Genre;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sprihgfreamwork.mvcthymeleaf.domain.model.Country.RUSSIA;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    private Genre genre = new Genre("genre_id", "genre_name");
    private Author author = new Author("author_id", "author_name", "author_surname", RUSSIA);
    private Book book = new Book("book_id", "book_name", new Date(), 1, Collections.emptyMap(), Collections.singleton(author), genre);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookService bookService;

    @Test
    public void bookControllerShouldCallViewWithAllGenresAndFillModel() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("allBooks"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasItems(Collections.singletonList(book).toArray())));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    public void bookControllerShouldReturnViewWithNameCreateGenre() throws Exception {
        List<Author> authors = Collections.singletonList(author);
        when(authorService.getAllAuthors()).thenReturn(authors);
        List<Genre> genres = Collections.singletonList(genre);
        when(genreService.getAllGenres()).thenReturn(genres);

        mockMvc.perform(get("/createBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("createBook"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", hasItems(authors.toArray())))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", hasItems(genres.toArray())));

        verify(authorService, times(1)).getAllAuthors();
        verify(genreService, times(1)).getAllGenres();
    }

    @Test
    public void bookControllerShouldCreateGenreOnPostMethodAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(post("/createBook").flashAttr("book", book))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).createBook(eq(book));
    }

    @Test
    public void bookControllerShouldReturnErrorPageCauseGenreAlreadyInDb() throws Exception {
        EntityExistsException ex = new EntityExistsException("testException");
        doThrow(ex).when(bookService).createBook(eq(book));

        mockMvc.perform(post("/createBook").flashAttr("book", book))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception", ex));
    }

    @Test
    public void bookControllerShouldReturnEditBookPageWithDataInModel() throws Exception {
        when(bookService.getBook(eq(book.getId()))).thenReturn(book);
        List<Author> authors = Collections.singletonList(author);
        when(authorService.getAllAuthors()).thenReturn(authors);
        List<Genre> genres = Collections.singletonList(genre);
        when(genreService.getAllGenres()).thenReturn(genres);

        mockMvc.perform(get("/editBook").param("id", book.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", hasItems(authors.toArray())))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", hasItems(genres.toArray())));
    }

    @Test
    public void bookControllerShouldUpdateGenreInDbAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(post("/editBook")
                .flashAttr("book", book)
                ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).updateBook(book);
    }

    @Test
    public void bookControllerShouldReturnErrorPageCauseNoGenreToUpdate() throws Exception {
        NotFoundException ex = new NotFoundException("testException");
        doThrow(ex).when(bookService).updateBook(eq(book));

        mockMvc.perform(post("/editBook")
                .flashAttr("book", book))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception", ex));

    }

    @Test
    public void bookControllerShouldDeleteGenreAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(delete("/deleteBook")
                .param("id", book.getId()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
    }

}