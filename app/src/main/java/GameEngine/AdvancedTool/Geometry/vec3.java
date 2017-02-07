package GameEngine.AdvancedTool.Geometry;

/**
 * Created by Quang Tung on 12/27/2015.
 */
public class vec3 {
    public static final vec3 i = new vec3(1, 0, 0);
    public static final vec3 j = new vec3(0, 1, 0);
    public static final vec3 k = new vec3(0, 0, 1);

    public float x, y, z;

    public vec3() {
        x = y = z = 0;
    }

    public vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public vec3(vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void toArray(float[] array) {
        array[0] = x;
        array[1] = y;
        array[2] = z;
    }

    public void assignTo(vec3 v) {
        v.x = this.x;
        v.y = this.y;
        v.z = this.z;
    }

    public vec3 add(vec3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public vec3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public vec3 negative() {
        x = -x;
        y = -y;
        x = -z;
        return this;
    }

    public vec3 sub(vec3 v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public vec3 sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public vec3 mul(float k) {
        this.x *= k;
        this.y *= k;
        this.z *= k;
        return this;
    }

    public float len() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public vec3 nor() {
        float len = (float)Math.sqrt(x * x + y * y + z * z);
        this.x /= len;
        this.y /= len;
        this.z /= len;
        return this;
    }

    public final vec3 cross(vec3 v) {
        final vec3 tmp = new vec3();

        this.assignTo(tmp);

        // i  j  k
        // x  y  z  : tmp
        // x  y  z  : v

        this.x = tmp.y * v.z - tmp.z * v.y;
        this.y = tmp.z * v.x - tmp.x * v.z;
        this.z = tmp.x * v.y - tmp.y * v.x;

        return this;
    }

    public final float dot(vec3 v) {
        return x*v.x + y*v.y + z*v.z;
    }


    public final void toMatrix(vec3 Ox, vec3 Oy, vec3 Oz, float[] m) {
        // 0  4  8   12
        // 1  5  9   13
        // 2  6  10  14
        // 3  7  11  15

        m[12] = this.x;
        m[13] = this.y;
        m[14] = this.z;
        m[15] = 1;

        final vec3 tmpOx = new vec3();
        final vec3 tmpOy = new vec3();
        final vec3 tmpOz = new vec3();

        if (Oz == null) {
            Ox.assignTo(tmpOx);
            Oy.assignTo(tmpOy);

            tmpOx.nor(); //Normalize Ox

            final vec3 v = new vec3();
            tmpOx.assignTo(v); // v = Ox
            //v = (Oy.Ox) * Ox
            v.mul(tmpOy.dot(tmpOx));
            //Oy = Oy - v
            tmpOy.sub(v);
            tmpOy.nor();

            //Oz = Ox x Oy
            tmpOx.assignTo(tmpOz);
            tmpOz.cross(tmpOy);

        } else if (Oy == null) {
            Ox.assignTo(tmpOx);
            Oz.assignTo(tmpOz);

            tmpOx.nor();

            final vec3 v = new vec3();
            tmpOx.assignTo(v);

            // v = (Oz.Ox) * Ox
            v.mul(tmpOx.dot(tmpOz));

            // Oz = Oz - v
            tmpOz.sub(v);
            tmpOz.nor();

            //Oy = Oz x Ox
            tmpOz.assignTo(tmpOy);
            tmpOy.cross(tmpOx);
        }

        m[0] = tmpOx.x;
        m[1] = tmpOx.y;
        m[2] = tmpOx.z;
        m[3] = 0;

        m[4] = tmpOy.x;
        m[5] = tmpOy.y;
        m[6] = tmpOy.z;
        m[7] = 0;

        m[8] = tmpOz.x;
        m[9] = tmpOz.y;
        m[10] = tmpOz.z;
        m[11] = 0;
    }

}
