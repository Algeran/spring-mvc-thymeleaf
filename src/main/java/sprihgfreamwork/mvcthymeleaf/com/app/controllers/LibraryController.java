package sprihgfreamwork.mvcthymeleaf.com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.AuthorService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.BookService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.CommentService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.GenreService;

@Controller
public class LibraryController {

    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final CommentService commentService;

    @Autowired
    public LibraryController(
            GenreService genreService,
            AuthorService authorService,
            BookService bookService,
            CommentService commentService
    ) {
        this.genreService = genreService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String welcomePage(Model model) {
        model.addAttribute("genres", genreService.countGenres());
        model.addAttribute("authors", authorService.countAuthors());
        model.addAttribute("books", bookService.countBooks());
        model.addAttribute("comments", commentService.countComments());
        return "index";
    }
}
