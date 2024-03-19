package study.dhh_admin.domain.store.dto;

import org.springframework.web.multipart.MultipartFile;
import study.dhh_admin.domain.owner.entity.Owner;
import study.dhh_admin.domain.store.entity.Store;


public class StoreRequestDto {

    public record Create(String name, String businessNumber, String address, String description) {

        public Store toEntity(Owner owner, String imageUrl, String originFileName) {
            return Store.builder()
                    .owner(owner)
                    .name(name)
                    .address(address)
                    .businessNumber(businessNumber)
                    .description(description)
                    .originFileName(originFileName)
                    .imageUrl(imageUrl)
                    .build();
        };
    }

    public record Update(String name, String address, String description) {

    }
}
