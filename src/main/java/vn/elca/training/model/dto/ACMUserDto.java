package vn.elca.training.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ACMUserDto {
    private Long id;

    private String userId;

    @NotBlank
    private String username;

    private String password;

    private String fullName;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String role;
    private String[] authorities;
    private Boolean isActive;
    private Boolean isNotLocked;
//    private Set<AddressDto> addressDto;
    private Integer version;
}
