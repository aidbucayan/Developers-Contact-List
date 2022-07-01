package com.adrian.bucayan.developercontacts.presentation.ui.util.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T, V: ViewBinding>(
    viewBinding: V
) : RecyclerView.ViewHolder(viewBinding.root) {

    open fun setViews(item: T) {

    }
}