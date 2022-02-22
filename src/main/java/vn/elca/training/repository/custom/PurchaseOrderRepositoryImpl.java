package vn.elca.training.repository.custom;

import com.querydsl.jpa.impl.JPAQuery;
import vn.elca.training.model.entity.PurchaseOrder;
import vn.elca.training.model.entity.QPurchaseOrder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class PurchaseOrderRepositoryImpl implements PurchaseOrderRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PurchaseOrder> findPObyUserId(Long id) {
        // TODO transform to only retrieve neccessary data
        return new JPAQuery<PurchaseOrder>(em)
                .from(QPurchaseOrder.purchaseOrder)
                .where(QPurchaseOrder.purchaseOrder.acmUser.id.eq(id))
                .fetch();
    }
}
