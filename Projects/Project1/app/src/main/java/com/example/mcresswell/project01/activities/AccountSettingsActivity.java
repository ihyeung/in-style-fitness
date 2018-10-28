package com.example.mcresswell.project01.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.fragments.AccountSettingsFragment;

import static com.example.mcresswell.project01.util.Constants.BACK_PRESSED;
import static com.example.mcresswell.project01.util.Constants.CREATE;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, CREATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        m_fTrans = getSupportFragmentManager().beginTransaction();
        if(isWideDisplay()){
            m_fTrans.replace(R.id.fl_detail_wd, new AccountSettingsFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        } else {
            m_fTrans.replace(R.id.fl_master_nd, new AccountSettingsFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, BACK_PRESSED);
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
