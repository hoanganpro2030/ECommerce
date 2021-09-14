package vn.elca.training.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class ACMUserDto {
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
