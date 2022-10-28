package com.example.pay_plugin;

import android.content.Intent;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class JumpChannel implements MethodChannel.MethodCallHandler {

    String ChannelName = "aabbcc";
    private MethodChannel methodChannel;

    private BinaryMessenger flutterEngine;
    private FlutterActivity mActivity;

    public JumpChannel(BinaryMessenger flutterEngine, FlutterActivity activity) {
        this.flutterEngine = flutterEngine;
        mActivity = activity;
        methodChannel = new MethodChannel(flutterEngine, ChannelName);

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("b Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("test1_function")) {
            Intent intent = new Intent(mActivity, Main2Activity.class);
            mActivity.startActivity(intent);
            result.success("跳转测试 ");
        } else {
            result.notImplemented();
        }
    }
}
