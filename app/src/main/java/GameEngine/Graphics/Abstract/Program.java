package GameEngine.Graphics.Abstract;

import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import GameEngine.Basic.ListHead;
import GameEngine.Graphics.GLBasic;
import GameEngine.Graphics.ShaderFactory;

/**
 * Created by Quang Tung on 2/2/2016.
 */
public class Program {
    private int program = 0;
    private StringBuilder vsByteCode;
    private StringBuilder fsByteCode;

    private static final int MAX = 20;

    private static Uniform nulluniform = null;
    private static Changable nullchangable = null;
    private static Attribute nullattribute = null;

    private static UniformOp mat4op = null;
    private static UniformOp vec4op = null;
    private static UniformOp vec3op = null;
    private static UniformOp vec2op = null;
    private static UniformOp floatop = null;
    private static AttributeOp normalop = null;

    private int uniform_index = -1;
    private int changable_index = -1;
    private int attribute_index = -1;

    private class Uniform {
        int loc;
        float[] data;
        UniformOp op;
    }

    private class Changable {
        int loc;
        UniformOp op;
    }

    private class Attribute {
        int loc;
        AttributeOp op;
    }

    private class Texture {
        int loc;
    }

    private interface UniformOp {
        void bind(int loc, float[] data);
    }

    private interface AttributeOp {
        void enable(int loc);

        void disable(int loc);
    }

    List<Uniform> uniforms = new ArrayList<>(MAX);
    List<Changable> changables = new ArrayList<>(MAX);
    List<Attribute> attributes = new ArrayList<>(MAX);
    List<Texture> textures = new ArrayList<>(4);

    private void prepare() {
        if (nulluniform == null) {
            UniformOp op = new NullUniformOp();

            nulluniform = new Uniform();
            nulluniform.loc = -1;
            nulluniform.data = null;
            nulluniform.op = op;

            nullchangable = new Changable();
            nullchangable.loc = -1;
            nullchangable.op = op;

            nullattribute = new Attribute();
            nullattribute.loc = -1;
            nullattribute.op = new NullAttributeOp();
        }

        if (mat4op == null) {
            mat4op = new Mat4Op();
            vec3op = new Vec3Op();
            vec2op = new Vec2Op();
            vec4op = new Vec4Op();
            floatop = new FloatOp();

            normalop = new NormalOp();
        }

        for (int i = 0; i < MAX; i++) {
            uniforms.add(nulluniform);
            changables.add(nullchangable);
            attributes.add(nullattribute);
        }

        for (int i = 0; i < 4; i++)
            textures.add(null);
    }

    public Program(String vsFileName, String fsFileName) {
        vsByteCode = new StringBuilder(10000);
        fsByteCode = new StringBuilder(10000);

        InputStream in;
        String newLine = System.getProperty("line.separator");

        //Read vertex shader from file to vsByteCode
        try {
            in = GLBasic.readAsset(vsFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                vsByteCode.append(line);
                vsByteCode.append(newLine);
            }

            in.close();

        } catch (IOException e) {
            throw new RuntimeException("Can't load vertex shader!");
        }

        //Read fragment shader from file to fsByteCode
        try {
            in = GLBasic.readAsset(fsFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                fsByteCode.append(line);
                fsByteCode.append(newLine);
            }

            in.close();

        } catch (IOException e) {
            throw new RuntimeException("Can't load fragment shader!");
        }

        prepare();
    }

    public final void onSurfaceCreated() {
        int vs; //Vertex shader handler
        int fs; //Fragment shader handler
        //Compile vertex shader & fragment shader and create program
        vs = ShaderFactory.compile(GLES20.GL_VERTEX_SHADER, vsByteCode.toString());
        fs = ShaderFactory.compile(GLES20.GL_FRAGMENT_SHADER, fsByteCode.toString());
        program = ShaderFactory.createProgram(vs, fs);
    }

    public final void changable(String name, int index, int nrfloat) {
        int loc = GLES20.glGetUniformLocation(program, name);
        Changable changable = new Changable();
        changable.loc = loc;

        if (changable_index < index)
            changable_index = index;

        switch (nrfloat) {
            case 1:
                changable.op = floatop;
                changables.set(index, changable);
                break;
            case 2:
                changable.op = vec2op;
                changables.set(index, changable);
                break;
            case 3:
                changable.op = vec3op;
                changables.set(index, changable);
                break;
            case 4:
                changable.op = vec4op;
                changables.set(index, changable);
                break;
            case 16:
                changable.op = mat4op;
                changables.set(index, changable);
                break;
            default:
                Log.e("Program", "Error!");
                break;
        }
    }

