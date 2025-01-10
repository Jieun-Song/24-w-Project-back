package org.project.exchange.global.api;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageResponse<T> {
    private int numOfRows;
    private int pageNo;
    private int totalCount;
    private List<T> content;

    public PageResponse(int numOfRows, int pageNo, int totalCount, List<T> content) {
        this.numOfRows = numOfRows;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
        this.content = content;
    }
}
