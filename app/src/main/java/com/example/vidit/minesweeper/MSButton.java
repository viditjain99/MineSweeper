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
    public void showButton()
    {
        setText(String.valueOf(nearbyMines));
        revealed=true;
        setBackgroundColor(Color.parseColor("#A8A8A8"));
        if(getText().toString()=="0")
        {
            setTextColor(Color.YELLOW);
        }
        else if(getText().toString()=="1")
        {
            setTextColor(Color.BLUE);
        }
        else if(getText().toString()=="2")
        {
            setTextColor(Color.GREEN);
        }
        else if(getText().toString()=="3")
        {
            setTextColor(Color.RED);
        }
        else
        {
            setTextColor(Color.parseColor("#000080"));
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
        //setBackground("@mipmap/ic_action_flag");
        setTextColor(Color.RED);
    }
    public boolean isFlag()
    {
        return flag;
    }
}
