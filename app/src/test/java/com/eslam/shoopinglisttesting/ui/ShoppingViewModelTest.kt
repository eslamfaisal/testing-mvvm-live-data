package com.eslam.shoopinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.eslam.shoopinglisttesting.MainDispatcherRule
import com.eslam.shoopinglisttesting.data.Status
import com.eslam.shoopinglisttesting.getOrAwaitValue
import com.eslam.shoopinglisttesting.repositories.FakeShoppingRepositoryAndroidTest
import com.eslam.shoopinglisttesting.utils.Constants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShoppingViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: FakeShoppingRepositoryAndroidTest

    @Mock
    private lateinit var observer: Observer<String>

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(repository)
    }

    @Test
    fun `insert shopping item with empty field, return err`() {
        viewModel.insertShoppingItem("eslam", "", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name, return err`() {
        val string = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(string, "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too price, return err`() {
        val string = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem("name", "5", string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, return err`() {
        viewModel.insertShoppingItem("name", "9999989898999999989898998989", "3.2")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid data, return success`() {
        viewModel.insertShoppingItem("name", "5", "3.2")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `curImageUrl should be empty initially`() {
        assertEquals("", viewModel.curImageUrl.value)
    }

    @Test
    fun `setCurImageUrl should update curImageUrl`() {
        val newUrl = "https://example.com/image.jpg"
        viewModel.setCurImageUrl(newUrl)
        assertEquals(newUrl, viewModel.curImageUrl.value)
    }

    @Test
    fun `setting new value for curImageUrl triggers observer`() {
        viewModel.curImageUrl.observeForever(observer)
        val newUrl = "https://example.com/image.jpg"
        viewModel.setCurImageUrl(newUrl)
        Mockito.verify(observer).onChanged(newUrl)
    }

    @Test
    fun `setting same value for curImageUrl does not trigger observer`() {
        viewModel.curImageUrl.observeForever(observer)
        val newUrl = "https://example.com/image.jpg"
        viewModel.setCurImageUrl(newUrl)
        viewModel.setCurImageUrl(newUrl)
        Mockito.verify(observer, Mockito.times(2)).onChanged(newUrl)
    }

    @Test
    fun `repository should not be accessed directly by viewModel`() {
        val newUrl = "https://example.com/image.jpg"
        viewModel.setCurImageUrl(newUrl)
        Mockito.verifyNoInteractions(repository)
    }

}