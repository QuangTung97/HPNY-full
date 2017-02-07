package quangtung.hpny.Effect;

import GameEngine.AdvancedTool.Geometry.vec3;
import GameEngine.Basic.Interfaces.Sound;
import GameEngine.Basic.ListHead;
import GameEngine.Graphics.Shader.PSShader;
import GameEngine.Graphics.Shader.ParticlesShader;
import GameEngine.Graphics.VertexBuffer.PSVB;
import GameEngine.Graphics.VertexBuffer.ParticlesVB;
import quangtung.hpny.Time.S;
import quangtung.hpny.Time.TimeObs;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public class FireWork implements TimeObs{
    private ListHead<TimeObs> list;
    public static final int STATE_START = 1;
    public static final int STATE_EXPLODE = 2;
    public static final int STATE_END = 3;

    private int state = 0;

    private float time = 0;
    private float start_end = 2;
    private float explode_end = 4;

    /** Vertex Buffer for firework */
    private PSVB tail;
    private PSVB head;
    private ParticlesVB streak;
    private ParticlesVB star;

    private Sound tail_sound;
    private Sound streak_sound;

    public FireWork() {
        list = new ListHead<TimeObs>(this);
    }

    @Override
    public ListHead<TimeObs> getList() {
        return list;
    }

    public final void setTail(PSVB tail) {
        this.tail = tail;
    }

    public final void setHead(PSVB head) {
        this.head = head;
        head.enableSwap();
    }

    public final void setStreak(ParticlesVB streak) {
        this.streak = streak;
    }

    public final void setStar(ParticlesVB star) {
        this.star = star;
    }

    public final void setTailSound(Sound sound) {
        this.tail_sound = sound;
    }

    public final void setStreakSound(Sound sound) {
        streak_sound = sound;
    }

    public final void setPosAndVelocity(float x, float y, float z,
                                        float vx, float vy, float vz) {
        tail.setVelocity(vx, vy, vz);
        head.setVelocity(vx, vy, vz);

        final vec3 tmp = new vec3();
        final vec3 a = new vec3();

        PSShader.get().accel.assignTo(a);
        a.mul(start_end).mul(start_end / 2.0f);
        tmp.set(vx, vy, vz);
        tmp.mul(start_end);
        tmp.add(x, y, z); //Pos
        tmp.add(a);

        tail.matrix = head.matrix;
        streak.matrix = star.matrix;
        tail.matrix.identity();
        tail.matrix.translate(x, y, z);

        streak.matrix.identity();
        streak.matrix.translate(tmp.x, tmp.y, tmp.z);
    }

    public final void start() {
        //entry of start state
        O.ops.add(PSShader.get(), tail);
        O.ops.add(PSShader.get(), head);
        tail_sound.play(20);
        S.subject.register(this);
        state = STATE_START;
    }

    @Override
    public void update(float dtime) {
        time += dtime;

        switch (state) {
            case STATE_START:
                if (time > start_end) {
                    //exit of start and entry of explode
                    O.ops.remove(head);
                    O.ops.remove(tail);
                    O.ops.add(ParticlesShader.get(), streak);
                    O.ops.add(ParticlesShader.get(), star);
                    streak_sound.play(40);
                    state = STATE_EXPLODE;
                }

                break;
            case STATE_EXPLODE:
                if (time >= explode_end) {
                    //exit of explode
                    state = STATE_END;

                    O.ops.remove(streak);
                    O.ops.remove(star);
                    S.subject.remove(this);

                    tail = null;
                    head = null;
                    streak = null;
                    star = null;
                    FireWorkQueue.get().push(this);
                }

                break;
            case STATE_END:
                break;
        }
    }
}
