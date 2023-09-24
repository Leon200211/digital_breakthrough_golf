package ru.neuron.sportapp.data

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File
import java.io.InputStream

class VideoRecordFileSource(
): VideoRecordRepository() {
    companion object {
        private const val videoRecordsFolderName = "video_records"
        private val downloadFolder: File = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS)
        val videoRecordsFolder = downloadFolder.resolve(videoRecordsFolderName)
    }

    override fun getVideoRecords(
        context: Context): List<VideoRecordModel> {
        val allFiles = mutableListOf<VideoRecordModel>()
        if (videoRecordsFolder.exists()) {
            videoRecordsFolder.listFiles()?.forEach {
                allFiles.add(VideoRecordModel(it.name))
            }
        }
        return allFiles.toList()
    }

    override fun getVideoRecord(videoRecordModel: VideoRecordModel,
                                context: Context): InputStream {
        val file = videoRecordsFolder.resolve(videoRecordModel.filename)
        if (!file.exists()) {
            throw VideoRecordRepository.NotFoundVideoRecordException()
        }
        return file.inputStream()
    }

    override fun saveVideoRecord(
        videoRecordModel: VideoRecordModel,
        inputStream: InputStream,
        context: Context
    ) {
        if (!videoRecordsFolder.exists()) {
            videoRecordsFolder.mkdir()
        }
        val file = videoRecordsFolder.resolve(videoRecordModel.filename)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.outputStream().use {
            inputStream.copyTo(it)
        }
        Log.d("MYDEBUG", "saved video file: ${file.absoluteFile}")
    }
}