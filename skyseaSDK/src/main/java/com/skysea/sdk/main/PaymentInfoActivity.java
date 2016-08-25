package com.skysea.sdk.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skysea.alipay.AlixPay;
import com.skysea.alipay.Pay;
import com.skysea.alipay.PayResult;
import com.skysea.android.app.lib.MResource;
import com.skysea.app.BaseActivity;
import com.skysea.async.AutoCancelController;
import com.skysea.async.AutoCancelServiceFramework;
import com.skysea.async.Cancelable;
import com.skysea.bean.CItem;
import com.skysea.bean.Item;
import com.skysea.bean.OrderInfo;
import com.skysea.config.Constants;
import com.skysea.exception.ResponseException;
import com.skysea.interfaces.IDispatcherCallback;
import com.skysea.request.impl.OrderRequest;
import com.skysea.sdk.R;
import com.skysea.service.impl.PlatServiceAgent;
import com.skysea.utils.OkHttpData;
import com.skysea.utils.Utils;
import com.skysea.view.FragmentLayoutWithLine;
import com.skysea.view.ViewHolder;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class PaymentInfoActivity extends FragmentActivity implements
        OnClickListener {

    String userid;
    String gameid;
    String gameserverid;
    String totlesMoney;
    String ordernum;
    String gamename;
    String servername;
    String username;
    String realTotleMoney;

    String xb_orderid;
    static IDispatcherCallback callback;

    ImageView back;
    TextView totalMoney;
    FragmentLayoutWithLine checkLine;
    ProgressDialog pd_pay;

    ListView listTab;
    List<CItem> datas = new ArrayList<CItem>();
    TextView totalMoneys;
    TextView payWay;
    Button paywaybtn;
    String text;
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private AutoCancelController mAutoCancelController = new AutoCancelController();

    public static String[] tabs = {"银行卡", "支付宝", "微信", "充值卡"};
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private OkHttpData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getIntentArgs(getIntent());
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.paymentinfos);
            initView();
        } else if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.paymentinfo);
            initViews();
        }
        super.onResume();
    }

    private void initView() {
        data = new OkHttpData();
        listTab = (ListView) findViewById(R.id.tab_list);
        totalMoneys = (TextView) findViewById(R.id.totalMoney);
        payWay = (TextView) findViewById(R.id.payway);
        paywaybtn = (Button) findViewById(R.id.paywaybtn);
        back = (ImageView) findViewById(R.id.back);
        datas.add(new CItem(0, "银行卡"));
        datas.add(new CItem(1, "支付宝"));
        datas.add(new CItem(2, "微信"));
        datas.add(new CItem(3, "充值卡"));
        datas.get(0).isSelect = true;

        back.setOnClickListener(this);
        paywaybtn.setOnClickListener(this);
        final BaseAdapter adapter = new CommonAdapter<CItem>(this, datas, R.layout.list_tab) {
            @Override
            public void convert(ViewHolder holder, CItem item, int position) {
                holder.setText(R.id.text, item.getValue());
                if (item.isSelect) {
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xfffa832d);
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.line_ver);
                } else {
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xff8c8c8c);
                    ((TextView) holder.getView(R.id.text)).setBackgroundColor(0xfff2f2f2);
                }
            }

        };
        listTab.setAdapter(adapter);
        payWay.setText("确认无误后去" + datas.get(0).getValue() + "付款");
        paywaybtn.setText("去" + datas.get(0).getValue() + "付款");
        listTab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                text = datas.get(position).getValue();
                for (int i = 0; i < datas.size(); i++) {
                    if (i == position) {
                        datas.get(i).isSelect = true;
                    } else {
                        datas.get(i).isSelect = false;
                    }
                }
                adapter.notifyDataSetChanged();
                totalMoneys.setText(totalMoneys.getText());
                if (datas.get(0).isSelect) {
                    payWay.setText("确认无误后去" + datas.get(0).getValue() + "付款");
                    paywaybtn.setText("去" + datas.get(0).getValue() + "付款");
                } else if (datas.get(1).isSelect) {
                    payWay.setText("确认无误后去" + datas.get(1).getValue() + "付款");
                    paywaybtn.setText("去" + datas.get(1).getValue() + "付款");
                } else if (datas.get(2).isSelect) {
                    payWay.setText("确认无误后去" + datas.get(2).getValue() + "付款");
                    paywaybtn.setText("去" + datas.get(2).getValue() + "付款");
                } else if (datas.get(3).isSelect) {
                    payWay.setText("确认无误后去" + datas.get(3).getValue() + "付款");
                    paywaybtn.setText("去" + datas.get(3).getValue() + "付款");
                }
            }
        });
    }

    private void getIntentArgs(Intent intent) {
        try {
            userid = intent.getExtras().getString("userid");
            gameid = intent.getExtras().getString("gameid");
            gameserverid = intent.getExtras().getString("gameserverid");
            xb_orderid = intent.getExtras().getString("xb_orderid");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            PaymentInfoActivity.this.finish();
            anim();
        }
        return false;
    }

    static public void setDispatcherCallBack(IDispatcherCallback listener) {
        callback = listener;
    }

    private void initViews() {
        back = (ImageView) findViewById(R.id.back);
        totalMoney = (TextView) findViewById(R.id.totalMoney);
        checkLine = (FragmentLayoutWithLine) findViewById(R.id.checkLine);
        back.setOnClickListener(this);
        fragments.clear();
        for (int i = 0; i < tabs.length; i++) {
            Bundle data = new Bundle();
            data.putString("text", tabs[i]);
            data.putString("userid", userid);
            data.putString("gameid", gameid);
            data.putString("gameserverid", gameserverid);
            data.putString("xb_orderid", xb_orderid);
            data.putString("totlesMoney", totalMoney.getText().toString());
            if (isFinishing()) {
                return;
            }
            Fragments fragmentses = new Fragments();
            fragmentses.setArguments(data);
            fragments.add(fragmentses);
        }
        checkLine.setScorllToNext(true);
        checkLine.setScorll(true);
        checkLine.setWhereTab(1);
        checkLine.setTabHeight(6, 0xfffa832d);//下划线的高度和颜色
        checkLine.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff8c8c8c);//未选中的字体颜色
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xfffa832d);//选中的字体颜色
                lastTabView.setBackgroundColor(0xffffffff);//未选中的背景色
                currentTabView.setBackgroundColor(0xffffffff);//选中的背景色

            }
        });
        checkLine.setAdapter(fragments, R.layout.tablayout_nevideo_player, 0x0102);
        checkLine.getViewPager().setOffscreenPageLimit(3);//设置tab数量 4个的话就设置3，比tab数量少1

    }

    @Override
    protected void onDestroy() {
        Utils.dismiss(pd_pay);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                anim();
                break;
            case R.id.paywaybtn:
                checkOrderInfo();
                break;
        }
    }

    private void anim() {
        overridePendingTransition(MResource.getIdByName(
                PaymentInfoActivity.this, "anim", "page_from_alpha"),
                MResource.getIdByName(PaymentInfoActivity.this, "anim",
                        "page_left_alpha"));
    }

    public void checkOrderInfo() {
        OrderInfo r = new OrderInfo();
        r.setUserid(userid);
        r.setGameid(gameid);
        r.setGameserverid(gameserverid);
        r.setXb_orderid(xb_orderid);
        r.setPayment_mode(2 + "");

        if (!totalMoneys.getText().equals("0")) {
            realTotleMoney = totalMoneys.getText().toString();
            r.setAmount(realTotleMoney);
            handlerOrder(r);
        } else {
            Toast.makeText(
                    PaymentInfoActivity.this,
                    getString(MResource.getIdByName(PaymentInfoActivity.this,
                            "string", "modeofpayment_check")),
                    Toast.LENGTH_SHORT).show();
        }
        if (datas.get(1).isSelect) {
            Pay pay = new Pay(PaymentInfoActivity.this);
            pay.pay(gamename, username + servername, ordernum, realTotleMoney, handler);
        }

    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    // String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PaymentInfoActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PaymentInfoActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PaymentInfoActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }
    };

    public void autoCancel(Cancelable task) {
        mAutoCancelController.add(task);
    }

    private void handlerOrder(OrderInfo info) {
        autoCancel(new AutoCancelServiceFramework<OrderInfo, Void, String>(mAutoCancelController) {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                pd_pay = Utils.show(PaymentInfoActivity.this, MResource
                        .getIdByName(PaymentInfoActivity.this
                                        .getApplicationContext(), "string",
                                "modeofpayment_tips"), MResource.getIdByName(
                        PaymentInfoActivity.this.getApplicationContext(),
                        "string", "modeofpayment_loading_orderinfo"));
            }

            @Override
            protected String doInBackground(OrderInfo... params) {
                // TODO Auto-generated method stub
                createIPlatCokeService();
                try {
                    return mIPlatService.toOrder(params[0]);
                } catch (CancellationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ResponseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                Utils.dismiss(pd_pay);
                if (result != null) {
                    String resultData[] = handlerResult(result);
                    // Message&Status&ordernum&GameName&ServerName&Username

                    if (resultData[1].equals("1")) {
                        ordernum = resultData[2];
                        gamename = resultData[3];
                        servername = resultData[4];
                        username = resultData[5];
                    }
                }
            }

        }.execute(info));
    }

    private String[] handlerResult(String result) {

        // Message&Status&ordernum&GameName&ServerName&Username
        String[] resultString = result.split("&");
        return resultString;
    }
}
