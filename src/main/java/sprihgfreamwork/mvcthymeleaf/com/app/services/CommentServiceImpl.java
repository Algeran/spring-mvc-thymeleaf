package sprihgfreamwork.mvcthymeleaf.com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.app.services.CommentService;
import sprihgfreamwork.mvcthymeleaf.domain.dao.CommentRepository;
import sprihgfreamwork.mvcthymeleaf.domain.model.Comment;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(
            CommentRepository commentRepository
    ) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public long countComments() {
        return commentRepository.count();
    }

    @Override
    public Comment getCommentById(String id) throws NotFoundException {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдено комментария"));
    }

    @Override
    public void updateComment(Comment comment) throws NotFoundException {
        commentRepository.findById(comment.getId())
                .orElseThrow(() -> new NotFoundException("Нет комментария в базе для обновления"));
        commentRepository.save(comment);
    }

    @Override
    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

}
