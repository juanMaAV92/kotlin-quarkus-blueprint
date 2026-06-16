package com.juanmaav.blueprint.core.domain

/**
 * A page of results plus the total count, returned by list queries.
 */
data class Page<T>(
    val items: List<T>,
    val page: Int,
    val limit: Int,
    val total: Long,
)
