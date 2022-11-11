package com.example.template.data.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.template.domain.Event
import com.example.template.ui.list.EventListAdapter


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, list: List<Event>?) {
    val adapter = recyclerView.adapter as EventListAdapter
    adapter.submitList(list)
}
