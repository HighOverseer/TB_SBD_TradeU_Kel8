package com.example.tradeu.ui.mainpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeu.R
import com.example.tradeu.data.entities.Item
import com.example.tradeu.databinding.ListItemBarangJualBinding
import com.example.tradeu.formatBalanceString
import com.example.tradeu.loadImage
import com.example.tradeu.ui.mainpage.domain.OnItemClickListener

class ListItemAdapter(
    private val listItems:List<Item>,
    private val onItemClickListener: OnItemClickListener<Item>
):RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    class ListItemViewHolder(val binding:ListItemBarangJualBinding, clickedAtPosition:(Int) -> Unit):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                clickedAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBarangJualBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding){adapterPosition ->
            onItemClickListener.onClick(listItems[adapterPosition])
        }
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val currentItem = listItems[position]
        holder.binding.apply {
            ivBarangg.loadImage(holder.itemView.context, currentItem.itemPhoto)
            tvName.text = currentItem.itemName
            tvPrice.text = holder.itemView.context.getString(R.string.balance_count, formatBalanceString(currentItem.price))
        }
    }

    override fun getItemCount() = listItems.size


}