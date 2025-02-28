package org.project.exchange.model.product.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImgContent {
    private String type;
    private Img image_url;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Img {
        private String url;
    }

}
