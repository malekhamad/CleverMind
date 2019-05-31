package com.geniusmind.malek.clevermind.HomeScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geniusmind.malek.clevermind.Model.NewsInformation;
import com.geniusmind.malek.clevermind.Model.NewsInformationLab;
import com.geniusmind.malek.clevermind.R;

import java.util.List;

public class LatestNewsActivity extends AppCompatActivity {
private ListView mListView;
private NewsInformationLab infoLab;
public static final String POSITION_KEY="position";
private int position;
private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position=(Integer)getIntent().getExtras().getInt(POSITION_KEY);
        setContentView(R.layout.activity_latest_news);
        mToolbar=findViewById(R.id.toolbar_news);
        setActionBar();


        mListView=findViewById(R.id.latest_news_list);
        infoLab= NewsInformationLab.getInstance();

        // set adapter using LatestNewsAdapter class in list view . . . ;
        mListView.setAdapter(new LatestNewsAdapter(this,R.layout.list_latest_news,infoLab.get()));

        //  set focus for the item from position variable . . . ;
        mListView.setSelection(position);


    }

    private void setActionBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // create adapter for list view  .. . .. ;
    private class LatestNewsAdapter extends ArrayAdapter<NewsInformation>{
        Context context;
        int resource;
        List<NewsInformation>objects;

        public LatestNewsAdapter( Context context, int resource, List<NewsInformation> objects) {
            super(context, resource, objects);
            this.context=context;
            this.resource=resource;
            this.objects=objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView= LayoutInflater.from(context).inflate(resource,parent,false);
            TextView text_product,text_subtitle,text_caption;
            ImageView imageProduct;

            // get object from information depending at position . . . ;
            NewsInformation information=objects.get(position);

            text_product=convertView.findViewById(R.id.text_title_product);
            text_subtitle=convertView.findViewById(R.id.text_subtitle_product);
            text_caption=convertView.findViewById(R.id.text_caption_product);
            imageProduct=convertView.findViewById(R.id.image_product);

            // put data inside views . . . ;;
            text_product.setText(information.getTitle());
            text_subtitle.setText(information.getSubtitle());
            text_caption.setText(information.getDescription());

            Glide.with(LatestNewsActivity.this)
                    .load(information.getImg_url())
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(imageProduct);




            return convertView;
        }
    }

    // create intent target method . . . ;
    public static Intent newIntent(Context context,int position){
        Intent intent=new Intent(context,LatestNewsActivity.class);
        intent.putExtra(POSITION_KEY,position);
        return intent;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_screen,menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
