package pl.sokn.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import pl.sokn.entity.ArticleGrade;
import pl.sokn.exception.OperationException;
import pl.sokn.service.implementation.ArticleGradeServiceImpl;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ArticleGradeServiceImplTest {

    private ArticleGrade articleGradeFull, articleGradeNotFull,articleGradeEmpty;
    private Long reviewerId;

    @InjectMocks
    private ArticleGradeServiceImpl articleGradeService;

    @Before
    public void prepare(){
        articleGradeNotFull = ArticleGrade.builder()
                .reviewerIdOne(1L)
                .reviewerIdTwo(2L)
                .positive(1)
                .neutral(1)
                .build();
        articleGradeFull = ArticleGrade.builder()
                .reviewerIdOne(1L)
                .reviewerIdTwo(2L)
                .reviewerIdThree(3L)
                .positive(2)
                .neutral(1)
                .build();
        articleGradeEmpty = ArticleGrade.builder()
                .positive(0)
                .neutral(0)
                .positive(0)
                .build();

        reviewerId = 3L;
    }

    @Test
    public void hasThreePartGradeTest(){
        assertFalse(articleGradeService.hasThreePartGrade(articleGradeNotFull));
        assertTrue(articleGradeService.hasThreePartGrade(articleGradeFull));
    }

    @Test
    public void canUserGiveReviewTest(){
        assertTrue(articleGradeService.canUserGiveReview(3L, articleGradeNotFull));
        assertFalse(articleGradeService.canUserGiveReview(1L, articleGradeNotFull));
        assertFalse(articleGradeService.canUserGiveReview(3L, articleGradeFull));
    }

    @Test
    public void addReviewerTestOK(){
       assertTrue(articleGradeService.addReviewer(3L, articleGradeNotFull));
       assertEquals(new Long(3), articleGradeNotFull.getReviewerIdThree());
        assertNotEquals(new Long(1), articleGradeNotFull.getReviewerIdThree());
    }

    @Test
    public void addReviewerTestFail(){
        assertFalse(articleGradeService.addReviewer(1L, articleGradeFull));
    }

}
