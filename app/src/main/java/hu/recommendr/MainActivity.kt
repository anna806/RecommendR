package hu.recommendr

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.recommendr.ui.common.ExpandedSongDialog
import hu.recommendr.ui.common.SongCard
import hu.recommendr.ui.theme.RecommendRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel = MainViewModel()
        setContent {
            RecommendRTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    MainPage(mainViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPage(mainViewModel: MainViewModel) {
    val mainUIState by mainViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val openAlertDialog = remember { mutableStateOf(false) }



    Scaffold { contentPadding ->

        when (openAlertDialog.value) {
            true -> {
                mainUIState.selectedSong?.let{
                    ExpandedSongDialog(song = it, context = context) {
                        openAlertDialog.value = false
                    }
                }
            }

            false -> {
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(30.dp))
            Text(
                AnnotatedString("RecommendR"),
                fontSize = 40.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                value = mainUIState.text,
                onValueChange = { mainViewModel.onTextChanged(it) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                //collapse inline keyboard on submit
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        modifier = Modifier.clickable {
                            if (mainUIState.text.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please enter a genre",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                keyboardController?.hide()
                                mainViewModel.sendMessage(mainUIState.text)
                            }
                        }
                    )
                }
            )
            Divider(modifier = Modifier.padding(24.dp))
            if (mainUIState.loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp, 16.dp),
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                itemsIndexed(mainUIState.response) { _, responseItem ->
                    SongCard(responseItem, onItemClicked = {
                        mainViewModel.onSongSelected(responseItem)
                        openAlertDialog.value = true
                    })
                }
            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecommendRTheme {
        MainPage(MainViewModel())
    }
}