package vn.elca.training.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.elca.training.model.dto.OrderDetail;
import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.Product;
import vn.elca.training.model.entity.PurchaseOrder;
import vn.elca.training.model.exception.EntityNotFoundException;
import vn.elca.training.model.exception.NotEnoughProductQuantityException;
import vn.elca.training.model.exception.UserNotFoundException;
import vn.elca.training.repository.ACMUserRepository;
import vn.elca.training.repository.ProductRepository;
import vn.elca.training.repository.PurchaseOrderRepository;
import vn.elca.training.service.PurchaseOrderService;
import vn.elca.training.util.MapService;
import vn.elca.training.util.POMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static vn.elca.training.constant.UserImplConstant.NO_USER_FOUND_BY_ID;

@Service
@Transactional(rollbackFor = Throwable.class)
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ACMUserRepository acmUserRepository;

    @Override
    public PurchaseOrderDto getPurchaseOrderById(Long id) {
        return POMapper.INSTANCE.purchaseOrderTopurchaseOrderDto(purchaseOrderRepository.findOne(id));
    }

    @Override
    public List<PurchaseOrderDto> getPurchaseOrderByUserId(Long uid) {
        return POMapper.INSTANCE.purchaseOrderListTopurchaseOrderDtoList(purchaseOrderRepository.findPObyUserId(uid));
    }

    @Override
    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto purchaseOrderDto) throws IOException, NotEnoughProductQuantityException {
        PurchaseOrder purchaseOrder = createPOGeneral(purchaseOrderDto);
        PurchaseOrder poSave = purchaseOrderRepository.save(purchaseOrder);
        return POMapper.INSTANCE.purchaseOrderTopurchaseOrderDto(poSave);
    }

    private PurchaseOrder createPOGeneral(PurchaseOrderDto purchaseOrderDto) throws IOException, NotEnoughProductQuantityException {
        PurchaseOrder purchaseOrder = POMapper.INSTANCE.purchaseOrderDtoTopurchaseOrder(purchaseOrderDto);
        purchaseOrder.setOrderDate(LocalDate.now());
        updateQuantityOfProductsInPO(purchaseOrder);
        return purchaseOrder;
    }

    @Override
    public PurchaseOrderDto createPurchaseOrderForUser(PurchaseOrderDto purchaseOrderDto, Long uid) throws IOException, NotEnoughProductQuantityException, UserNotFoundException {
        PurchaseOrder purchaseOrder = createPOGeneral(purchaseOrderDto);
        ACMUser acmUser = acmUserRepository.findUserById(uid);
        if (acmUser == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_ID + uid);
        }
        purchaseOrder.setAcmUser(acmUser);
        PurchaseOrder poSave = purchaseOrderRepository.save(purchaseOrder);
        return POMapper.INSTANCE.purchaseOrderTopurchaseOrderDto(poSave);
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
