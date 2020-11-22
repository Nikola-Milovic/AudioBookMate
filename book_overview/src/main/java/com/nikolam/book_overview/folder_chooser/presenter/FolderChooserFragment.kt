package com.nikolam.book_overview.folder_chooser.presenter


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikolam.book_overview.R
import com.nikolam.book_overview.folder_chooser.di.storageModule
import com.nikolam.book_overview.folder_chooser.di.viewModelModule
import com.nikolam.book_overview.misc.observe
import com.nikolam.book_overview.misc.PermissionHelper
import com.nikolam.book_overview.misc.Permissions
import kotlinx.android.synthetic.main.folder_chooser_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


class FolderChooserFragment : Fragment() {

    private val viewModel: FolderChooserViewModel by viewModel()

    private lateinit var adapter: FolderChooserAdapter

    private val stateObserver = Observer<FolderChooserViewModel.ViewState> {
        adapter.newData(it.files)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.folder_chooser_fragment, container, false)
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

        folder_chooser_recyclerView.adapter = adapter

        observe(viewModel.stateLiveData, stateObserver)
        viewModel.loadData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadKoinModules(listOf(viewModelModule, storageModule))
    }


}