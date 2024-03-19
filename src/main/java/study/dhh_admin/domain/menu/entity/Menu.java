package study.dhh_admin.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.dhh_admin.domain.store.entity.Store;

@Entity
@Getter
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name ="store_id")
    private Store store;

    @Builder
    public Menu(Long id, String name, int price, String imageUrl, String description, Store store) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.store = store;
    }
}
