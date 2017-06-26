package com.apilevelmaximum.modulereloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.StreamCorruptedException;

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
        EditText cmdT= (EditText) findViewById(R.id.commandText);
        saveParams(textEntry.getText().toString(),cmdT.getText().toString());
        Context context= getApplicationContext();
        AlarmManager alarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(getApplicationContext(),backgroundservice.class), 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
        //this.startService(new Intent(getApplicationContext(),backgroundservice.class));
    }
    private void saveParams(String module, String command){
        try{
            FileOutputStream fos = getApplicationContext().openFileOutput("module",MODE_PRIVATE);
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
