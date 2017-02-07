package GameEngine.Graphics;

import GameEngine.Basic.ListHead;

/**
 * Created by Quang Tung on 12/26/2015.
 */
public class TextureRoot {
    public static ListHead<Texture> list;

    public TextureRoot() {
        list = new ListHead<>(null);
    }

    //Used in init
    //Not Used int any Other Thread
    public static void addTexture(Texture texture) {
        list.addLast(texture.list);
    }

    public static void onSufaceCreated() {
        Texture.allowInitialize = false;

        ListHead<Texture> entry = list.next;
        while (entry != list) {
            entry.object.onSurfaceCreated();
            entry = entry.next;
        }
    }
}
