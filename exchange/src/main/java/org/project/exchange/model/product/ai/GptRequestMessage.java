package org.project.exchange.model.product.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptRequestMessage {
    private String role;
    private List<Object> content;
}
