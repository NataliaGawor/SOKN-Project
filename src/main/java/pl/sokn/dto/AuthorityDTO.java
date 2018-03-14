package pl.sokn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO implements Serializable {
    private static final long serialVersionUID = -3665542098319008959L;

    private Long id;
    private String role;
    private String description;
}
