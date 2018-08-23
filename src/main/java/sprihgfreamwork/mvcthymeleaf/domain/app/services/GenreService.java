package sprihgfreamwork.mvcthymeleaf.domain.app.services;

import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.model.Genre;

import java.util.List;

public interface GenreService {

    Genre createGenre(Genre genre) throws EntityExistsException;

    List<Genre> getAllGenres();

    long countGenres();

    void deleteGenre(String id);

    void updateGenre(Genre genre) throws NotFoundException;

    Genre getGenreById(String genreId) throws NotFoundException;
}
