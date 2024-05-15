package hu.recommendr.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.recommendr.data.Song
import hu.recommendr.ui.theme.RecommendRTheme


@Composable
fun SongCard(responseItem: Song, onItemClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onItemClicked)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,) {
                Text(
                    responseItem.artist,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(8.dp))
                Text(
                    responseItem.genre,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(8.dp))
            }
            Divider(modifier = Modifier.fillMaxWidth().padding(8.dp, 0.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                ,
            ){
                Text(
                    responseItem.title,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecommendRTheme {
        SongCard(Song("Title", "Artist", "Genre"), onItemClicked = {})
    }
}
