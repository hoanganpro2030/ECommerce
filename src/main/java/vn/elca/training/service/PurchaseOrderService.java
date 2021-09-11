package vn.elca.training.service;

import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.model.exception.NotEnoughProductQuantityException;

import java.io.IOException;

public interface PurchaseOrderService {
    PurchaseOrderDto getPurchaseOrderById(Long id) throws EntityNotFoundException;
    PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto purchaseOrderDto) throws IOException, NotEnoughProductQuantityException;
    PurchaseOrderDto updatePurchaseOrder(PurchaseOrderDto purchaseOrderDto);
    boolean deletePurchaseOrder(Long id) throws EntityNotFoundException;
}
