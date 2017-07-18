package com.ailicai.app.ui.paypassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.rsa.RSAEncrypt;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.PayPwdResetRequest;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author owen
 *         2016/1/6
 */
public class PayPwdResetActivity extends BaseBindActivity {

    @Bind(R.id.etPayPwdName)
    EditText etName;

    @Bind(R.id.etPayPwdIdCard)
    EditText etIdCard;

    @Bind(R.id.btPayPwdNext)
    Button btPayPwdNext;

    private boolean isNameNull;
    private boolean isIdCardNull;

    @Override
    public int getLayout() {
        return R.layout.activity_pay_pwd_reset;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && isIdCardNull) {
                    btPayPwdNext.setEnabled(true);
                } else {
                    btPayPwdNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isNameNull = s.length() > 0;

            }
        });
        etIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 17 && isNameNull) {
                    btPayPwdNext.setEnabled(true);
                } else {
                    btPayPwdNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isIdCardNull = s.length() > 17;
            }
        });
    }

    @OnClick(R.id.btPayPwdNext)
    void next() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        requestForNext();
    }

    @OnClick(R.id.idCardScan)
    void idCardScan() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        //TODO nanshan 扫描身份证 暂时隐藏
//        Intent intent = new Intent(this, IDCardScanActivity.class);
//        startActivityForResult(intent, 17);
    }

    private void requestForNext() {
        PayPwdResetRequest params = new PayPwdResetRequest();
        params.setUserName(getName());
        params.setIdCardNo(getIdCardNo());
        ServiceSender.exec(this, params, new IwjwRespListener<Response>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(Response jsonObject) {
                showContentView();
                startNext();
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.show(errorInfo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                showContentView();
            }
        });
    }

    private void startNext() {
        Intent intent = new Intent(PayPwdResetActivity.this, PayPwdCheckPhoneActivity.class);
        intent.putExtra("TYPE", 2);
        startActivityForResult(intent, 200);
    }

    private String getName() {
        return RSAEncrypt.encrypt(etName.getText().toString());
    }

    private String getIdCardNo() {
        return RSAEncrypt.encrypt(etIdCard.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        if (requestCode == 17 && resultCode == RESULT_OK) {
            if (data != null) {
                String cardName = data.getStringExtra("cardName");
                String idCardNo = data.getStringExtra("idCardNumber");
                etName.setText(cardName);
                etName.setSelection(cardName.length());
                etIdCard.setText(idCardNo);
                etIdCard.setSelection(idCardNo.length());
            }
        }

    }
}
