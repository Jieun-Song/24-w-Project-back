package org.project.exchange.model.photo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.exchange.model.product.Product;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "photo") // 사진 테이블
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", nullable = false) // 사진 ID
    private Long photoId;

    @Column(name = "location", nullable = false, length = 255) // 사진 저장 경로
    private String location;

    @OneToOne(mappedBy = "photo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Product product; // 연결된 상품

    @Builder
    public Photo(Long photoId, String location, Product product) {
        this.photoId = photoId;
        this.location = location;
        this.product = product;
    }
}
