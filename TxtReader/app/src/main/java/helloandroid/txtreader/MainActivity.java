package helloandroid.txtreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_to_Txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_to_Txt = (Button) findViewById(R.id.btn_to_Txt);
        btn_to_Txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_to_Txt:
                Intent intent = new Intent(MainActivity.this, ReaderActivity.class);
                startActivity(intent);
                break;
        }
    }
}
