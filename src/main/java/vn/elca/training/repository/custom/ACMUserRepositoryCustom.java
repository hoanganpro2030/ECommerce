package vn.elca.training.repository.custom;

import vn.elca.training.model.entity.ACMUser;

public interface ACMUserRepositoryCustom {
    ACMUser findUserById(Long uid);
}
