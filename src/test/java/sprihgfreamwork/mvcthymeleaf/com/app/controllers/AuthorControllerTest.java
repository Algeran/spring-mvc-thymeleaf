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
import sprihgfreamwork.mvcthymeleaf.domain.model.Author;
import sprihgfreamwork.mvcthymeleaf.domain.model.Country;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sprihgfreamwork.mvcthymeleaf.domain.model.Country.RUSSIA;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    private Author author = new Author("author_id", "author_name", "author_surname", RUSSIA);
    
    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authorControllerShouldCallViewWithAllGenresAndFillModel() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(author));

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("allAuthors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", hasItems(Collections.singletonList(author).toArray())));

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    public void authorControllerShouldReturnViewWithNameCreateGenre() throws Exception {
        mockMvc.perform(get("/createAuthor"))
                .andExpect(status().isOk())
                .andExpect(view().name("createAuthor"))
                .andExpect(model().attributeExists("countries"))
                .andExpect(model().attribute("countries", Country.values()));
    }

    @Test
    public void authorControllerShouldCreateGenreOnPostMethodAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(post("/createAuthor").flashAttr("author", author))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService, times(1)).createAuthor(eq(author));
    }

    @Test
    public void authorControllerShouldReturnErrorPageCauseGenreAlreadyInDb() throws Exception {
        EntityExistsException ex = new EntityExistsException("testException");
        doThrow(ex).when(authorService).createAuthor(eq(author));

        mockMvc.perform(post("/createAuthor").flashAttr("author", author))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception", ex));
    }

    @Test
    public void authorControllerShouldUpdateGenreInDbAndRedirectToAllGenresView() throws Exception {
        when(authorService.getAuthorById(eq(author.getId()))).thenReturn(author);

        mockMvc.perform(post("/editAuthor")
                .param("id", author.getId())
                .param("namer", author.getName())
                .param("surnamer", author.getSurname())
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService, times(1)).getAuthorById(eq(author.getId()));
        verify(authorService, times(1)).updateAuthor(author);
    }

    @Test
    public void authorControllerShouldReturnErrorPageCauseNoGenreToUpdate() throws Exception {
        NotFoundException ex = new NotFoundException("testException");
        when(authorService.getAuthorById(eq(author.getId()))).thenThrow(ex);

        mockMvc.perform(post("/editAuthor")
                .param("id", author.getId())
                .param("namer", author.getName())
                .param("surnamer", author.getSurname()))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception", ex));

    }

    @Test
    public void authorControllerShouldDeleteGenreAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(delete("/deleteAuthor")
                .param("id", author.getId()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/authors"));
    }
    

}