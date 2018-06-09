package pl.sokn.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.controller.config.BaseTest;
import pl.sokn.definitions.SoknDefinitions;
import pl.sokn.repository.ArticleRepository;
import pl.sokn.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest extends BaseTest {

    private static final String AUTHOR_EMAIL = "prelegent@email.com";
    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    @Test
    public void addArticle() throws Exception {

        MockMultipartFile file=
                new MockMultipartFile("file","test.txt", MediaType.TEXT_PLAIN_VALUE,"test".getBytes());

        String token = obtainToken(AUTHOR_EMAIL, "pass");

        mvc.perform(fileUpload("/uploadArticle")
                .file(file)
                .param("subject", "Test")
                .param("fieldOfArticle", "2")
                .header(SoknDefinitions.AuthorizationHelper.AUTHORIZATION, SoknDefinitions.AuthorizationHelper.BEARER + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addArticleWithBadSubject() throws Exception {
        String token = obtainToken(AUTHOR_EMAIL, "pass");

        MockMultipartFile file =
                new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());


        mvc.perform(fileUpload("/uploadArticle")
                .file(file)
                .param("subject", "Test_2")
                .param("fieldOfArticle", "2")
                .header(SoknDefinitions.AuthorizationHelper.AUTHORIZATION, SoknDefinitions.AuthorizationHelper.BEARER + token))
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void getAllArticle() throws Exception{
        String token = obtainToken(AUTHOR_EMAIL, "pass");
        mvc.perform(get("/getAllArticles")
                .header(SoknDefinitions.AuthorizationHelper.AUTHORIZATION, SoknDefinitions.AuthorizationHelper.BEARER + token))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
