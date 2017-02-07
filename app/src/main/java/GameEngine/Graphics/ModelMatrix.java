package GameEngine.Graphics;

import android.opengl.GLES20;
import android.opengl.Matrix;

import GameEngine.AdvancedTool.Geometry.Quaternion;
import GameEngine.AdvancedTool.Geometry.vec2;
import GameEngine.AdvancedTool.Geometry.vec3;

/**
 * Created by Quang Tung on 12/16/2015.
 */
public class ModelMatrix {
    public static final float PI = 3.14159265f;
    private float[] matrix = new float[16];

    public ModelMatrix() {
        Matrix.setIdentityM(matrix, 0);
    }

    //Used in Render Thread
    public final void bind(int loc){
        synchronized (this) {
            GLES20.glUniformMatrix4fv(loc, 1, false, matrix, 0);
        }
    }

    //Used in logic thread
    public final void fromVec2(vec2 translate, vec2 Ox) {
        synchronized (this) {
            translate.toMatrix(Ox, this.matrix);
        }
    }

    //Used in logic thread
    public final void fromQuaternion(Quaternion q, vec3 translate) {
        synchronized (this) {
            q.toMatrix(translate, matrix);
        }
    }

    public final void translate(float x, float y, float z) {
        synchronized (this) {
            Matrix.translateM(matrix, 0, x, y, z);
        }
    }

    public final void identity() {
        synchronized (this) {
            Matrix.setIdentityM(matrix, 0);
        }
    }

    //Used in logic thread
    public final void fromVec3(vec3 translate, vec3 Ox, vec3 Oy, vec3 Oz) {
        synchronized (this) {
            translate.toMatrix(Ox, Oy, Oz, matrix);
        }
    }
}
