package com.example.pay_plugin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.allinpay.unifypay.sdk.Allinpay;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class AllinPayActivity extends AppCompatActivity {

    private TextView textView;
    private EditText etAmt;
    private EditText etBank;
    private static String appid;
    private static String orgid;
    private static String cusid;
    private static String key;

    static {
        // 生产参数
//        appid = "00000003";
//        orgid = "";
//        cusid = "990440148166000";
//        key = "allinpay999";

        appid = "00222247";
        orgid = "";
        cusid = "55329005812194Z";
        key = "allinpay";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_result);
        etAmt = findViewById(R.id.et_amt);
        etBank = findViewById(R.id.et_bank);
        // 调试模式，提供日志打印
        Allinpay.debug();
    }

    /**
     * 跳转SDK支付选择界面
     *
     * @param view
     */
    public void turnToPay(View view) {
        openPay(false);
    }

    public void turnToPayNoCredit(View view) {
        openPay(true);
    }

    private void openPay(boolean noCredit) {
        // 该params应由服务端返回，demo只为方便提供示例
        HashMap<String, String> params = new HashMap<>();
        params.put("trxamt", "1");
        String amt = etAmt.getText().toString();
        if (!TextUtils.isEmpty(amt)) {
            params.put("trxamt", new BigDecimal(amt).multiply(new BigDecimal(100)).toBigInteger().toString());
        }
        if (noCredit) {
            params.put("limitpay", "no_credit");
        }
        params.put("reqsn", String.valueOf(System.currentTimeMillis()));
        params.put("subject", "Android测试支付下单");
        params.put("trxreserve", "备注");
        params.put("validtime", "30");
        params.put("schemeurl", "allinpaysdk://com.allinpay.test/result");
        params.put("notifyurl", "http://www.xxx.com");
        params.put("appid", appid);
        params.put("orgid", orgid);
        params.put("cusid", cusid);
//        params.put("signtype", "RSA");
        params.put("sign", signParam(params));

        Allinpay.openPay(this, params);
    }

    /**
     * SDK直接下单支付
     *
     * @param view
     */
    public void pay(View view) {
        // 该params应由服务端返回，demo只为方便提供示例，paytype可在移动端添加
        HashMap<String, String> params = new HashMap<>();
        params.put("trxamt", "1");
        String amt = etAmt.getText().toString();
        if (!TextUtils.isEmpty(amt)) {
            params.put("trxamt", new BigDecimal(amt).multiply(new BigDecimal(100)).toBigInteger().toString());
        }
        params.put("reqsn", String.valueOf(System.currentTimeMillis()));
        params.put("subject", "Android测试支付下单");
        params.put("trxreserve", "备注");
        params.put("validtime", "30");
        params.put("schemeurl", "allinpaysdk://com.allinpay.test/result");
        params.put("notifyurl", "http://www.xxx.com");
        params.put("appid", appid);
        params.put("orgid", orgid);
        params.put("cusid", cusid);
//        params.put("signtype", "RSA");
        params.put("sign", signParam(params));
        // 支付方式，不用参与签名
        String bank = etBank.getText().toString();
        bank = TextUtils.isEmpty(bank) ? Allinpay.PAY_TYPE_ICBC : bank;
        params.put("paytype", bank);

        // createPayment(Activity activity, HashMap<String, String> payment, boolean showProgress)
        // showProgress为显示loading，如果传false，即使用APP的loading，自行在onActivityResult中取消loading
        Allinpay.createPayment(this, params, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Allinpay.REQUEST_CODE_PAY) {
            if (resultCode == RESULT_OK) {
                // ALLINPAY_SUCCESS 10000  支付下单成功
                // ALLINPAY_UNKNOWN 10001  未知状态
                // ALLINPAY_ERR_INSTALL 10002  未安装支付渠道客户端
                // ALLINPAY_ERR_CANCEL 10003  用户取消操作
                // ALLINPAY_ERR_FAIL 10005  接口请求失败
                // ALLINPAY_ERR_CONNECT 10006  网络连接失败
                // ALLINPAY_ERR_PAY_TYPE 10007  不支持的支付方式
                // SDK状态
                int retCode = data.getIntExtra("retCode", Allinpay.ALLINPAY_UNKNOWN);
                String retErrmsg = data.getStringExtra("retErrmsg");
                retErrmsg = TextUtils.isEmpty(retErrmsg) ? "" : retErrmsg;
                String oldMsg = textView.getText().toString();
                if (retCode == Allinpay.ALLINPAY_SUCCESS) {
                    // 下单成功，返回订单状态
                    // 订单id
                    String orderId = data.getStringExtra("orderId");
                    // 订单状态，具体状态说明见文档
                    String trxStatus = data.getStringExtra("trxStatus");
                    // 订单状态说明
                    String trxErrmsg = data.getStringExtra("trxErrmsg");
                    String msg = retCode + ":" + retErrmsg + "\n" +
                            "orderId:" + orderId + "\n" +
                            "trxStatus:" + trxStatus + "\n" +
                            "trxErrmsg:" + trxErrmsg + "\n" +
                            (TextUtils.isEmpty(oldMsg) ? "" : ("=====================" + "\n")) +
                            oldMsg;
                    textView.setText(msg);
                } else {
                    // 其他状态
                    String msg = retCode + ":" + retErrmsg + "\n" +
                            (TextUtils.isEmpty(oldMsg) ? "" : ("=====================" + "\n")) +
                            oldMsg;
                    textView.setText(msg);
                }
            }
        }
    }

    /**
     * 签名订单信息
     *
     * @param params
     * @return
     */
    private String signParam(HashMap<String, String> params) {
        try {
            String signtype = params.get("signtype");

            String md5Sign;
            if (!TextUtils.isEmpty(signtype) && "RSA".equals(signtype)) {
                String blankData = EncryptUtil.getSignStr(params, null, true);
                md5Sign = EncryptUtil.rsa(blankData, SYB_RSACUSPRIKEY);
            } else {
                params.put("key", key);
                String blankData = EncryptUtil.getSignStr(params, null, true);
                md5Sign = EncryptUtil.md5(blankData.getBytes(StandardCharsets.UTF_8));
                params.remove("key");
            }
            return md5Sign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String SYB_RSACUSPRIKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAO0HpPUP+eHk//Ba6ZOePvoZVDpOCRtt943oeVfCTllye43bqja1jVIaebX0MgX+yPYnWIQIOJ9ubSH0R4iyY9y1/HR00qkUpfW3/0usBPt9qn7r0xtFHerhVCd4dT2rKb2Oc5IhKOg05cw/BmMFohMkFsqt0jlrUXI8zJOlLIcxAgMBAAECgYA9lt/pAYa3iK5sQOMyhUrt54j4QXCiXPeXOxHUmNuM6G9sU+itoI0hCVoYymP5JNQJCf45CH3WB3Z5/SRdQ6Uoo1cjao6cCohPLxMSfJglsZCHckPH53o25RKEza4njIgKC+yN7HAhanKymhw/yYQ6i0aXq38zFIk8djMtE7R6xQJBAP6jvNy7UhPKO5rxGFKR+MvvbO3qnYH6x0jZCGY3FlxuGfbavueOiFtMeK67FuDv683dcUKi+M48yR4kH5CfIusCQQDuS9KF6mlm3kHAiZWgVhE8VVNYGpRLCRDgAKm4InGmvk5mUv+O1yAtAFVAEHWIgD4awC7Eqf1YFrSF/It9HV9TAkEAsXiU7JJxhfFw0XAvL30lFZ1tIfReinSp6A+7VuIV552k4vNaEjC4wEjv43fpXiRZCEXJ5lOHbNXYpfUvOrBuuQJAOpow8rf8Jc0g1G3Be0XPRUwii/c1YuKe4Meo9VybIIuKkkV1Dba/9fEwBepGTURkgYWjur+nSyOCT7UUxLcVewJAPLig8dVfKpsiNwYuveEYMcFaO5xoRuiB7v+CMmvxpuuK+rrFS+d7RdmwDbnBiDV4JkTgFObUiGvB7MtS+LGfhw==";
}