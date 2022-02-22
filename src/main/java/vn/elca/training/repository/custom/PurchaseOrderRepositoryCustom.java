package vn.elca.training.repository.custom;

import vn.elca.training.model.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderRepositoryCustom {
    List<PurchaseOrder> findPObyUserId(Long id);
}
