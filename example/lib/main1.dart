import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pay_plugin/pay_plugin.dart';

void main() {
  runApp(MyAppLess());
}

class MyAppLess extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      routes: {'/other': (BuildContext context) => Other()},
      home: MyApp1(),
    );
  }
}

class MyApp1 extends StatefulWidget {
  const MyApp1({Key? key}) : super(key: key);

  @override
  State<MyApp1> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp1> {
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
    return Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
        // child: Text('Running on: $_platformVersion\n'),
        child: new TextButton(
            child: Text("main1"),
            onPressed: () {
              initPlatformState();
            }),
      ),
    );
  }

//平台通道––––跳转到Android页面
/*static const platform = const MethodChannel('samples.flutter.jumpto.android');

  //跳转到Android页面
  Future<Null> _jumpToAndroidMethod() async {
    final String result = await platform.invokeMethod('jumpToAndroidPage');
    print('result===$result');
  }*/
}

class Other extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("other"),
      ),
    );
  }
}
