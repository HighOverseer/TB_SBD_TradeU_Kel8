package com.example.tradeu.ui.mainpage.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeu.R
import com.example.tradeu.data.entities.relations.ItemAndFavoriteStatus
import com.example.tradeu.databinding.ListItemBarangJualBinding
import com.example.tradeu.formatBalanceString
import com.example.tradeu.loadImage
import com.example.tradeu.ui.mainpage.domain.OnItemClickListener
import com.example.tradeu.ui.productdetail.ProductDetailActivity

class ListItemAdapter(
    private val listItems:List<ItemAndFavoriteStatus>,
    private val onItemClickListener: OnItemClickListener<ItemAndFavoriteStatus>
):RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    class ListItemViewHolder(val binding:ListItemBarangJualBinding, clickedAtPosition:(Int, ActivityOptionsCompat) -> Unit, btnFavoriteAction:(Int, Boolean) -> Unit):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                clickedAtPosition(adapterPosition, getSharedElementTransition())
            }
            binding.ibFavorite.setOnClickListener {
                val isFavorite = binding.ibFavorite.isActivated
                btnFavoriteAction(adapterPosition, isFavorite)
            }
        }

        private fun getSharedElementTransition(): ActivityOptionsCompat {
            return ActivityOptionsCompat.makeSceneTransitionAnimation(
                itemView.context as Activity,
                Pair(binding.ivBarangg, ProductDetailActivity.SHARED_ELEMENT),
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBarangJualBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val clickedAtPosition:(Int, ActivityOptionsCompat) -> Unit = {adapterPosition, sharedElementTransition ->
            onItemClickListener.onClick(listItems[adapterPosition], sharedElementTransition)
        }
        val btnFavoriteAction:(Int, Boolean) -> Unit = { adapterPosition, isFavorite ->
            onItemClickListener.updateFavoriteStatus(listItems[adapterPosition].itemId, isFavorite)
        }
        return ListItemViewHolder(binding, clickedAtPosition, btnFavoriteAction)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val currentItem = listItems[position]
        holder.binding.apply {
            ivBarangg.loadImage(holder.itemView.context, currentItem.itemPhoto)
            tvName.text = currentItem.itemName
            ibFavorite.isActivated = currentItem.userId != 0L && currentItem.itemid != 0L
            tvPrice.text = holder.itemView.context.getString(R.string.balance_count, formatBalanceString(currentItem.price))
        }
    }

    override fun getItemCount() = listItems.size


}