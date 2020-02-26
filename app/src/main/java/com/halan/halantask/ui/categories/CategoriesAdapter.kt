package com.halan.halantask.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.halan.halantask.data.model.Category
import com.halan.halantask.data.model.Service
import com.halan.halantask.databinding.ItemCategoryBinding
import com.halan.halantask.ui.services.ServicesAdapter

class CategoriesAdapter : ListAdapter<Category, CategoriesAdapter.ViewHolder>(
    CategoriesDiffCallback()){

    private var mListener: OnItemClickListener? = null

    class ViewHolder private constructor(
        private val binding: ItemCategoryBinding,
        private val mListener: OnItemClickListener
    )  : RecyclerView.ViewHolder(binding.root),
        ServicesAdapter.OnItemClickListener {

        fun bind(item: Category) {
            binding.category = item
            binding.rvServices.apply {
                adapter = ServicesAdapter(this@ViewHolder)
                (adapter as ServicesAdapter).submitList(item.services)
                layoutManager = GridLayoutManager(context,2)
            }
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup,mListener: OnItemClickListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding,mListener)
            }
        }
        override fun onItemClick(service: Service) {
            mListener.onItemClick(service)
        }
    }

    class CategoriesDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent,mListener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickListener {
        fun onItemClick(service: Service)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener) {
        this.mListener = mListener
    }
}