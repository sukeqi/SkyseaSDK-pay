package com.skysea.sdk.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skysea.alipay.AlixPay;
import com.skysea.android.app.lib.MResource;
import com.skysea.app.BaseActivity;
import com.skysea.async.AutoCancelController;
import com.skysea.async.AutoCancelServiceFramework;
import com.skysea.async.Cancelable;
import com.skysea.bean.CItem;
import com.skysea.bean.OrderInfo;
import com.skysea.exception.ResponseException;
import com.skysea.sdk.R;
import com.skysea.utils.UtilTools;
import com.skysea.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Created by jyd-pc006 on 16/8/16.
 */
public class Fragments extends Fragment implements View.OnClickListener {

    private View view;
    private TextView confirmpay;
    private Button gotopay;
    private TextView version;
    private List<CItem> items;

    ProgressDialog pd_pay;
    String text;
    String userid;
    String gameid;
    String gameserverid;
    String xb_orderid;
    String totlesMoney;

    String ordernum;
    String gamename;
    String servername;
    String username;
    private AutoCancelController mAutoCancelController = new AutoCancelController();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gotopay:
                checkOrderInfo();
                break;
        }
    }

    private View initView() {
        view = View.inflate(getActivity(), R.layout.paymentway, null);
        confirmpay = (TextView) view.findViewById(R.id.confirmpay);
        gotopay = (Button) view.findViewById(R.id.gotopay);
        version = (TextView) view.findViewById(R.id.version);
        gotopay.setOnClickListener(this);

        version.setText(UtilTools.getVersionName(getActivity()));
        Bundle bundle = getArguments();
        text = bundle.getString("text");
        userid = bundle.getString("userid");
        gameid = bundle.getString("gameid");
        gameserverid = bundle.getString("gameserverid");
        totlesMoney = bundle.getString("totlesMoney");

        xb_orderid = bundle.getString("xb_orderid");

        return view;
    }

    public void checkOrderInfo() {
        OrderInfo r = new OrderInfo();
        r.setUserid(userid);
        r.setGameid(gameid);
        r.setGameserverid(gameserverid);
        r.setXb_orderid(xb_orderid);
        r.setPayment_mode(text);

        if (!totlesMoney.equals("0")) {
            r.setAmount(totlesMoney);
            handlerOrder(r);
        } else {
            Toast.makeText(
                    getActivity(),
                    getString(MResource.getIdByName(getActivity(),
                            "string", "modeofpayment_check")),
                    Toast.LENGTH_SHORT).show();
        }
        if (text.equals(PaymentInfoActivity.tabs[0])) {
            confirmpay.setText("确认无误后去" + text + "支付");
            gotopay.setText("去" + text + "支付");
        } else if (text.equals(PaymentInfoActivity.tabs[1])) {
            confirmpay.setText("确认无误后去" + text + "支付");
            gotopay.setText("去" + text + "支付");
            AlixPay alixPay = new AlixPay(getActivity(), r);
            alixPay.pay();
        } else if (text.equals(PaymentInfoActivity.tabs[2])) {
            confirmpay.setText("确认无误后去" + text + "支付");
            gotopay.setText("去" + text + "支付");
        } else if (text.equals(PaymentInfoActivity.tabs[3])) {
            confirmpay.setText("确认无误后去" + text + "支付");
            gotopay.setText("去" + text + "支付");
        }
    }

    public void autoCancel(Cancelable task) {
        mAutoCancelController.add(task);
    }

    private void handlerOrder(OrderInfo info) {
        autoCancel(new AutoCancelServiceFramework<OrderInfo, Void, String>(mAutoCancelController) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
