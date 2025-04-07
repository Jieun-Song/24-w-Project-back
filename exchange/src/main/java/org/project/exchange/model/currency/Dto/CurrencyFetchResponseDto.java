package org.project.exchange.model.currency.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@Slf4j
public class CurrencyFetchResponseDto {
    @JsonProperty("result")
    private int result; // 결과 코드

    @JsonProperty("cur_unit")
    private String curUnit; // 통화 코드

    @JsonProperty("ttb")
    private String ttb; // 전신환(송금) 받으실 때

    @JsonProperty("tts")
    private String tts; // 전신환(송금) 보내실 때

    @JsonProperty("deal_bas_r")
    private String dealBasR; // 매매 기준율

    @JsonProperty("bkpr")
    private String bkpr; // 장부가격

    @JsonProperty("yy_efee_r")
    private String yyEfeeR; // 년환가료율

    @JsonProperty("ten_dd_efee_r")
    private String tenDdEfeeR; // 10일환가료율

    @JsonProperty("kftc_bkpr")
    private String kftcBkpr; // 서울외국환중개 장부가격

    @JsonProperty("kftc_deal_bas_r")
    private String kftcDealBasR; // 서울외국환중개 매매기준율

    @JsonProperty("cur_nm")
    private String curNm; // 통화명
}
