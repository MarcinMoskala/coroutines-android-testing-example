package com.example.coroutines_android_testing_example.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coroutines_android_testing_example.databinding.MainFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListeners()
        viewModel.onCreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        with(lifecycleScope) {
            launch {
                viewModel.progressBarVisible.collect { shouldBeVisible ->
                    binding.progressView.visibility = if (shouldBeVisible) View.VISIBLE else View.GONE
                }
            }
            launch {
                viewModel.articlesListState.collect { state ->
                    when (state) {
                        ArticlesListState.Initial -> {
                        }
                        is ArticlesListState.ArticlesLoaded -> {
                            val items = state.articles.map(::ArticleItemAdapter)
                            binding.listView.layoutManager = LinearLayoutManager(context)
                            binding.listView.adapter = ArticlesListAdapter(items)
                        }
                    }
                }
            }
        }
    }
}