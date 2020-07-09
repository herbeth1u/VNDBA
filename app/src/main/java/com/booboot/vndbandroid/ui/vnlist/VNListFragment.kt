package com.booboot.vndbandroid.ui.vnlist

import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.fastScroll
import com.booboot.vndbandroid.extensions.getThemeColor
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.extensions.postponeEnterTransitionIfExists
import com.booboot.vndbandroid.extensions.removeFocus
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toggleBottomSheet
import com.booboot.vndbandroid.model.vndbandroid.FilterData
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SORT_ID
import com.booboot.vndbandroid.model.vndbandroid.SORT_LENGTH
import com.booboot.vndbandroid.model.vndbandroid.SORT_POPULARITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_PRIORITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_RATING
import com.booboot.vndbandroid.model.vndbandroid.SORT_RELEASE_DATE
import com.booboot.vndbandroid.model.vndbandroid.SORT_STATUS
import com.booboot.vndbandroid.model.vndbandroid.SORT_TITLE
import com.booboot.vndbandroid.model.vndbandroid.SORT_VOTE
import com.booboot.vndbandroid.model.vndbandroid.VnlistData
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.filters.FilterCheckboxItem
import com.booboot.vndbandroid.ui.filters.SortItem
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.filter_bottom_sheet.*
import kotlinx.android.synthetic.main.floating_search_toolbar.*
import kotlinx.android.synthetic.main.floating_search_toolbar.view.*
import kotlinx.android.synthetic.main.vnlist_fragment.*

class VNListFragment : BaseFragment<VNListViewModel>(), SwipeRefreshLayout.OnRefreshListener {
    override val layout: Int = R.layout.vnlist_fragment
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val adapter by lazy { VNAdapter(::onVnClicked) }
    private val filtersAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(VNListViewModel::class.java)
        filtersAdapter += viewModel.sortGroup
        filtersAdapter += viewModel.filterGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        setupStatusBar(true, searchBarCardView)
        setupToolbar()
        floatingSearchBar.setupWithContainer(constraintLayout, vnList)

        viewModel.restoreState(savedInstanceState)
        viewModel.vnlistData.observeNonNull(this, ::showVns)
        viewModel.filterData.observeNonNull(this, ::showFilters)
        viewModel.scrollToTopData.observeOnce(this) { if (it) scrollToTop() }
        viewModel.errorData.observeOnce(this, ::showError)
        home()?.viewModel?.accountData?.observeNonNull(this) { update() }
        home()?.viewModel?.loadingData?.observeNonNull(this, ::showLoading)

        if (viewModel.vnlistData.value != null) {
            postponeEnterTransitionIfExists()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.onStateChanged(
            onCollapsed = {
                removeFocus()
                iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp)
            },
            onExpanding = { iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp) }
        )

        floatingSearchBar.setTextChangedListener { viewModel.getVns(_filter = it, scrollToTop = floatingSearchBar.searchBar.hasFocus()) }
        bottomSheetHeader.setOnClickListener { bottomSheet.toggleBottomSheet() }

        adapter.onUpdate = ::onAdapterUpdate
        vnList.setHasFixedSize(true)
        vnList.layoutManager = GridAutofitLayoutManager(activity, Pixels.px(300))
        vnList.adapter = adapter
        vnList.fastScroll()

        backgroundInfo.setButtonOnClickListener { findNavController().navigate(R.id.searchFragment) }
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeColors(activity.getThemeColor(R.attr.colorAccent))

        filters.layoutManager = FlexboxLayoutManager(activity).apply {
            alignItems = AlignItems.FLEX_START
            justifyContent = JustifyContent.FLEX_START
        }
        filters.addItemDecoration(object : FlexboxItemDecoration(activity) {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) =
                if (view.id != R.id.filterButton) {
                    outRect.set(0, 0, 0, 0)
                } else {
                    super.getItemOffsets(outRect, view, parent, state)
                }
        }.apply {
            setDrawable(ContextCompat.getDrawable(activity, R.drawable.flexbox_divider_8dp))
            setOrientation(FlexboxItemDecoration.VERTICAL)
        })
        filters.adapter = filtersAdapter
    }

    private fun update() = viewModel.getVns(scrollToTop = false)

    private fun showVns(vnlistData: VnlistData) {
        adapter.sort = Preferences.sort
        adapter.data = vnlistData

        view?.post {
            startPostponedEnterTransition()
        }
    }

    private fun showFilters(filterData: FilterData) {
        viewModel.sortSection.update(listOf(
            FilterCheckboxItem(-3, R.string.reverse_order, filterData.reverseSort).apply {
                onClicked = { viewModel.getVns(_reverseSort = !selected) }
            }
        ) + listOf(
            SortItem(SORT_ID, R.string.id, filterData.sort == SORT_ID),
            SortItem(SORT_TITLE, R.string.title, filterData.sort == SORT_TITLE),
            SortItem(SORT_RELEASE_DATE, R.string.release_date, filterData.sort == SORT_RELEASE_DATE),
            SortItem(SORT_LENGTH, R.string.length, filterData.sort == SORT_LENGTH),
            SortItem(SORT_POPULARITY, R.string.popularity, filterData.sort == SORT_POPULARITY),
            SortItem(SORT_RATING, R.string.rating, filterData.sort == SORT_RATING),
            SortItem(SORT_STATUS, R.string.status, filterData.sort == SORT_STATUS),
            SortItem(SORT_VOTE, R.string.vote, filterData.sort == SORT_VOTE),
            SortItem(SORT_PRIORITY, R.string.priority, filterData.sort == SORT_PRIORITY)
        ).onEach { sortItem ->
            sortItem.onSortClicked = ::onSortClicked
        })
    }

    override fun onRefresh() {
        home()?.startupSync()
    }

    override fun scrollToTop() {
        vnList.scrollToPosition(0)
        floatingSearchBar.setExpanded(true, true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.vnlist_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> findNavController().navigate(R.id.searchFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onSortClicked(item: SortItem) {
        viewModel.getVns(_sort = item.id)
    }
}