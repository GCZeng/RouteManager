package org.zgc.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;
import org.zgc.R;
import org.zgc.activity.base.BaseActivity;
import org.zgc.util.RouteUtil;
import org.zgc.util.ToastUtil;
import org.zgc.util.VerifyUtil;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nick on 2017/10/2
 */
public class MACAddACtivity extends BaseActivity implements View.OnClickListener {
    private EditText et_mac1;
    private EditText et_mac2;
    private EditText et_mac3;
    private EditText et_mac4;
    private EditText et_mac5;
    private EditText et_mac6;
    private Button btn_add;

    @Override
    public void initView() {
        setContentView(R.layout.activity_mac_add);

        et_mac1 = (EditText) findViewById(R.id.et_mac1);
        et_mac2 = (EditText) findViewById(R.id.et_mac2);
        et_mac3 = (EditText) findViewById(R.id.et_mac3);
        et_mac4 = (EditText) findViewById(R.id.et_mac4);
        et_mac5 = (EditText) findViewById(R.id.et_mac5);
        et_mac6 = (EditText) findViewById(R.id.et_mac6);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                addMac();
                break;
        }
    }

    public void addMac() {
        String mac = getMac();

        Observable.create(subscriber -> {


            try {
                RouteUtil.getInstance().addMac(mac);
            } catch (IOException e) {
                e.printStackTrace();
            }
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                }, Throwable::printStackTrace, () -> {
                });
    }

    private String getMac() {
        String mac1 = et_mac1.getText().toString().trim();
        String mac2 = et_mac2.getText().toString().trim();
        String mac3 = et_mac3.getText().toString().trim();
        String mac4 = et_mac4.getText().toString().trim();
        String mac5 = et_mac5.getText().toString().trim();
        String mac6 = et_mac6.getText().toString().trim();

        if (StringUtils.isEmpty(mac1) || StringUtils.isEmpty(mac2) || StringUtils.isEmpty(mac3) ||
                StringUtils.isEmpty(mac4) || StringUtils.isEmpty(mac5) || StringUtils.isEmpty(mac6)) {
            ToastUtil.showLong(R.string.please_input_mac_address2);
            return null;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(mac1);
        sb.append(":");
        sb.append(mac2);
        sb.append(":");
        sb.append(mac3);
        sb.append(":");
        sb.append(mac4);
        sb.append(":");
        sb.append(mac5);
        sb.append(":");
        sb.append(mac6);
        String mac = sb.toString();
        if (!VerifyUtil.verifyMAC(mac)) {
            ToastUtil.showLong(R.string.please_input_mac_right);
            return null;
        }

        try {
            List<String> macList = getIntent().getExtras().getStringArrayList(MainActivity.MAC_LIST);
            for (String macStr : macList) {
                if (macStr.equals(mac.toUpperCase())) {
                    ToastUtil.showLong(R.string.the_mac_is_alread_exited);
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mac;
    }
}

