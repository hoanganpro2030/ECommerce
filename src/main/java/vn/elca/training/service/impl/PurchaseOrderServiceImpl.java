package vn.elca.training.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.elca.training.model.dto.OrderDetail;
import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.entity.PurchaseOrder;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.model.exception.NotEnoughProductQuantityException;
import vn.elca.training.repository.ProductRepository;
import vn.elca.training.repository.PurchaseOrderRepository;
import vn.elca.training.service.PurchaseOrderService;
import vn.elca.training.util.MapService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Throwable.class)
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public PurchaseOrderDto getPurchaseOrderById(Long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto purchaseOrderDto) throws IOException, NotEnoughProductQuantityException {
        PurchaseOrder purchaseOrder = MapService.INSTANCE.purchaseOrderDtoTopurchaseOrder(purchaseOrderDto);
        purchaseOrder.setOrderDate(LocalDate.now());
        updateQuantityOfProductsInPO(purchaseOrder);
        PurchaseOrder poSave = purchaseOrderRepository.save(purchaseOrder);
        return MapService.INSTANCE.purchaseOrderTopurchaseOrderDto(poSave);
    }

    private void updateQuantityOfProductsInPO(PurchaseOrder purchaseOrder) throws IOException, NotEnoughProductQuantityException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderDetail> orderDetails = objectMapper.readValue(purchaseOrder.getDetail(), new TypeReference<List<OrderDetail>>(){});
        List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.findAll(productIds);
        for (OrderDetail orderDetail: orderDetails) {
            if (orderDetail.getQuantity() != null && orderDetail.getProductId() != null) {
                Product product = products.stream().filter(p -> p.getId().equals(orderDetail.getProductId())).findFirst().orElse(null);
                if (product != null) {
                    int quantityRemain = product.getQuantity() - orderDetail.getQuantity();
                    if (quantityRemain >= 0) {
                        product.setQuantity(quantityRemain);
                        productRepository.save(product);
                    } else {
                        throw new NotEnoughProductQuantityException(product.getName());
                    }
                }
            }
        }
    }

    @Override
    public PurchaseOrderDto updatePurchaseOrder(PurchaseOrderDto purchaseOrderDto) {
        return null;
    }

    @Override
    public boolean deletePurchaseOrder(Long id) throws EntityNotFoundException {
        return false;
    }
}
