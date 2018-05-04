package pl.sokn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@ApiModel(value = "Reviewer")
public class ReviewerDTO extends UserDTO {

    @NotBlank
    @ApiModelProperty(required = true, position = 12, example = "Sztuczna Inteligencja")
    private String fieldOfArticleOne;

    private String fieldOfArticleTwo;

    private String fieldOfArticleThree;

    public ReviewerDTO(){
        super();
    }

}
