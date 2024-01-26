package edu.trakya.hazimomertr.currentlocation

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}