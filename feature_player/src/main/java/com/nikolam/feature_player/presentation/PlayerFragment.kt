package com.nikolam.feature_player.presentation

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikolam.feature_player.R
import com.nikolam.feature_player.databinding.PlayerFragmentBinding
import com.nikolam.feature_player.di.viewmodelModule
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class PlayerFragment : Fragment() {

    //Koin
    private val moduleList = arrayListOf(viewmodelModule)

    private val loadModules by lazy {
        loadKoinModules(moduleList)
    }

    private fun injectFeatures() = loadModules

    private val viewModel: PlayerViewModel by inject()

    lateinit var binding : PlayerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.player_fragment, container, false)
        binding.apply {

            val layoutMana =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            lifecycleOwner = this@PlayerFragment
        }

        return binding.root
    }


    // Loading unloading modules
    override fun onAttach(context: Context) {
        super.onAttach(context)

         injectFeatures()
    }

    override fun onDetach() {
        unloadKoinModules(moduleList)
        super.onDetach()
    }


}