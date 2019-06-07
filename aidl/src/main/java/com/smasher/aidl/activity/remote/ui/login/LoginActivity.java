package com.smasher.aidl.activity.remote.ui.login;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.smasher.aidl.IMyAidlInterface;
import com.smasher.aidl.R;
import com.smasher.aidl.service.local.LocalService;


/**
 * @author matao
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ConstraintLayout mContainer;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private ProgressBar mLoading;
    private Button mBind;
    private Button mUnBind;
    private Button mGetInt;


    private LoginViewModel loginViewModel;
    private Intent mIntent;
    private boolean isBind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
        initService();
    }

    private void initService() {
        mIntent = new Intent(this, LocalService.class);
        startService(mIntent);
    }

    private void initListener() {

        mLogin.setOnClickListener(mOnClickListener);
        mBind.setOnClickListener(mOnClickListener);
        mUnBind.setOnClickListener(mOnClickListener);
        mGetInt.setOnClickListener(mOnClickListener);

        mUsername.addTextChangedListener(afterTextChangedListener);
        mPassword.addTextChangedListener(afterTextChangedListener);
        mPassword.setOnEditorActionListener(mOnEditorActionListener);
    }

    private void initView() {
        mContainer = findViewById(R.id.container);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mLoading = findViewById(R.id.loading);
        mBind = findViewById(R.id.bind);
        mUnBind = findViewById(R.id.unBind);
        mGetInt = findViewById(R.id.getInt);


        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, loginFormState);
        loginViewModel.getLoginResult().observe(this, loginResult);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        unBindMService();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // ignore
        }

        @Override
        public void afterTextChanged(Editable s) {
            loginViewModel.loginDataChanged(mUsername.getText().toString(),
                    mPassword.getText().toString());
        }
    };


    TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(mUsername.getText().toString(),
                        mPassword.getText().toString());
            }
            return false;
        }
    };


    Observer<LoginFormState> loginFormState = new Observer<LoginFormState>() {
        @Override
        public void onChanged(@Nullable LoginFormState loginFormState) {
            if (loginFormState == null) {
                return;
            }
            mLogin.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                mUsername.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                mPassword.setError(getString(loginFormState.getPasswordError()));
            }
        }
    };


    Observer<LoginResult> loginResult = new Observer<LoginResult>() {
        @Override
        public void onChanged(@Nullable LoginResult loginResult) {
            if (loginResult == null) {
                return;
            }
            mLoading.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            finish();
        }
    };


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.login) {
                mLoading.setVisibility(View.VISIBLE);
                loginViewModel.login(mUsername.getText().toString(),
                        mPassword.getText().toString());
            } else if (i == R.id.bind) {
                try {
                    isBind = bindService(mIntent, mConnection, BIND_AUTO_CREATE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (i == R.id.unBind) {
                unBindMService();
            } else if (i == R.id.getInt) {
                try {
                    if (myAidlInterface != null) {
                        int test = myAidlInterface.getTestInt();
                        Log.d(TAG, "getInt: " + test);

                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void unBindMService() {
        if (isBind) {
            unbindService(mConnection);
            isBind = false;
        }
    }

    IMyAidlInterface myAidlInterface;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}
