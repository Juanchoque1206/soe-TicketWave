package com.soe.jcb.eventdriven.demo.common.domain;

import java.util.List;

/**
 * A framework-independent pagination result returned by application use-cases.
 *
 * <p>Kept in {@code common.domain} (pure Java, no Spring) so that the
 * application layer does not depend on Spring Data's {@code Page} types. The
 * web adapter is responsible for converting this into an API response.
 */
public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}