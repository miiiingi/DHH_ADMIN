package study.dhh_admin.domain.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.dhh_admin.domain.store.dto.StoreRequestDto;
import study.dhh_admin.domain.store.dto.StoreResponseDto;
import study.dhh_admin.domain.store.service.StoreService;
import study.dhh_admin.global.handler.exception.BusinessException;
import study.dhh_admin.global.handler.exception.ErrorCode;
import study.dhh_admin.global.security.UserDetailsImpl;


import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;
    private final ObjectMapper objectMapper;


    @GetMapping("/v2")
    private String ownerStore(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(String.valueOf(userDetails.getUser().isStoreStatus()));
        if (userDetails.getUser().isStoreStatus() == false) {
            return "storeRegister";
        }
        return "owner";
    }

    @PostMapping("/v2/store")
    public String createOwnerStore(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                   @RequestPart("file") MultipartFile file,
                                   @RequestPart("request")String jsonData,
                                   Model model) {
        try {
             StoreRequestDto.Create requestDto = objectMapper.readValue(jsonData, StoreRequestDto.Create.class);
            storeService.createOwnerStore(requestDto, userDetails.getUser(), file);
        } catch (BusinessException ex) {
            model.addAttribute("ErrorCode", ex.getErrorCode().getStatus());
            model.addAttribute("ErrorMessage", ex.getErrorCode().getMessage());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return "owner";
    }

    @GetMapping("/v2/store")
    public String getOwnerStore(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {

        String storeName = storeService.getOwnerStore(userDetails.getUser().getId()).storeName();
        List<StoreResponseDto.GetMenuList> menuLists = storeService.getOwnerStore(userDetails.getUser().getId()).menuLists();
        model.addAttribute("storeName", storeName);
        model.addAttribute("menuList", menuLists);

        return "ownerStore";
    }

    @PutMapping("/v2/store")
    public String updateOwnerStore(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                   @RequestPart("file") MultipartFile file,
                                   @RequestPart("request")String jsonData,
                                   Model model) throws IOException {
        StoreRequestDto.Update requestDto = objectMapper.readValue(jsonData, StoreRequestDto.Update.class);
        storeService.updateOwnerStore(userDetails.getUser().getId(), requestDto, file);
        model.addAttribute("msg", "수정이 완료되었습니다.");

        return "ownerStore";
    }

    @DeleteMapping("/v2/store")
    public String deleteOwnerStore(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        storeService.deleteOwnerStore(userDetails.getUser());
        model.addAttribute("msg", "삭제가 완료되었습니다.");

        return "owner";
    }

    @PostMapping("/v2/store/password-check")
    public ResponseEntity<Boolean> checkPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody String enteredPassword) {
        enteredPassword = enteredPassword.replaceFirst("password=", "");
        log.info("password : " + enteredPassword);
        // 서비스로부터 비밀번호 일치 여부 확인 후 반환
        boolean check = storeService.checkPassword(userDetails.getPassword(), enteredPassword);
        log.info(String.valueOf(check));
        return ResponseEntity.ok(check);
    }

}
