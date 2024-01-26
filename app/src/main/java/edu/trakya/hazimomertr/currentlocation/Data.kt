package com.example.observecurrentlocationjetpackcompose

data class LocationDetails(
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val postalCode: String = "",
    val knownName: String = ""
)

data class AddressDetails(
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String,
    val knownName: String
)