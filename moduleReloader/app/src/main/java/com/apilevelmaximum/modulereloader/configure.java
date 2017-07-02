package com.apilevelmaximum.modulereloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Calendar;

public class configure extends AppCompatActivity {
private String module,command;
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
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((EditText)findViewById(R.id.commandText)).setText(command);
        ((EditText)findViewById(R.id.editText)).setText(module);
    }
    private void load() throws IOException {
        String filename="module";
        FileInputStream fis= getApplicationContext().openFileInput(filename);
        byte[] buffer= new byte[1024]; int len;
        while ((len=fis.read(buffer))>0)
        {
            module +=new String(buffer,0,len);
        }
        fis.close();
        filename="command";
        fis= getApplicationContext().openFileInput(filename);
        buffer= new byte[1024];
        while ((len=fis.read(buffer))>0)
        {
            command +=new String(buffer,0,len);
        }
        fis.close();
        while (module.contains("null")){
            module=module.replaceFirst("null","");
        }
        while (command.contains("null")){
            command=command.replaceFirst("null","");
        }

    }
    private void startProcess(){
        EditText textEntry= (EditText) findViewById(R.id.editText);
        EditText cmdT= (EditText) findViewById(R.id.commandText);
        saveParams(textEntry.getText().toString(),cmdT.getText().toString());
        Context context= getApplicationContext();
        AlarmManager alarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, new Intent(getApplicationContext(),backgroundservice.class), 0);
        long trigger = System.currentTimeMillis() + (100);
        alarmMgr.setInexactRepeating(AlarmManager.RTC, trigger,1000*60, alarmIntent);
    }
    private void saveParams(String module, String command){
        if (module==null||command==null){
            return;
        }
        try{
            FileOutputStream fos = getApplicationContext().openFileOutput("module",MODE_PRIVATE);
            Log.d("MUKODIK", module);
            fos.write(module.getBytes());
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try{
            FileOutputStream fos = getApplicationContext().openFileOutput("command",MODE_PRIVATE);
            fos.write(command.getBytes());
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
