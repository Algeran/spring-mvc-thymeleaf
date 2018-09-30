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
import sprihgfreamwork.mvcthymeleaf.domain.app.services.CommentService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.GenreService;
import sprihgfreamwork.mvcthymeleaf.domain.model.Author;
import sprihgfreamwork.mvcthymeleaf.domain.model.Book;
import sprihgfreamwork.mvcthymeleaf.domain.model.Comment;
import sprihgfreamwork.mvcthymeleaf.domain.model.Genre;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sprihgfreamwork.mvcthymeleaf.domain.model.Country.RUSSIA;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    private Genre genre = new Genre("genre_id", "genre_name");
    private Author author = new Author("author_id", "author_name", "author_surname", RUSSIA);
    private Book book = new Book("book_id", "book_name", new Date(), 1, Collections.emptyMap(), Collections.singleton(author), genre);
    private Comment comment = new Comment("comment_id", "username", "comment", Collections.singleton(book));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;
    @MockBean
    private BookService bookService;

    @Test
    public void commentControllerShouldCallViewWithAllGenresAndFillModel() throws Exception {
        when(commentService.getAllComments()).thenReturn(Collections.singletonList(comment));

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(view().name("allComments"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("comments", hasItems(Collections.singletonList(comment).toArray())));

        verify(commentService, times(1)).getAllComments();
    }

    @Test
    public void commentControllerShouldReturnViewWithNameCreateComment() throws Exception {
        List<Book> books = Collections.singletonList(book);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/createComment"))
                .andExpect(status().isOk())
                .andExpect(view().name("createComment"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasItems(books.toArray())));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    public void commentControllerShouldCreateGenreOnPostMethodAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(post("/createComment").flashAttr("comment", comment))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/comments"));

        verify(commentService, times(1)).createComment(eq(comment));
    }

    @Test
    public void commentControllerShouldReturnEditBookPageWithDataInModel() throws Exception {
        when(commentService.getCommentById(eq(comment.getId()))).thenReturn(comment);
        List<Book> books = Collections.singletonList(book);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/editComment").param("id", comment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("editComment"))
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attribute("comment", comment))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasItems(books.toArray())));

        verify(commentService, times(1)).getCommentById(eq(comment.getId()));
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    public void commentControllerShouldUpdateGenreInDbAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(post("/editComment")
                .flashAttr("comment", comment))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/comments"));

        verify(commentService, times(1)).updateComment(comment);
    }

    @Test
    public void commentControllerShouldReturnErrorPageCauseNoGenreToUpdate() throws Exception {
        NotFoundException ex = new NotFoundException("testException");
        doThrow(ex).when(commentService).updateComment(eq(comment));

        mockMvc.perform(post("/editComment")
                .flashAttr("comment", comment))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception", ex));

    }

    @Test
    public void commentControllerShouldDeleteGenreAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(delete("/deleteComment")
                .param("id", comment.getId()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/comments"));
    }
}