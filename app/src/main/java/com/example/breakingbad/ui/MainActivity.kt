package com.example.breakingbad.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.example.breakingbad.databinding.ActivityMainBinding
import com.example.breakingbad.paging.adapter.CharacterLoadStateAdapter
import com.example.breakingbad.paging.adapter.CharactersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val charactersAdapter by lazy { CharactersAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        bindViewModel()
    }

    private fun bindViewModel() {

        viewModel.run {
            viewModel.getCharacters().observe(this@MainActivity) {
                charactersAdapter.submitData(this@MainActivity.lifecycle, it)
            }
        }
    }

    private fun initViews() {
        binding.run {
            rvCharacters.setHasFixedSize(true)
            rvCharacters.itemAnimator = null
            rvCharacters.adapter = charactersAdapter.withLoadStateHeaderAndFooter(
                header = CharacterLoadStateAdapter { charactersAdapter.retry() },
                footer = CharacterLoadStateAdapter { charactersAdapter.retry() },
            )

            buttonRetry.setOnClickListener{
                charactersAdapter.retry()
            }

            charactersAdapter.addLoadStateListener { loadState ->
                //refreshing list with new data -> show/hide progressBar
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                //loading is finished without error -> show/hide rv
                rvCharacters.isVisible =  loadState.source.refresh is LoadState.NotLoading
                //error happened -> show/hide retry button , errorMsg
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading //no error
                    && loadState.append.endOfPaginationReached  //no more data
                    && charactersAdapter.itemCount < 1 //empty data set
                ) {
                    //hide rv and show emptyMsg
                    rvCharacters.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    //hide emptyMsg
                    textViewEmpty.isVisible = false

                }
            }
        }
    }
}