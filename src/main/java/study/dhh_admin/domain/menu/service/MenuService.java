package study.dhh_admin.domain.menu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.dhh_admin.domain.menu.dto.MenuRequestDto;
import study.dhh_admin.domain.menu.entity.Menu;
import study.dhh_admin.domain.menu.repository.MenuRepository;
import study.dhh_admin.domain.owner.entity.Owner;
import study.dhh_admin.domain.store.entity.Store;
import study.dhh_admin.domain.store.repository.StoreRepository;

import java.io.File;
import java.io.IOException;
@Slf4j(topic = "menuService")
@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    private final String uploadDir = "D:/항해99/spring/DHH/src/main/resources/static/images/";

    // 메뉴 추가
    @Transactional
    public void createMenu(MenuRequestDto.CreateMenuDto requestDto, Owner owner) throws IOException {
        // owner 정보에서 가게 정보 뽑기
        Store store = storeRepository.findByOwnerId(owner.getId());
        log.info("img path : " + uploadDir);

        MultipartFile file = requestDto.menuImg();
        String originFileName = file.getOriginalFilename(); // img 원본 이름
        String imageUrl = uploadDir + originFileName; // 사진 저장 경로 + 원본 이름
        file.transferTo(new File(imageUrl));
        Menu menu = requestDto.toEntity(store, imageUrl, originFileName);

        menuRepository.save(menu);
    }
}
