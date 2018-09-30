package sprihgfreamwork.mvcthymeleaf.domain.app.services;

import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    long countAuthors();

    void createAuthor(Author author) throws EntityExistsException;

    Author getAuthorById(String authorId) throws NotFoundException;

    void updateAuthor(Author author) throws NotFoundException;

    void deleteAuthor(String id);
}
