package GameEngine.AdvancedTool.Geometry;

import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by Quang Tung on 12/27/2015.
 */
public class Quaternion {
    public float a, b, c, d;

    public Quaternion() {
        a = 1;
        b = c = d = 0;
    }

    public Quaternion(Quaternion q) {
        a = q.a;
        b = q.b;
        c = q.c;
        d = q.d;
    }

    public Quaternion(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public final void set(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

    }

    public final void assignTo(Quaternion q) {
        q.a = this.a;
        q.b = this.b;
        q.c = this.c;
        q.d = this.d;
    }

    public final Quaternion fromVec3(vec3 v) {
        a = 0;
        b = v.x;
        c = v.y;
        d = v.z;
        return this;
    }

    public final void toVec3(vec3 v) {
        v.x = b;
        v.y = c;
        v.z = d;
    }

    public final void rotate(float angle, vec3 v) {
        angle = angle / 2.0f;

        a = (float) Math.cos(angle);
        float sin = (float)Math.sin(angle);

        final vec3 vec = new vec3();
        v.assignTo(vec);

        vec.nor();

        b = sin * vec.x;
        c = sin * vec.y;
        d = sin * vec.z;
    }

    public final Quaternion product(Quaternion q) {
        final Quaternion tmp = new Quaternion();
        assignTo(tmp);

        //a = a1a2 - b1b2 - c1c2 - d1d2
        //b = a1b2 + b1a2 + c1d2 - d1c2
        //c = a1c2 - b1d2 + c1a2 + d1b2
        //d = a1d2 + b1c2 - c1b2 + d1a2
        this.a = tmp.a*q.a - tmp.b*q.b - tmp.c*q.c - tmp.d*q.d;
        this.b = tmp.a*q.b + tmp.b*q.a + tmp.c*q.d - tmp.d*q.c;
        this.c = tmp.a*q.c - tmp.b*q.d + tmp.c*q.a + tmp.d*q.b;
        this.d = tmp.a*q.d + tmp.b*q.c - tmp.c*q.b + tmp.d*q.a;

        return this;
    }

    public final Quaternion conjugate() {
        b = -b;
        c = -c;
        d = -d;

        return this;
    }

    public final float lenSquare() {
        return a*a + b*b + c*c + d*d;
    }

    public final Quaternion inverse() {
        float lq = lenSquare();

        //q^-1 = q * / ||q||^2
        a = a / lq;
        b = -b / lq;
        c = -c / lq;
        d = -d / lq;
        return this;
    }

    //Interpolate


    public final Quaternion slerp(Quaternion q2, float t) {
        final Quaternion dq = new Quaternion();
        final vec3 tmpVec = new vec3(0, 0, 0);
        final Quaternion q1 = new Quaternion();

        q2.assignTo(dq);
        this.assignTo(q1);

        //dq = q2 * q1^-1
        dq.product(q1.inverse());

        float a, b, angle;
        float dqLen = (float)Math.sqrt(dq.lenSquare());
        // a = cos theta /2
        // b = sin theta /2
        a = dq.a / dqLen;

        dq.toVec3(tmpVec);
        float len = tmpVec.len();
        b = len / dqLen;

        angle = (float)Math.acos(a);
        if (b < 0)
            angle = -angle;

        angle *= t;

        tmpVec.mul(dqLen / len * (float) Math.sin(angle));
        dq.fromVec3(tmpVec);

        dq.a = dqLen * (float)Math.cos(angle);

        //q = q * dq;
        return this.product(dq);
    }

    public final void toMatrix(vec3 translate, float[] matrix) {
        final Quaternion q2 = new Quaternion();
        final Quaternion q1 = new Quaternion();

        final vec3 newI = new vec3();
        final vec3 newJ = new vec3();
        final vec3 newK = new vec3();

        matrix[15] = 1;
        matrix[12] = translate.x;
        matrix[13] = translate.y;
        matrix[14] = translate.z;

        // _ _ _ x
        // _ _ _ y
        // _ _ _ z
        // 0 0 0 1

        matrix[3] = 0;
        matrix[7] = 0;
        matrix[11] = 0;

        //q2 = q^-1
        //q1 = q
        assignTo(q2);
        q2.inverse();

        // i' = q1 tmp q2
        // j' = q1 tmp q2
        // k' = q1 tmp q2

        this.assignTo(q1);
        q1.productI().product(q2).toVec3(newI);

        this.assignTo(q1);
        q1.productJ().product(q2).toVec3(newJ);

        this.assignTo(q1);
        q1.productK().product(q2).toVec3(newK);


        matrix[0] = newI.x;
        matrix[1] = newI.y;
        matrix[2] = newI.z;

        matrix[4] = newJ.x;
        matrix[5] = newJ.y;
        matrix[6] = newJ.z;

        matrix[8] = newK.x;
        matrix[9] = newK.y;
        matrix[10] = newK.z;
    }

    private Quaternion productI() {
        final Quaternion tmp = new Quaternion();
        assignTo(tmp);

        this.a = -tmp.b;
        this.b = tmp.a;
        this.c = tmp.d;
        this.d = -tmp.c;

        return this;
    }

    private Quaternion productJ() {
        final Quaternion tmp = new Quaternion();
        assignTo(tmp);

        a = -tmp.c;
        b = -tmp.d;
        c = tmp.a;
        d = tmp.b;

        return this;
    }

    private Quaternion productK() {
        final Quaternion tmp = new Quaternion();
        assignTo(tmp);

        a = -tmp.d;
        b = tmp.c;
        c = -tmp.b;
        d = tmp.a;

        return this;
    }
}
