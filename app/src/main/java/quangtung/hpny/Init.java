package quangtung.hpny;

import GameEngine.AdvancedTool.Interface.Initializer;
import GameEngine.AdvancedTool.Screen;

/**
 * Created by Quang Tung on 2/1/2016.
 */
public class Init implements Initializer {
    public static Screen screen;

    @Override
    public void onInitialize() {
        new CrazyWork();

        screen = new HPNYScreen();
    }
}
