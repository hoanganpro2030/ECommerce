package vn.elca.training.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.elca.training.model.enumeration.TypeProductCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer price;

    private String image;

    @NotNull
    private LocalDate dateStockIn;

    @NotNull
    private TypeProductCode productType;

    private CategoryDto category;

    private Integer version;
}
