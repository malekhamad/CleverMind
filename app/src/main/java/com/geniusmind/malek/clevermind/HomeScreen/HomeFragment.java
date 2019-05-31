package com.geniusmind.malek.clevermind.HomeScreen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.geniusmind.malek.clevermind.ContractUrl;
import com.geniusmind.malek.clevermind.Model.CourseInformation;
import com.geniusmind.malek.clevermind.Model.CourseInformationLab;
import com.geniusmind.malek.clevermind.Model.MySingleton;
import com.geniusmind.malek.clevermind.Model.NewsInformation;
import com.geniusmind.malek.clevermind.Model.NewsInformationLab;
import com.geniusmind.malek.clevermind.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ViewPager mViewPager;
    private TextView textlabel1,textlabel2;
    private LinearLayout mLinearLayout;
    private NewsPagerAdapter mNewsPagerAdapter;


    // create static method to initalize object from this class using from  another class . . . ;
    public static Fragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_training_center);
        mViewPager = view.findViewById(R.id.viewpager_news);
        textlabel1=view.findViewById(R.id.textView5);
        textlabel2=view.findViewById(R.id.textView6);
        mLinearLayout=view.findViewById(R.id.home_progress);
        Hideviews(View.GONE);
        int courseSize=CourseInformationLab.getInstance().get().size();
        int courseNews=NewsInformationLab.getInstance().get().size();

        if(courseSize>0&&courseNews>0){
            updateRecyclerView(CourseInformationLab.getInstance().get());
            updateViewPager(NewsInformationLab.getInstance().get());
        }else {
            getCoursesFromDatabase();
            getNewsFromDatabase();
        }

        return view;
    }

    private void updateRecyclerView(List<CourseInformation>list) {
        // for recycler view . . . ;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new TrainingCenterAdapter(list));
        Hideviews(View.VISIBLE);

    }

    private void updateViewPager(List<NewsInformation> newsList) {
        mNewsPagerAdapter = new NewsPagerAdapter(newsList, getActivity());
        mViewPager.setAdapter(mNewsPagerAdapter);
        mViewPager.setPadding(200, 0, 200, 0);
        int NewsSize = newsList.size();
        mViewPager.setCurrentItem(getMiddlePosition(NewsSize), true);
        mViewPager.setPageMargin(24);
        mViewPager.setOffscreenPageLimit(3);
        Hideviews(View.VISIBLE);

        // Todo : set view pager style for previous and newx card . . . ;
    }

    // get middle position in view pager . . . ;
    public static int getMiddlePosition(int size) {

        return size % 2 == 0 ? size / 2 - 1 : size / 2;

    }

    // create view holder class . . . ;
    private class TrainingCenterHolder extends RecyclerView.ViewHolder {
        private TextView captionText, dateText, timeText;
        private ImageButton attendButton;
        private ImageView productImage;

        public TrainingCenterHolder(View view) {
            super(view);
            captionText = view.findViewById(R.id.training_title_label);
            dateText = view.findViewById(R.id.training_day_label);
            timeText = view.findViewById(R.id.training_time_label);
            attendButton = view.findViewById(R.id.attend_now_btn);
            productImage=view.findViewById(R.id.training_course_image);

        }

        // bind data in views. . . . . .
        public void bind(CourseInformation info) {
            captionText.setText(info.getTitle());
            dateText.setText(info.getDays());
            timeText.setText(info.getTime());

            // put image from url to image view using glide library . . . ;
            Glide.with(getActivity())
                    .load(info.getUrl())
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(productImage);


        }

    }

    // create adapter for view holder to bind data inside recycler_view . . . ;
    private class TrainingCenterAdapter extends RecyclerView.Adapter<TrainingCenterHolder> {
        List<CourseInformation> listTraining;

        // create constructor to pass data .  .  . . ;
        public TrainingCenterAdapter(List<CourseInformation> listTraining) {
            this.listTraining = listTraining;
        }


        @Override
        public TrainingCenterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_training_center, viewGroup, false);
            return new TrainingCenterHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TrainingCenterHolder trainingCenterHolder, final int i) {
            // get training information by using positiion id . . ;
            final CourseInformation training = listTraining.get(i);
            // pass training object to bind method to put value inside views  . . . ;
            trainingCenterHolder.bind(training);
            // when click on attend button . . . ;
            trainingCenterHolder.attendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = training.getId();
                    startActivity(TrainingCenterActivity.newIntent(getActivity(), ID));
                }
            });


        }

        @Override
        public int getItemCount() {
            return listTraining.size();
        }
    }

    // create adapter for view pager items . . . ;
    private class NewsPagerAdapter extends PagerAdapter {
        LayoutInflater mLayoutInflater;
        List<NewsInformation> list;
        Context context;


        // constructor for pass array list and context from the fragment . . . . ;
        public NewsPagerAdapter(List<NewsInformation> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view.equals(o);
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            mLayoutInflater = LayoutInflater.from(context);
            View view = mLayoutInflater.inflate(R.layout.list_news_pager, container, false);
            TextView textProduct, textTitle;
            ImageView imageProduct;
            textProduct = view.findViewById(R.id.text_news_product);
            textTitle = view.findViewById(R.id.text_news_title);
            imageProduct = view.findViewById(R.id.image_news_product);

            // get object from news information class depending to his position  . . . ;
            NewsInformation newsInformation = list.get(position);

            textProduct.setText(newsInformation.getTitle());
            textTitle.setText(newsInformation.getSubtitle());

            Glide.with(getActivity())
                    .load(newsInformation.getImg_url())
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(imageProduct);

            container.addView(view, 0);

            // set onclick listener for views . . . ;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(LatestNewsActivity.newIntent(getActivity(), position));
                }
            });

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    private void getNewsFromDatabase() {
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, ContractUrl.NEWS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("no item")) {
                    Toast.makeText(getActivity(), "no item found !", Toast.LENGTH_SHORT).show();
                } else {
                    fetchDataFromJsonResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Request Error", error.toString()
                );
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void fetchDataFromJsonResponse(String response) {
        List<NewsInformation> list = new ArrayList<>();
        try {
            JSONArray root = new JSONArray(response);

            for (int i = 0; i < root.length(); i++) {
                JSONObject object = root.getJSONObject(i);
                String title = object.getString("title");
                String subtitle = object.getString("subtitle");
                String description = object.getString("description");
                String img_url = object.getString("image_url");
                list.add(new NewsInformation(title, subtitle, description, img_url));

            }
            NewsInformationLab.getInstance().deleteNews();
            NewsInformationLab.getInstance().putNews(list);
            // update view pager . . . ;
            updateViewPager(NewsInformationLab.getInstance().get());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getCoursesFromDatabase(){
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.POST, ContractUrl.COURSE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              if(response.contains("no course")){
                  Toast.makeText(getActivity(), "no course found", Toast.LENGTH_SHORT).show();
              }else {
                  fetchCourseFromJsonResponse(response);
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);


    }

    private void fetchCourseFromJsonResponse(String response) {
        List<CourseInformation>courseList=new ArrayList<>();
        try {
            JSONArray root=new JSONArray(response);
            for(int i=0;i<root.length();i++){
                JSONObject course=root.getJSONObject(i);
                String id=course.getString("id");
                String title=course.getString("title");
                String days=course.getString("days");
                String time=course.getString("time");
                String description=course.getString("description");
                String price=course.getString("price");
                String isPublic=course.getString("public");
                String img_url=course.getString("img_url");
                // Todo : check if user is attended for one of these courses then put the last argument true  . . . . ;
                courseList.add(new CourseInformation(id,title,days,time,img_url,description,stringToInt(price),stringToBoolean(isPublic),false));

            }
            // delete Courses (if exists) before put courses in list . . .  ;
            CourseInformationLab.getInstance().deleteCourses();

            // put course list inside list in Course Training list (Singleton class ) . . . ;
            CourseInformationLab.getInstance().putCourses(courseList);
            // for put data inside recycler view . . .
            updateRecyclerView(CourseInformationLab.getInstance().get());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // convert data from string to int . . . ;
    public int stringToInt(String info){
        return Integer.parseInt(info);
    }
    // conver data from string to boolean . . . ;
    public boolean stringToBoolean(String info){
        int num=Integer.parseInt(info);
        if(num==0){
            return false;
        }
        return true;
    }

    // to set view (visisble or Gone) . . . ;
    private void Hideviews(int Visiblity){
        mRecyclerView.setVisibility(Visiblity);
        mViewPager.setVisibility(Visiblity);
        textlabel1.setVisibility(Visiblity);
        textlabel2.setVisibility(Visiblity);

        if(Visiblity==View.GONE){
            mLinearLayout.setVisibility(View.VISIBLE);
        }else {
            mLinearLayout.setVisibility(View.GONE);
        }
    }

}
