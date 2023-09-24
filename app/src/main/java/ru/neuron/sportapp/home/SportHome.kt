package ru.neuron.sportapp.home

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.kotlinx.dl.example.app.MainCameraActivity
import ru.neuron.sportapp.R
import ru.neuron.sportapp.data.VideoRecordFileSource
import ru.neuron.sportapp.geofindbutton.GeoFindButton
import ru.neuron.sportapp.ui.sport_learning_item



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SportHome(homeViewModel: HomeViewModel) {
    val cameraAppLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { _ ->
        }
    )
    val context = LocalContext.current
    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { videoUri ->
        if (videoUri != null) {
            context.contentResolver.openInputStream(videoUri)
                ?.use { homeViewModel.onVideoSelected(context, it) }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SportGeoFindButton()
        FlowRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 10.dp)
                .fillMaxWidth(),
        ) {
            val itemModifier = Modifier.padding(22.dp)
            SportHomeLearningItem(
                label = "Обучение",
                painter = painterResource(id = R.drawable.book),
                modifier = itemModifier,
            )
            SportHomeLearningItem(
                label = "Тренировка",
                painter = painterResource(id = R.drawable.dumbbel),
                onItemClick = {
                    cameraAppLauncher.launch(
                        Intent(
                            context,
                            MainCameraActivity::class.java
                        )
                    )},
                modifier = itemModifier,
            )
            SportHomeLearningItem(
                label = "Вспомогательные материалы",
                painter = painterResource(id = R.drawable.teacher),
                modifier = itemModifier,
            )
            SportHomeLearningItem(
                label = "История тренировок",
                painter = painterResource(id = R.drawable.script),
                onItemClick = {
                    Toast.makeText(
                        context,
                        VideoRecordFileSource.videoRecordsFolder.absoluteFile.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = itemModifier,
            )
        }
        FlowRow (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            Button(onClick = {
                pickFileLauncher.launch("video/*")
            }) {
                Text(stringResource(R.string.upload_video))
            }
        }

    }
}

@Preview
@Composable
fun SportGeoFindButton() {
    Box(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 40.dp)
            .requiredHeight(140.dp)
            .fillMaxWidth()
        ,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.geo_find_button),
            contentScale = ContentScale.Crop,
            contentDescription = null)

        GeoFindButton(
            modifier = Modifier.padding(20.dp),
            label = stringResource(R.string.geo_find_button)
        )
    }

}
@Composable
fun SportHomeLearningItem(
    modifier: Modifier = Modifier,
    label: String = "Тест",
    painter: Painter = painterResource(id = R.drawable.ic_launcher_background),
    onItemClick: () -> Unit = {}
) {
    Button(
        onClick = onItemClick,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .size(134.dp, 90.dp),
        ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = sport_learning_item,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Image(
                painter = painter,
                contentScale = ContentScale.Fit,
                contentDescription = null,
                alpha = 0.2f,
                modifier = Modifier.fillMaxSize()
            )
            Text(label,
                color = Color.White.copy(alpha = 0.87f),
                textAlign = TextAlign.Center
            )
        }
    }
}
@Preview
@Composable
fun SportHomeLearningItemPreview() {
    Surface {
        SportHomeLearningItem(painter = painterResource(id = R.drawable.book))
    }
}