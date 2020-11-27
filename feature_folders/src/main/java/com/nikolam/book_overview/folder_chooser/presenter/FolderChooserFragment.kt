package com.nikolam.book_overview.folder_chooser.presenter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikolam.book_overview.R
import com.nikolam.book_overview.folder_chooser.di.storageModule
import com.nikolam.book_overview.folder_chooser.di.viewModelModule
import com.nikolam.book_overview.misc.observe
import kotlinx.android.synthetic.main.folder_chooser_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules


class FolderChooserFragment : Fragment() {

    private val viewModel: FolderChooserViewModel by viewModel()

    private lateinit var adapter: FolderChooserAdapter

    lateinit var callback : OnBackPressedCallback

    val args: FolderChooserFragmentArgs by navArgs()

    private val stateObserver = Observer<FolderChooserViewModel.ViewState> {
        adapter.newData(it.files)
        choose_button.isEnabled = !it.isError
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(listOf(viewModelModule, storageModule))
        viewModel.setOperationMode(OperationMode.valueOf(args.operationMode))
        // This callback will only be called when MyFragment is at least Started.
        callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.folder_chooser_fragment, container, false)
    }

    fun onBackPressed() {
        callback.isEnabled = viewModel.backConsumed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FolderChooserAdapter(requireContext(), OperationMode.COLLECTION_BOOK) { file ->
            viewModel.fileSelected(file)
        }

        folder_chooser_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        folder_chooser_recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        choose_button.setOnClickListener{
            viewModel.fileChosen()
//            val fm = requireActivity()
//                .supportFragmentManager.popBackStackImmediate()
        }

        abort_button.setOnClickListener {

        }

        folder_chooser_recyclerView.adapter = adapter

        observe(viewModel.stateLiveData, stateObserver)
        viewModel.loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(listOf(viewModelModule, storageModule))
    }

}