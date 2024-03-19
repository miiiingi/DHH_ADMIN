package study.dhh_admin.domain.store.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.dhh_admin.domain.menu.entity.Menu;
import study.dhh_admin.domain.menu.repository.MenuRepository;
import study.dhh_admin.domain.owner.entity.Owner;
import study.dhh_admin.domain.owner.repository.OwnerRepository;
import study.dhh_admin.domain.store.dto.StoreRequestDto.Create;
import study.dhh_admin.domain.store.dto.StoreRequestDto.Update;
import study.dhh_admin.domain.store.dto.StoreResponseDto;
import study.dhh_admin.domain.store.entity.Store;
import study.dhh_admin.domain.store.repository.StoreRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;


//    private final String uploadDir = "/Users/aper/Desktop/DHH/DHH_ADMIN/src/main/resources/static/images/";
    private final String uploadDir = "D:/항해99/spring/DHH/src/main/resources/static/images/";

    @Transactional
    public void createOwnerStore(Create requestDto, Owner owner, MultipartFile file) throws IOException {

        String fullPath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(fullPath));

        Owner ownerDB = ownerRepository.getReferenceById(owner.getId());

        ownerDB.hasStore();

        storeRepository.save(requestDto.toEntity(owner, fullPath, file.getOriginalFilename()));
    }

    @Transactional(readOnly = true)
    public StoreResponseDto.GetStore getOwnerStore(Long owner) {

        Store store = storeRepository.findByOwnerId(owner);

        return getGetMenuList(store);
    }

    @Transactional
    public void updateOwnerStore(Long owner, Update requestDto, MultipartFile file) throws IOException {

        Store store = storeRepository.findByOwnerId(owner);

        String fullPath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(fullPath));

        store.update(requestDto.name(),
                fullPath,
                requestDto.address(),
                requestDto.description(),
                file.getOriginalFilename());

    }

    @Transactional
    public void deleteOwnerStore(Owner owner) {

        Owner ownerDB = ownerRepository.getReferenceById(owner.getId());

        ownerDB.deleteStore();

        storeRepository.deleteByOwner(owner);

    }

    @NotNull
    private StoreResponseDto.GetStore getGetMenuList(Store store) {
        List<Menu> menus = menuRepository.findByStore(store);

        List<StoreResponseDto.GetMenuList> menuLists = new ArrayList<>();

        for (Menu menu : menus) {
            menuLists.add(
                    new StoreResponseDto.GetMenuList(
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            menu.getImageUrl(),
                            menu.getDescription()
                    )
            );
        }

        return new StoreResponseDto.GetStore(menuLists, store.getName());
    }

    public boolean checkPassword(String password, String inputPassword) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashedPassword = encoder.encode(inputPassword);

        return passwordEncoder.matches(password, hashedPassword);
    }
}
