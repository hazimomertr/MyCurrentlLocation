package edu.trakya.hazimomertr.currentlocation




import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.observecurrentlocationjetpackcompose.AddressDetails
import com.example.observecurrentlocationjetpackcompose.LocationDetails
import com.google.android.gms.location.*
import edu.trakya.hazimomertr.currentlocation.ui.theme.CurrentLocationTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Locale


class MainActivity : ComponentActivity() {

    private var locationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequired = false
    var test by mutableStateOf(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CurrentLocationTheme (darkTheme = isSystemInDarkTheme()){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF050606),

                ) {


                    val context = LocalContext.current
                    var currentLocation by remember {
                        mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble()))
                    }
                    var isLoading by remember { mutableStateOf(false) }

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(p0: LocationResult) {

                            for (lo in p0.locations) {
                                val addresses = getAddressesFromLocation(lo.latitude, lo.longitude)
                                currentLocation = LocationDetails(
                                    latitude = lo.latitude,
                                    longitude = lo.longitude,
                                    address = addresses.address,
                                    city = addresses.city,
                                    state = addresses.state,
                                    country = addresses.country,
                                    postalCode = addresses.postalCode,
                                    knownName = addresses.knownName
                                )
                            }
                            isLoading = false
                        }
                    }
                    val connection by connectivityState()

                    val isConnected = connection === ConnectionState.Available
                    if (isConnected) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xFF050606))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxSize()
                                    .background(color = Color(0xFF050606)),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp, top = 100.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.location),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(250.dp)
                                            .padding(end = 30.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .padding(end = 8.dp, start = 30.dp),
                                            color = Color(0xFF161616)
                                        )
                                    }
                                    Text(
                                        text = if (isLoading) "Loading..." else currentLocation.address,
                                        modifier = Modifier.padding(top = 16.dp),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE0E0E0),
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = Color(0xFF161616)),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        onClick = {
                                            isLoading = true
                                            startLocationUpdates()

                                        },
                                    ) {
                                        if (isLoading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .padding(end = 8.dp),
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                        Text(
                                            text = if (isLoading) "Loading..." else "Get Location",
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }

                                    ShareButton(
                                        shareText = "My current location: ${currentLocation.address}"
                                    )

                                }
                            }
                        }
                    } else {
                        OfflineScreen()
                    }
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 1000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }
    private fun getAddressesFromLocation(latitude: Double, longitude: Double): AddressDetails {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        return if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0) ?: ""
                val city = addresses[0].locality ?: ""
                val state = addresses[0].adminArea ?: ""
                val country = addresses[0].countryName ?: ""
                val postalCode = addresses[0].postalCode ?: ""
                val knownName = addresses[0].featureName ?: ""

                AddressDetails(address, city, state, country, postalCode, knownName)
            } else {
                AddressDetails("", "", "", "", "", "")
            }
        } else {
            // Return a default AddressDetails object in case addresses is null
            AddressDetails("", "", "", "", "", "")
        }
    }


    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }


}


@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    return produceState(initialValue = context.currentConnectivityState) {
        context.observeConnectivityAsFlow().collect { value = it }
    }
}
