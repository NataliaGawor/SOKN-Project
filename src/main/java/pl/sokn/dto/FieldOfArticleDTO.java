package pl.sokn.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FieldOfArticle")
public class FieldOfArticleDTO implements Serializable {
    private static final long serialVersionUID = -3665542098319007959L;

    private Long id;
    private String field;
}