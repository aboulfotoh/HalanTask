package com.halan.halantask.ui.categories


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.halan.halantask.data.model.Service
import com.halan.halantask.databinding.FragmentCategoryBinding
import com.halan.halantask.network.ApiEndpointInterface
import com.orhanobut.hawk.Hawk
import mumayank.com.airlocationlibrary.AirLocation
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


/**
 * A simple [Fragment] subclass.
 */
class CategoriesFragment : Fragment(),
    CategoriesAdapter.OnItemClickListener {

    private var airLocation: AirLocation? = null
    private val REQUEST_CHECK_SETTINGS = 101
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private val apiEndpointInterface: ApiEndpointInterface by inject { parametersOf(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoriesAdapter = CategoriesAdapter()
        categoriesAdapter.setOnItemClickListener(this)
        categoriesViewModel = ViewModelProvider(this,
            CategoriesViewModelFactory(apiEndpointInterface,context!!))
            .get(CategoriesViewModel::class.java)

        categoriesViewModel.categories.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it?.data?.size !=0){
                categoriesAdapter.submitList(it?.data)
                categoriesAdapter.notifyDataSetChanged()
                binding.pbLoading.visibility = View.GONE
                binding.rvCategories.visibility = View.VISIBLE
                Hawk.put("Categories",it)
            } else {
                getLocation()
            }
        })
        binding = FragmentCategoryBinding.inflate(inflater)
        binding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.root.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fetchLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != -1)
            fetchLocation()
        else
            activity?.finish()
    }

    private fun getLocation(){
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(activity!!).checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it?.getResult(ApiException::class.java)!!
                fetchLocation()
            } catch (exception: ApiException ) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val resolvable : ResolvableApiException = exception as ResolvableApiException
                            resolvable.startResolutionForResult(activity,REQUEST_CHECK_SETTINGS)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException ) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                        activity?.finish()
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.

                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun fetchLocation() {
        airLocation = AirLocation(activity!!,
            shouldWeRequestPermissions = true,
            shouldWeRequestOptimization = false,
            callbacks = object : AirLocation.Callbacks {
                override fun onSuccess(location: Location) {
                    categoriesViewModel.getCategories(location)
                }
                override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                   when(locationFailedEnum){
                       AirLocation.LocationFailedEnum.LocationOptimizationPermissionNotGranted -> fetchLocation()
                       else -> activity?.finish()
                    }
                }
            })
    }

    override fun onItemClick(service: Service) {
        if (service.terms.isNotEmpty()){
            binding.tvTerms.text = service.terms
                BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        } else{
            if (BottomSheetBehavior.from(binding.bottomSheet).state == BottomSheetBehavior.STATE_EXPANDED) {
                BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
            }
            findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToServiceFragment(service))
        }
        binding.btnAccept.apply {
            setOnClickListener {
                BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
                findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToServiceFragment(service))
            }
        }
        binding.btnReject.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}
