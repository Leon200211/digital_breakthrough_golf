package org.jetbrains.kotlinx.dl.example.app

import ai.onnxruntime.OrtSession
import android.graphics.Bitmap
import org.jetbrains.kotlinx.dl.api.core.shape.TensorShape
import org.jetbrains.kotlinx.dl.api.inference.InferenceModel
import org.jetbrains.kotlinx.dl.api.inference.posedetection.DetectedPose
import org.jetbrains.kotlinx.dl.api.inference.posedetection.PoseEdge
import org.jetbrains.kotlinx.dl.api.inference.posedetection.PoseLandmark
import org.jetbrains.kotlinx.dl.api.preprocessing.Operation
import org.jetbrains.kotlinx.dl.api.preprocessing.pipeline
import org.jetbrains.kotlinx.dl.impl.preprocessing.TensorLayout
import org.jetbrains.kotlinx.dl.impl.preprocessing.resize
import org.jetbrains.kotlinx.dl.impl.preprocessing.rotate
import org.jetbrains.kotlinx.dl.impl.preprocessing.toFloatArray
import org.jetbrains.kotlinx.dl.onnx.inference.CameraXCompatibleModel
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxHighLevelModel
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.jetbrains.kotlinx.dl.onnx.inference.OrtSessionResultConversions.get2DFloatArray


import kotlin.math.min

class MoveNet(
    override val internalModel: OnnxInferenceModel,
    modelKindDescription: String? = null,
    outputName: String
) : SinglePoseDetectionModelBaseMy<Bitmap>(modelKindDescription, outputName),
    InferenceModel by internalModel,
    CameraXCompatibleModel {
    override val preprocessing: Operation<Bitmap, Pair<FloatArray, TensorShape>>
        get() = pipeline<Bitmap>()
            .resize {
                outputHeight = internalModel.inputDimensions[0].toInt()
                outputWidth = internalModel.inputDimensions[1].toInt()
            }
            .rotate { degrees = targetRotation.toFloat() }
            .toFloatArray { layout = TensorLayout.NHWC }

    override var targetRotation: Int = 0

    override fun close() {
        internalModel.close()
    }
}


fun SinglePoseDetectionModelBaseMy<Bitmap>.detectPose(image: Bitmap): DetectedPose = detectPose(image)


abstract class SinglePoseDetectionModelBaseMy<I>(
    override val modelKindDescription: String? = null,
    protected val outputName: String
) :
    OnnxHighLevelModel<I, DetectedPose> {

    protected val keyPointsLabels: Map<Int, String> = keyPoints

    protected val edgeKeyPoints: List<Pair<Int, Int>> = edgeKeyPointsPairs

    override fun convert(output: OrtSession.Result): DetectedPose {
        val rawPoseLandMarks = output.get2DFloatArray(outputName)

        val foundPoseLandmarks = mutableListOf<PoseLandmark>()
        for (i in rawPoseLandMarks.indices) {
            val poseLandmark = PoseLandmark(
                label = keyPointsLabels[i]!!,
                x = rawPoseLandMarks[i][1],
                y = rawPoseLandMarks[i][0],
                probability = rawPoseLandMarks[i][2]
            )
            foundPoseLandmarks.add(i, poseLandmark)
        }

        val foundPoseEdges = buildPoseEdges(foundPoseLandmarks, edgeKeyPoints)

        return DetectedPose(foundPoseLandmarks, foundPoseEdges)
    }

    fun detectPose(image: I): DetectedPose = predict(image)
}

internal fun buildPoseEdges(
    foundPoseLandmarks: List<PoseLandmark>,
    edgeKeyPoints: List<Pair<Int, Int>>
): List<PoseEdge> {
    val foundPoseEdges = mutableListOf<PoseEdge>()
    edgeKeyPoints.forEach {
        val startPoint = foundPoseLandmarks[it.first]
        val endPoint = foundPoseLandmarks[it.second]
        foundPoseEdges.add(
            PoseEdge(
                start = startPoint,
                end = endPoint,
                probability = min(startPoint.probability, endPoint.probability),
                label = startPoint.label + "_" + endPoint.label
            )
        )
    }
    return foundPoseEdges
}

/**
 * Dictionary that maps from joint names to keypoint indices.
 */
public val keyPoints: Map<Int, String> = mapOf(
    0 to "nose",
    1 to "left_eye",
    2 to "right_eye",
    3 to "left_ear",
    4 to "right_ear",
    5 to "left_shoulder",
    6 to "right_shoulder",
    7 to "left_elbow",
    8 to "right_elbow",
    9 to "left_wrist",
    10 to "right_wrist",
    11 to "left_hip",
    12 to "right_hip",
    13 to "left_knee",
    14 to "right_knee",
    15 to "left_ankle",
    16 to "right_ankle"
)

/**
 * Pair of points which define body edges.
 */
public val edgeKeyPointsPairs: List<Pair<Int, Int>> = listOf(
    Pair(0, 1),
    Pair(0, 2),
    Pair(1, 3),
    Pair(2, 4),
    Pair(0, 5),
    Pair(0, 6),
    Pair(5, 7),
    Pair(7, 9),
    Pair(6, 8),
    Pair(8, 10),
    Pair(5, 6),
    Pair(5, 11),
    Pair(6, 12),
    Pair(11, 12),
    Pair(11, 13),
    Pair(13, 15),
    Pair(12, 14),
    Pair(14, 16)
)
