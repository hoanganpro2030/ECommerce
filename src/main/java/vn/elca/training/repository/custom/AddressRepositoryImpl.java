package vn.elca.training.repository.custom;

import com.querydsl.jpa.impl.JPAQuery;
import vn.elca.training.model.entity.Address;
import vn.elca.training.model.entity.QAddress;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class AddressRepositoryImpl implements AddressRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Address> findAllAddressByUid(Long uid) {
        return new JPAQuery<Address>(em)
                .from(QAddress.address)
                .where(QAddress.address.acmUser.id.eq(uid))
                .fetch();
    }
}
