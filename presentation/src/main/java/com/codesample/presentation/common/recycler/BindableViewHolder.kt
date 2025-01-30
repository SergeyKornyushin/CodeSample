package com.codesample.presentation.common.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.codesample.presentation.common.models.Identity

abstract class BindableViewHolder<T : Identity<*>>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    lateinit var item: T

    protected abstract fun T.bind()

    protected open fun T.applyPayload(payload: Any) = Unit

    fun onBind(item: T, payload: Any? = null) {
        this.item = item
        if (payload == null) {
            item.bind()
        } else {
            item.applyPayload(payload)
        }
    }
}