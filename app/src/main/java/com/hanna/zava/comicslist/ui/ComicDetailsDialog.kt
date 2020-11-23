package com.hanna.zava.comicslist.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hanna.zava.comicslist.R
import com.hanna.zava.comicslist.databinding.DialogBaseBinding
import com.hanna.zava.comicslist.di.DaggerInjectHelper
import com.hanna.zava.comicslist.di.modules.GlideApp
import com.hanna.zava.comicslist.extensions.extraNotNull
import com.hanna.zava.comicslist.extensions.provideViewModel
import com.hanna.zava.comicslist.extensions.throttledClickListener
import com.hanna.zava.comicslist.extensions.viewBinding
import com.hanna.zava.comicslist.viewmodel.ComicsViewModel
import com.hanna.zava.comicslist.viewmodel.ComicsViewModelFactory
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import javax.inject.Inject

//Prototypes - V
//Tests - V
class ComicDetailsDialog : BottomSheetDialogFragment() {

    private lateinit var primaryButtonObserver: Disposable

    private val comicId: Int by extraNotNull(COMIC_ID)

    override fun getTheme(): Int = R.style.BaseBottomSheetDialog

    private val binding: DialogBaseBinding by viewBinding(DialogBaseBinding::bind)

    @Inject
    lateinit var factory: ComicsViewModelFactory
    val viewModel: ComicsViewModel by provideViewModel { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val comic = viewModel.getComicById(comicId)
            binding.title.text = comic.title
            binding.description.isVisible = comic.description != null
            binding.description.text =
                comic.description?.let { getString(R.string.description_format, it) }.orEmpty()

            binding.pages.text = getString(R.string.page_count_format, comic.pageCount)
            GlideApp.with(requireContext()).load(comic.thumbnail.fullUrl).into(binding.image)
        }

        primaryButtonObserver =
            binding.primaryButton.throttledClickListener { primaryButtonClicked() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    private fun primaryButtonClicked() {
        dismiss()
    }

    override fun onDestroyView() {
        primaryButtonObserver.dispose()
        super.onDestroyView()
    }

    companion object {
        const val COMIC_ID = "comic_id"
        fun newInstance(id: Int): ComicDetailsDialog {
            return ComicDetailsDialog().apply {
                arguments = bundleOf(COMIC_ID to id)
            }
        }
    }
}