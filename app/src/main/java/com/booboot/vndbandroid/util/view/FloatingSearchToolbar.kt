package com.booboot.vndbandroid.util.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.booboot.vndbandroid.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import kotlinx.android.synthetic.main.floating_search_toolbar.view.*

class FloatingSearchToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.floating_search_toolbar, this)

        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))

        attrs?.let {
            with(context.obtainStyledAttributes(it, R.styleable.FloatingSearchToolbar)) {
                searchBarTextInputLayout.startIconDrawable = getDrawable(R.styleable.FloatingSearchToolbar_startIcon)
                searchBar.hint = getString(R.styleable.FloatingSearchToolbar_hintText)
                searchBarCardView.updateLayoutParams<LayoutParams> {
                    if (getBoolean(R.styleable.FloatingSearchToolbar_hideOnScroll, false)) {
                        scrollFlags = scrollFlags or SCROLL_FLAG_SCROLL
                    }
                }
                recycle()
            }
        }
    }
}