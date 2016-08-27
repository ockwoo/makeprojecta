package it.make.must.lettle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoadingActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getSupportActionBar().hide();
        findViewById(R.id.loginButton).setOnClickListener(loginButtonClickListener);
        findViewById(R.id.signButton).setOnClickListener(signButtonClickListener);
    }

    Button.OnClickListener loginButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), MainActivity.class);
            startActivity(i);
        }
    };

    Button.OnClickListener signButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), SignActivity.class);
            startActivity(i);
        }
    };
}
