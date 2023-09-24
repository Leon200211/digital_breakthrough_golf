package ru.neuron.sportapp.home

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.kotlinx.dl.example.app.VideoProcessor
import ru.neuron.sportapp.data.VideoRecordFileSource
import ru.neuron.sportapp.data.VideoRecordModel
import ru.neuron.sportapp.data.VideoRecordRepository
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalTime

class HomeViewModel(
    private val videoRecordRepository: VideoRecordRepository = VideoRecordFileSource()
): ViewModel() {
    fun onVideoSelected(context: Context, videoSelected: InputStream) {
        val videoRecord = VideoRecordModel(
            filename = "video_${LocalDate.now()}_${LocalTime.now().toSecondOfDay()}.mp4"
        )
//            Thread {
//                try {
//                    videoRecordRepository.saveVideoRecord(
//                        videoRecord, videoSelected, context
//                    )
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Log.e("MYDEBUG", "Error: ${e.message} ${e.stackTraceToString()}")
//                }
//            }.start()
        videoRecordRepository.saveVideoRecord(videoRecord, videoSelected, context)
        Log.d("MYDEBUG", "Video saved into ${context.filesDir.canonicalPath}")
        viewModelScope.launch {
            try {
                val videoFile = VideoRecordFileSource.videoRecordsFolder.resolve(videoRecord.filename)
                val analyzeVideoResult = withContext(Dispatchers.Main) {
                    videoRecordRepository.analyzeVideo(context, videoFile)
                    // run on ui thread
                }
                Toast.makeText(
                    context,
                    "Вероятности поз: ${analyzeVideoResult.posesProbabilities}",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MYDEBUG", "Error: ${e.message} ${e.stackTraceToString()}")
            }
        }

    }
}