package sprihgfreamwork.mvcthymeleaf.com.app.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.AuthorService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.BookService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.CommentService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.GenreService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookService bookService;
    @MockBean
    private CommentService commentService;

    @Test
    public void libraryControllerShouldReturnViewAndFilledModel() throws Exception {
        when(genreService.countGenres()).thenReturn(1L);
        when(authorService.countAuthors()).thenReturn(1L);
        when(bookService.countBooks()).thenReturn(1L);
        when(commentService.countComments()).thenReturn(1L);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", 1L))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", 1L))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("comments", 1L))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", 1L));

        verify(genreService, times(1)).countGenres();
        verify(bookService, times(1)).countBooks();
        verify(authorService, times(1)).countAuthors();
        verify(commentService, times(1)).countComments();
    }
}