package com.booboot.vndbandroid.ui.vnlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.diff.VNDiffCallback
import com.booboot.vndbandroid.extensions.lowerCase
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.extensions.upperCase
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FilterData
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
import com.booboot.vndbandroid.model.vndbandroid.VnlistData
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.booboot.vndbandroid.ui.filters.FilterTitleItem
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Section
import javax.inject.Inject

class VNListViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val vnlistData = MutableLiveData<VnlistData>()
    val filterData = MutableLiveData<FilterData>()
    val scrollToTopData = MutableLiveData<Boolean>()

    var filter = ""
    val sortGroup: ExpandableGroup
    val sortSection = Section()
    val filterGroup: ExpandableGroup

    init {
        (application as App).appComponent.inject(this)
        sortGroup = ExpandableGroup(FilterTitleItem(-1, R.string.sort_vns_by, R.drawable.ic_sort_24dp), true).apply {
            add(sortSection)
        }
        filterGroup = ExpandableGroup(FilterTitleItem(-2, R.string.filter_vns_by, R.drawable.ic_filter_list_24dp), true)
    }

    fun getVns(
        _filter: String = filter,
        @SortOptions _sort: Long = Preferences.sort,
        _reverseSort: Boolean = Preferences.reverseSort,
        scrollToTop: Boolean = true
    ) {
        filter = _filter.lowerCase()
        Preferences.sort = _sort
        Preferences.reverseSort = _reverseSort

        coroutine(JOB_GET_VNS) {
            /* #122 : to make DiffUtil work in the Adapter, the items must be deep copied here so the contents can be identified as different when changed from inside the app */
            val accountItems = accountRepository.getItems().deepCopy()

            val sorter: Comparator<(Pair<Long, VN>)> = when (Preferences.sort) {
                SORT_TITLE -> compare(nullsLast()) { (_, vn) -> vn.title.trim().upperCase() }
                SORT_RELEASE_DATE -> compare(nullsFirst()) { (_, vn) -> vn.released }
                SORT_LENGTH -> compare(nullsLast()) { (_, vn) -> vn.length }
                SORT_POPULARITY -> compare(nullsLast()) { (_, vn) -> vn.popularity }
                SORT_RATING -> compare(nullsLast()) { (_, vn) -> vn.rating }
                SORT_STATUS -> compare(nullsLast()) { (_, vn) -> accountItems.userList[vn.id]?.firstStatus() }
                SORT_PRIORITY -> compare(nullsLast()) { (_, vn) -> accountItems.userList[vn.id]?.firstWishlist() }
                SORT_VOTE -> compare(nullsFirst()) { (_, vn) -> accountItems.userList[vn.id]?.vote }
                else -> compare(nullsLast()) { (id, _) -> id }
            }

            accountItems.vns = accountItems.vns
                .run {
                    if (filter.isEmpty()) this
                    else filterValues { vn -> vn.title.trim().lowerCase().contains(filter) }
                }
                .toList()
                .sortedWith(sorter)
                .toMap()

            val diffResult = DiffUtil.calculateDiff(VNDiffCallback(vnlistData.value?.items ?: AccountItems(), accountItems))
            vnlistData += VnlistData(accountItems, diffResult)
            filterData += FilterData(_sort, _reverseSort)
            scrollToTopData += scrollToTop
        }
    }

    private fun <T> compare(comparator: Comparator<in T>, sorter: (Pair<Long, VN>) -> T) =
        if (Preferences.reverseSort) compareByDescending(comparator, sorter) else compareBy(comparator, sorter)

    companion object {
        private const val JOB_GET_VNS = "JOB_GET_VNS"
    }
}