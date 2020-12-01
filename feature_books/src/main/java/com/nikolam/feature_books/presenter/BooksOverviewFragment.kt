package com.nikolam.feature_books.presenter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikolam.feature_books.R

class BooksOverviewFragment : Fragment() {

    companion object {
        fun newInstance() = BooksOverviewFragment()
    }

    private lateinit var viewModel: BooksOverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.books_overview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BooksOverviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}