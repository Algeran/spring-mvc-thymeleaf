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
import sprihgfreamwork.mvcthymeleaf.domain.app.services.GenreService;
import sprihgfreamwork.mvcthymeleaf.domain.model.Genre;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    private Genre genre = new Genre("genre_id", "genre_name");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Test
    public void genreControllerShouldCallViewWithAllGenresAndFillModel() throws Exception {
        when(genreService.getAllGenres()).thenReturn(Collections.singletonList(genre));

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("allGenres"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", hasItems(Collections.singletonList(genre).toArray())));

        verify(genreService, times(1)).getAllGenres();
    }

    @Test
    public void genreControllerShouldReturnViewWithNameCreateGenre() throws Exception {
        mockMvc.perform(get("/createGenre"))
                .andExpect(status().isOk())
                .andExpect(view().name("createGenre"))
                .andExpect(model().size(0));
    }

    @Test
    public void genreControllerShouldCreateGenreOnPostMethodAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(post("/createGenre").flashAttr("genre", genre))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/genres"));

        verify(genreService, times(1)).createGenre(eq(genre));
    }

    @Test
    public void genreControllerShouldReturnErrorPageCauseGenreAlreadyInDb() throws Exception {
        EntityExistsException ex = new EntityExistsException("testException");
        when(genreService.createGenre(eq(genre))).thenThrow(ex);

        mockMvc.perform(post("/createGenre").flashAttr("genre", genre))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception", ex));
    }

    @Test
    public void genreControllerShouldUpdateGenreInDbAndRedirectToAllGenresView() throws Exception {
        when(genreService.getGenreById(eq(genre.getId()))).thenReturn(genre);

        mockMvc.perform(post("/editGenre")
                .param("id", genre.getId())
                .param("namer", genre.getName())
        ).andExpect(status().isFound())
        .andExpect(redirectedUrl("/genres"));

        verify(genreService, times(1)).getGenreById(eq(genre.getId()));
        verify(genreService, times(1)).updateGenre(genre);
    }

    @Test
    public void genreControllerShouldReturnErrorPageCauseNoGenreToUpdate() throws Exception {
        NotFoundException ex = new NotFoundException("testException");
        when(genreService.getGenreById(eq(genre.getId()))).thenThrow(ex);

        mockMvc.perform(post("/editGenre")
                .param("id", genre.getId())
                .param("namer", genre.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception", ex));

    }

    @Test
    public void genreControllerShouldDeleteGenreAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(delete("/deleteGenre")
                .param("id", genre.getId()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/genres"));
    }
}