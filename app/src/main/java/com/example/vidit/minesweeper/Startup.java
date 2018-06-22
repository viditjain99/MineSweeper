package com.example.vidit.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Startup extends AppCompatActivity implements View.OnClickListener
{
    EditText editText;
    RadioGroup radioGroup;
    Button button;
    RadioButton radioButton;
    public String name="";
    public int id;
    public static final String NAME_KEY="NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button=findViewById(R.id.start);
        editText=findViewById(R.id.nameEditText);
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        name=editText.getText().toString();
        radioGroup=findViewById(R.id.radioGrp);
        id=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(id);
        if(name.length()>0 && (radioButton.getId()==R.id.Easy || radioButton.getId()==R.id.Medium || radioButton.getId()==R.id.Hard))
        {
            Intent intent=new Intent(this,MainActivity.class);
            intent.putExtra(NAME_KEY,name);
            intent.putExtra("LEVEL",id);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Please enter the details",Toast.LENGTH_SHORT).show();
        }
    }
}
