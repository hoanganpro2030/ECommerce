package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.model.exception.NotEnoughProductQuantityException;
import vn.elca.training.model.exception.UserNotFoundException;
import vn.elca.training.service.PurchaseOrderService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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

    @PostMapping("/create/{uid}")
    @ResponseBody
    public PurchaseOrderDto createProductForUser(@RequestBody @Valid PurchaseOrderDto purchaseOrderDto, @PathVariable Long uid) throws IOException, NotEnoughProductQuantityException, UserNotFoundException {
        return purchaseOrderService.createPurchaseOrderForUser(purchaseOrderDto, uid);
    }

    @GetMapping("/get/user/{uid}")
    @ResponseBody
    public List<PurchaseOrderDto> getPoForUser(@PathVariable Long uid) {
        return purchaseOrderService.getPurchaseOrderByUserId(uid);
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public PurchaseOrderDto getPoById(@PathVariable Long id) {
        return purchaseOrderService.getPurchaseOrderById(id);
    }
}
