package com.nikolam.book_overview.folders_overview.presenter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikolam.book_overview.R
import com.nikolam.book_overview.folder_chooser.di.storageModule
import com.nikolam.book_overview.folder_chooser.di.viewModelModule
import com.nikolam.book_overview.folder_chooser.presenter.OperationMode
import com.nikolam.common.observe
import kotlinx.android.synthetic.main.folders_overview_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FoldersOverviewFragment : Fragment(), FolderOverviewItemListener {


    private val viewModel: FoldersOverviewViewModel by viewModel()

    private lateinit var adapter: FoldersOverviewAdapter

    lateinit var callback: OnBackPressedCallback

    private val stateObserver = Observer<FoldersOverviewViewModel.ViewState> {
        adapter.newData(it.files)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.folders_overview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FoldersOverviewAdapter(requireContext(), this)

        folders_overview_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        folders_overview_recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        folders_overview_recyclerView.adapter = adapter

        addAsSingle.setOnClickListener {
            viewModel.startFolderChooserWithOperationMode(OperationMode.SINGLE_BOOK)
        }
        addAsLibrary.setOnClickListener {
            viewModel.startFolderChooserWithOperationMode(OperationMode.COLLECTION_BOOK)
        }

        addAsSingle.setIconDrawable(requireContext().getDrawable(R.drawable.ic_folder)!!
            .tinted(Color.WHITE))
        addAsLibrary.setIconDrawable(requireContext().getDrawable(R.drawable.ic_album)!!
            .tinted(Color.WHITE))
        addAsSingle.title = "Single book e.g. Harry Potter 4"
        addAsLibrary.title = "Folder with multiple books e.g. AudioBooks"

        observe(viewModel.stateLiveData, stateObserver)
        viewModel.loadData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(listOf(viewModelModule, storageModule))
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(listOf(viewModelModule, storageModule))
    }

    override fun deleteFolder(folder: String) {
        viewModel.deleteFolder(folder)
    }

}


private fun Drawable.tinted(@ColorInt color: Int): Drawable = mutate().apply { setTint(color) }
