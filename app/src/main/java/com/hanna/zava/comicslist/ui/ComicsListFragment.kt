package com.hanna.zava.comicslist.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.hanna.zava.comicslist.OpenForTesting
import com.hanna.zava.comicslist.R
import com.hanna.zava.comicslist.databinding.FragmentComicListBinding
import com.hanna.zava.comicslist.datasource.network.Status
import com.hanna.zava.comicslist.di.DaggerInjectHelper
import com.hanna.zava.comicslist.extensions.provideViewModel
import com.hanna.zava.comicslist.extensions.viewBinding
import com.hanna.zava.comicslist.util.ViewStates
import com.hanna.zava.comicslist.viewmodel.ComicsViewModel
import com.hanna.zava.comicslist.viewmodel.ComicsViewModelFactory
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

//Prototypes - V
//Tests - V
@OpenForTesting
class ComicsListFragment : Fragment(R.layout.fragment_comic_list) {

    @Inject
    lateinit var factory: ComicsViewModelFactory
    val viewModel: ComicsViewModel by provideViewModel { factory }
    private val binding: FragmentComicListBinding by viewBinding(FragmentComicListBinding::bind)
    var comicsListAdapter = ComicListAdapter(::onComicClicked)
    val viewStates: ViewStates by lazy {
        ViewStates(requireView())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.comicsList.adapter = comicsListAdapter
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.comicsList().collect { resource ->
                resource.data?.let { comicList ->
                    comicsListAdapter.submitList(comicList)
                    viewStates.setState(ViewStates.State.MAIN)
                } ?: run {
                    val state = when (resource.status) {
                        Status.LOADING -> ViewStates.State.LOADING.takeIf { resource.data == null }
                            ?: ViewStates.State.MAIN
                        Status.SUCCESS -> ViewStates.State.MAIN
                        Status.ERROR -> ViewStates.State.ERROR
                    }
                    viewStates.setState(state)
                }
            }
        }
    }

    private fun onComicClicked(id: Int) {
        ComicDetailsDialog.newInstance(id)
            .show(parentFragmentManager, ComicDetailsDialog::class.java.name)
    }
}