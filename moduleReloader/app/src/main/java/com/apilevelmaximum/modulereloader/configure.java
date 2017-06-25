package com.apilevelmaximum.modulereloader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;

public class configure extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confugure);
        Button tempButton = (Button) findViewById(R.id.ok);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProcess();
            }
        });
    }
    private void startProcess(){
        EditText textEntry= (EditText) findViewById(R.id.editText);
        saveParams(textEntry.getText().toString());
        this.startService(new Intent(getApplicationContext(),backgroundservice.class));
    }
    private void saveParams(String module){
        try{
            FileOutputStream fos = getApplicationContext().openFileOutput("module",MODE_PRIVATE);
            fos.write(module.getBytes());
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
