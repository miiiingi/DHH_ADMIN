package study.dhh_admin.domain.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.dhh_admin.domain.menu.dto.MenuRequestDto;
import study.dhh_admin.domain.menu.service.MenuService;
import study.dhh_admin.domain.owner.entity.Owner;
import study.dhh_admin.domain.store.dto.StoreResponseDto;
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

    @Operation(summary = "선택 메뉴 상세 페이지 이동", description = "선택한 메뉴의 상세페이지로 이동합니다.")
    @GetMapping("/menu/{id}")
    public String getMenu(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails){

        Owner owner = userDetails.getUser();
        StoreResponseDto.GetMenuList menu = menuService.getMenu(id, owner);
        model.addAttribute("menu",menu);
        return "menuDetail";
    }

    @Operation(summary = "메뉴의 내용 수정", description = "메뉴의 내용을 수정합니다.")
    @PutMapping("/menu/{id}")
    @ResponseBody
    public ResponseEntity<String> updateMenu(@PathVariable Long id, @RequestPart(value="key") MenuRequestDto.UpdateMenuDto requestDto,
                             @RequestPart(value = "menuImg", required = false) MultipartFile menuImg) throws IOException {

        try{
            menuService.updateMenu( id, requestDto,menuImg);
            String message = "수정이 완료되었습니다.";
            return ResponseEntity.status(HttpStatus.CREATED).body(message);

        }catch (IOException e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
