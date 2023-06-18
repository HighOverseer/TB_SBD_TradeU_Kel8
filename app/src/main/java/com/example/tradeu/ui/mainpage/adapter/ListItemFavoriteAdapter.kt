package com.example.tradeu.ui.mainpage.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeu.R
import com.example.tradeu.data.entities.relations.ItemAndFavorite
import com.example.tradeu.databinding.ListItemFavoriteBinding
import com.example.tradeu.formatBalanceString
import com.example.tradeu.loadImage
import com.example.tradeu.ui.mainpage.domain.OnItemClickListener
import com.example.tradeu.ui.productdetail.ProductDetailActivity

class ListItemFavoriteAdapter(
    private val listItemFavorite:List<ItemAndFavorite>,
    private val onItemClickListener: OnItemClickListener<ItemAndFavorite>
):RecyclerView.Adapter<ListItemFavoriteAdapter.ListItemFavoriteViewHolder>(){
    class ListItemFavoriteViewHolder(val binding:ListItemFavoriteBinding, clickedAtPosition:(Int, ActivityOptionsCompat)->Unit, unfavoriteItemAtPosition:(Int)->Unit?):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                clickedAtPosition(adapterPosition, getSharedElementTransition())
            }
            binding.ibDeleteFavorite.setOnClickListener {
                unfavoriteItemAtPosition(adapterPosition)
            }
        }

        private fun getSharedElementTransition(): ActivityOptionsCompat {
            return ActivityOptionsCompat.makeSceneTransitionAnimation(
                itemView.context as Activity,
                Pair(binding.ivBarang, ProductDetailActivity.SHARED_ELEMENT),
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListItemFavoriteViewHolder {
        val binding = ListItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val clickedAtPosition = {adapterPosition:Int, sharedElementTransition:ActivityOptionsCompat ->
            onItemClickListener.onClick(listItemFavorite[adapterPosition], sharedElementTransition)
        }
        val unfavoriteItemAtPosition = {adapterPosition:Int ->
            listItemFavorite[adapterPosition].itemAndUser.item.itemId?.let {
                onItemClickListener.updateFavoriteStatus(
                    it, true)
            }
        }
        return ListItemFavoriteViewHolder(binding, clickedAtPosition, unfavoriteItemAtPosition)
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