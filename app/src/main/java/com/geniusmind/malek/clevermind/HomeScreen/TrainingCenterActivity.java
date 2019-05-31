package com.geniusmind.malek.clevermind.HomeScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geniusmind.malek.clevermind.Model.CourseInformation;
import com.geniusmind.malek.clevermind.Model.CourseInformationLab;
import com.geniusmind.malek.clevermind.R;

public class TrainingCenterActivity extends AppCompatActivity {
    private static final String ID_KEY = "id_keys";
    private String ID;
    private Toolbar mToolbar;
    private TextView captionText, dayText, timeText, infoText, priceText;
    private ImageView CourseImage;
    private RadioButton radioBtnPrivate, radioBtnPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get course id from previous intent . . . ;
        ID = getIntent().getExtras().getString(ID_KEY);

        setContentView(R.layout.activity_training_center);

        // set action bar attribute from below method . . . ;
        setActionBar();

        // initalize views . . . ;
        initializeViews();


        // get data from CourseInformationLab(singleton class) then put data in Training info object . . . ;
        CourseInformation trainingInfo = CourseInformationLab.getInstance().get(ID);

        // put data inside views . . . ;
        putDataInsideViews(trainingInfo);
    }

    // initialize views and casting(if required) . . . ;
    private void initializeViews() {
        captionText = findViewById(R.id.caption_training_center);
        dayText = findViewById(R.id.day_training_center);
        timeText = findViewById(R.id.date_training_center);
        infoText = findViewById(R.id.textView17);
        priceText = findViewById(R.id.text_price_label);
        CourseImage = findViewById(R.id.imageView6);
        radioBtnPrivate = findViewById(R.id.radio_btn_private);
        radioBtnPublic = findViewById(R.id.radio_btn_public);


    }

    // set action bar attribute inside these method . . .  ;
    private void setActionBar() {
        mToolbar = findViewById(R.id.toolbar_training_center);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.training_center);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // create newIntent Method for start this activity and pass data from another activity . . . ;
    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, TrainingCenterActivity.class);
        intent.putExtra(ID_KEY, id);
        return intent;
    }

    // put data inside views . . . ;
    private void putDataInsideViews(CourseInformation information) {
        captionText.setText(information.getTitle());
        dayText.setText(information.getDays());
        timeText.setText(information.getTime());
        infoText.setText(information.getCourseInfo());
        priceText.setText(String.valueOf(information.getPrice() + " JD"));
        // put image from url to image view using glide library . . . ;
        Glide.with(this)
                .load(information.getUrl())
                .centerCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(CourseImage);





    }

    // Todo Set Event for attend button and send private or public tutorial . . . ;
    public void AttendBtn(View view) {


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
