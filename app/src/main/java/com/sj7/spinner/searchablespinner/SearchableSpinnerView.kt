package com.sj7.spinner.searchablespinner

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import com.sj7.spinner.R

class SearchableSpinnerView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0,
                R.drawable.ic_drop_down_arrow_black, 0
            )

        }
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0,
                R.drawable.ic_drop_down_arrow_black, 0
            )

        }
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0,
                R.drawable.ic_drop_down_arrow_black, 0
            )

        }
    }

    private fun initView() {
        this.apply {
            gravity = Gravity.CENTER_VERTICAL
            isSingleLine = true
            compoundDrawablePadding = 5
            ellipsize = TextUtils.TruncateAt.END
        }
    }
}