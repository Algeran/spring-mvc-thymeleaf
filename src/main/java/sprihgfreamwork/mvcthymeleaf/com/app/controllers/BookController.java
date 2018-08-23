package sprihgfreamwork.mvcthymeleaf.com.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.AuthorService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.BookService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.GenreService;
import sprihgfreamwork.mvcthymeleaf.domain.model.Author;
import sprihgfreamwork.mvcthymeleaf.domain.model.Book;
import sprihgfreamwork.mvcthymeleaf.domain.model.Genre;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookController(
            BookService bookService,
            AuthorService authorService,
            GenreService genreService
    ) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/books")
    public String showAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "allBooks";
    }

    @GetMapping("/createBook")
    public String createBookPage(Model model) {
        List<Author> authors = authorService.getAllAuthors();
        List<Genre> genres = genreService.getAllGenres();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "createBook";
    }

    @PostMapping(value = "/createBook")
    public String createBook(@ModelAttribute Book book) throws EntityExistsException {
        bookService.createBook(book);
        return "redirect:/books";
    }

    @GetMapping("/editBook")
    public String editBookPage(
            @RequestParam("id") String id,
            Model model
    ) throws NotFoundException {
        Book book = bookService.getBook(id);
        List<Author> authors = authorService.getAllAuthors();
        List<Genre> genres = genreService.getAllGenres();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editBook";
    }

    @PostMapping("/editBook")
    public String editBook(
            @ModelAttribute Book book
    ) throws NotFoundException {
        bookService.updateBook(book);
        return "redirect:/books";
    }

    @DeleteMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") String id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
