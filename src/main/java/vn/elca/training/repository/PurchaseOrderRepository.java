package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;
import vn.elca.training.model.entity.PurchaseOrder;
import vn.elca.training.repository.custom.PurchaseOrderRepositoryCustom;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, QueryDslPredicateExecutor<PurchaseOrder>, PurchaseOrderRepositoryCustom {
}
