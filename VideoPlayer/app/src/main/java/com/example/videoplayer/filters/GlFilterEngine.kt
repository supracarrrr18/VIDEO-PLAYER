package com.example.videoplayer.filters

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class GlFilterEngine {

    private var programHandle = 0
    private var uSTMatrixHandle = 0
    private var aPositionHandle = 0
    private var aTextureCoordHandle = 0

    // Filter uniforms
    private var uBrightnessHandle = 0
    private var uContrastHandle = 0
    private var uSaturationHandle = 0
    private var uExposureHandle = 0
    private var uGammaHandle = 0
    private var uTemperatureHandle = 0
    private var uTintHandle = 0
    private var uHueHandle = 0
    private var uVibranceHandle = 0
    private var uHighlightsHandle = 0
    private var uShadowsHandle = 0
    private var uSharpnessHandle = 0
    private var uVignetteHandle = 0
    private var uFilmGrainHandle = 0
    private var uTexWidthHandle = 0
    private var uTexHeightHandle = 0
    private var uTimeHandle = 0

    private val vertexBuffer: FloatBuffer
    private val textureCoordBuffer: FloatBuffer

    private val squareCoords = floatArrayOf(
        -1.0f, -1.0f, 0.0f,
         1.0f, -1.0f, 0.0f,
        -1.0f,  1.0f, 0.0f,
         1.0f,  1.0f, 0.0f
    )

    private val textureCoords = floatArrayOf(
        0.0f, 0.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f
    )

    init {
        vertexBuffer = ByteBuffer.allocateDirect(squareCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(squareCoords)
        vertexBuffer.position(0)

        textureCoordBuffer = ByteBuffer.allocateDirect(textureCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(textureCoords)
        textureCoordBuffer.position(0)
    }

    fun init(vertexShaderSource: String, fragmentShaderSource: String) {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource)

        programHandle = GLES20.glCreateProgram()
        GLES20.glAttachShader(programHandle, vertexShader)
        GLES20.glAttachShader(programHandle, fragmentShader)
        GLES20.glLinkProgram(programHandle)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            val error = GLES20.glGetProgramInfoLog(programHandle)
            Log.e("GlFilterEngine", "Could not link GL program: $error")
            GLES20.glDeleteProgram(programHandle)
            programHandle = 0
            return
        }

        // Get attribute and uniform handles
        aPositionHandle = GLES20.glGetAttribLocation(programHandle, "aPosition")
        aTextureCoordHandle = GLES20.glGetAttribLocation(programHandle, "aTextureCoord")
        uSTMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uSTMatrix")

        uBrightnessHandle = GLES20.glGetUniformLocation(programHandle, "uBrightness")
        uContrastHandle = GLES20.glGetUniformLocation(programHandle, "uContrast")
        uSaturationHandle = GLES20.glGetUniformLocation(programHandle, "uSaturation")
        uExposureHandle = GLES20.glGetUniformLocation(programHandle, "uExposure")
        uGammaHandle = GLES20.glGetUniformLocation(programHandle, "uGamma")
        uTemperatureHandle = GLES20.glGetUniformLocation(programHandle, "uTemperature")
        uTintHandle = GLES20.glGetUniformLocation(programHandle, "uTint")
        uHueHandle = GLES20.glGetUniformLocation(programHandle, "uHue")
        uVibranceHandle = GLES20.glGetUniformLocation(programHandle, "uVibrance")
        uHighlightsHandle = GLES20.glGetUniformLocation(programHandle, "uHighlights")
        uShadowsHandle = GLES20.glGetUniformLocation(programHandle, "uShadows")
        uSharpnessHandle = GLES20.glGetUniformLocation(programHandle, "uSharpness")
        uVignetteHandle = GLES20.glGetUniformLocation(programHandle, "uVignette")
        uFilmGrainHandle = GLES20.glGetUniformLocation(programHandle, "uFilmGrain")
        uTexWidthHandle = GLES20.glGetUniformLocation(programHandle, "uTexWidth")
        uTexHeightHandle = GLES20.glGetUniformLocation(programHandle, "uTexHeight")
        uTimeHandle = GLES20.glGetUniformLocation(programHandle, "uTime")
    }

    fun renderFrame(
        textureId: Int,
        stMatrix: FloatArray,
        filter: VideoFilter,
        texWidth: Float,
        texHeight: Float,
        timeSeconds: Float
    ) {
        if (programHandle == 0) return

        GLES20.glUseProgram(programHandle)

        // Set vertices
        GLES20.glEnableVertexAttribArray(aPositionHandle)
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer)

        // Set texture coords
        GLES20.glEnableVertexAttribArray(aTextureCoordHandle)
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureCoordBuffer)

        // Set surface transform matrix
        GLES20.glUniformMatrix4fv(uSTMatrixHandle, 1, false, stMatrix, 0)

        // Bind OES Texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)

        // Upload filter uniforms
        GLES20.glUniform1f(uBrightnessHandle, filter.brightness)
        GLES20.glUniform1f(uContrastHandle, filter.contrast)
        GLES20.glUniform1f(uSaturationHandle, filter.saturation)
        GLES20.glUniform1f(uExposureHandle, filter.exposure)
        GLES20.glUniform1f(uGammaHandle, filter.gamma)
        GLES20.glUniform1f(uTemperatureHandle, filter.temperature)
        GLES20.glUniform1f(uTintHandle, filter.tint)
        GLES20.glUniform1f(uHueHandle, filter.hue)
        GLES20.glUniform1f(uVibranceHandle, filter.vibrance)
        GLES20.glUniform1f(uHighlightsHandle, filter.highlights)
        GLES20.glUniform1f(uShadowsHandle, filter.shadows)
        GLES20.glUniform1f(uSharpnessHandle, filter.sharpness)
        GLES20.glUniform1f(uVignetteHandle, filter.vignette)
        GLES20.glUniform1f(uFilmGrainHandle, filter.filmGrain)
        GLES20.glUniform1f(uTexWidthHandle, texWidth)
        GLES20.glUniform1f(uTexHeightHandle, texHeight)
        GLES20.glUniform1f(uTimeHandle, timeSeconds)

        // Draw quad
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(aPositionHandle)
        GLES20.glDisableVertexAttribArray(aTextureCoordHandle)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    fun release() {
        if (programHandle != 0) {
            GLES20.glDeleteProgram(programHandle)
            programHandle = 0
        }
    }
}
