package quangtung.hpny;

import android.os.Bundle;

import GameEngine.AdvancedTool.AT;
import GameEngine.AdvancedTool.GameActivity;

public class MainActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AT(this, new Init());
    }
}
