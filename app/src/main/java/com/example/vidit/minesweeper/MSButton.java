package com.example.vidit.minesweeper;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.webkit.WebSettings;


public class MSButton extends AppCompatButton
{
    private boolean isAMine=false,flag=false;
    public boolean revealed=false;
    private int nearbyMines=0;
    public MSButton(Context context)
    {
        super(context);
    }
    public void setAsMine()
    {
        isAMine=true;
        //setText("M");
    }
    public boolean status()
    {
        return isAMine;
    }
    public void addMine()
    {
        nearbyMines++;
        //setText(String.valueOf(nearbyMines));
    }
    public void toggleStatus()
    {
        if(isAMine==true)
        {
            isAMine=false;
        }
    }
    public void setZero()
    {
        nearbyMines=0;
    }
    public void showButton()
    {
        setText(String.valueOf(nearbyMines));
        revealed=true;
        setBackgroundColor(Color.parseColor("#B1B5B5"));
        if(getText().toString()=="0")
        {
            setTextColor(Color.YELLOW);
            setTextSize(25);
        }
        else if(getText().toString()=="1")
        {
            setTextColor(Color.BLUE);
            setTextSize(25);
        }
        else if(getText().toString()=="2")
        {
            setTextColor(Color.GREEN);
            setTextSize(25);
        }
        else if(getText().toString()=="3")
        {
            setTextColor(Color.RED);
            setTextSize(25);
        }
        else
        {
            setTextColor(Color.parseColor("#000080"));
            setTextSize(25);
        }
        setEnabled(false);
    }
    public int numOfMines()
    {
        return nearbyMines;
    }
    public void unsetFlag()
    {
        flag=false;
        setClickable(true);
        setText("");
    }
    public void setFlag()
    {
        setClickable(false);
        flag=true;
        setText("F");
        setTextSize(25);
        setTextColor(Color.RED);
    }
    public boolean isFlag()
    {
        return flag;
    }
}
