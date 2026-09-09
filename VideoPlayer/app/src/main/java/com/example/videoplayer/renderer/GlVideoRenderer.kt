package com.example.videoplayer.renderer

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.Surface
import androidx.media3.exoplayer.ExoPlayer
import com.example.videoplayer.R
import com.example.videoplayer.filters.GlFilterEngine
import com.example.videoplayer.filters.VideoFilter
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlVideoRenderer(
    private val context: Context,
    private val player: ExoPlayer
) : GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private val filterEngine = GlFilterEngine()
    private var surfaceTexture: SurfaceTexture? = null
    private var outputSurface: Surface? = null
    private var textureId = 0
    private val stMatrix = FloatArray(16)

    @Volatile
    private var updateSurface = false

    var currentFilter: VideoFilter = VideoFilter()
        set(value) {
            field = value
        }

    var videoWidth: Int = 1920
    var videoHeight: Int = 1080
    var startTimeMs: Long = System.currentTimeMillis()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        textureId = textures[0]

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        val vertexShader = loadRawResource(R.raw.filter_vertex)
        val fragmentShader = loadRawResource(R.raw.filter_fragment)
        filterEngine.init(vertexShader, fragmentShader)

        val surfaceTex = SurfaceTexture(textureId)
        surfaceTex.setOnFrameAvailableListener(this)
        surfaceTexture = surfaceTex
        outputSurface = Surface(surfaceTex)

        player.setVideoSurface(outputSurface)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        synchronized(this) {
            if (updateSurface) {
                surfaceTexture?.updateTexImage()
                surfaceTexture?.getTransformMatrix(stMatrix)
                updateSurface = false
            }
        }

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val timeSeconds = (System.currentTimeMillis() - startTimeMs) / 1000.0f
        filterEngine.renderFrame(
            textureId = textureId,
            stMatrix = stMatrix,
            filter = currentFilter,
            texWidth = videoWidth.toFloat(),
            texHeight = videoHeight.toFloat(),
            timeSeconds = timeSeconds
        )
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        synchronized(this) {
            updateSurface = true
        }
    }

    private fun loadRawResource(resId: Int): String {
        val inputStream = context.resources.openRawResource(resId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }

    fun release() {
        filterEngine.release()
        outputSurface?.release()
        surfaceTexture?.release()
    }
}
