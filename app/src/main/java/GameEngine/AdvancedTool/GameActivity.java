package GameEngine.AdvancedTool;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Quang Tung on 1/4/2016.
 */
public class GameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AT.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AT.onPause();
    }
}
