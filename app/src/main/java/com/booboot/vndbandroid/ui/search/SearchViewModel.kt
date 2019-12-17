package com.booboot.vndbandroid.ui.search

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import javax.inject.Inject

class SearchViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var vnRepository: VNRepository

    val searchData: MutableLiveData<AccountItems> = MutableLiveData()

    var currentPage = -1

    init {
        (application as App).appComponent.inject(this)
    }

    fun getTabTitles(query: String) = coroutine(JOB_SEARCH) {
        /* Getting account items so we can show whether the results are in the user's lists */
        val items = accountRepository.getItems(this).await()
        // TODO vnRepository.search()
    }

    override fun restoreState(state: Bundle?) {
        super.restoreState(state)
        state ?: return
        currentPage = state.getInt(CURRENT_PAGE)
    }

    override fun saveState(state: Bundle) {
        super.saveState(state)
        state.putInt(CURRENT_PAGE, currentPage)
    }

    companion object {
        private const val JOB_SEARCH = "JOB_SEARCH"
        private const val CURRENT_PAGE = "CURRENT_PAGE"
    }
}