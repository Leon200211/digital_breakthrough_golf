package ru.neuron.sportapp.data

import android.content.Context
import org.jetbrains.kotlinx.dl.example.app.VideoProcessor
import java.io.File
import java.io.InputStream


abstract class VideoRecordRepository(
) {
    abstract fun getVideoRecords(
        context: Context): List<VideoRecordModel>

    abstract fun getVideoRecord(videoRecordModel: VideoRecordModel,
                                context: Context): InputStream
    abstract fun saveVideoRecord(
        videoRecordModel: VideoRecordModel,
        inputStream: InputStream,
        context: Context
    )
    
    fun analyzeVideo(
        context: Context,
        videoRecord: File
    ): AnalyzeVideoResult {
        val videoProcessor: VideoProcessor = VideoProcessor(context, context.resources)
        val a = videoProcessor.analyze(videoRecord)
        return AnalyzeVideoResult(
            posesProbabilities = a.first
        )
    }

    open class AnalyzeVideoResult(
        val posesProbabilities: IntArray
    )
    class NotFoundVideoRecordException: Exception()
}