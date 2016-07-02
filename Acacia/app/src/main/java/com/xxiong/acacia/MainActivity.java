package com.xxiong.acacia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent startIntentService = new Intent(this, MyIntentService.class);
        startService(startIntentService);

        text = (TextView) findViewById(R.id.editText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(getInfo).start();
                Snackbar.make(view, "正在获取天气数据", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add((CharSequence) ("Exit"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String weather = data.getString("value");
            String info = null;
            try {
                info = new String(weather.getBytes(), "UTF-8");
                if (info.length() == 0) {
                    info = " Checkout your Network Please.";
                }
            } catch (Exception e) { }
            info = info.substring(1).replace("{", "\n").replace("}", "").replace(",", "\n");
            info = info.replace("weatherinfo", "天气信息").replace("city", "城市");
            info = info.replace("cityid", "城市代码").replace("temp", "气温");
            info = info.replace("WD", "风向").replace("SD", "湿度");
            info = info.replace("WSE", "风力").replace("time", "时间");
            info = info.replace("qy", "气压").replace("\"", "");
            text.setText(info);
        }
    };

    Runnable getInfo = new Runnable() {
        @Override
        public void run() {
            String line = "";
            try {
                URL url = new URL("http://www.weather.com.cn/data/sk/101040100.html");
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("Connection", "close");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                line = reader.readLine();
                reader.close();
            } catch (Exception e) { }
            Message mmsg = new Message();
            Bundle data = new Bundle();
            data.putString("value", line);
            mmsg.setData(data);
            handler.sendMessage(mmsg);
        }
    };
}
