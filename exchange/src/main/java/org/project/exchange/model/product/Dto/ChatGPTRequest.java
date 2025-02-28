package org.project.exchange.model.product.Dto;

import lombok.Data;
import org.project.exchange.model.product.ai.GptRequestMessage;
import org.project.exchange.model.product.ai.ImgContent;
import org.project.exchange.model.product.ai.TextContent;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<GptRequestMessage> messages;

    public ChatGPTRequest(String model, String base64image) {
        this.model = model;
        this.messages = new ArrayList<>();
        List<Object> list = List.of(new ImgContent("image_url",new ImgContent.Img("data:image/jpeg;base64," + base64image)), new TextContent("text", "다음 이미지에서 상품명과 가격만 추출해줘. 예시는 다음과 같아. 그리고 이미지에 알맞게 틀린 단어가 되지 않게 추출해줘. \n" +
                "바라스윗 바닐라파인트 474 - 6,900원\n" +
                "바라스윗 초코파인트 474ml - 6,900원\n" +
                "비닐봉투 - 20원"));
        this.messages.add(new GptRequestMessage("user", list));
    }
}
