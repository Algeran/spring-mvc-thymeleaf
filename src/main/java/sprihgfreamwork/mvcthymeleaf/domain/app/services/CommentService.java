package sprihgfreamwork.mvcthymeleaf.domain.app.services;

import sprihgfreamwork.mvcthymeleaf.com.app.utility.NotFoundException;
import sprihgfreamwork.mvcthymeleaf.domain.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComments();

    long countComments();

    Comment getCommentById(String id) throws NotFoundException;

    void updateComment(Comment comment) throws NotFoundException;

    void createComment(Comment comment);

    void deleteComment(String id);
}
