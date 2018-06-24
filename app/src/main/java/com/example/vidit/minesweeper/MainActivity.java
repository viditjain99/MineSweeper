package com.example.vidit.minesweeper;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener
{
    LinearLayout rootLayout;
    TextView nameTextView,flagTextView,timeTextView;
    public int SIZE=8;
    public int numberOfMines=10;
    public int gameStatus=-1;
    public int incomplete=1;
    public int complete=2;
    public int checked;
    public long startTime;
    public long timeInMillies;
    public long finalTime;
    public long timeSwap;
    public int r;
    public ArrayList<LinearLayout> rows;
    public boolean firstClick=true;
    public MSButton[][] board;
    public boolean easy,medium,hard;
    public String name=" ";
    public int flags=0;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent=getIntent();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        name=intent.getStringExtra(Startup.NAME_KEY);
        checked=0;
        int id=intent.getIntExtra("LEVEL",8);
        if(id==R.id.Easy)
        {
            SIZE=8;
            numberOfMines=10;
            flags=numberOfMines;
        }
        else if(id==R.id.Medium)
        {
            SIZE=9;
            numberOfMines=13;
            flags=numberOfMines;
        }
        else if(id==R.id.Hard)
        {
            SIZE=10;
            numberOfMines=18;
            flags=numberOfMines;
        }
        rootLayout=findViewById(R.id.rootLayout);
        setupBoard();
        easy=true;
        setupMines();
        updateNeighbours();
    }
    public void setupBoard()
    {
        rows = new ArrayList<>();
        r=5;
        checked=0;
        gameStatus=incomplete;
        rootLayout.removeAllViews();
        startTime=0L;
        finalTime=0L;
        timeSwap=0L;
        timeInMillies=0L;
        board=new MSButton[SIZE][SIZE];
        easy=false;
        medium=false;
        hard=false;
        LinearLayout infoLayout=new LinearLayout(this);
        infoLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams infoParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200);
        infoLayout.setLayoutParams(infoParams);
        infoLayout.setBackgroundColor(Color.parseColor("#86C9AA"));
        rootLayout.addView(infoLayout);
        nameTextView=new TextView(this);
        flagTextView=new TextView(this);
        timeTextView=new TextView(this);
        TableRow.LayoutParams params1=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f);
        nameTextView.setLayoutParams(params1);
        nameTextView.setGravity(Gravity.CENTER);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f);
        flagTextView.setLayoutParams(params2);
        flagTextView.setGravity(Gravity.CENTER);
        TableRow.LayoutParams params3=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f);
        timeTextView.setLayoutParams(params3);
        timeTextView.setGravity(Gravity.CENTER);
        infoLayout.addView(nameTextView);
        infoLayout.addView(timeTextView);
        infoLayout.addView(flagTextView);
        String n="Player Name: "+"<b>"+name+"</b>";
        nameTextView.setText(Html.fromHtml(n));
        nameTextView.setTextSize(20);
        nameTextView.setTextColor(Color.parseColor("#595B5B"));
        String f="Flags: "+"<b>"+flags+"</b>";
        flagTextView.setText(Html.fromHtml(f));
        flagTextView.setTextColor(Color.parseColor("#595B5B"));
        flagTextView.setTextSize(20);
        String t="<b>"+"0"+"</b>";
        timeTextView.setText(Html.fromHtml(t));
        timeTextView.setTextColor(Color.parseColor("#595B5B"));
        timeTextView.setTextSize(20);
        for(int i=0;i<SIZE;i++)
        {
            LinearLayout linearLayout=new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1);
            linearLayout.setLayoutParams(layoutParams);
            rootLayout.addView(linearLayout);
            rows.add(linearLayout);
        }
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                MSButton button=new MSButton(this);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                button.setLayoutParams(layoutParams);
                LinearLayout row=rows.get(i);
                row.addView(button);
                board[i][j]=button;
                button.setOnClickListener(this);
                button.setOnLongClickListener(this);
                //button.setText("0");
            }
        }
    }
    public void setupMines()
    {
        int minesPlaced=0;
        Random random=new Random();
        while(minesPlaced<numberOfMines)
        {
            if(easy==true && medium==false && hard==false)
            {
                int x=random.nextInt(8);
                int y=random.nextInt(8);
                if(board[x][y].status()==false)
                {
                    board[x][y].setAsMine();
                    minesPlaced++;
                }
            }
            else if(easy==false && medium==true && hard==false)
            {
                int x=random.nextInt(9);
                int y=random.nextInt(9);
                if(board[x][y].status()==false)
                {
                    board[x][y].setAsMine();
                    minesPlaced++;
                }
            }
            else if(easy==false && medium==false && hard==true)
            {
                int x=random.nextInt(10);
                int y=random.nextInt(10);
                if(board[x][y].status()==false)
                {
                    board[x][y].setAsMine();
                    minesPlaced++;
                }
            }
        }
    }
    public void updateNeighbours()
    {
        if(board[0][0].status()==true)
        {
            if(board[0][1].status()==false)
            {
                board[0][1].addMine();
            }
            if(board[1][0].status()==false)
            {
                board[1][0].addMine();
            }
            if(board[1][1].status()==false)
            {
                board[1][1].addMine();
            }
        }
        if(board[0][SIZE-1].status()==true)
        {
            if(board[0][SIZE-2].status()==false)
            {
                board[0][SIZE-2].addMine();
            }
            if(board[1][SIZE-1].status()==false)
            {
                board[1][SIZE-1].addMine();
            }
            if(board[1][SIZE-2].status()==false)
            {
                board[1][SIZE-2].addMine();
            }
        }
        if(board[SIZE-1][0].status()==true)
        {
            if(board[SIZE-2][0].status()==false)
            {
                board[SIZE-2][0].addMine();
            }
            if(board[SIZE-1][1].status()==false)
            {
                board[SIZE-1][1].addMine();
            }
            if(board[SIZE-2][1].status()==false)
            {
                board[SIZE-2][1].addMine();
            }
        }
        if(board[SIZE-1][SIZE-1].status()==true)
        {
            if(board[SIZE-1][SIZE-2].status()==false)
            {
                board[SIZE-1][SIZE-2].addMine();
            }
            if(board[SIZE-2][SIZE-1].status()==false)
            {
                board[SIZE-2][SIZE-1].addMine();
            }
            if(board[SIZE-2][SIZE-2].status()==false)
            {
                board[SIZE-2][SIZE-2].addMine();
            }
        }
        for(int i=1;i<=SIZE-2;i++)
        {
            if(board[i][0].status()==true)
            {
                if(board[i-1][0].status()==false)
                {
                    board[i-1][0].addMine();
                }
                if(board[i-1][1].status()==false)
                {
                    board[i-1][1].addMine();
                }
                if(board[i][1].status()==false)
                {
                    board[i][1].addMine();
                }
                if(board[i+1][1].status()==false)
                {
                    board[i+1][1].addMine();
                }
                if(board[i+1][0].status()==false)
                {
                    board[i+1][0].addMine();
                }
            }
            if(board[i][SIZE-1].status()==true)
            {
                if(board[i-1][SIZE-1].status()==false)
                {
                    board[i-1][SIZE-1].addMine();
                }
                if(board[i-1][SIZE-2].status()==false)
                {
                    board[i-1][SIZE-2].addMine();
                }
                if(board[i][SIZE-2].status()==false)
                {
                    board[i][SIZE-2].addMine();
                }
                if(board[i+1][SIZE-2].status()==false)
                {
                    board[i+1][SIZE-2].addMine();
                }
                if(board[i+1][SIZE-1].status()==false)
                {
                    board[i+1][SIZE-1].addMine();
                }
            }
        }
        for(int j=1;j<=SIZE-2;j++)
        {
            if(board[0][j].status()==true)
            {
                if(board[0][j-1].status()==false)
                {
                    board[0][j-1].addMine();
                }
                if(board[1][j-1].status()==false)
                {
                    board[1][j-1].addMine();
                }
                if(board[1][j].status()==false)
                {
                    board[1][j].addMine();
                }
                if(board[1][j+1].status()==false)
                {
                    board[1][j+1].addMine();
                }
                if(board[0][j+1].status()==false)
                {
                    board[0][j+1].addMine();
                }
            }
            if(board[SIZE-1][j].status()==true)
            {
                if(board[SIZE-1][j-1].status()==false)
                {
                    board[SIZE-1][j-1].addMine();
                }
                if(board[SIZE-2][j-1].status()==false)
                {
                    board[SIZE-2][j-1].addMine();
                }
                if(board[SIZE-2][j].status()==false)
                {
                    board[SIZE-2][j].addMine();
                }
                if(board[SIZE-2][j+1].status()==false)
                {
                    board[SIZE-2][j+1].addMine();
                }
                if(board[SIZE-1][j+1].status()==false)
                {
                    board[SIZE-1][j+1].addMine();
                }
            }
        }
        for(int i=1;i<=SIZE-2;i++)
        {
            for(int j=1;j<=SIZE-2;j++)
            {
                if(board[i][j].status()==true)
                {
                    if(board[i][j-1].status()==false)
                    {
                        board[i][j-1].addMine();
                    }
                    if(board[i-1][j-1].status()==false)
                    {
                        board[i-1][j-1].addMine();
                    }
                    if(board[i-1][j].status()==false)
                    {
                        board[i-1][j].addMine();
                    }
                    if(board[i-1][j+1].status()==false)
                    {
                        board[i-1][j+1].addMine();
                    }
                    if(board[i][j+1].status()==false)
                    {
                        board[i][j+1].addMine();
                    }
                    if(board[i+1][j+1].status()==false)
                    {
                        board[i+1][j+1].addMine();
                    }
                    if(board[i+1][j].status()==false)
                    {
                        board[i+1][j].addMine();
                    }
                    if(board[i+1][j-1].status()==false)
                    {
                        board[i+1][j-1].addMine();
                    }
                }
            }
        }
    }
    public void showAllMines()
    {
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                if(board[i][j].status()==true)
                {
                    board[i][j].setText("");
                    board[i][j].setBackground(getResources().getDrawable(R.drawable.mine_1,null));
                    //board[i][j].setTextColor(Color.BLACK);
                }
            }
        }
    }
    public void buttonShow(MSButton button,int x,int y)
    {
        if(button.isFlag())
        {
            return;
        }
        else
        {
            if (button.status()==true)
            {
                //timeSwap+=timeInMillies;
                //myHandler.removeCallbacks(updateTimerMethod);
                Toast.makeText(this, "Game Over!!", Toast.LENGTH_SHORT).show();
                showAllMines();
                button.revealed=true;
                gameStatus=complete;
                for (int i=0;i<SIZE;i++) {
                    for (int j=0;j<SIZE;j++) {
                        board[i][j].setEnabled(false);
                    }
                }
                return;
            }
            else if (button.numOfMines()>0)
            {
                button.showButton();
                checked++;
                return;
            }
            else
            {
                //button.showButton();
                int d=revealNeighbours(button,x,y);
                if(d==-1)
                {
                    return;
                }
            }
        }
    }
    public int revealNeighbours(MSButton button,int x,int y)
    {
        int[] arrx={x,x+1,x+1,x+1,x,x-1,x-1,x-1};
        int[] arry={y-1,y-1,y,y+1,y+1,y+1,y,y-1};
        if(button.isFlag()==true)
        {
            return -1;
        }
        else if(button.revealed==true)
        {
            return -1;
        }
        else if(button.status()==true)
        {
            return -1;
        }
        else if(button.numOfMines()>0)
        {
            checked++;
            button.showButton();
            return -1;
        }
        else
        {
            button.showButton();
            checked++;
            for(int i=0;i<8;i++)
            {
                int a=arrx[i];
                int b=arry[i];
                if(isValid(a,b))
                {
                    revealNeighbours(board[a][b],a,b);
                }
            }
        }
        return 1;
    }
    public boolean isValid(int x,int y)
    {
        if((x>=0 && x<SIZE) && (y>=0 && y<SIZE))
        {
            return true;
        }
        return false;
    }
    public int checkGameStatus()
    {
        if(checked<(SIZE*SIZE)-numberOfMines)
       {
           r=-1;
       }
       else if(checked==(SIZE*SIZE)-numberOfMines)
       {
           r=0;
       }
       return r;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       getMenuInflater().inflate(R.menu.main_menu,menu);
       return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.resetItem)
        {
            flags=numberOfMines;
            setupBoard();
            easy=true;
            setupMines();
            updateNeighbours();
            firstClick=true;
        }
        else if(id==R.id.easy)
        {
            SIZE=8;
            numberOfMines=10;
            flags=numberOfMines;
            setupBoard();
            easy=true;
            medium=false;
            hard=false;
            firstClick=true;
            setupMines();
            updateNeighbours();
        }
        else if(id==R.id.medium)
        {
            SIZE=9;
            numberOfMines=13;
            flags=numberOfMines;
            setupBoard();
            easy=false;
            medium=true;
            hard=false;
            firstClick=true;
            flags=numberOfMines;
            setupMines();
            updateNeighbours();
        }
        else if(id==R.id.hard)
        {
            SIZE=10;
            numberOfMines=18;
            flags=numberOfMines;
            setupBoard();
            easy=false;
            medium=false;
            flags=numberOfMines;
            hard=true;
            firstClick=true;
            setupMines();
            updateNeighbours();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view)
    {
        if(gameStatus==incomplete)
        {
            MSButton button=(MSButton) view;
            if(firstClick==true && button.status()==true)
            {
                for(int i=0;i<SIZE;i++)
                {
                    for(int j=0;j<SIZE;j++)
                    {
                        board[i][j].setZero();
                    }
                }
                int x_coord=0,y_coord=0;
                if(button.status()==true)
                {
                    button.toggleStatus();
                }
                for(int i=0;i<SIZE;i++)
                {
                    for(int j=0;j<SIZE;j++)
                    {
                        if(button==board[i][j])
                        {
                           x_coord=i;
                           y_coord=j;
                        }
                    }
                }
                int num=1;
                int mines=0;
                Random random=new Random();
                while(mines<num)
                {
                    int x=random.nextInt(SIZE);
                    int y=random.nextInt(SIZE);
                    if(board[x][y].status()==false && (x!=x_coord || y!=y_coord))
                    {
                        board[x][y].setAsMine();
                        mines++;
                    }
                }
                updateNeighbours();
            }
            /*if(firstClick==true)
            {
                //startTime= SystemClock.uptimeMillis();
                //myHandler.postDelayed(updateTimerMethod,0);
            }*/
            firstClick=false;
            int x=0,y=0;
            for(int i=0;i<SIZE;i++)
            {
                for(int j=0;j<SIZE;j++)
                {
                    if(board[i][j]==button)
                    {
                        x=i;
                        y=j;
                    }
                }
            }
            buttonShow(button,x,y);
        }
        if(checkGameStatus()==0)
        {
            Toast.makeText(this,"You Win!!",Toast.LENGTH_SHORT).show();
            gameStatus=complete;
            for(int i=0;i<SIZE;i++)
            {
                for(int j=0;j<SIZE;j++)
                {
                    board[i][j].setEnabled(false);
                }
            }
            return;
        }
    }
    @Override
    public boolean onLongClick(View view)
    {
        MSButton button1=(MSButton) view;
        if(button1.isFlag())
        {
            button1.unsetFlag();
            flags++;
            String f="Flags: "+"<b>"+flags+"</b>";
            flagTextView.setText(Html.fromHtml(f));
        }
        else
        {
            button1.setFlag();
            flags--;
            String f="Flags: "+"<b>"+flags+"</b>";
            flagTextView.setText(Html.fromHtml(f));
        }
        return true;
    }
    /*public Runnable updateTimerMethod=new Runnable() {
        @Override
        public void run() {
            timeInMillies=SystemClock.uptimeMillis()-startTime;
            finalTime=timeSwap+timeInMillies;
            int seconds=(int) (finalTime/1000);
            String t="<b>"+seconds+"</b>";
            timeTextView.setText(Html.fromHtml(t));
            myHandler.postDelayed(this,0);
        }
    };*/
}
