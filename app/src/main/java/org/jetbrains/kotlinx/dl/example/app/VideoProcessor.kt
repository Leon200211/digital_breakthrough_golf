package org.jetbrains.kotlinx.dl.example.app

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.camera.core.ImageProxy
import org.jetbrains.kotlinx.dl.api.inference.FlatShape
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File

class VideoProcessor(
    val context: Context,
    private val resources: Resources,
) {
    private val moveNet = loadPoseEstimator(context)
    private val swingNet = SwingNet(context, context.resources)

    fun analyze(videoFile: File): Pair<IntArray, ArrayList<Prediction?>> {
        val bitmapSeq = getBitmapseq(videoFile)
        val keyFrames = swingNet.predict(bitmapSeq)
        val keyPoints = ArrayList<Prediction?>()
        for (bitmap in keyFrames){
            keyPoints.add(moveNet.analyze(bitmapSeq[bitmap], 0.5F))
        }
        return keyFrames to keyPoints
    }

    fun close() {
        moveNet.close()
        swingNet.close()
    }

    companion object {
        private const val confidenceThreshold = 0.5f
    }

    fun getBitmapseq(file: File): ArrayList<Bitmap>{
        val med = FFmpegMediaMetadataRetriever()
        med.setDataSource(file.path)
        val value: String? = med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)
        var vidLength: Long = value!!.toLong() // it gives duration in seconds

        val list_mat = ArrayList<Bitmap>()
        var i = 0
        Log.d("MYDEBUG", context.filesDir.toString())
        for(j in 0 until vidLength step vidLength.div(200)) {
            Log.d("MYDEBUG","read $i frame")
            val bitmap = med.getFrameAtTime(
                j * 1000,
                FFmpegMediaMetadataRetriever.OPTION_CLOSEST
            )
            list_mat.add(bitmap)
            Log.d("MYDEBUG","write $i frame")
            i += 1
        }
        return list_mat
    }

    private fun loadPoseEstimator(context: Context): PoseDetectionPipelineMy{
        val modelResourceId = resources.getIdentifier(
            "movenet16",
            "raw",
            context.packageName
        )
        val inferenceModel = OnnxInferenceModel {
            resources.openRawResource(modelResourceId).use { it.readBytes() }
        }

        return PoseDetectionPipelineMy(MoveNet(inferenceModel, "1234", "StatefulPartitionedCall:0"))
    }
}

//data class AnalysisResult (
//    val prediction: Prediction,
//    val metadata: ImageMetadata
//)
//interface Prediction {
//    val shapes: List<FlatShape<*>>
//    val confidence: Float
//    fun getText(context: Context): String
//}
//
//data class ImageMetadata(
//    val width: Int,
//    val height: Int,
//)