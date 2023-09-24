package org.jetbrains.kotlinx.dl.example.app

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import org.jetbrains.kotlinx.dl.api.inference.FlatShape
import org.jetbrains.kotlinx.dl.api.inference.posedetection.DetectedPose
import org.jetbrains.kotlinx.dl.onnx.inference.executionproviders.ExecutionProvider.CPU
import org.jetbrains.kotlinx.dl.onnx.inference.inferUsing
import ru.neuron.sportapp.R

interface InferencePipeline {
    fun analyze(image: Bitmap, confidenceThreshold: Float): Prediction?
    fun close()
}

class PoseDetectionPipelineMy(private val model: MoveNet) : InferencePipeline {
    override fun analyze(image: Bitmap, confidenceThreshold: Float): Prediction? {
        val detectedPose = model.inferUsing(CPU()) {
            it.detectPose(image)
        }

        if (detectedPose.landmarks.isEmpty()) return null

        return PredictedPose(detectedPose)
    }

    override fun close() = model.close()

    class PredictedPose(val pose: DetectedPose) : Prediction {
        override val shapes: List<FlatShape<*>> get() = listOf(pose)
        override val confidence: Float get() = pose.landmarks.maxOf { it.probability }
        override fun getText(context: Context): String = context.getString(R.string.label_pose)
    }
}
