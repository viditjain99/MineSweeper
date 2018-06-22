package com.example.vidit.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener
{
    LinearLayout rootLayout;
    public int SIZE=8;
    public int numberOfMines=10;
    public int gameStatus=-1;
    public int incomplete=1;
    public int complete=2;
    public int checked=0;
    public int r;
    public ArrayList<LinearLayout> rows;
    public MSButton[][] board;
    public boolean easy,medium,hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        String name=intent.getStringExtra(Startup.NAME_KEY);
        int id=intent.getIntExtra("LEVEL",8);
        if(id==R.id.Easy)
        {
            SIZE=8;
            numberOfMines=10;
        }
        else if(id==R.id.Medium)
        {
            SIZE=9;
            numberOfMines=13;
        }
        else if(id==R.id.Hard)
        {
            SIZE=10;
            numberOfMines=18;
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
        gameStatus=incomplete;
        rootLayout.removeAllViews();
        board=new MSButton[SIZE][SIZE];
        easy=false;
        medium=false;
        hard=false;
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
                    board[i][j].setText("M");
                    board[i][j].setTextColor(Color.BLACK);
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
            setupBoard();
            easy=true;
            setupMines();
            updateNeighbours();
        }
        else if(id==R.id.easy)
        {
            SIZE=8;
            numberOfMines=10;
            setupBoard();
            easy=true;
            medium=false;
            hard=false;
            setupMines();
            updateNeighbours();
        }
        else if(id==R.id.medium)
        {
            SIZE=9;
            numberOfMines=13;
            setupBoard();
            easy=false;
            medium=true;
            hard=false;
            setupMines();
            updateNeighbours();
        }
        else if(id==R.id.hard)
        {
            SIZE=10;
            numberOfMines=18;
            setupBoard();
            easy=false;
            medium=false;
            hard=true;
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
        }
        else
        {
            button1.setFlag();
        }
        return true;
    }
}
