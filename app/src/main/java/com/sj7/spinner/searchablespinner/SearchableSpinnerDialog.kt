package com.sj7.spinner.searchablespinner

import android.app.Dialog
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sj7.spinner.R
import com.sj7.spinner.common.Constants
import com.sj7.spinner.common.DialogListener
import com.sj7.spinner.common.Spinner
import com.sj7.spinner.common.SpinnerListener
import com.sj7.spinner.databinding.DialogSpinnerBinding


class SearchableSpinnerDialog {

    private lateinit var dialog: Dialog
    private lateinit var context: Context
    private lateinit var dialogSpinnerBinding: DialogSpinnerBinding
    private lateinit var searchableSpinnerListAdapter: SearchableSpinnerListAdapter
    private lateinit var spinnerListener: SpinnerListener
    private lateinit var searchableSpinnerView: SearchableSpinnerView
    private var list = ArrayList<Spinner>()
    private var lastSelectionIndex: Int = 0
    private var selectedItemIndex: Int = 0
    private lateinit var dialogListener: DialogListener

    fun setSpinnerDialog(
        context: Context,
        searchableSpinnerView: SearchableSpinnerView,
        dialogTitle: String,
        list: ArrayList<Spinner>,
        spinnerListener: SpinnerListener
    ): Dialog {
        this.context = context
        dialog = Dialog(context, R.style.DialogTheme)
        dialogSpinnerBinding = DialogSpinnerBinding.inflate(LayoutInflater.from(context))
        this.spinnerListener = spinnerListener
        this.searchableSpinnerView = searchableSpinnerView
        this.list = list

        /*  textView.apply {
              gravity = Gravity.CENTER_VERTICAL
              isSingleLine = true
              compoundDrawablePadding = 5
              setTextColor(Color.BLACK)
              setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_drop_down_arrow_black,0)
              ellipsize = TextUtils.TruncateAt.END
          }
  */
        for (i in 0 until list.size) {
            list[i].parentId = i
        }

        dialogSpinnerBinding.apply {
            tvTitle.text = dialogTitle
            tvTitle.setTextColor(fetchPrimaryColor())

            dialogListener = object : DialogListener {
                override fun saveChanges() {
                    dialog.dismiss()
                    for (i in 0 until list.size) {
                        if (list[i].isSelected) {
                            selectedItemIndex = i
                            break
                        }
                    }
                    setLastSelectedItemIndex(selectedItemIndex)
                }
            }
            rvList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            searchableSpinnerListAdapter =
                SearchableSpinnerListAdapter(context, list, dialogListener)
            rvList.adapter = searchableSpinnerListAdapter

            searchableSpinnerView.setOnClickListener {
                Log.d(Constants.TAG, "on Load : $lastSelectionIndex")
                show()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(Constants.TAG, "search..")

                    if (!TextUtils.isEmpty(newText)) {
                        val query: String = newText.toString().toLowerCase().trim()
                        val searchList = ArrayList<Spinner>()

                        for (i in 0 until list.size) {
                            if (list[i].name.toLowerCase().contains(query)) {
                                searchList.add(list[i])
                            }
                        }

                        dialogSpinnerBinding.rvList.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        searchableSpinnerListAdapter =
                            SearchableSpinnerListAdapter(context, searchList, dialogListener, true)
                        dialogSpinnerBinding.rvList.adapter = searchableSpinnerListAdapter
                        searchableSpinnerListAdapter.setMainListForSearch(list)
                        //spinnerListAdapter.setSearchList(searchList, list)
                    } else {
                        dialogSpinnerBinding.rvList.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        searchableSpinnerListAdapter =
                            SearchableSpinnerListAdapter(context, list, dialogListener)
                        dialogSpinnerBinding.rvList.adapter = searchableSpinnerListAdapter
                        //spinnerListAdapter.setMainList(list)
                    }
                    return true
                }
            })

            btnCancel.setTextColor(fetchPrimaryColor())
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(dialogSpinnerBinding.root)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setBackgroundDrawableResource(R.drawable.back_dialog_spinner)
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
            setOnDismissListener {
                //setSelection(lastSelectionIndex)
                dialogSpinnerBinding.searchView.setQuery("", true)
            }
        }

        setLastSelectedItemIndex(0)
        return dialog
    }

    fun show() {
        for (i in 0 until list.size) {
            list[i].isSelected = false
        }
        if (list.size >= lastSelectionIndex + 1) {
            list[lastSelectionIndex].isSelected = true
        }

        dialogSpinnerBinding.apply {
            rvList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            searchableSpinnerListAdapter =
                SearchableSpinnerListAdapter(context, list, dialogListener)
            rvList.adapter = searchableSpinnerListAdapter

            //searchView.clearFocus()
        }

        //spinnerListAdapter.setMainList(list)

        dialog.show()
        Log.d(Constants.TAG, "show() - list size ${searchableSpinnerListAdapter.itemCount}")
    }

    fun dismiss() {
        dialog.dismiss()
    }

    private fun setSelection(index: Int) {
        if (list.size >= index + 1) {
            searchableSpinnerView.text = list[index].name
        }
        searchableSpinnerListAdapter.setSelection(index)
        spinnerListener.setOnItemClickListener(index)
    }

    fun setLastSelectedItemIndex(index: Int) {
        lastSelectionIndex = index
        setSelection(index)
    }

    fun getSelectedItemIndex(): Int {
        return lastSelectionIndex
    }

    fun getSelectedItemId(): String {
        return list[lastSelectionIndex].id
    }

    fun getSelectedItemName(): String {
        return list[lastSelectionIndex].name
    }

    fun findSelectedItemIndexByName(selectedItemName: String?) {
        var position = 0
        for (i in 0 until list.size) {
            if (list[i].name == selectedItemName) {
                position = i
                break
            }
        }
        setLastSelectedItemIndex(position)
    }

    fun findSelectedItemIndexById(selectedItemId: String?) {
        var position = 0
        for (i in 0 until list.size) {
            if (list[i].id == selectedItemId) {
                position = i
                break
            }
        }
        setLastSelectedItemIndex(position)
    }

    private fun fetchPrimaryColor(): Int {
        val typedValue = TypedValue()
        val a: TypedArray =
            context.obtainStyledAttributes(typedValue.data, intArrayOf(android.R.attr.colorPrimary))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }
}
