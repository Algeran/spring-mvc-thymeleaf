package sprihgfreamwork.mvcthymeleaf.com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.BookService;
import sprihgfreamwork.mvcthymeleaf.domain.dao.BookRepository;
import sprihgfreamwork.mvcthymeleaf.domain.model.Book;

import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(
            BookRepository bookRepository
    ) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public long countBooks() {
        return bookRepository.count();
    }

    @Override
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBook(String id) throws NotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдено книги в базе"));
    }

    @Override
    public void updateBook(Book book) throws NotFoundException {
        bookRepository.findById(book.getId())
                .orElseThrow(() -> new NotFoundException("Нет книги в базе для обновления"));
        bookRepository.save(book);
    }

    @Override
    public void createBook(Book book) throws EntityExistsException {
        Optional<Book> bookFromRepo = bookRepository.findByName(book.getName());
        if (!bookFromRepo.isPresent()) {
            bookRepository.save(book);
        } else {
            throw new EntityExistsException("Книга с таким наименованием уже в базе");
        }
    }
}