    public final void uniform(String name, int index, int nrfloat, float[] data) {
        int loc = GLES20.glGetUniformLocation(program, name);
        Uniform uniform = new Uniform();
        uniform.loc = loc;
        uniform.data = data;

        if (uniform_index < index)
            uniform_index = index;

        switch (nrfloat) {
            case 1:
                uniform.op = floatop;
                uniforms.set(index, uniform);
                break;
            case 2:
                uniform.op = vec2op;
                uniforms.set(index, uniform);
                break;
            case 3:
                uniform.op = vec3op;
                uniforms.set(index, uniform);
                break;
            case 4:
                uniform.op = vec4op;
                uniforms.set(index, uniform);
                break;
            case 16:
                uniform.op = mat4op;
                uniforms.set(index, uniform);
                break;
            default:
                Log.e("Program", "Error!");
                break;
        }
    }

    public final void attribute(String name, int index) {
        int loc = GLES20.glGetAttribLocation(program, name);
        Attribute attribute = new Attribute();
        attribute.loc = loc;
        attribute.op = normalop;

        if (attribute_index < index)
            attribute_index = index;

        attributes.set(index, attribute);
    }

    public final void texture(String name, int unit) {
        int loc = GLES20.glGetUniformLocation(program, name);
        Texture tex = new Texture();
        tex.loc = loc;
        textures.set(unit, tex);
    }

    public final int getAttribLoc(int index) {
        return attributes.get(index).loc;
    }

    public final int getChangableLoc(int index) {
        return changables.get(index).loc;
    }

    public final void setChangable(int index, float[] data) {
        Changable changable = changables.get(index);
        changable.op.bind(changable.loc, data);
    }

    public final void use() {
        GLES20.glUseProgram(program);
    }

    public final void onDrawFrame(ListHead<VertexBuffer> bufferList) {
        //Enable
        for (int i = 0; i <= attribute_index; i++) {
            Attribute e = attributes.get(i);
            e.op.enable(e.loc);
        }

        //Uniform
        for (int i = 0; i <= uniform_index; i++) {
            Uniform uniform = uniforms.get(i);
            uniform.op.bind(uniform.loc, uniform.data);
        }

        //Texture
        for (int i = 0; i < textures.size(); i++)
            if (textures.get(i) != null)
                GLES20.glUniform1i(textures.get(i).loc, i);

        ListHead<VertexBuffer> entry = bufferList.next;
        while (entry != bufferList) {
            entry.object.onDrawFrame(this);
            entry = entry.next;
        }

        //Disable
        for (int i = 0; i <= attribute_index; i++) {
            Attribute e = attributes.get(i);
            e.op.disable(e.loc);
        }
    }


    //Uniform Ops
    private class NullUniformOp implements UniformOp {
        @Override
        public void bind(int loc, float[] data) {
        }
    }

    private class Mat4Op implements UniformOp {
        @Override
        public void bind(int loc, float[] data) {
            GLES20.glUniformMatrix4fv(loc, 1, false, data, 0);
        }
    }

    private class Vec3Op implements UniformOp {
        @Override
        public void bind(int loc, float[] data) {
            GLES20.glUniform3fv(loc, 1, data, 0);
        }
    }

    private class Vec2Op implements UniformOp {
        @Override
        public void bind(int loc, float[] data) {
            GLES20.glUniform2fv(loc, 1, data, 0);
        }
    }

    private class Vec4Op implements UniformOp {
        @Override
        public void bind(int loc, float[] data) {
            GLES20.glUniform4fv(loc, 1, data, 0);
        }
    }

    private class FloatOp implements UniformOp {
        @Override
        public void bind(int loc, float[] data) {
            GLES20.glUniform1f(loc, data[0]);
        }
    }


    //Attribute Ops
    private class NullAttributeOp implements AttributeOp {
        @Override
        public void enable(int loc) {

        }

        @Override
        public void disable(int loc) {

        }
    }

    private class NormalOp implements AttributeOp {
        @Override
        public void enable(int loc) {
            GLES20.glEnableVertexAttribArray(loc);
        }

        @Override
        public void disable(int loc) {
            GLES20.glDisableVertexAttribArray(loc);
        }
    }
}
