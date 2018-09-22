package com.booboot.vndbandroid.ui.vnlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.getThemeColor
import com.booboot.vndbandroid.extensions.hideOnBottom
import com.booboot.vndbandroid.extensions.openVN
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.VNLIST
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.vn_card.view.*
import kotlinx.android.synthetic.main.vn_list_fragment.*

class VNListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, (View, VN) -> Unit {
    override val layout: Int = R.layout.vn_list_fragment
    private lateinit var viewModel: VNListViewModel
    private lateinit var adapter: VNAdapter
    private var tabValue: Int = 0
    private var listType: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        tabValue = arguments?.getInt(HomeTabsFragment.TAB_VALUE_ARG) ?: -1
        listType = arguments?.getInt(HomeTabsFragment.LIST_TYPE_ARG) ?: VNLIST

        viewModel = ViewModelProviders.of(this).get(VNListViewModel::class.java)
        viewModel.accountData.observe(this, Observer { showVns(it) })
        viewModel.errorData.observe(this, Observer { showError(it) })
        update(false)

        return rootView
    }

    fun update(force: Boolean = true) = viewModel.getVns(listType, tabValue, force)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = context ?: return
        adapter = VNAdapter(this)
        vnList.layoutManager = GridAutofitLayoutManager(context, Pixels.px(300))
        vnList.adapter = adapter
        vnList.hideOnBottom(home()?.floatingSearchButton)

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeColors(context.getThemeColor(R.attr.colorAccent))
        showLoading(home()?.isLoading() == true)
    }

    private fun showVns(accountItems: AccountItems?) {
        if (accountItems == null) return
        adapter.filterString = home()?.savedFilter ?: ""
        adapter.items = accountItems
    }

    fun filter(search: CharSequence) = adapter.filter.filter(search)

    override fun invoke(itemView: View, vn: VN) {
        activity?.openVN(vn, itemView.image)
    }

    override fun onRefresh() {
        home()?.startupSync()
    }
}