package pl.sokn.service;

import pl.sokn.entity.ArticleGrade;
import pl.sokn.exception.OperationException;

public interface ArticleGradeService extends GenericService<ArticleGrade,Long>{
    void addPartGrade(Long reviewerId, ArticleGrade articleGrade, int partGrade, String comment) throws OperationException;
    boolean hasThreePartGrade(ArticleGrade articleGrade);
    boolean canUserGiveReview(Long id, ArticleGrade articleGrade);
    boolean addReviewer(Long id, ArticleGrade articleGrade);
}
