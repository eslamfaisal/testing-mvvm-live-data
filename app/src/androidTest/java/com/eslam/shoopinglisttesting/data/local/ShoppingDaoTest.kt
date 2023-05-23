package com.eslam.shoopinglisttesting.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.eslam.shoopinglisttesting.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    val instantTaskExecutorRule = ShoppingFlowDaoTest()


    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context = context, ShoppingItemDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = database.shoppingDao()
    }

    @After
    fun after() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("eslam", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems.contains(shoppingItem))
    }

    @Test
    fun deleteShoppingItemTest() = runTest {
        val shoppingItem = ShoppingItem("eslam", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems.contains(shoppingItem)).isFalse()
    }

}