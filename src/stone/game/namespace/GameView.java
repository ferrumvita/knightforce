package stone.game.namespace;

import stone.game.namespace.KnightView.AnimationType;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.*;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class GameView extends SurfaceView  
{
       /**Загружаемая картинка*/
      // private Bitmap background;
      // private Bitmap knight;
       
       /**Объект класса Sprite*/
       private KnightView knight;
       private BackgroundView background;
       // Получения разрешений
       private WindowManager mWindowManager;
       private DisplayMetrics metrics; 
       
       public DisplayMetrics getMetrics() {
			return metrics;
		}
	
	
		public void setMetrics(DisplayMetrics metrics) {
			this.metrics = metrics;
		}
	
		private Display mDisplay;
       
       /**Объект класса GameManager*/
       private GameManager gameLoopThread;
       
       /**Наше поле рисования*/
       private SurfaceHolder holder;
 
       //конструктор
       public GameView(Context context) 
       {
             super(context);
             holder = getHolder();
             
             gameLoopThread = new GameManager(this);
             holder.addCallback(new SurfaceHolder.Callback() 
             {
                    public void surfaceDestroyed(SurfaceHolder holder) 
                    {
                        boolean retry = true;
                        gameLoopThread.setRunning(false);
                        while (retry) {
                               try {
                                     gameLoopThread.join();
                                     retry = false;
                               } catch (InterruptedException e) {
                               }
                        }
                    }
 
                    public void surfaceCreated(SurfaceHolder holder) 
                    {
                        gameLoopThread.setRunning(true);
                        gameLoopThread.start();
                    }
                    
                    // зачем это?
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
                    {
                    }
                    
                    //
             });
             // Get an instance of the WindowManager
             mWindowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
             mDisplay = mWindowManager.getDefaultDisplay();
             
             metrics = new DisplayMetrics();
             mDisplay.getMetrics(metrics);
             //drow k and bg
             
             knight = new KnightView(this, KnightView.AnimationType.BREATH);
             background = new BackgroundView(this);
             
             //loadGlobalResources();
             initWorldState();
       }
       
       int nextAnimation = 0;
       // (Controllers) Display touch events
       
       
       // !!! Думаю стоит переместить в GameManager !!!
       
       
       public boolean onTouchEvent(MotionEvent event) 
       {
    	   //Џеребор анимации по касанию
    	   //nextAnimation = (nextAnimation+1) % KnightView.AnimationType.values().length;
    	   //knight.setAnimationState(KnightView.AnimationType.values()[ nextAnimation ]);
    	   
    	   
    	   //ПраваЯ часть экрана
    	   if( event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE ) 
    	   {
    		   if(event.getY() <= metrics.heightPixels / 3) { 
    			   
    			   if( knight.getYSpeed() == 0 && knight.getYAccel() == 0) {
    				   knight.setVelocity( knight.getXSpeed(), -150);
    				   knight.setAccelerates( knight.getXAccel(), 15);
    				   knight.setAnimationState(AnimationType.MOVE);
    			   }
    			   
    		   } else
    		   if (event.getY() > metrics.heightPixels / 3) {
    			   float presure = event.getPressure() * 1.4f;
    			  
    			   int xSpeed = (int)(15.f * presure) * (event.getX() > metrics.widthPixels / 2 ? 1 : -1); // Изменить константу на параметр диспленя / 800, примерно
    			   if(knight.getYAccel() == 0)
    				   knight.setVelocity(xSpeed, 0);
    			   knight.setAnimationState(AnimationType.MOVE);
    		   }
    		   return true;
    	   }
    	   
    	   
    	   if(event.getAction() == MotionEvent.ACTION_UP )
    	   {
    		   knight.setXAccel(5);
    		   knight.setAnimationState(AnimationType.BREATH);
    		   return true;
    	   }
    	   
           return super.onTouchEvent(event);
       }
       
       
       private void initWorldState()
       {
    	   knight.move(mDisplay.getWidth()/2 - knight.getWidth()/2, mDisplay.getHeight() - (int)(knight.getHeight()*1.2f) );
       }
       /* Отрисовка заднего фона 
       private void loadGlobalResources()
       {
           Bitmap resBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
           background = Bitmap.crefateScaledBitmap(resBackground, metrics.widthPixels, metrics.heightPixels, true);
       }
       */
       protected void update(long ticksPerSecond)
       {
    	   knight.update(ticksPerSecond);
       }
 
       //Рисуем нашу картинку на черном фоне
      
       protected void onDraw(Canvas canvas) 
       {
    	   // Задний план
            background.onDraw(canvas); 
           // Герой  
             knight.onDraw(canvas);
       }
       
}