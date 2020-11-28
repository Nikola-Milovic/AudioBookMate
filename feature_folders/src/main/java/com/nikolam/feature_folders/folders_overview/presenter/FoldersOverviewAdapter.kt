package com.nikolam.feature_folders.folders_overview.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikolam.feature_folders.R
import com.nikolam.feature_folders.databinding.FolderOverviewFolderItemBinding
import com.nikolam.common.utils.bindings

class FoldersOverviewAdapter(
    private val listener : FolderOverviewItemListener
) : RecyclerView.Adapter<FoldersOverviewAdapter.FolderViewholder>() {

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
            .inflate(R.layout.folder_overview_folder_item, parent, false)
        return FolderViewholder(view)
    }

    override fun getItemCount() = data.size

    private val data = ArrayList<String>()

    fun newData(newData: List<String>) {
        if (data == newData)
            return
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class FolderViewholder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val binding : FolderOverviewFolderItemBinding by bindings(view)
        private var file : String = ""

        init{
            binding.remove.setOnClickListener {
                listener.deleteFolder(file)
            }
        }

        fun bind(fileDir: String) {
            file = fileDir
            binding.apply {
                folderName = fileDir
               // Timber.d("FOlder name %s", fileDir)
                executePendingBindings()
            }
        }
    }

}