package vn.elca.training.service;

import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.model.exception.NotEnoughProductQuantityException;
import vn.elca.training.model.exception.UserNotFoundException;

import java.io.IOException;
import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderDto getPurchaseOrderById(Long id);
    List<PurchaseOrderDto> getPurchaseOrderByUserId(Long uid);
    PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto purchaseOrderDto) throws IOException, NotEnoughProductQuantityException;
    PurchaseOrderDto createPurchaseOrderForUser(PurchaseOrderDto purchaseOrderDto, Long uid) throws IOException, UserNotFoundException, NotEnoughProductQuantityException, UserNotFoundException;
    PurchaseOrderDto updatePurchaseOrder(PurchaseOrderDto purchaseOrderDto);
    boolean deletePurchaseOrder(Long id) throws EntityNotFoundException;
}
