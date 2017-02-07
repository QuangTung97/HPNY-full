package GameEngine.Graphics;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by Quang Tung on 12/3/2015.
 */

//Used by shader class
public class ShaderFactory {
    //Compile the Shader ByteCode String
    public static int compile(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        final int[] compiled = new int[1];

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e("Graphics Tool", "Can't compile shader " + type + ":");
            Log.e("Graphics Tool", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            throw new RuntimeException("Can't compile shader!");
        }

        return shader;
    }

    //Create shader program from vertex shader
    //& fragment shader created by function above
    public static int createProgram(int vertexShader, int fragmentShader) {
        int shaderProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e("Graphics Tool", "Can't link program: ");
            Log.e("Graphics Tool", GLES20.glGetProgramInfoLog(shaderProgram));
            GLES20.glDeleteProgram(shaderProgram);
            throw new RuntimeException("Can't link program!");
        }

        return shaderProgram;
    }
}
