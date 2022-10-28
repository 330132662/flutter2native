import 'dart:async';

import 'package:flutter/services.dart';

class PayPlugin {
  static const MethodChannel _channel = MethodChannel('pay_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static const BasicMessageChannel _basicMessageChannel =
      const BasicMessageChannel(
          'basic_message_channel', StandardMessageCodec());

  static Future<String?> get test1 async {
// 向原生发送 数据 并接收返回数据
    String data = "我是flutter向android传递的数据";
    // String reply = await _basicMessageChannel.send(data);
    Map<String, String> map = {"flutter": data};
    final String? version = await _channel.invokeMethod('test1_function', map);
    return version;
  }
/**
 * 向activity 传递数据 http://www.kaotop.com/it/153727.html
 *
 *
 * 原生向 flutter传递数据 https://www.cnblogs.com/suiyilaile/p/11039563.html
 *
 */
}
