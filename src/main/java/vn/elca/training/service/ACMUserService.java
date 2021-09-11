package vn.elca.training.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.ACMUserDetail;
import vn.elca.training.repository.ACMUserRepository;

@Service
public class ACMUserService implements UserDetailsService {
    @Autowired
    private ACMUserRepository acmUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ACMUser user = acmUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new ACMUserDetail(user);
    }
}
