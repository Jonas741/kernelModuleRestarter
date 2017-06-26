package com.apilevelmaximum.modulereloader;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yallntin on 25/06/2017.
 */

public class backgroundservice extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private Context cont;
    private String module;
    private String command;
    public backgroundservice() {
        super("Module_ReStarter");
        module ="";
        command="";
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

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        cont=getApplicationContext();
        try {
            load();
        }
        catch (IOException ignored){

        }
        Log.d("MUKODIK","ORULUNK");
        //Tick();
    }
    public boolean isScreenOn(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    screenOn = true;
                }
            }
            return screenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //noinspection deprecation
            return pm.isScreenOn();
        }
    }
    private void Tick(){
        if (!isScreenOn(cont)) {
            String result=sudoForResult("lsmod");
            if (!result.contains(module)){
                sudoForResult(command);
            }
        }
    }
    public static String sudoForResult(String...strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try{
            Process su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            response = su.getInputStream();

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            Closer.closeSilently(outputStream, response);
        }
        return res;
    }
    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }
}
