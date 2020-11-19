package com.amrdeveloper.askme.net

import androidx.paging.PagedList

object PagingConfig {

    private val config : PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(DEFAULT_QUERY_PAGE_SIZE)
        .build()

    fun getConfig() = config
}