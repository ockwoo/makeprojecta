package it.make.must.lettle;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import it.make.must.model.Lettle;
import it.make.must.util.DeleteLettle;
import it.make.must.util.GetLettle;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "[Lettle]";

    private static String Get_Server_Url = "http://52.79.196.78:3000/v1/lettle";
    private final static String Delete_Server_Url = "http://52.79.196.78:3000/v1/lettle/";

    private Button newButton;
    public static ArrayList<Lettle> myLettle = new ArrayList<Lettle>();

    public RecyclerView rlv;
    public RecyclerAdapter recyclerAdapter;

    private Context mContext;
    Toolbar toolbar;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newButton = (Button) findViewById(R.id.mainNewLettleWriter);

        rlv = (RecyclerView) findViewById(R.id.mainRecyclerview);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), R.layout.activity_main, myLettle);
        rlv.setAdapter(recyclerAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        rlv.setHasFixedSize(true);
        rlv.setLayoutManager(layoutManager);

        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        registerForContextMenu(rlv);

        mContext = MainActivity.this;

        // ------------------------------------------------------------//
        // - EVENT LISTENER
        // ------------------------------------------------------------//
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NewMessageActivity.class);
                startActivity(i);
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        getLettleList();
        super.onStart();
    }

    public void refreshUI() {
        recyclerAdapter.notifyDataSetChanged();
    }

    public void getLettleList() {
        try {
            GetLettle hu = new GetLettle(mContext);
            hu.execute(Get_Server_Url);
        } catch (Exception e) {
            Toast.makeText(mContext, "서버 접속에 실패 하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_listview, menu);
        super.onCreateContextMenu(menu,v,menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int index = recyclerAdapter.getItemPosition();
        if(index < 0) return true;
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                alert_confirm.setMessage("레틀을 삭제 하시겠습니까?")
                        .setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        DeleteLettle hu = new DeleteLettle(mContext);
                        hu.execute(Delete_Server_Url + myLettle.get(index).getId());
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                break;
            default:
                break;
        }
        return true;
    }

}


class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {
    Context context;
    ArrayList<Lettle> list;
    int layout;
    int myposition;

    public RecyclerAdapter(Context context, int layout, ArrayList<Lettle> al) {
        this.context = context;
        this.layout = layout;
        this.list = al;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.ow,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Lettle item = list.get(position);
        final int itemPosition = position;

        // - Title
        String title = item.getContents().getTitle();
        if(title != null) {
            if(title.length() > 15) title = title.substring(0,15) + "...";
            holder.tvTitle.setText(title);
        }

        // - Msg Body
        String msg = item.getContents().getMsg();
        if(msg != null && msg.length() > 60) {
            msg = msg.substring(0,65) + "...";
        }
        holder.tvMsg.setText(msg);

        // - Date
        holder.tvDate.setText(item.getDateTime());

        // - Item Click Event
        holder.tvlettleListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ViewerActivity.class);
                i.putExtra("lettleObject", item);
                v.getContext().startActivity(i);
            }
        });

        holder.tvlettleListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.showContextMenu();
                myposition = itemPosition;
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public int getItemPosition() {
        return this.myposition;
    }

    public String getItemLettleId(int position) {
        return this.list.get(position).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvTitle;
        TextView tvMsg;
        LinearLayout tvlettleListView;
        Context mContext;

        public ViewHolder(View convertView) {
            super(convertView);
            tvDate = (TextView) convertView.findViewById(R.id.textDate);
            tvTitle = (TextView) convertView.findViewById(R.id.textTitle);
            tvMsg = (TextView) convertView.findViewById(R.id.textMsg);
            tvlettleListView = (LinearLayout) convertView.findViewById(R.id.lettleListView);
        }
    }
}