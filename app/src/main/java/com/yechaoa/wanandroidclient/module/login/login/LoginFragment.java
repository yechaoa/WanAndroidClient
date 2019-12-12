package com.yechaoa.wanandroidclient.module.login.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.yechaoa.wanandroidclient.R;
import com.yechaoa.wanandroidclient.base.BaseBean;
import com.yechaoa.wanandroidclient.base.BaseFragment;
import com.yechaoa.wanandroidclient.bean.User;
import com.yechaoa.wanandroidclient.common.GlobalConstant;
import com.yechaoa.wanandroidclient.module.MainActivity;
import com.yechaoa.wanandroidclient.module.login.LoginActivity;
import com.yechaoa.wanandroidclient.module.login.register.RegisterFragment;
import com.yechaoa.yutils.SpUtil;
import com.yechaoa.yutils.ToastUtil;
import com.yechaoa.yutils.YUtils;

import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment<LoginPresenter> implements ILoginView {

    @BindView(R.id.til_username)
    TextInputLayout mTilUsername;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.til_password)
    TextInputLayout mTilPassword;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    private String mUsername;
    private String mPassword;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initData() {
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mTilUsername.getEditText().getText().length() > mTilUsername.getCounterMaxLength())
                mTilUsername.setError("输入内容超过上限");
            else if (mTilUsername.getEditText().getText().length() < mTilUsername.getCounterMaxLength() / 2)
                mTilUsername.setError("最少6位");
            else
                mTilUsername.setError(null);

            if (mTilPassword.getEditText().getText().length() > mTilPassword.getCounterMaxLength())
                mTilPassword.setError("输入内容超过上限");
            else if (mTilPassword.getEditText().getText().length() < mTilPassword.getCounterMaxLength() / 2)
                mTilPassword.setError("最少6位");
            else
                mTilPassword.setError(null);
        }
    };

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                YUtils.closeSoftKeyboard();
                if (isUsernameValid() && isPasswordValid()) {
                    showLoading();
                    presenter.submit(mUsername, mPassword);
                } else {
                    ToastUtil.showToast("Failed (°∀°)ﾉ");
                }
                break;
            case R.id.btn_register:
                LoginActivity activity = (LoginActivity) getActivity();
                activity.addFragment(new RegisterFragment(), "register");
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("login"));
                fragmentTransaction.commit();
                break;
        }
    }

    private boolean isUsernameValid() {
        mUsername = mEtUsername.getText().toString().trim();
        return !TextUtils.isEmpty(mUsername) && mUsername.length() <= mTilUsername.getCounterMaxLength() && mTilUsername.getCounterMaxLength() / 2 <= mUsername.length();
    }

    private boolean isPasswordValid() {
        mPassword = mEtPassword.getText().toString().trim();
        return !TextUtils.isEmpty(mPassword) && mPassword.length() <= mTilPassword.getCounterMaxLength() && mTilPassword.getCounterMaxLength() / 2 <= mPassword.length();
    }

    @Override
    public void showLoginSuccess(String successMessage) {
        hideLoading();
        ToastUtil.showToast(successMessage);
    }

    @Override
    public void showLoginFailed(String errorMessage) {
        hideLoading();
        ToastUtil.showToast(errorMessage);
    }

    @Override
    public void doSuccess(BaseBean<User> user) {
        SpUtil.setBoolean(GlobalConstant.IS_LOGIN, true);
        SpUtil.setString(GlobalConstant.USERNAME, user.data.username);
        SpUtil.setString(GlobalConstant.USERNAME, user.data.password);

        startActivity(new Intent(mContext, MainActivity.class));
        getActivity().finish();
    }

}
