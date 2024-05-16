package hu.recommendr.ui.common

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import hu.recommendr.ExpandedSongDialogViewModel
import hu.recommendr.R
import hu.recommendr.data.Song

enum class ImageState{
    NotNeeded,
    Loading,
    Loaded
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ExpandedSongDialog(
    song: Song,
    context: Context?,
    onDismissRequest: () -> Unit
){
    val viewModel = ExpandedSongDialogViewModel()
    val openDialog = remember { mutableStateOf(false) }
    val imageState = remember { mutableStateOf(ImageState.NotNeeded) }

    val imageServiceUIState by viewModel.uiState.collectAsState()

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setMessage("Are you sure you want to generate image for the song?")
        .setTitle("Generate image")
        .setPositiveButton("Yes") { _, _ ->
            imageState.value = ImageState.Loading
            Log.d("ExpandedSongDialog", "${song.title} ${song.artist}")
            viewModel.getImageUrl(song.title)
        }
        .setNegativeButton("No") { _, _ ->
            Log.d("ExpandedSongDialog", "No clicked")
        }

    val dialog: AlertDialog = builder.create()


    Dialog(
        onDismissRequest = {  }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp),
            ) {
                Row {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", modifier = Modifier
                        .wrapContentSize()
                        .clickable(onClick = { onDismissRequest() }))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        song.artist,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(8.dp)
                    )
                    Text(
                        song.genre,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(8.dp)
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    Text(
                        song.title,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(8.dp)
                    )
                }
                if (imageState.value == ImageState.NotNeeded) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                dialog.show()
                            },
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(8.dp)
                        ) {
                            Text("Load image")
                        }
                    }

                }
                else {
                    AsyncImage(
                        model = imageServiceUIState.uri,
                        contentDescription = "Music cover image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            painter = rememberAsyncImagePainter(model = R.drawable.outline_save_alt_24),
                            contentDescription = "Download",
                            modifier = Modifier.wrapContentSize()
                                .clickable {
                                    if (context != null) {
                                        viewModel.downloadImage(context)
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            modifier = Modifier.wrapContentSize()
                                .clickable {
                                    if (context != null) {
                                        viewModel.shareImage(context)
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun ExpandedSongDialogPreview() {
    ExpandedSongDialog(Song("Title", "Artist", "Genre"), context = null, onDismissRequest = {})
}
