package org.jetbrains.kotlinx.dl.example.app

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import org.jetbrains.kotlinx.dl.api.preprocessing.pipeline
import org.jetbrains.kotlinx.dl.impl.preprocessing.TensorLayout
import org.jetbrains.kotlinx.dl.impl.preprocessing.resize
import org.jetbrains.kotlinx.dl.impl.preprocessing.toFloatArray
import org.jetbrains.kotlinx.dl.onnx.inference.OrtSessionResultConversions.get2DFloatArray
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class SwingNet(
    context: Context,
    resources: Resources
){

    private var env: OrtEnvironment = OrtEnvironment.getEnvironment()
    private var session = loadModel(context, resources)

    fun directFloatBufferFromFloatArray(data: ArrayList<FloatArray>): FloatBuffer? {
        var buffer: FloatBuffer? = null
        val byteBuffer = ByteBuffer.allocateDirect(data.get(0).size * 4 * data.size)
        byteBuffer.order(ByteOrder.nativeOrder())
        buffer = byteBuffer.asFloatBuffer()
        for (i in data){
            buffer.put(FloatBuffer.wrap(i))
        }
        buffer.position(0)
        return buffer
    }

    fun argmax(inputs: Array<FloatArray>): IntArray {
        var n = inputs.size
        var m = inputs[0].size
        var currentMax = FloatArray(m) { _ -> 0.0f }
        var result = IntArray(m) { _ -> 0 }
        for (i in 0 until n) {
            for (j in 0 until m) {
                var curVal = inputs[i][j]
                if (curVal > currentMax[j]) {
                    currentMax[j] = curVal
                    result[j] = i
                }
            }
        }
        return result
    }

    fun inference(sourceArray: ArrayList<FloatArray>): Array<FloatArray> {
        val inputTensor = directFloatBufferFromFloatArray(sourceArray)
        val shape = longArrayOf(1, sourceArray.size.toLong(), 3, 160, 160)
        val tensorFromArray = OnnxTensor.createTensor(env, inputTensor, shape)
        var t1: OnnxTensor = tensorFromArray
        val inputs = mapOf<String, OnnxTensor>("input_1" to t1)
        val results = session.run(inputs)
        return results.get2DFloatArray("output_1")
    }

    fun predict(bitmaps: ArrayList<Bitmap>): IntArray {
        val array = ArrayList<FloatArray>()
        for (i in bitmaps){
            array.add(preprocessing.apply(i).first)
        }
        return argmax(inference(array))
    }

    fun close(){
        session.close()
    }

    private fun loadModel(context: Context, resources: Resources): OrtSession {
        val modelResourceId = resources.getIdentifier(
            "swingnet_norm",
            "raw",
            context.packageName
        )
        val inferenceModel = resources.openRawResource(modelResourceId).use { it.readBytes() }
        val options = OrtSession.SessionOptions()
        options.addCPU(true)
        return env.createSession(inferenceModel, OrtSession.SessionOptions())
    }


    val preprocessing = pipeline<Bitmap>()
        .resize {
            outputHeight = 160
            outputWidth = 160
        }
        .toFloatArray { layout = TensorLayout.NHWC }

}


