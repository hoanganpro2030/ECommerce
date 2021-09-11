package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.exception.NotEnoughProductQuantityException;
import vn.elca.training.service.PurchaseOrderService;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = "http://127.0.0.1:4200")
@RestController
@RequestMapping("/purchase-order")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @PostMapping("/create")
    @ResponseBody
    public PurchaseOrderDto createProduct(@RequestBody @Valid PurchaseOrderDto purchaseOrderDto) throws IOException, NotEnoughProductQuantityException {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
    }
}
