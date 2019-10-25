package com.spgroup.digital.psiindex.ui.views.renderers

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

class HazyBackgroundRenderer (private val vertexShader: String, private val fragmentShader: String) : GLSurfaceView.Renderer {

    private var startTime: Long = 0L
    private var resolutionLocation: Int = 0
    private var timeLocation: Int = 0
    private var positionLocation: Int = 0
    private var mvpMatrixLocation: Int = 0
    private var offsetLocation: Int = 0

    private val positionMatrix = floatArrayOf(
        -1f, -1f, 1f,
        1f, -1f, 1f,
        -1f, 1f, 1f,
        1f, 1f, 1f
    )

    private val mvpMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private val positionBuffer = ByteBuffer.allocateDirect(positionMatrix.size * 4)
        .order(ByteOrder.nativeOrder()).asFloatBuffer().put(positionMatrix)

    private val currentTime: Float
        get() = (System.currentTimeMillis() - startTime) / 1000f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1f, 1f, 1f, 0f)

        val iVShader = loadShader(vertexShader, GLES20.GL_VERTEX_SHADER)
        val iFShader = loadShader(fragmentShader, GLES20.GL_FRAGMENT_SHADER)

        initValues(startShadersProgram(iVShader, iFShader))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glUniform2f(resolutionLocation, width.toFloat(), height.toFloat())
        GLES20.glUniform2f(offsetLocation, Random.nextFloat(), Random.nextFloat())
        Matrix.frustumM(projectionMatrix, 0, -1f, 1f, -1f, 1f, 1f, 2f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        GLES20.glUniform1f(timeLocation, currentTime)

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 2f, 0f, 0f, -1f, 0f, 1f, 0f)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0)

        positionBuffer.position(0)
        GLES20.glVertexAttribPointer(positionLocation, 3, GLES20.GL_FLOAT, false, 0, positionBuffer)
        GLES20.glEnableVertexAttribArray(positionLocation)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun initValues(programId: Int) {
        startTime = System.currentTimeMillis()
        positionLocation = GLES20.glGetAttribLocation(programId, "position")
        mvpMatrixLocation = GLES20.glGetUniformLocation(programId, "mvpMatrix")
        resolutionLocation = GLES20.glGetUniformLocation(programId, "resolution")
        timeLocation = GLES20.glGetUniformLocation(programId, "time")
        offsetLocation = GLES20.glGetUniformLocation(programId, "offset")
    }

    private fun startShadersProgram(iVShader: Int, iFShader: Int): Int {
        val programId = GLES20.glCreateProgram()
        val link  = IntArray(1)

        GLES20.glAttachShader(programId, iVShader)
        GLES20.glAttachShader(programId, iFShader)
        GLES20.glLinkProgram(programId)

        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, link, 0)
        if (link[0] != GLES20.GL_TRUE)
            throw RuntimeException("GLSL program failed to load")

        GLES20.glDeleteShader(iVShader)
        GLES20.glDeleteShader(iFShader)
        GLES20.glUseProgram(programId)

        return programId
    }

    private fun loadShader(strSource: String, iType: Int): Int {
        val compiled = IntArray(1)
        val iShader = GLES20.glCreateShader(iType)
        GLES20.glShaderSource(iShader, strSource)
        GLES20.glCompileShader(iShader)
        GLES20.glGetShaderiv(iShader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] != GLES20.GL_TRUE)
            throw RuntimeException("Shader compilation failed : " + GLES20.glGetShaderInfoLog(iShader))
        return iShader
    }
}