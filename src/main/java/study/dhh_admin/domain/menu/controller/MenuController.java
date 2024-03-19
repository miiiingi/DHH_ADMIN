package study.dhh_admin.domain.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.dhh_admin.domain.menu.dto.MenuRequestDto;
import study.dhh_admin.domain.menu.service.MenuService;
import study.dhh_admin.domain.owner.entity.Owner;
import study.dhh_admin.domain.store.entity.Store;
import study.dhh_admin.domain.user.entity.User;
import study.dhh_admin.global.handler.exception.BusinessException;
import study.dhh_admin.global.handler.exception.ErrorCode;
import study.dhh_admin.global.security.UserDetailsImpl;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "메뉴 CRUD")
@Tag(name = "Menu API", description = "메뉴 CRUD")
@RequestMapping("/v2")
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @Operation(summary = "가게 메뉴 등록", description = "가게 메뉴를 추가한 뒤 메인페이지로 이동합니다.")
    @PostMapping("/menu")
    public String createMenu(@ModelAttribute MenuRequestDto.CreateMenuDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        // 유저 정보에서 가게 정보 가져오기
        try {
            Owner owner = userDetails.getUser();
            menuService.createMenu(requestDto, owner);
            return "redirect:/v2/store";
        }catch (BusinessException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }catch (IOException e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


}
