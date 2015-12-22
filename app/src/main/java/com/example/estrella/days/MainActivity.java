package com.example.estrella.days;

import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView tv_selDate  = (TextView)findViewById(R.id.tv_sel_date);
    static int Y, M, D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("DatePrefs", 0);

        if(preferences.getInt("year", 0) != 0){
            Y = preferences.getInt("year", 0);
            M = preferences.getInt("month", 0);
            D = preferences.getInt("day", 0);
        }else {
            GregorianCalendar calendar = new GregorianCalendar();
            Y = calendar.get(calendar.YEAR);
            M = calendar.get(calendar.MONTH);
            D = calendar.get(calendar.DATE);
        }

//        tv_selDate = (TextView)findViewById(R.id.tv_sel_date);
        String x = Y + "-" + M + "-"  + D;
        tv_selDate.setText(x);

        findViewById(R.id.btn_set_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, dateSetListener, Y, M, D).show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String msg = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            editor = preferences.edit();
            editor.putInt("year", year);
            editor.putInt("month", monthOfYear);
            editor.putInt("day", dayOfMonth);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, MainAppWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            MainActivity.this.sendBroadcast(intent);
///////////////////

            if(preferences.getString("date", "").isEmpty() ){
                editor.putString("date", msg);
                editor.apply();
                tv_selDate.setText(msg);
            }else
                tv_selDate.setText(preferences.getString("date", ""));
///////////////////
        }

    };

}
