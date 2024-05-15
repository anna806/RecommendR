package hu.recommendr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.recommendr.ui.theme.RecommendRTheme
import kotlinx.coroutines.launch

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

@Composable
fun MainPage(mainViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val mainUIState by mainViewModel.uiState.collectAsState()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                AnnotatedString("RecommendR"),
                fontSize = 40.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 50.dp, bottom = 16.dp))
            OutlinedTextField(
                value = mainUIState.text,
                onValueChange = { mainViewModel.onTextChanged(it) },
                textStyle = TextStyle(fontSize = 20.sp),
                singleLine = true,
                modifier = Modifier.padding(end = 10.dp))
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = {
                    if (mainUIState.text.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please enter a message")
                        }
                    }
                    else {
                        mainViewModel.sendMessage(mainUIState.text)
                    }

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF536891))) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.baseline_autorenew_24), contentDescription = "Generate")
                    Text("Generate", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(8.dp))
                }
            }
            Spacer(modifier = Modifier.padding(16.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(mainUIState.response) { _, responseItem ->
                    ResponseCard(responseItem)
                }
            }

        }

    }

}

@Composable
fun ResponseCard(responseItem: Song) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columnWidth = ((screenWidth - 16.dp) / 3)
    Card(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Column(modifier = Modifier.size(columnWidth).padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start) {
                Text(
                    responseItem.title,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.size(columnWidth).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    responseItem.artist,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.size(columnWidth).padding(8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center) {
                Text(
                    responseItem.genre,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val mainViewModel = MainViewModel()
    RecommendRTheme {
        MainPage(mainViewModel)
    }
}
