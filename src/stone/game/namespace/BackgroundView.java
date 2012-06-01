package stone.game.namespace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import java.lang.*;

import stone.game.namespace.KnightView.AnimationType;


import android.util.DisplayMetrics;

public class BackgroundView 
{
	/** Ћбъект класса GameView */
	private GameView gameView;

	private Bitmap resBackMain;

    /**Конструктор*/
    public BackgroundView(GameView gameView) 
    {
          this.gameView=gameView;
          
          loadResources();

    }
	public void onDraw(Canvas canvas) 
	{
		canvas.drawBitmap(resBackMain, 5, 5, null);
	}
	public void loadResources()
	{
		DisplayMetrics metrics = gameView.getMetrics();
		resBackMain = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.background);
		
		resBackMain = Bitmap.createScaledBitmap(resBackMain, metrics.widthPixels, metrics.heightPixels, true);
			
	}
}