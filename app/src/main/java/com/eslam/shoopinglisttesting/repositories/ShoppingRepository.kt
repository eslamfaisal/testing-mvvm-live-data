package com.eslam.shoopinglisttesting.repositories

import androidx.lifecycle.LiveData

import com.eslam.shoopinglisttesting.data.Resource
import com.eslam.shoopinglisttesting.data.local.ShoppingItem
import com.eslam.shoopinglisttesting.data.remote.responses.ImageResponse

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}