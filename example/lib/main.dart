import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pay_plugin/pay_plugin.dart';
import 'package:pay_plugin_example/main1.dart';

void main() {
  runApp(Appless());
}

class Appless extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      home: MyApp(),
    );
  }
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    // initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion = "";
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      /*   platformVersion =
          await PayPlugin.platformVersion ?? 'Unknown platform version';*/
      platformVersion = await PayPlugin.test1 ?? 'test1';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          // child: Text('Running on: $_platformVersion\n'),
          child: new RaisedButton(
              child: Text("main"),
              onPressed: () async {
                //  先跳  main1  再 跳android 最后跳到main1

                /*Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (context) => MyAppLess(),
                  ),
                );*/
                initPlatformState();
              }),
        ),
      ),
    );
  }

//平台通道––––跳转到Android页面
  static const platform = const MethodChannel('samples.flutter.jumpto.android');

  //跳转到Android页面
  Future<Null> _jumpToAndroidMethod() async {
    final String result = await platform.invokeMethod('jumpToAndroidPage');
    print('result===$result');
  }
}
