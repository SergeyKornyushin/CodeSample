package com.yusmp.presentation.common.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.yusmp.presentation.common.models.Identity

abstract class GenericListAdapter<T : Identity<*>, VH : BindableViewHolder<T>>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {

    abstract fun createViewHolder(parent: ViewGroup): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position), null)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        holder.onBind(getItem(position), payloads.firstOrNull())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createViewHolder(parent)
    }
}