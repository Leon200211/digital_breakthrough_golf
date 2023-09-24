package org.jetbrains.kotlinx.dl.example.app

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import org.jetbrains.kotlinx.dl.api.inference.FlatShape
import org.jetbrains.kotlinx.dl.impl.preprocessing.camerax.toBitmap
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel

internal class ImageAnalyzer(
    context: Context,
    private val resources: Resources,
    private val uiUpdateCallBack: (AnalysisResult?) -> Unit,
) {
    private val moveNet = loadPoseEstimator(context)
    private val swingNet = SwingNet(context, context.resources)

    fun analyze(image: ImageProxy, isImageFlipped: Boolean) {
        val result = moveNet.analyze(image.toBitmap(), confidenceThreshold)

        val rotationDegrees = image.imageInfo.rotationDegrees
        image.close()

        if (result != null && result.confidence >= confidenceThreshold) {
            uiUpdateCallBack(
                AnalysisResult(
                    result,
                    ImageMetadata(image.width, image.height, isImageFlipped, rotationDegrees)
                )
            )
        }
    }

    fun close() {
        moveNet.close()
        swingNet.close()
    }

    companion object {
        private const val confidenceThreshold = 0.5f
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

data class AnalysisResult (
    val prediction: Prediction,
    val metadata: ImageMetadata
)
interface Prediction {
    val shapes: List<FlatShape<*>>
    val confidence: Float
    fun getText(context: Context): String
}

data class ImageMetadata(
    val width: Int,
    val height: Int,
    val isImageFlipped: Boolean
) {

    constructor(width: Int, height: Int, isImageFlipped: Boolean, rotationDegrees: Int)
            : this(
        if (areDimensionSwitched(rotationDegrees)) height else width,
        if (areDimensionSwitched(rotationDegrees)) width else height,
        isImageFlipped
    )

    companion object {
        private fun areDimensionSwitched(rotationDegrees: Int): Boolean {
            return rotationDegrees == 90 || rotationDegrees == 270
        }
    }
}