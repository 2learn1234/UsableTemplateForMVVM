package com.example.template.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.template.databinding.TemplateItemBinding
import com.example.template.domain.Event

class EventListAdapter(
    diffCallBack: EventDiffCallback = EventDiffCallback(),
    private val onClick: (Event) -> Unit
) : ListAdapter<Event, EventListAdapter.ViewHolder>(diffCallBack), Filterable {

    inner class ViewHolder(
        private val binding: TemplateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.event = event
            binding.root.setOnClickListener { onClick(event) }
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TemplateItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

    }

    override fun getFilter(): Filter {
        val filter = object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var filterResults = FilterResults()
                if (p0 == null || p0.isEmpty()) {
                    filterResults.values = currentList
                    filterResults.count = currentList.size
                } else {
                    var searchChar = p0.toString()

                    var filteredResults: MutableList<Event> = ArrayList<Event>()
                    for (event in currentList) {
                        if (event.name.contains(searchChar)) {
                            filteredResults.add(event)
                        }
                    }
                    filterResults.values = filteredResults
                    filterResults.count = filterResults.count

                }

                return filterResults

            }

            override fun publishResults(p0: CharSequence?, results: FilterResults) {
                // val eventList = results as List<Event>
                //var submitList2:List<Event>=

                results.let {
                    submitList(results.values as MutableList<Event>)
                }
                notifyDataSetChanged()
            }

        }

        return filter
    }
}