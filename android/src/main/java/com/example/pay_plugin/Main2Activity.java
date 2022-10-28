package com.example.pay_plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class Main2Activity extends AppCompatActivity {


    private String route = "main.dart";
    private static final String BASIC_MESSAGE_CHANNEL = "basic_message_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cache(this, route);

        Intent intent = getIntent();
        String aa = intent.getStringExtra(PayPlugin.Key);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "a", Toast.LENGTH_LONG).show();
                startActivity(Main2Activity.this, route);
            }
        });
    }

    void cache(Context context, String url) {
        FlutterEngine flutterEngine = new FlutterEngine(context);
        //可设置初始路由
        flutterEngine.getNavigationChannel().setInitialRoute(url);
        flutterEngine.getDartExecutor().executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());
        FlutterEngineCache
                .getInstance()
                .put(url, flutterEngine);
    }

    public void startActivity(Context activity, String url) {
        // url 不存在报错 https://www.codenong.com/jse6e3ff165d29/  https://woomiao.cn/archives/510.html
        if (FlutterEngineCache.getInstance().contains(url)) {
            Intent intent = FlutterActivity
                    .withCachedEngine(url)
                    .build(activity);
            try {
                intent.putExtra("mydata", "66778899");
                activity.startActivity(intent);
            } catch (Exception e) {
                Log.d("s", e.getLocalizedMessage());
            }
        }
    }
}