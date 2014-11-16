package com.example.user.breath;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Formatter;


public class BreathActivity extends Activity {
    private NumberPicker np;
    private TextView txtCounter, txtAction, txtTotalTime;
    private Thread t;
    private Integer i, seed, action, total_seconds;
    private StringBuilder s = new StringBuilder();
    private Formatter f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);
        txtCounter = (TextView) findViewById(R.id.txtCounter);
        txtAction = (TextView) findViewById(R.id.txtAction);
        txtTotalTime = (TextView) findViewById(R.id.txtTotalTime);
        i = 0;
        action = 0;
        total_seconds = 0;
        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(20);
        np.setValue(7);
    }

    public void updateTotalTime(){
        Integer minutes = total_seconds/60;
        Integer seconds = total_seconds - minutes*60;
        txtTotalTime.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.breath, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateTextView(){
        if(action == 0){
            if(i == seed)nextStep();
        }else if (action == 2){
            if(i == seed*4)nextStep();
        }else{
            if(i==seed*2)nextStep();
        }
        updateInterface();
        i++;
        total_seconds++;
    }

    public void nextStep(){
        i = 0;
        action++;
        if(action>2) action = 0;
    }

    public void onClickStart(View v){
        seed = np.getValue();
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    public void onClickStop(View v){
        t.interrupt();
    }

    public void updateInterface(){
        updateTotalTime();
        txtCounter.setText(Integer.toString(i));
        switch (action){
            case 0: txtAction.setText("In");
                break;
            case 1: txtAction.setText("Hold");
                break;
            case 2: txtAction.setText("Out");
                break;
        }
    }

    public void onClickReset(View v){
        total_seconds = i = action = 0;
        updateInterface();
    }
}
