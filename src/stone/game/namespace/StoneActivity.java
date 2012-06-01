package stone.game.namespace;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

// ---

public class StoneActivity extends Activity 
{
    /** Called when the activity is first created. */
    
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE); // ������ �����
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(new GameView(this));
               
    };
    //----------------------------------------------------------------

}