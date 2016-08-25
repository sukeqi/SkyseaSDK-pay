package com.skysea.alipay;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.CancellationException;

import com.alipay.sdk.app.PayTask;
import com.skysea.android.app.lib.MResource;
import com.skysea.app.BaseActivity;
import com.skysea.async.AutoCancelServiceFramework;
import com.skysea.bean.OrderInfo;
import com.skysea.exception.ResponseException;
import com.skysea.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class AlixPay {

    static String TAG = "AlixPay";
    private ProgressDialog mProgress = null;
    private AlertDialog.Builder abBuilder = null;
    Activity context;
    OrderInfo orderInfo;

    public AlixPay(Activity context, OrderInfo info) {
        this.context = context;
        this.orderInfo = info;
    }

    public void pay() {
        final String order = getOrderInfo();
        String signType = getSignType();
        String strsign = sign(signType, order);
        try {

            strsign = URLEncoder.encode(strsign, "utf-8");
            String info = order + "&sign=" + "\"" + strsign + "\"" + "&"
                    + getSignType();

            Log.d(TAG, info);
        } catch (Exception e) {
            Toast.makeText(
                    context,
                    context.getString(MResource.getIdByName(
                            context.getApplicationContext(), "string",
                            "remote_call_failed")), Toast.LENGTH_SHORT).show();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask payTask = new PayTask(context);
                String result = payTask.pay(order, true);
                Message message = new Message();
                message.what = AlixId.RQF_PAY;
                message.obj = result;
                mHandler.sendMessage(message);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    AlixPayHandler mHandler = new AlixPayHandler();

    @SuppressLint("HandlerLeak")
    class AlixPayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                String strRet = (String) msg.obj;
                Log.d(TAG, strRet);
                switch (msg.what) {
                    case AlixId.RQF_PAY: {
                        try {
                            String tradeStatus = "resultStatus={";
                            int imemoStart = strRet.indexOf("resultStatus=");
                            imemoStart += tradeStatus.length();
                            int imemoEnd = strRet.indexOf("};memo=");
                            tradeStatus = strRet.substring(imemoStart, imemoEnd);

                            ResultChecker resultChecker = new ResultChecker(strRet);
                            int retVal = resultChecker.checkSign();

                            if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
                                BaseHelper.showDialog(
                                        context,
                                        "提示",
                                        context.getResources().getString(
                                                MResource.getIdByName(context
                                                                .getApplicationContext(),
                                                        "string",
                                                        "check_sign_failed")),
                                        android.R.drawable.ic_dialog_alert);
                            } else {
                                if (tradeStatus.equals("9000")) {
                                    final String result = tradeStatus;
                                    postResult(context, Utils.subPayResultString(strRet));
                                    abBuilder = Utils.showAlertDialog(context,
                                            "提示", "支付成功，点击确定返回游戏。");
                                    abBuilder.setPositiveButton("确定",
                                            new OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    Intent data = new Intent();
                                                    data.putExtra("tradeStatus",
                                                            result);
                                                    context.setResult(200, data);
                                                    context.finish();
                                                }
                                            }).show();

                                } else {
                                    BaseHelper
                                            .showDialog(
                                                    context,
                                                    "提示",
                                                    "支付失败,交易状态码:" + tradeStatus,
                                                    MResource.getIdByName(
                                                            context.getApplicationContext(),
                                                            "drawable", "info"));
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String getOrderInfo() {
        String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
        strOrderInfo += "&";
        strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
        strOrderInfo += "&";
        strOrderInfo += "out_trade_no=" + "\"" + orderInfo.getOrder_num()
                + "\"";
        strOrderInfo += "&";
        strOrderInfo += "subject=" + "\"" + orderInfo.getGamename()
                + orderInfo.getGameservername() + "充值" + "\"";
        strOrderInfo += "&";
        strOrderInfo += "body=" + "\"" + orderInfo.getGamename()
                + orderInfo.getGameservername() + "充值" + "\"";
        strOrderInfo += "&";
        strOrderInfo += "total_fee=" + "\"" + orderInfo.getAmount() + "\"";
        strOrderInfo += "&";
        strOrderInfo += "notify_url=" + "\""
                + Constant.NOTIFY_URL
                + "\"";

        return strOrderInfo;
    }

    String getSignType() {
        String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
        return getSignType;
    }

    String sign(String signType, String content) {
        return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
    }

    void closeProgress() {
        try {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class AlixOnCancelListener implements
            DialogInterface.OnCancelListener {
        Activity mcontext;

        AlixOnCancelListener(Activity context) {
            mcontext = context;
        }

        public void onCancel(DialogInterface dialog) {
            mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
        }
    }

    private void postResult(Activity context, String result) {
        ((BaseActivity) context)
                .autoCancel(new AutoCancelServiceFramework<String, Void, String>(
                        (BaseActivity) context) {

                    @Override
                    protected void onPreExecute() {
                        // TODO Auto-generated method stub
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        // TODO Auto-generated method stub
                        //Log.v(TAG, "result:"+(result.equals("1")?"success":"false"));
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        createIPlatCokeService();
                        try {
                            return mIPlatService.toPostResult(params[0]);
                        } catch (CancellationException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ResponseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(result));
    }
}