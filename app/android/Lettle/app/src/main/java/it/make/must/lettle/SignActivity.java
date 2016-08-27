package it.make.must.lettle;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.EditText;
import android.widget.TextView;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        EditText tvPhone = (EditText)findViewById(R.id.signPhoneNumber);

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telManager.getLine1Number();
        tvPhone.setText(phoneNumber);

        getSupportActionBar().setTitle("LETTLE REGISTRATION");
    }
}
