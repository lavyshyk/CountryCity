package com.lavyshyk.countrycity.ui.startFragment

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.lavyshyk.countrycity.base.mvi.BaseViewModel
import com.lavyshyk.domain.dto.news.ArticleDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.repository.flow.NewsNetworkRepositoryOnFlowDagger
import com.simple.mvi.features.home.StartAction
import com.simple.mvi.features.home.StartIntent
import com.simple.mvi.features.home.StartState
import com.simple.mvi.features.home.reduce
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject


@InternalCoroutinesApi
class StartViewModel @Inject constructor(
    context: Context,
    private val mNewsNetworkRepositoryOnFlowForDaggerImpl: NewsNetworkRepositoryOnFlowDagger

) : BaseViewModel<StartIntent, StartAction, StartState>() {

    private val mGeocoder = Geocoder(context, Locale.getDefault())

    @SuppressLint("VisibleForTests")
    private val mLocationProvider = FusedLocationProviderClient(context)
    var mAddresses = mutableListOf<Address>()
    var mCountryCode = " "


    override fun intentToAction(intent: StartIntent): StartAction {
        return when (intent) {
            is StartIntent.LoadNewsByCode -> StartAction.GetNewsByCode
            is StartIntent.LoadNews -> StartAction.GetNews
            is StartIntent.Exception -> StartAction.Fail(intent.t)
        }
    }

    @SuppressLint("MissingPermission")
    override fun handleAction(action: StartAction) {
        launchOnUI {
            when (action) {
                is StartAction.GetNewsByCode -> {
                    mLocationProvider.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            try {
                                mGeocoder
                                    .getFromLocation(
                                        location.latitude,
                                        location.longitude, 1
                                    ).also { mAddresses = it }
                            } catch (e: Exception) {
                                dispatchIntent(StartIntent.Exception(e))
                            }
                            if (mAddresses.isNotEmpty()) {
                                mCountryCode = mAddresses[0].countryCode.lowercase()
                                getNewsByCode(mCountryCode)
                            }else {
                                getAllNews()
                            }
                        }
                    }
                }
                is StartAction.GetNews -> {
                   getAllNews()
                }
                is StartAction.Fail -> {
                    mState.value = (Outcome.failure<List<ArticleDto>>(action.t)).reduce()
                }
            }
        }
    }
    fun getNewsByCode(countryCode : String){
        launchOnUI {
            mNewsNetworkRepositoryOnFlowForDaggerImpl
                .getNewsByCountryCodeDagger(countryCode).collect {
                    mState.value = it.reduce()
                }
        }
    }
    fun getAllNews(){
        launchOnUI {
            mNewsNetworkRepositoryOnFlowForDaggerImpl.getNewsDagger()
                .collect {
                    mState.value = it.reduce()
                }
        }
    }
}

