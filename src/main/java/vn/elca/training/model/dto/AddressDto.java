package vn.elca.training.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto extends AbstractDto{
    private String title;

    private String phoneNumber;

    private String province;

    private String district;

    private String ward;

    private String street;

    private String note;

    private Boolean isDefault;

    @JsonIgnore
    private ACMUserDto acmUserDto;
}
