package vn.elca.training.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotEnoughProductQuantityException extends Exception {
    private String productName;
    public NotEnoughProductQuantityException(String productName) {
        super();
        this.productName = productName;
    }
}
