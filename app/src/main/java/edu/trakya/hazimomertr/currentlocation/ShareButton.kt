package edu.trakya.hazimomertr.currentlocation

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat





@Composable
fun ShareButton(shareText: String) {
    val context = LocalContext.current

    val shareContent: (String) -> Unit = { content ->
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        ContextCompat.startActivity(context, shareIntent, null)
    }

    IconButton(
        onClick = {
            shareContent(shareText)
        },
        content = {
            Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
        },
        modifier = Modifier
            .padding(8.dp)
            .size(30.dp))
}