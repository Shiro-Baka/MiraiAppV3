package com.example.miraiappv2;


import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Button;
import android.widget.GridLayout;
import androidx.appcompat.widget.AppCompatDrawableManager;
import com.example.miraiappv2.R;


public class BubbleMatchButton extends androidx.appcompat.widget.AppCompatButton {

    protected int row;
    protected int col;
    protected int frontDrawableId;

    protected boolean isMatched = false;

    protected Drawable front;

    @SuppressLint("RestrictedApi")
    public BubbleMatchButton(Context context, int r, int c, int frontImageDrawableId)
    {
        super(context);

        row = r;
        col = c;
        frontDrawableId = frontImageDrawableId;

        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);

        setBackground(front);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));

        tempParams.width = (int) getResources().getDisplayMetrics().density * 150;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 100;
        tempParams.bottomMargin = (int) getResources().getDisplayMetrics().density * 10;

        setLayoutParams(tempParams);

    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }


    /*public void flip()
    {
        if(isMatched)
            return;
        if(isFlipped)
        {
            //setBackground(front);
            setBackground(back);
            isFlipped = false;
        }
        else
        {
            setBackground(front);
            isFlipped = true;
        }
    }*/
}
