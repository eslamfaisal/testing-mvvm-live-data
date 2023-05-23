package com.eslam.shoopinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.eslam.shoopinglisttesting.adapters.ImageAdapter
import com.eslam.shoopinglisttesting.data.Status
import com.eslam.shoopinglisttesting.databinding.FragmentImagePickBinding
import com.eslam.shoopinglisttesting.utils.Constants.GRID_SPAN_COUNT
import com.eslam.shoopinglisttesting.utils.Constants.SEARCH_TIME_DELAY
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
) : Fragment() {

    private var _binding: FragmentImagePickBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePickBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        subscribeToObservers()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageUrl(it)
        }
    }

    private fun subscribeToObservers() {
        viewModel.images.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val urls = result.data?.hits?.map { imageResult -> imageResult.previewURL }
                        imageAdapter.images = urls ?: listOf()
                        binding.progressBar.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        Snackbar.make(
                            binding.root,
                            result.message ?: "An unknown error occured.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.GONE
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}