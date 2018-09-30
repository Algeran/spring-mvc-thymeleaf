package sprihgfreamwork.mvcthymeleaf.domain.app.services;

import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    long countBooks();

    void deleteBook(String id);

    Book getBook(String id) throws NotFoundException;

    void updateBook(Book book) throws NotFoundException;

    void createBook(Book book) throws EntityExistsException;
}
