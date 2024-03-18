package study.dhh_admin.domain.owner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.dhh_admin.domain.owner.dto.SignupRequestRecord;
import study.dhh_admin.domain.owner.entity.Owner;
import study.dhh_admin.domain.owner.repository.OwnerRepository;
import study.dhh_admin.global.handler.exception.BusinessException;
import study.dhh_admin.global.handler.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignupRequestRecord requestDto) {
        if (ownerRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        String password = passwordEncoder.encode(requestDto.password());

        Owner owner = Owner.builder()
                .email(requestDto.email())
                .password(password)
                .name(requestDto.name())
                .build();
        ownerRepository.save(owner);
    }

}
