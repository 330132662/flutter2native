import 'dart:async';

import 'package:flutter/services.dart';

class PayPlugin {
  static const MethodChannel _channel = MethodChannel('pay_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String?> get test1 async {
    final String? version = await _channel.invokeMethod('test1_function');
    return version;
  }
}
