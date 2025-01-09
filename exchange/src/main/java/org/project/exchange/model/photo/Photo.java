package org.project.exchange.model.photo;

import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.product.Product;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "사진")
public class Photo {

    @Id
    @Column(name = "사진_id", length = 255)
    private String photoId;

    @Column(name = "사진_location", length = 255)
    private String photoLocation;

    @OneToOne(mappedBy = "photo", fetch = FetchType.LAZY)
    private Product product;
}
