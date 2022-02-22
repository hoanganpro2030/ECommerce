package vn.elca.training.repository.custom;

import com.querydsl.jpa.impl.JPAQuery;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.Address;
import vn.elca.training.model.entity.QACMUser;
import vn.elca.training.model.entity.QAddress;
import vn.elca.training.service.ACMUserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ACMUserRepositoryImpl implements ACMUserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public ACMUser findUserById(Long uid) {
        return new JPAQuery<ACMUser>(em)
                .from(QACMUser.aCMUser)
                .where(QACMUser.aCMUser.id.eq(uid))
                .fetchOne();
    }
}
