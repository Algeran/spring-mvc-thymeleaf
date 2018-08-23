package sprihgfreamwork.mvcthymeleaf.domain.dao;


import org.springframework.data.mongodb.repository.MongoRepository;
import sprihgfreamwork.mvcthymeleaf.domain.model.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {

    Optional<Genre> findByName(String name);

    void deleteByName(String name);
}
