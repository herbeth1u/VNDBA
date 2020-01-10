package com.booboot.vndbandroid.ui.vnlist

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SORT_LENGTH
import com.booboot.vndbandroid.model.vndbandroid.SORT_POPULARITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_PRIORITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_RATING
import com.booboot.vndbandroid.model.vndbandroid.SORT_RELEASE_DATE
import com.booboot.vndbandroid.model.vndbandroid.SORT_STATUS
import com.booboot.vndbandroid.model.vndbandroid.SORT_TITLE
import com.booboot.vndbandroid.model.vndbandroid.SORT_VOTE
import com.booboot.vndbandroid.model.vndbandroid.SortOptions
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

class VNListViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val vnlistData: MutableLiveData<AccountItems> = MutableLiveData()
    val sortData: MutableLiveData<Preferences> = MutableLiveData()

    var sortBottomSheetState = BottomSheetBehavior.STATE_HIDDEN
    var filteredVns: AccountItems? = null

    init {
        (application as App).appComponent.inject(this)
    }

    fun getVns(force: Boolean = true) = coroutine(JOB_GET_VNS, !force && vnlistData.value != null) {
        /* #122 : to make DiffUtil work in the Adapter, the items must be deep copied here so the contents can be identified as different when changed from inside the app */
        val accountItems = accountRepository.getItems().deepCopy()

        val sorter: (Pair<Long, VN>) -> Comparable<*>? = { (id, vn) ->
            when (Preferences.sort) {
                SORT_TITLE -> vn.title
                SORT_RELEASE_DATE -> vn.released
                SORT_LENGTH -> vn.length
                SORT_POPULARITY -> vn.popularity
                SORT_RATING -> vn.rating
                SORT_STATUS -> vn.title // TODO reimplement it properly
                SORT_VOTE -> vn.title // TODO reimplement it properly
                SORT_PRIORITY -> vn.title // TODO reimplement it properly
                else -> id
            }
        }

        accountItems.vns = accountItems.vns
            .toList()
            .sortedWith(if (Preferences.reverseSort) compareByDescending(sorter) else compareBy(sorter))
            .toMap()

        vnlistData += accountItems
    }

    fun setSort(@SortOptions sort: Int) {
        Preferences.sort = sort
        sortData.value = Preferences
    }

    fun reverseSort() {
        Preferences.reverseSort = !Preferences.reverseSort
        sortData.value = Preferences
    }

    override fun restoreState(state: Bundle?) {
        super.restoreState(state)
        state ?: return
        sortBottomSheetState = state.getInt(SORT_BOTTOM_SHEET_STATE)
    }

    override fun saveState(state: Bundle) {
        super.saveState(state)
        state.putInt(SORT_BOTTOM_SHEET_STATE, sortBottomSheetState)
    }

    companion object {
        private const val JOB_GET_VNS = "JOB_GET_VNS"
        private const val SORT_BOTTOM_SHEET_STATE = "SORT_BOTTOM_SHEET_STATE"
    }
}