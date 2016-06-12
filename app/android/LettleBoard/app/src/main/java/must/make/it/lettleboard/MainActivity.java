package must.make.it.lettleboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button newButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.newButton).setOnClickListener(newButtonClickListener);

    }

    Button.OnClickListener newButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), NewMessage.class);
            startActivity(i);
        }
    };

}
