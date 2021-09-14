package vn.elca.training.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.UserPrincipal;
import vn.elca.training.repository.ACMUserRepository;
import vn.elca.training.service.ACMUserService;

import java.util.Date;

@Service
@Transactional(rollbackFor = Throwable.class)
@Qualifier("UserDetailsService")
@Slf4j
public class ACMUserServiceImpl implements UserDetailsService, ACMUserService {
    @Autowired
    private ACMUserRepository acmUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ACMUser user = acmUserRepository.findACMUserByUsername(username);
        if (user == null) {
            log.error("User not found by user name: " + username);
            throw new UsernameNotFoundException(username);
        } else {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            acmUserRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            log.info("Returning found user by username");
            return userPrincipal;
        }
    }
}
