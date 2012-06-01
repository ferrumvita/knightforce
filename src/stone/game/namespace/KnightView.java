package stone.game.namespace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import java.lang.*;


import android.util.DisplayMetrics;

public class KnightView {
	/** Объект класса GameView */
	private GameView gameView;

	/** Спрайт с анимацией*/
	private Bitmap currentSprite;
	private Bitmap flipSprite;
	
	public enum AnimationType {
	    BREATH, 
	    MOVE,
	    JUMP,
	}
	
	private Bitmap[] spritesAnimations;
	private AnimationType currentAnimationState;
	private int speedFramesAnimations[];
	private int nAnimationCount;

	/** IIоложение на экране */
	private int x = 5;
	private int y = 5;

	/** Sкорость */
	private int xAccel = 0;
	private int yAccel = 0;
	
	private int xSpeed = 0;
	private int ySpeed = 0;

	/** Текущий кадр = 0 */
	private int frames = 0; // Число кадров
	private int currentFrame = 0; 
	private int delayFramesAnimation = 6; // ол-во фреймов до обновлени€ анимации
 
	//If true, then face on right
	private boolean bFaceOrientRight = true;
	private boolean bKnightJump = false;
	private boolean bAnimationDirectionToRight = true;
	/** Высота */
	private int width;
	private int height;
	
	private int currentSpriteRows;
	private int currentSpriteColumns;
	
	private int spritesRows[];
	private int spritesColumns[];

	/**  онструктор */
	public KnightView(GameView gameView, AnimationType state) {
		
		this.gameView = gameView;
		
		nAnimationCount = AnimationType.values().length;
		spritesAnimations = new Bitmap [nAnimationCount];
		spritesColumns = new int [nAnimationCount];
		spritesRows = new int [nAnimationCount];
		speedFramesAnimations = new int [nAnimationCount];
		
	    loadResources();
	    setAnimationState(state);
	}
	
	public int getXAccel()
	{
		return this.xAccel;
	}
	
	public void setXSpeed(int xSpeed)
	{
		this.xSpeed = xSpeed;
	}
	
	public void setXAccel(int xAccel)
	{
		this.xAccel = xAccel;
	}
	
	public int getYAccel()
	{
		return this.yAccel;
	}
	
	
	public int getXSpeed()
	{
		return this.xSpeed;
	}
	
	public int getYSpeed()
	{
		return this.ySpeed;
	}
	
	public void setYSpeed(int ySpeed)
	{
		this.ySpeed = ySpeed;
	}
	
	public void setVelocity(int xSpeed, int ySpeed)
	{
		//Мы остановились, ориентируемся по старой скорости
		if(xSpeed == 0) {
			bFaceOrientRight = (this.xSpeed > 0 ? true : false);
		} else {
			bFaceOrientRight = (xSpeed > 0 ? true : false);
		}
		
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	public void setAccelerates(int xAccel, int yAccel)
	{
		this.xAccel = xAccel;
		this.yAccel = yAccel;
	}
	
	public void loadResources()
	{
		DisplayMetrics metrics = gameView.getMetrics();
		Bitmap resKnight = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.breath);
		Bitmap scaleKnight = Bitmap.createScaledBitmap(resKnight, (int)(resKnight.getWidth()*metrics.scaledDensity), 
		        		(int)(resKnight.getHeight()*metrics.scaledDensity), true);

		spritesAnimations[AnimationType.BREATH.ordinal()] = scaleKnight;
		spritesColumns[AnimationType.BREATH.ordinal()] = 4;
		spritesRows[AnimationType.BREATH.ordinal()] = 1;
		speedFramesAnimations[AnimationType.BREATH.ordinal()] = 6;
		
		resKnight = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.run);
		scaleKnight = Bitmap.createScaledBitmap(resKnight, (int)(resKnight.getWidth()*metrics.scaledDensity), 
		        		(int)(resKnight.getHeight()*metrics.scaledDensity), true);
		

		spritesAnimations[AnimationType.MOVE.ordinal()] = scaleKnight;
		spritesColumns[AnimationType.MOVE.ordinal()] = 4;
		spritesRows[AnimationType.MOVE.ordinal()] = 2;

		speedFramesAnimations[AnimationType.MOVE.ordinal()] = (25 / 
				spritesColumns[AnimationType.MOVE.ordinal()] * spritesRows[AnimationType.MOVE.ordinal()])/4;

		spritesAnimations[AnimationType.MOVE.ordinal()] = scaleKnight;				
	}
	
	public void setAnimationState(AnimationType state)
	{
		if(currentAnimationState == state)
			return;
		
		currentSprite = spritesAnimations[state.ordinal()];
		currentAnimationState = state;
		
		currentSpriteRows = spritesRows[state.ordinal()];
		currentSpriteColumns = spritesColumns[state.ordinal()];
		delayFramesAnimation = speedFramesAnimations[state.ordinal()];
		
	    this.width = currentSprite.getWidth() / currentSpriteColumns;
	    this.height = currentSprite.getHeight() / currentSpriteRows;
	    
	    currentFrame = 0;
	   
	    flipSprite = flip(new BitmapDrawable(currentSprite) ).getBitmap();
	}

	public void update(long ticksPerSecond) {
		++frames;
		//Next frame ready to change?
		int iNextFrameReady = (frames % delayFramesAnimation == 0 ? 1 : 0);
		
		//Count of all frames
		int nTotalFrames = (currentSpriteRows * currentSpriteColumns);
		
		if(xAccel > xSpeed && xSpeed >= 0)
		{
			xAccel = xSpeed;
		}
		
		if(xSpeed >= 0)
			xSpeed -= xAccel;
		else
			xSpeed += xAccel;
		
		ySpeed += yAccel;
		
		x += xSpeed;
		y += ySpeed;
		
		delayFramesAnimation = speedFramesAnimations[currentAnimationState.ordinal()] - xSpeed / 20;
		
		//Не даем выйти за границы экрана
		if (x > gameView.getMetrics().widthPixels - width)
			x = gameView.getMetrics().widthPixels - width;
		if (x < 0 )
			x = 0;
		
		if (y > gameView.getMetrics().heightPixels - (int)(height*1.2f)) {

	    	y = gameView.getMetrics().heightPixels - (int)(height*1.2f);
	    	   
			ySpeed = 0;
			yAccel = 0;
		}
		
		currentFrame = (iNextFrameReady + currentFrame) % (nTotalFrames);
	}
	
	BitmapDrawable flip(BitmapDrawable d)
	{
	    Matrix m = new Matrix();
	    m.preScale(-1, 1);
	    Bitmap src = d.getBitmap();
	    Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
	    dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
	    return new BitmapDrawable(dst);
	}

	public void onDraw(Canvas canvas) {
		int srcX = (currentFrame % currentSpriteColumns) * width;
		int srcY = (currentFrame / currentSpriteColumns) * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		
		//Отражаем спрайт игрока
		if ( bFaceOrientRight == true ) {
			currentSprite = spritesAnimations[currentAnimationState.ordinal()];
			bAnimationDirectionToRight = true;
		} else {
			currentSprite = flipSprite;
			if( currentAnimationState == AnimationType.MOVE )
				bAnimationDirectionToRight = false;
		}
		
		canvas.drawBitmap(currentSprite, src, dst, null);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void move(int x , int y) {
		this.x = x;
		this.y = y;
	}
}