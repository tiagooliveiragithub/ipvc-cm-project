package online.tripguru.tripguruapp.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.FragmentTripBinding
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.viewmodels.MainViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapterVertical
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import online.tripguru.tripguruapp.views.ui.MainActivity

@AndroidEntryPoint
class TripFragment : Fragment(), OnLocalClickListener {
    private lateinit var localAdapter: LocalAdapterVertical
    private lateinit var binding: FragmentTripBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val authViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        listeners()
        observers()
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewVertical
        recyclerView.layoutManager = LinearLayoutManager(context)
        localAdapter = LocalAdapterVertical(this)
        recyclerView.adapter = localAdapter
    }

    private fun observers() {
        authViewModel.isOnline().observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                disableButtons()
            } else {
                selectedTripObserver()
                createAllDataFetchResultObserver()
                deleteTripResultObserver()
            }
        }

    }

    private fun selectedTripObserver() {
        mainViewModel.getSelectedTrip().observe(viewLifecycleOwner) { selectedTrip ->
            if (selectedTrip != null) {
                binding.textViewTripName.text = selectedTrip.name
                binding.buttonEditTripPage.visibility = View.VISIBLE
                binding.buttonCreateLocalPage.visibility = View.VISIBLE
                binding.buttonDeleteTrip.visibility = View.VISIBLE
                binding.recyclerViewVertical.visibility = View.VISIBLE
                binding.textViewTripLocals.visibility = View.VISIBLE
                mainViewModel.getAllLocalsforSelectedTrip().observe(viewLifecycleOwner) { trips ->
                    localAdapter.setLocals(trips)
                }
            } else {
                binding.textViewTripName.text = getString(R.string.notripselected_label)
                binding.buttonEditTripPage.visibility = View.GONE
                binding.buttonCreateLocalPage.visibility = View.GONE
                binding.buttonDeleteTrip.visibility = View.GONE
                binding.recyclerViewVertical.visibility = View.GONE
                binding.textViewTripLocals.visibility = View.GONE
                binding.textViewTripName.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        }
    }

    private fun createAllDataFetchResultObserver() {
        mainViewModel.resultAllDataFetch.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    disableButtons()
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    enableButtons()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error fetching data", Toast.LENGTH_LONG).show()
                    activity?.finish()
                }
            }
        }
    }

    private fun deleteTripResultObserver() {
        mainViewModel.resultDeleteTrip.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Local deleted successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    changeToHomeFragment()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun listeners() {
        binding.buttonEditTripPage.setOnClickListener {
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
        binding.buttonCreateLocalPage.setOnClickListener {
            mainViewModel.updateSelectedLocal(null)
            startActivity(Intent(context, CreateLocalActivity::class.java))
        }
        binding.buttonDeleteTrip.setOnClickListener {
            mainViewModel.deleteTrip(mainViewModel.getSelectedTrip().value?.id!!)
            changeToHomeFragment()
        }
    }

    private fun disableButtons() {
        binding.buttonEditTripPage.isEnabled = false
        binding.buttonCreateLocalPage.isEnabled = false
        binding.buttonDeleteTrip.isEnabled = false
    }

    private fun enableButtons() {
        binding.buttonEditTripPage.isEnabled = true
        binding.buttonCreateLocalPage.isEnabled = true
        binding.buttonDeleteTrip.isEnabled = true
    }

    override fun onLocalClick(local: Local) {
        mainViewModel.updateSelectedLocal(local)
        startActivity(Intent(context, CreateLocalActivity::class.java))
    }

    private fun changeToHomeFragment() {
        (activity as MainActivity).replaceFragment(HomeFragment())
    }

}