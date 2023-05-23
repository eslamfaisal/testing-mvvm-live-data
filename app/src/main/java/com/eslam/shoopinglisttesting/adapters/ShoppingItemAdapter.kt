package com.eslam.shoopinglisttesting.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.eslam.shoopinglisttesting.data.local.ShoppingItem
import com.eslam.shoopinglisttesting.databinding.ItemShoppingBinding
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    class ShoppingItemViewHolder(val binding: ItemShoppingBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        return ShoppingItemViewHolder(
            ItemShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        )
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.itemView.apply {
            glide.load(shoppingItem.imageUrl).into(holder.binding.ivShoppingImage)

            holder.binding.tvName.text = shoppingItem.name
            val amountText = "${shoppingItem.amount}x"
            holder.binding.tvShoppingItemAmount.text = amountText
            val priceText = "${shoppingItem.price}€"
            holder.binding.tvShoppingItemPrice.text = priceText
        }
    }
}