package pl.sokn.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.sokn.entity.ArticleGrade;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.GenericRepository;
import pl.sokn.service.ArticleGradeService;

@Service
public class ArticleGradeServiceImpl extends AbstractGenericService<ArticleGrade,Long> implements ArticleGradeService{


    ArticleGradeServiceImpl(GenericRepository<ArticleGrade, Long> repository) {
        super(repository);
    }

    @Override
    public boolean hasThreePartGrade(ArticleGrade articleGrade){
        if( articleGrade.getNegative() + articleGrade.getNeutral() + articleGrade.getPositive() >= 3)
            return true;
        return false;
    }

    @Override
    public boolean canUserGiveReview(Long id, ArticleGrade articleGrade){

        if(articleGrade.getReviewerIdOne() != null && articleGrade.getReviewerIdOne() == id)
            return false;

        if(articleGrade.getReviewerIdTwo() != null && articleGrade.getReviewerIdTwo() == id)
            return false;

        if(articleGrade.getReviewerIdThree() != null && articleGrade.getReviewerIdThree() == id)
            return false;

        return true;
    }

    @Override
    public boolean addReviewer(Long id, ArticleGrade articleGrade) {
        if(articleGrade.getReviewerIdOne() == null) {
            articleGrade.setReviewerIdOne(id);
            return true;
        }
        if(articleGrade.getReviewerIdTwo() == null) {
            articleGrade.setReviewerIdTwo(id);
            return true;
        }
        if(articleGrade.getReviewerIdThree() == null) {
            articleGrade.setReviewerIdThree(id);
            return true;
        }

        return false;
    }

    @Override
    public void addPartGrade(Long reviewerId, ArticleGrade articleGrade, int partGrade, String comment) throws OperationException {
        String commentTitle = "";

        if(hasThreePartGrade(articleGrade))
            throw new OperationException(HttpStatus.FORBIDDEN, "Artykuł już zawiera maksymalna liczbę ocen.");

        if(!addReviewer(reviewerId, articleGrade))
            throw new OperationException(HttpStatus.FORBIDDEN, "Nie można przypisać recenzenta do oceny.");

        //add singleGrade to articleGrade
        if(partGrade < 0){
            articleGrade.setNegative(articleGrade.getNegative() + 1);
            commentTitle = "Ocena negatywna";
        }
        else if (partGrade == 0) {
            articleGrade.setNeutral(articleGrade.getNeutral() + 1);
            commentTitle = "Ocena Neutralna";
        }
        else {
            articleGrade.setPositive(articleGrade.getPositive() + 1);
            commentTitle = "Ocena Pozytywna";
        }
        //add comment to articleGrade
        articleGrade.setComment(articleGrade.getComment() + commentTitle + "<br/>" + comment + "<br/><br/>");

        update(articleGrade);
    }
}
