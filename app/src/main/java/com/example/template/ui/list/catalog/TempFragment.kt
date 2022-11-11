package com.example.template.ui.list.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.template.R
import com.example.template.databinding.FragmentTempBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.CustomInjection.inject
import javax.inject.Inject

@AndroidEntryPoint
class TempFragment : Fragment(R.layout.fragment_temp) {
    private lateinit var binding: FragmentTempBinding
    private val viewModel: CatalogViewModel by viewModels()
    private val args by navArgs<TempFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTempBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.event.let { event ->
            binding.apply {
                edTitle.setText(event?.name)
                edTemp.setText(event?.type)
                if (event?.image_url is String) {
                    Glide.with(imgUrl.context)
                        .load(imgUrl)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                        .into(imgUrl)
                }
            }
            binding.imgDeleteEvent.visibility = View.VISIBLE
        }
    }
}
