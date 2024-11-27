package com.google.firebase.example.rgm33880603.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.example.rgm33880603.Filters

/**
 * ViewModel for [com.google.firebase.example.rgm33880603.MainActivity].
 */

class MainActivityViewModel : ViewModel() {

    var isSigningIn: Boolean = false
    var filters: Filters = Filters.default
}
