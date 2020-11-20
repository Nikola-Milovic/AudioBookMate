package com.nikolam.book_overview.folder_chooser.presenter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikolam.book_overview.R

class FolderChooserFragment : Fragment() {

    companion object {
        fun newInstance() = FolderChooserFragment()
    }

    private lateinit var viewModel: FolderChooserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.folder_chooser_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FolderChooserViewModel::class.java)
    }

}