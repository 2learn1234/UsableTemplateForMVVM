package com.example.template.ui.list.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.*
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.template.R
import com.example.template.databinding.FragmentTempsListBinding
import com.example.template.ui.list.EventListAdapter
import dagger.hilt.android.AndroidEntryPoint

import androidx.core.view.MenuProvider

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.template.data.database.EventEntity
import com.example.template.domain.Event
import com.example.template.ui.dialogs.showInfoDialog
import com.example.template.workmanager.RefreshWorker


@AndroidEntryPoint
class TemplatesListFragment : Fragment(R.layout.fragment_temps_list), MenuProvider, SearchView.OnQueryTextListener {
    private  val viewModel: CatalogViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentTempsListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTempsListBinding.inflate(inflater)

        navController = findNavController()

        // setup fragment menu
        requireActivity().addMenuProvider(
            this, viewLifecycleOwner, Lifecycle.State.RESUMED
        )

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.rvTemps.addItemDecoration(divider)
        binding.rvTemps.adapter = EventListAdapter(EventListAdapter.EventDiffCallback()) { event ->
            val bundle=Bundle().apply {
                putParcelable("event", event.asEventEntity())
            }
            navController.navigate(R.id.action_catalogFragment_to_tempFragment4,bundle)
          //  val action = TemplatesListFragmentDirections.actionTemplatesListFragmentToTempFragment()
          //  navController.navigate(action)
        }

        binding.lifecycleOwner = viewLifecycleOwner
       binding.viewModel = viewModel

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.liveStatus.observe(viewLifecycleOwner){ status->
            binding.swipeRefresh.isRefreshing=
                status==CatalogViewModel.Status.LOADING
            if(status==CatalogViewModel.Status.ERROR){
                showInfoDialog(
                    title = "Events Data",
                  message="Can't load data"
                )
                viewModel.clearError()
            }

        }

    return binding.root
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.catalog, menu)
        val search=menu?.findItem(R.id.action_search)
        val searchView=search?.actionView as? SearchView
        searchView?.maxWidth=Int.MAX_VALUE
        searchView?.isSubmitButtonEnabled=true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_refresh -> {
                viewModel.refresh()
                binding.rvTemps.adapter?.notifyDataSetChanged()
                true
            }
            R.id.action_clear -> {
                viewModel.clearLocalData()
                true
            }
            R.id.action_search -> {
                viewModel.clearLocalData()
                true
            }
            else -> false
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchDb(query)
        }

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
           // searchDb(query)
            (binding.rvTemps.adapter as EventListAdapter).filter.filter(query!!)
        }
        return true
    }

    private fun searchDb(query:String){
        val searchQuery= "%$query%"

        viewModel.searchDatabase(searchQuery).observe(this,{  list ->
            list.let{
                viewModel.refresh(list)
                //viewModel._liveStatus=CatalogViewModel.Status.SUCCESS
            }
        })
    }
    private fun Event.asEventEntity() =
        EventEntity(
            id = id,
            name = name,
            type = type,
            image_url = image_url
        )

}