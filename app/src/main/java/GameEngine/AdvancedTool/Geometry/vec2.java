package GameEngine.AdvancedTool.Geometry;

/**
 * Created by Quang Tung on 12/27/2015.
 */
public class vec2 {

    public float x, y;

    public vec2() {
        x = y = 0;
    }

    public vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public vec2(vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void assignTo(vec2 v) {
        v.x = this.x;
        v.y = this.y;
    }

    public vec2 add(vec2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public vec2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public vec2 sub(vec2 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public vec2 sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public vec2 mul(float k) {
        this.x *= k;
        this.y *= k;
        return this;
    }

    public vec2 nor() {
        float len = (float)Math.sqrt(x*x + y*y);
        this.x /= len;
        this.y /= len;
        return this;
    }

    public float len() {
        float len = (float)Math.sqrt(x*x + y*y);
        return len;
    }

    public boolean greater(vec2 v) {
        float len1 = this.x * this.x + this.y * this.y;
        float len2 = v.x * v.x + v.y * v.y;
        if (len1 > len2)
            return true;
        else
            return false;
    }

    public boolean less(vec2 v) {
        float len1 = this.x * this.x + this.y * this.y;
        float len2 = v.x * v.x + v.y * v.y;
        if (len1 < len2)
            return true;
        else
            return false;
    }

    public void toMatrix(vec2 Ox, float[] matrix) {
        final vec2 tmp = new vec2();

        matrix[15] = 1;
        matrix[12] = this.x;
        matrix[13] = this.y;
        matrix[14] = 0;

        matrix[3] = 0;
        matrix[7] = 0;
        matrix[11] = 0;

        Ox.assignTo(tmp);
        tmp.nor();

        matrix[0] = tmp.x;
        matrix[1] = tmp.y;
        matrix[2] = 0;

        matrix[8] = 0;
        matrix[9] = 0;
        matrix[10] = 1;

        matrix[4] = -tmp.y;
        matrix[5] = tmp.x;
        matrix[6] = 0;
    }
}
