package com.eslam.shoopinglisttesting.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItem::class], version = 1
)
abstract class ShoppingItemDatabase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDao
    abstract fun shoppingFlowDao(): ShoppingFlowDao
}