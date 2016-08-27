package it.make.must.lettle;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import it.make.must.model.Lettle;
import it.make.must.util.GetLettle;

public class MainActivity extends AppCompatActivity {

    private final String  TAG = "[Lettle]";

    private Button newButton;
    public static ArrayList<Lettle> myLettle = new ArrayList<Lettle>();
    public ListView lv = null;
    private MyAdapter dataAdapter = null;
    private Context mContext;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mainNewLettleWriter).setOnClickListener(newButtonClickListener);
        lv = (ListView)findViewById(R.id.mainListView);
        dataAdapter = new MyAdapter(getApplicationContext(), R.layout.ow, myLettle);
        lv.setAdapter(dataAdapter);

        // 이벤트 처리
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test", "아이템클릭, postion : " + position +", id : " + id);
                Intent i = new Intent(view.getContext(), ViewerActivity.class);
                i.putExtra("lettleObject", myLettle.get(position));
                startActivity(i);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mContext = MainActivity.this;
    }

    @Override
    protected void onStart() {
        try {
            GetLettle hu = new GetLettle(mContext);
            hu.execute("http://52.79.196.78:3000/v1/lettle");
        } catch (Exception e) {
            Toast.makeText(mContext, "서버 접속에 실패 하였습니다.", Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }

    Button.OnClickListener newButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), NewMessageActivity.class);
            startActivity(i);
        }
    };

    public void refreshUI() {
        String result = "";
        for(int i = 0; i < myLettle.size(); i++) {
            Log.d(TAG, "[MainActivity] getId() = " + myLettle.get(i).getId());
        }
        dataAdapter.notifyDataSetChanged();
    }
}



class MyAdapter extends BaseAdapter {

    Context context;     // 현재 화면의 제어권자
    int layout;              // 한행을 그려줄 layout
    ArrayList<Lettle> al;     // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요

    public MyAdapter(Context context, int layout, ArrayList<Lettle> al) {
        this.context = context;
        this.layout = layout;
        this.al = al;
        this.inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int position) {
        return al.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inf.inflate(layout, null);

        TextView tvDate=(TextView)convertView.findViewById(R.id.textDate);
        TextView tvTitle=(TextView)convertView.findViewById(R.id.textTitle);
        TextView tvMsg =(TextView)convertView.findViewById(R.id.textMsg);
        Lettle m = al.get(position);

        String title = m.getContents().getTitle();
        if(title != null) {
            if(title.length() > 15) {
                title = title.substring(0,15) + "...";
            }
            tvTitle.setText(title);
        }
        tvDate.setText(m.getDateTime());
        tvMsg.setText(m.getContents().getMsg());
       /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try{
            Date tempDate = sdf.parse(m.getEvent().getTimestamp());
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            tvDate.setText(sdf2.format(tempDate));
        } catch (Exception e) {
            Log.d("LETTLE", e.toString());
        }*/
        return convertView;
    }
}