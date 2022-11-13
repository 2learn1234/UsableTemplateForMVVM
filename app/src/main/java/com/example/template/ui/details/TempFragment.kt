package com.example.template.ui.details

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.template.R
import com.example.template.databinding.FragmentTempBinding
import com.example.template.ui.list.catalog.CatalogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TempFragment : Fragment(R.layout.fragment_temp), MenuProvider {
    private lateinit var binding: FragmentTempBinding
    private val viewModel: TempViewModel by viewModels()
    private val args by navArgs<TempFragmentArgs>()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTempBinding.inflate(inflater)
        navController = findNavController()

        // setup fragment menu
        requireActivity().addMenuProvider(
            this, viewLifecycleOwner, Lifecycle.State.RESUMED
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }


    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)

         args.event.let { event ->
             binding.apply {
                 edTitle.setText(event?.name)
                 edTemp.setText(event?.type)
                 if (event?.image_url is String) {
                     Glide.with(imgUrl.context)
                         .load(event?.image_url)
                         .placeholder(R.drawable.loading_animation)
                         .error(R.drawable.ic_broken_image)
                         .into(imgUrl)
                 }
             }
             binding.imgDeleteEvent.visibility = View.VISIBLE
         }
     }*/

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.detail, menu)
    }


    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.ic_back  -> {
                navController.navigateUp()
                true
            }
            else -> {
                navController.navigateUp()
                true
            }
        }
    }
}
