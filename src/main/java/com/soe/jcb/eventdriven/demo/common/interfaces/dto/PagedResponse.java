package com.soe.jcb.eventdriven.demo.common.interfaces.dto;

import com.soe.jcb.eventdriven.demo.common.domain.PageResult;

import java.util.List;

/**
 * Web-layer pagination envelope.
 *
 * <p>Converted from the framework-independent {@link PageResult} produced by
 * application use-cases, so the web shape is decoupled from how paging is
 * materialised.
 */
public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PagedResponse<T> from(PageResult<T> page) {
        return new PagedResponse<>(
                page.content(),
                page.page(),
                page.size(),
                page.totalElements(),
                page.totalPages()
        );
    }
}