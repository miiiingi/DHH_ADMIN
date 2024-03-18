package study.dhh_admin.global.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.dhh_admin.domain.owner.entity.Owner;
import study.dhh_admin.domain.owner.repository.OwnerRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final OwnerRepository ownerRepository;

    public UserDetailsServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Owner user = ownerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        return new UserDetailsImpl(user);
    }
}