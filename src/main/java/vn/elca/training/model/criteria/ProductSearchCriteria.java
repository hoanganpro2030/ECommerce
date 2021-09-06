package vn.elca.training.model.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.elca.training.model.enumeration.TypeProductCode;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ProductSearchCriteria {
    private Long id;

    private String name;

    private String description;

    private Integer quantity;

    private Integer price;

    private LocalDate dateStockIn;

    private TypeProductCode productType;
}
