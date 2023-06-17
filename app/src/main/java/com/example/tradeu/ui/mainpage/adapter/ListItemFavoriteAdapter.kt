package com.example.tradeu.ui.mainpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeu.R
import com.example.tradeu.data.entities.relations.ItemAndFavorite
import com.example.tradeu.data.entities.relations.ItemUserAndFavorite
import com.example.tradeu.databinding.ListItemFavoriteBinding
import com.example.tradeu.formatBalanceString
import com.example.tradeu.loadImage
import com.example.tradeu.ui.mainpage.domain.OnItemClickListener

class ListItemFavoriteAdapter(
    private val listItemFavorite:List<ItemAndFavorite>,
    private val onItemClickListener: OnItemClickListener<ItemAndFavorite>
):RecyclerView.Adapter<ListItemFavoriteAdapter.ListItemFavoriteViewHolder>(){
    class ListItemFavoriteViewHolder(val binding:ListItemFavoriteBinding, clickedAtPosition:(Int)->Unit):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                clickedAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListItemFavoriteViewHolder {
        val binding = ListItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemFavoriteViewHolder(binding){adapterPosition ->
            onItemClickListener.onClick(listItemFavorite[adapterPosition])
        }
    }

    override fun onBindViewHolder(
        holder: ListItemFavoriteViewHolder,
        position: Int
    ) {
        val currentItem = listItemFavorite[position]
        holder.binding.apply {
            ivBarang.loadImage(holder.itemView.context, currentItem.itemAndUser.item.itemPhoto)
            tvName.text = currentItem.itemAndUser.item.itemName
            tvLocation.text = currentItem.itemAndUser.user.address
            tvPrice.text = holder.itemView.context.getString(R.string.balance_count, formatBalanceString(currentItem.itemAndUser.item.price))
        }
    }

    override fun getItemCount()=listItemFavorite.size


}