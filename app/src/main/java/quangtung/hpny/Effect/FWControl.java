package quangtung.hpny.Effect;

import java.util.Random;

import GameEngine.AdvancedTool.Geometry.vec3;
import GameEngine.Basic.Basic;
import GameEngine.Basic.Interfaces.Sound;
import GameEngine.Basic.ListHead;
import GameEngine.Graphics.VertexBuffer.PSVB;
import GameEngine.Graphics.VertexBuffer.ParticlesVB;
import quangtung.hpny.RandomList;
import quangtung.hpny.Time.S;
import quangtung.hpny.Time.TimeObs;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public class FWControl implements TimeObs, RestartObs{
    private ListHead<TimeObs> list = new ListHead<TimeObs>(this);
    private FireWorkQueue queue;
    public Random random = new Random();

    RandomList<PSVB> tails = new RandomList<>();
    RandomList<PSVB> heads = new RandomList<>();
    RandomList<StreakStar> ss = new RandomList<>();

    RandomList<Sound> tail_sounds = new RandomList<>();
    RandomList<Sound> streak_sounds = new RandomList<>();

    private static final int STAGE1 = 1;
    private static final int STAGE2 = 2;
    private static final int STAGE_END = 3;

    private static final int AVERAGE_NR_FIREWORK = 4;
    private static final float FLOOR_Y = -4;
    private static final float RMIN = 2;
    private static final float RMAX = 4;
    private static final float VMIN = 3.3f;
    private static final float VMAX = 3.7f;
    private static final float PI = (float)Math.PI;

    private int stage = 0;

    public float end_stage1 = 30;
    public float end_stage2 = 35;

    private void __setting() {
        tails.add(new PSVB("tail/1.ft", "tail/1.png"));
        tails.add(new PSVB("tail/2.ft", "tail/2.png"));
        tails.add(new PSVB("tail/3.ft", "tail/3.png"));

        heads.add(new PSVB("head/1.ft", "head/1.png"));
        heads.add(new PSVB("head/2.ft", "head/2.png"));
        heads.add(new PSVB("head/3.ft", "head/3.png"));


        ss.add(new StreakStar(
                new ParticlesVB("streak/streak1.pts", "streak/streak1.png"),
                new ParticlesVB("streak/star1.pts", "streak/star1.png")));
        ss.add(new StreakStar(
                new ParticlesVB("streak/streak2.pts", "streak/streak2.png"),
                new ParticlesVB("streak/star2.pts", "streak/star2.png")));
        ss.add(new StreakStar(
                new ParticlesVB("streak/streak3.pts", "streak/streak3.png"),
                new ParticlesVB("streak/star3.pts", "streak/star3.png")));
        ss.add(new StreakStar(
                new ParticlesVB("streak/streak4.pts", "streak/streak4.png"),
                new ParticlesVB("streak/star4.pts", "streak/star4.png")));

        //Sounds
        tail_sounds.add(Basic.newSound("music/tail.ogg"));
        tail_sounds.add(Basic.newSound("music/tail2.ogg"));

        streak_sounds.add(Basic.newSound("music/streak.ogg"));
        streak_sounds.add(Basic.newSound("music/streak2.ogg"));
    }

    public FWControl() {
        S.subject.register(this);
        O.rs.register(this);

        queue = FireWorkQueue.get();
        for (int i = 0; i < 40; i++) {
            FireWork ins = new FireWork();
            queue.push(ins);
        }

        __setting();
    }

    @Override
    public ListHead<TimeObs> getList() {
        return list;
    }

    private float interval() {
        int nr = random.nextInt() % 1000;
        if (nr < 1000)
            nr += 1000;
        return (float)nr / 999.0f;
    }

    private void ____gen_firework() {
        FireWork fw = queue.take();

        StreakStar s = ss.getRandom(random);

        fw.setTail(tails.getRandom(random).vbClone());
        fw.setHead(heads.getRandom(random).vbClone());
        fw.setStreak(s.streak.vbClone());
        fw.setStar(s.star.vbClone());

        fw.setTailSound(tail_sounds.getRandom(random));
        fw.setStreakSound(streak_sounds.getRandom(random));

        //calculate velocity and position
        final vec3 pos = new vec3();
        final vec3 Ox = new vec3();
        final vec3 Oy = new vec3(0, 1, 0);
        final vec3 Oz = new vec3();

        float x, y, z;
        float phi1, phi2, teta;
        float radius;
        float u;

        u = interval();
        radius = RMIN * u + RMAX * (1 - u);

        u = interval();
        phi1 = PI * 2 * u;
        u = interval();
        phi2 = -PI / 6 * u + PI / 6 * (1 - u);
        u = interval();
        teta = PI / 6 * (1 - u);

        pos.x = (float)Math.sin(phi1);
        pos.y = 0;
        pos.z = (float)Math.cos(phi1);
        pos.assignTo(Oz);
        Oz.mul(-1);
        pos.mul(radius);
        pos.y = FLOOR_Y;

        Oy.assignTo(Ox);
        Ox.cross(Oz);

        u = interval();
        float vlen = VMIN * u + VMAX * (1 - u);
        y = vlen * (float)Math.cos(teta);
        x = vlen * (float)Math.sin(teta) * (float)Math.sin(phi2);
        z = vlen * (float)Math.sin(teta) * (float)Math.cos(phi2);

        final vec3 v = new vec3();
        v.add(Ox.mul(x)).add(Oy.mul(y)).add(Oz.mul(z));

        fw.setPosAndVelocity(pos.x, pos.y, pos.z,
                            v.x, v.y, v.z);
        fw.start();
    }

    private float prev_time = 0;
    private void __generate() {
        float dt = currtime - prev_time;
        if (dt >= 0.25f) {
            prev_time = currtime;

            int rand = random.nextInt() % 16;
            if (rand < 0)
                rand += 16;

            if (rand < AVERAGE_NR_FIREWORK) {
                ____gen_firework();
            }
        }
    }

    private void __generate_end() {
        ____gen_firework();
        ____gen_firework();
        ____gen_firework();
        ____gen_firework();
        ____gen_firework();
    }

    float currtime = 0;
    @Override
    public void update(float dtime) {
        currtime += dtime;
        switch (stage) {
            case STAGE1:
                if (currtime > end_stage1) {
                    stage = STAGE2;
                    //entry of stage2 and exit of stage 1
                }
                __generate();
                break;
            case STAGE2:
                if (currtime > end_stage2) {
                    stage = STAGE_END;
                    //entry of end stage
                    __generate_end();
                }
                break;
            case STAGE_END:
                break;
        }
    }

    @Override
    public void restart() {
        stage = STAGE1;
        //entry of stage 1
        currtime = 0;
    }
}
