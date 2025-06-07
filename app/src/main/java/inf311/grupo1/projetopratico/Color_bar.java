package inf311.grupo1.projetopratico;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;


public class Color_bar extends View
{

    private  Paint paint;

    /*public Color_bar(Context context) {
        super(context);
        init();
    }*/

    public Color_bar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Color_bar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        paint.setColor(Color.RED);  // Cor padrão

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Color_bar);
            int color = a.getColor(R.styleable.Color_bar_bar_color, Color.RED);




            paint.setColor(color);

            a.recycle();
        } else {
            paint.setColor(Color.RED);

        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Desenha um retângulo preenchido que ocupa toda a View
        canvas.drawRoundRect(0, 0, getWidth(), getHeight(),20.0f,20.0f,paint);
    }

    public void set_color(int c)
    {
        paint.setColor(c);
    }
}

