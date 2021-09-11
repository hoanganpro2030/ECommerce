package vn.elca.training.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import vn.elca.training.model.enumeration.StatusOrder;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class PurchaseOrderDto {
    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String addressDetail;

    @NotBlank
    private String province;

    @NotBlank
    private String district;

    @NotBlank
    private String ward;

    private String note;

    @NotNull
    private Integer totalPrice;

    @NotBlank
    private String detail;

    private LocalDate orderDate;

    @NotNull
    private StatusOrder status;

    private Integer version;
}
