package sprihgfreamwork.mvcthymeleaf.com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.EntityExistsException;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.AuthorService;
import sprihgfreamwork.mvcthymeleaf.domain.dao.AuthorRepository;
import sprihgfreamwork.mvcthymeleaf.domain.model.Author;

import java.util.List;
import java.util.Optional;


@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public long countAuthors() {
        return authorRepository.count();
    }

    @Override
    public void createAuthor(Author author) throws EntityExistsException {
        Optional<Author> authorFromRepo = authorRepository.findByNameAndSurname(author.getName(), author.getSurname());
        if (!authorFromRepo.isPresent()) {
            authorRepository.save(author);
        } else {
            throw new EntityExistsException("Автор с таким именем и фамилией уже есть в базе");
        }
    }

    @Override
    public Author getAuthorById(String authorId) throws NotFoundException {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Не найден автор в базе для обновления"));
    }

    @Override
    public void updateAuthor(Author author) throws NotFoundException {
        authorRepository.findById(author.getId())
                .orElseThrow(() -> new NotFoundException("Нет автора в базе для обновления"));
        authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }
}
