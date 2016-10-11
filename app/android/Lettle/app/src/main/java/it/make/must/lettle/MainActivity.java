package it.make.must.lettle;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
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

    private  TextView mainTextTile;
    private ImageView toolbarBackImg;
    public static ArrayList<Lettle> myLettle = new ArrayList<Lettle>();

    public RecyclerView rlv;
    public RecyclerAdapter recyclerAdapter;

    private Context mContext;
    Toolbar toolbar;

    private AppBarLayout appbar;
    private  CollapsingToolbarLayout collToolbar;
    private GoogleApiClient client;

    private Button homeBtn;
    private Button letterBtn;
    private Button bottleBtn;
    private Button writeBtn;
    private Button settingBtn;
    private View underBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeBtn = (Button) findViewById(R.id.mainHomeBtn);
        letterBtn = (Button) findViewById(R.id.mainLetterBtn);
        bottleBtn = (Button) findViewById(R.id.mainBottleBtn);
        writeBtn = (Button) findViewById(R.id.mainWriterBtn);
        settingBtn = (Button) findViewById(R.id.mainSettingBtn);

        rlv = (RecyclerView) findViewById(R.id.mainRecyclerview);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), R.layout.activity_main, myLettle);
        rlv.setAdapter(recyclerAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rlv.setHasFixedSize(true);
        rlv.setLayoutManager(layoutManager);

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.mainCollapsing);
        mCollapsingToolbarLayout.setTitle("Lettle");

        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        registerForContextMenu(rlv);

        mContext = MainActivity.this;

        // ------------------------------------------------------------//
        // - EVENT LISTENER
        // ------------------------------------------------------------//
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NewMessageActivity.class);
                startActivity(i);
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mainTextTile = (TextView) findViewById(R.id.mainlogoTitle);
        mainTextTile.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"NanumBrush.otf"));

        appbar =  (AppBarLayout) findViewById(R.id.mainAppbar);
        underBar = (View) findViewById(R.id.mainunderbar);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset < -450) {
                    underBar.setVisibility(View.GONE);
                    homeBtn.setText(""); homeBtn.setPadding(80,0,0,0);
                    homeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.mainhomebtn, 0, 0, 0);

                    letterBtn.setText(""); letterBtn.setPadding(80,0,0,0);
                    letterBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.mainletterbtn, 0, 0, 0);

                    bottleBtn.setText(""); bottleBtn.setPadding(80,0,0,0);
                    bottleBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.mainbottlebtn, 0, 0, 0);

                    writeBtn.setText(""); writeBtn.setPadding(80,0,0,0);
                    writeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.mainwritebtn, 0, 0, 0);

                    settingBtn.setText(""); settingBtn.setPadding(80,0,0,0);
                    settingBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.mainsettingbtn, 0, 0, 0);
                } else {
                    underBar.setVisibility(View.VISIBLE);
                    homeBtn.setText("HOME"); homeBtn.setBackgroundColor(Color.WHITE); homeBtn.setPadding(0,0,0,0);
                    homeBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);

                    letterBtn.setText("LETTER"); letterBtn.setBackgroundColor(Color.WHITE); letterBtn.setPadding(0,0,0,0);
                    letterBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);

                    bottleBtn.setText("BOTTLE"); bottleBtn.setBackgroundColor(Color.WHITE); bottleBtn.setPadding(0,0,0,0);
                    bottleBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);

                    writeBtn.setText("WRITE"); writeBtn.setBackgroundColor(Color.WHITE); writeBtn.setPadding(0,0,0,0);
                    writeBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);

                    settingBtn.setText("SETTING"); settingBtn.setBackgroundColor(Color.WHITE); settingBtn.setPadding(0,0,0,0);
                    settingBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                }
            }
        });



    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_back_img_05);
        bitmap = cropCenterBitmap(bitmap, toolbar.getWidth(), toolbar.getHeight());

        /*RenderScript rs = RenderScript.create(getApplicationContext());
        Allocation input = Allocation.createFromBitmap(rs, bitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(5.f); //0.0f ~ 25.0f
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);*/

        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
        //toolbar.setBackground(ob);
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

    public static Bitmap cropCenterBitmap(Bitmap src, int w, int h) {
        if(src == null) return null;

        int width = src.getWidth();
        int height = src.getHeight();

        if(width < w && height < h) return src;

        int x = 0;
        int y = 0;

        if(width > w) x = (width - w) / 2;

        if(height > h) y = (height - h) / 2;

        int cw = w; // crop width
        int ch = h; // crop height

        if(w > width) cw = width;
        if(h > height) ch = height;

        return Bitmap.createBitmap(src, x, y, cw, ch);
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
        if(msg != null && msg.length() > 75) {
            msg = msg.substring(0,75) + "...";
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

            tvMsg.setTypeface(Typeface.createFromAsset(context.getAssets(),"NanumGothic.otf"));
            tvTitle.setTypeface(Typeface.createFromAsset(context.getAssets(),"NanumGothicBold.otf"));
            tvDate.setTypeface(Typeface.createFromAsset(context.getAssets(),"NanumGothicBold.otf"));
        }
    }
}