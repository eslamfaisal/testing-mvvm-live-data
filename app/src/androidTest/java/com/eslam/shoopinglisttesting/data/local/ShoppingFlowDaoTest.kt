package com.eslam.shoopinglisttesting.data.local


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ShoppingFlowDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: ShoppingItemDatabase
    private lateinit var shoppingDao: ShoppingFlowDao


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()

        shoppingDao = database.shoppingFlowDao()
    }

    @Test
    fun insertShoppingItemTest() = runTest {
        val shoppingItem =
            ShoppingItem("Item 1", 2, 10.0f, "https://example.com/image1.jpg", id = 1)
        shoppingDao.insertShoppingItem(shoppingItem)

        val shoppingItems = shoppingDao.observeAllShoppingItems().first()
        assertThat(shoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItemTest() = runTest {
        val shoppingItem =
            ShoppingItem("Item 1", 2, 10.0f, "https://example.com/image1.jpg", id = 1)
        shoppingDao.insertShoppingItem(shoppingItem)
        shoppingDao.deleteShoppingItem(shoppingItem)

        val shoppingItems = shoppingDao.observeAllShoppingItems().first()
        assertThat(shoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeAllShoppingItemsTest() = runTest {
        val shoppingItem1 =
            ShoppingItem("Item 1", 2, 10.0f, "https://example.com/image1.jpg", id = 1)
        val shoppingItem2 =
            ShoppingItem("Item 2", 1, 15.0f, "https://example.com/image2.jpg", id = 2)

        shoppingDao.insertShoppingItem(shoppingItem1)
        shoppingDao.insertShoppingItem(shoppingItem2)

        val shoppingItems = shoppingDao.observeAllShoppingItems().first()
        assertThat(shoppingItems.size).isEqualTo(2)
        assertThat(shoppingItems).containsExactly(shoppingItem1, shoppingItem2)
    }

    @Test
    fun observeTotalPriceTest() = runTest {
        val shoppingItem1 =
            ShoppingItem("Item 1", 2, 10.0f, "https://example.com/image1.jpg", id = 1)
        val shoppingItem2 =
            ShoppingItem("Item 2", 1, 15.0f, "https://example.com/image2.jpg", id = 2)

        shoppingDao.insertShoppingItem(shoppingItem1)
        shoppingDao.insertShoppingItem(shoppingItem2)

        val totalPrice = shoppingDao.observeTotalPrice().first()
        assertThat(totalPrice).isEqualTo(35.0f)
    }

    @After
    fun tearDown() {
        database.close()
    }
}
