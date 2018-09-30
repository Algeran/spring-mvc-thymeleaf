package sprihgfreamwork.mvcthymeleaf.com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.GenreService;
import sprihgfreamwork.mvcthymeleaf.domain.dao.GenreRepository;
import sprihgfreamwork.mvcthymeleaf.domain.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre createGenre(Genre genre) throws EntityExistsException {
        Optional<Genre> genreByName = genreRepository.findByName(genre.getName());
        if (!genreByName.isPresent()) {
            genreRepository.save(genre);
            return genre;
        } else {
            throw new EntityExistsException("Жанр с таким именем уже существует");
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public long countGenres() {
        return genreRepository.count();
    }

    @Override
    public void deleteGenre(String id) {
        genreRepository.deleteById(id);
    }

    @Override
    public void updateGenre(Genre genre) throws NotFoundException {
        Optional<Genre> genreFromRepo = genreRepository.findByName(genre.getName());
        if (genreFromRepo.isPresent()) {
            genreRepository.save(genre);
        } else {
            throw new NotFoundException("Жанр с таким именем уже существует");
        }
    }

    @Override
    public Genre getGenreById(String genreId) throws NotFoundException {
        return genreRepository.findById(genreId).orElseThrow(() -> new NotFoundException("Не найдено жанра"));
    }
}
