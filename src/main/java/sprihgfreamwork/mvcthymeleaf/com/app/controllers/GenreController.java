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
import sprihgfreamwork.mvcthymeleaf.domain.app.services.GenreService;
import sprihgfreamwork.mvcthymeleaf.domain.model.Genre;

import java.util.List;

@Controller
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(
            GenreService genreService
    ) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public String showAllGenres(Model model) {
        List<Genre> genres = genreService.getAllGenres();
        model.addAttribute("genres", genres);
        return "allGenres";
    }

    @GetMapping("/createGenre")
    public String createGenreForm() {
        return "createGenre";
    }

    @PostMapping("/createGenre")
    public String createGenre(
            @ModelAttribute Genre genre) throws EntityExistsException {
        genreService.createGenre(genre);
        return "redirect:/genres";
    }

    @PostMapping("/editGenre")
    public String editGenre(
            @RequestParam("id") String genreId,
            @RequestParam("namer") String newName) throws NotFoundException {
        Genre genre = genreService.getGenreById(genreId);
        genre.setName(newName);
        genreService.updateGenre(genre);
        return "redirect:/genres";
    }

    @DeleteMapping("/deleteGenre")
    public String deleteGenre(@RequestParam("id") String id) {
        genreService.deleteGenre(id);
        return "redirect:/genres";
    }
}
