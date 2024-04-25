package com.example.healthy_farm.com.example.healthy_farm

import android.location.GnssAntennaInfo.Listener
import com.example.healthy_farm.OrderClickListener

data class ProductCart(
    val productname: String? = null,
    val farmername: String? = null,
    val price: String? = null,
    val location: String? = null,
    val produce: String? = null,
    val image: String?=null
)
