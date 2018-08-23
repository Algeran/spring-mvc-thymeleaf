package sprihgfreamwork.mvcthymeleaf.com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.BookService;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.CommentService;
import sprihgfreamwork.mvcthymeleaf.domain.model.Book;
import sprihgfreamwork.mvcthymeleaf.domain.model.Comment;

import java.util.List;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final BookService bookService;

    @Autowired
    public CommentController(
            CommentService commentService,
            BookService bookService
    ) {
        this.commentService = commentService;
        this.bookService = bookService;
    }

    @GetMapping("/comments")
    public String getAllComments(Model model) {
        List<Comment> allComments = commentService.getAllComments();
        model.addAttribute("comments", allComments);
        return "allComments";
    }

    @GetMapping("/createComment")
    public String createCommentPage(Model model) {
        List<Book> allBooks = bookService.getAllBooks();
        model.addAttribute("books", allBooks);
        return "createComment";
    }

    @PostMapping("/createComment")
    public String createComment(@ModelAttribute Comment comment) {
        commentService.createComment(comment);
        return "redirect:/comments";
    }

    @GetMapping("/editComment")
    public String editCommentPage(
            @RequestParam("id") String id,
            Model model) throws NotFoundException {
        Comment comment = commentService.getCommentById(id);
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("comment", comment);
        model.addAttribute("books", books);
        return "editComment";
    }

    @PostMapping("/editComment")
    public String editComment(@ModelAttribute Comment comment) throws NotFoundException {
        commentService.updateComment(comment);
        return "redirect:/comments";
    }

    @DeleteMapping("/deleteComment")
    public String deleteComment(
            @RequestParam("id") String id
    ) {
        commentService.deleteComment(id);
        return "redirect:/comments";
    }

}
