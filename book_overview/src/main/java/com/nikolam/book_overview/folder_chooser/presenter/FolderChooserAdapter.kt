package com.nikolam.book_overview.folder_chooser.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikolam.book_overview.R
import com.nikolam.book_overview.databinding.FolderChooserFolderItemBinding
import com.nikolam.book_overview.misc.bindings
import java.io.File

class FolderChooserAdapter(
    private val c: Context,
    private val mode: OperationMode,
    private val listener: (selected: File) -> Unit
) : RecyclerView.Adapter<FolderChooserAdapter.FolderViewholder>() {

    override fun onBindViewHolder(holder: FolderViewholder, position: Int) {
        val data = data[position]
        try {
            holder.bind(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_chooser_folder_item, parent, false)
        return FolderViewholder(view, mode, listener)
    }

    override fun getItemCount() = data.size

    private val data = ArrayList<File>()

    fun newData(newData: List<File>) {
        if (data == newData)
            return
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class FolderViewholder(
        view: View,
        private val mode: OperationMode,
        private val listener: (selected: File) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private var boundFile: File? = null
        private val binding : FolderChooserFolderItemBinding by bindings(view)

        init {
            itemView.setOnClickListener {
                boundFile?.let(listener)
            }
        }

        fun bind(selectedFile: File) {
            boundFile = selectedFile
            val isDirectory = selectedFile.isDirectory

            binding.text.text = selectedFile.name

            // if its not a collection its also fine to pick a file
            if (mode == OperationMode.COLLECTION_BOOK) {
                binding.text.isEnabled = isDirectory
            }

            val context = itemView.context
            val drawableRes = if (isDirectory) R.drawable.ic_folder else R.drawable.ic_album
            binding.icon.setImageResource(drawableRes)
            binding.icon.contentDescription = (
                if (isDirectory) {
                    "Folder"
                } else "File"
            )
        }
    }

}