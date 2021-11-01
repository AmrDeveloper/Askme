package com.amrdeveloper.askme.data.source.remote.net

import androidx.paging.PagedList
import com.amrdeveloper.askme.data.DEFAULT_QUERY_PAGE_SIZE

object PagingConfig {

    private val config : PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(DEFAULT_QUERY_PAGE_SIZE)
        .build()

    fun getConfig() = config
}