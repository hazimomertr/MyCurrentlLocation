package edu.trakya.hazimomertr.currentlocation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OfflineScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(color = Color(0xFF050606))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.offline),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Text(
            text = "You are offline. Please check your internet connection.",
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
