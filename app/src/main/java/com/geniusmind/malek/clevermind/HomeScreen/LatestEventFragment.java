package com.geniusmind.malek.clevermind.HomeScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geniusmind.malek.clevermind.ContractUrl;
import com.geniusmind.malek.clevermind.Model.LatestEventInformation;
import com.geniusmind.malek.clevermind.Model.LatestEventInformationLab;
import com.geniusmind.malek.clevermind.Model.MySingleton;
import com.geniusmind.malek.clevermind.Model.SoonInformationLab;
import com.geniusmind.malek.clevermind.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LatestEventFragment extends Fragment {
   private ListView mListView;
   private ViewPager mViewPager;
   private LatestEventAdapter mLatestEventAdapter;
   private LinearLayout linearProgress;
   private ConstraintLayout mConstraintLayout;

    public static Fragment getInstance() {
        return new LatestEventFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_latest_event,container,false);
        mListView=view.findViewById(R.id.list_soon);
        mViewPager=view.findViewById(R.id.viewPager_latest);
        linearProgress=view.findViewById(R.id.event_progress);
        mConstraintLayout=view.findViewById(R.id.event_layout);
        // to get soon information and update list view (soonList) . . . ;
        if(SoonInformationLab.getInstance().get().size()>0&&LatestEventInformationLab.getInstance().get().size()>0) {
            mListView.setAdapter(new SoonAdapter(getActivity(),R.layout.list_soon,SoonInformationLab.getInstance().get()));
            UpdateViewPager(LatestEventInformationLab.getInstance().get());


        }else {
          getSoonFromDatabase();
          getLatestNewsFromDatabase();
        }


        return view;
    }
    private void UpdateViewPager(List<LatestEventInformation>list){
        mLatestEventAdapter=new LatestEventAdapter(getActivity(),list);
        mViewPager.setAdapter(mLatestEventAdapter);
        mViewPager.setPadding(200, 0, 200, 0);
        int NewsSize = list.size();
        mViewPager.setCurrentItem(HomeFragment.getMiddlePosition(NewsSize), true);
        mViewPager.setPageMargin(24);
        mViewPager.setOffscreenPageLimit(3);
        HideViews(View.VISIBLE);
    }

    private class LatestEventAdapter extends PagerAdapter {
        Context context;
        List<LatestEventInformation>mList;
        LayoutInflater layoutInflater;
        public LatestEventAdapter(Context context, List<LatestEventInformation>mList){
               this.context=context;
               this.mList=mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view.equals(o);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater=LayoutInflater.from(context);
            View view=layoutInflater.inflate(R.layout.list_latest_event,container,false);
             TextView textTitle,textDay,textDate,textTime;
            textTitle=view.findViewById(R.id.latest_title);
            textDay=view.findViewById(R.id.latest_days);
            textDate=view.findViewById(R.id.latest_date);
            textTime=view.findViewById(R.id.latest_time);

            LatestEventInformation information= LatestEventInformationLab.getInstance().get().get(position);
            textTitle.setText(information.getTitle());
            textDay.setText(information.getDays());
            textDate.setText(information.getDate());
            textTime.setText(information.getTime());

            container.addView(view,0);


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

    private class SoonAdapter extends ArrayAdapter<String> {
        private Context context;
        private int resource;
        private List<String>objects;

        public SoonAdapter( Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context=context;
            this.resource=resource;
            this.objects=objects;
        }


        @Override
        public View getView(int position,View convertView, ViewGroup parent) {
            convertView=LayoutInflater.from(context).inflate(resource,parent,false);
            TextView text_soon=convertView.findViewById(R.id.soon_text);
            ImageView imageView=convertView.findViewById(R.id.bell_btn);

            String text=objects.get(position);
            text_soon.setText(text);

            return convertView;

        }
    }

    private void getLatestNewsFromDatabase(){
       MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
       StringRequest request=new StringRequest(Request.Method.GET, ContractUrl.LATEST_EVENT_URL, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
             if(response.contains("no item")){
                 Toast.makeText(getActivity(), "not Latest Event found !", Toast.LENGTH_SHORT).show();
             }else {
                 fetchLatestEventFromJsonResponse(response);
             }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
           }
       });
       MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void fetchLatestEventFromJsonResponse(String response) {
        List<LatestEventInformation>list=new ArrayList<>();
        try {
            JSONArray root=new JSONArray(response);
            for(int i=0;i<root.length();i++){
                JSONObject object=root.getJSONObject(i);
                String title=object.getString("title");
                String day=object.getString("day");
                String date=object.getString("date");
                String time=object.getString("time");
                list.add(new LatestEventInformation(title,day,date,time));
            }
            LatestEventInformationLab.getInstance().deleteJobs();
            LatestEventInformationLab.getInstance().setEventList(list);
            UpdateViewPager(LatestEventInformationLab.getInstance().get());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getSoonFromDatabase(){
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.POST, ContractUrl.SOON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              if(response.contains("no item")){
                  Toast.makeText(getActivity(), "no item found", Toast.LENGTH_SHORT).show();
              }else {
                  fetchSoonFromJsonResponse(response);
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             error.printStackTrace();
            }
        });
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);

    }

    private void fetchSoonFromJsonResponse(String response) {
        List<String>list=new ArrayList<>();
        try {
            JSONArray root=new JSONArray(response);
            for(int i=0;i<root.length();i++){
                // Todo : use id and date to retrieve token and push notification . . . ;
                JSONObject object=root.getJSONObject(i);
                String id=object.getString("id");
                String title=object.getString("title");
                String date=object.getString("date");
                list.add(title);
            }
            SoonInformationLab.getInstance().deleteSoonInfo();
            SoonInformationLab.getInstance().putSoonInfo(list);
            mListView.setAdapter(new SoonAdapter(getActivity(),R.layout.list_soon,SoonInformationLab.getInstance().get()));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void HideViews(int Visiblity){
        if(Visiblity==View.VISIBLE){
            mConstraintLayout.setVisibility(View.VISIBLE);
            linearProgress.setVisibility(View.GONE);
        }else {
            mConstraintLayout.setVisibility(View.GONE);
            linearProgress.setVisibility(View.VISIBLE);
        }
    }
}
