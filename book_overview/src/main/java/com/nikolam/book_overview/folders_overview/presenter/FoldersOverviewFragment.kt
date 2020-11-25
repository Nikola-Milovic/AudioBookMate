package com.nikolam.book_overview.folders_overview.presenter

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikolam.book_overview.R
import com.nikolam.book_overview.folder_chooser.di.storageModule
import com.nikolam.book_overview.folder_chooser.di.viewModelModule
import com.nikolam.book_overview.folder_chooser.presenter.FolderChooserAdapter
import com.nikolam.book_overview.folder_chooser.presenter.FolderChooserViewModel
import com.nikolam.book_overview.folder_chooser.presenter.OperationMode
import com.nikolam.book_overview.misc.observe
import kotlinx.android.synthetic.main.folder_chooser_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FoldersOverviewFragment : Fragment() {


    private val viewModel: FoldersOverviewViewModel by viewModel()

    private lateinit var adapter: FolderChooserAdapter

    lateinit var callback : OnBackPressedCallback

    private val stateObserver = Observer<FoldersOverviewViewModel.ViewState> {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.folder_chooser_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.stateLiveData, stateObserver)
        viewModel.loadData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadKoinModules(listOf(viewModelModule, storageModule))
    }

    override fun onDetach() {
        super.onDetach()
        unloadKoinModules(listOf(viewModelModule, storageModule))
    }

}