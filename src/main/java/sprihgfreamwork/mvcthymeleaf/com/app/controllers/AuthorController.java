package sprihgfreamwork.mvcthymeleaf.com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import sprihgfreamwork.mvcthymeleaf.domain.model.Author;
import sprihgfreamwork.mvcthymeleaf.domain.model.Country;

import java.util.List;

@Controller
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(
            AuthorService authorService
    ) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String showAllAuthors(Model model) {
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("countries", Country.values());
        model.addAttribute("authors", authors);
        return "allAuthors";
    }

    @GetMapping("/createAuthor")
    public String createAuthorForm(Model model) {
        model.addAttribute("countries", Country.values());
        return "createAuthor";
    }

    @PostMapping("/createAuthor")
    public String createAuthor(@ModelAttribute Author author) throws EntityExistsException {
        authorService.createAuthor(author);
        return "redirect:/authors";
    }

    @PostMapping("/editAuthor")
    public String editAuthor(
            @RequestParam("id") String authorId,
            @RequestParam("namer") String newName,
            @RequestParam("surnamer") String newSurname
            ) throws NotFoundException {
        Author author = authorService.getAuthorById(authorId);
        author.setName(newName);
        author.setSurname(newSurname);
        authorService.updateAuthor(author);
        return "redirect:/authors";
    }

    @DeleteMapping("/deleteAuthor")
    public String deleteGenre(@RequestParam("id") String id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }
}
