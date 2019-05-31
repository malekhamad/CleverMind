package com.geniusmind.malek.clevermind.HomeScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.geniusmind.malek.clevermind.ContractUrl;
import com.geniusmind.malek.clevermind.Model.AttendedInformation;
import com.geniusmind.malek.clevermind.Model.AttendedInformationLab;
import com.geniusmind.malek.clevermind.Model.MySingleton;
import com.geniusmind.malek.clevermind.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendedFragment extends Fragment {
private TextView attendText;
private RecyclerView recyclerView;
private ConstraintLayout attendedLayout,emptyLayout;
private LinearLayout linearProgress;
private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attended, container, false);
         initalizeViews(view);

         boolean AttendedEmpty=AttendedInformationLab.getInstance().get().isEmpty();

         // if data is stored in singleton class before update ui directly without access to network state . . .  ;
         if(AttendedEmpty){
             getDataFromDatabase();
         }else {
             updateUi(AttendedInformationLab.getInstance().get());

         }

        // to convert string to html code . . . ;
        String attend = getString(R.string.attended_course);
        Spanned htmlText= Html.fromHtml(attend);
        attendText.setText(htmlText);
        return view;



    }
    // to initalize views and casting (if required) . . . ;
    private void initalizeViews(View view){
        mAuth=FirebaseAuth.getInstance();
        attendText=view.findViewById(R.id.title_attended_text);
        recyclerView=view.findViewById(R.id.recycler_attended);
        attendedLayout=view.findViewById(R.id.constraint_attended);
        emptyLayout=view.findViewById(R.id.constraint_noItem);
        linearProgress=view.findViewById(R.id.progress_attended);
    }

    // visiblity of views if not item o
    private void HideViews(int Visiblity){
        if(Visiblity==View.VISIBLE){
            attendedLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            attendText.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            linearProgress.setVisibility(View.GONE);

        }else {
            attendedLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            linearProgress.setVisibility(View.GONE);

        }
    }

    private void updateUi(List<AttendedInformation>list){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new AttendedAdapter(getActivity(),list));
        HideViews(View.VISIBLE);
    }

    public static Fragment newInstance() {
        return new AttendedFragment();
    }


    private void getDataFromDatabase(){
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.POST, ContractUrl.ATTENDED_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("no courses")){
                    HideViews(View.GONE);
                }else {
                    fetchAttendedCourse(response);
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
                params.put("user_id",mAuth.getCurrentUser().getUid());
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


    private void fetchAttendedCourse(String response) {
        List<AttendedInformation>list=new ArrayList<>();
        try {
            JSONArray array=new JSONArray(response);
            for(int i=0;i<array.length();i++){
                JSONObject object=array.getJSONObject(i);
                String id=object.getString("id");
                String title=object.getString("title");
                String days=object.getString("days");
                String time=object.getString("time");
                String img_url=object.getString("img_url");

                list.add(new AttendedInformation(id,title,days,time,img_url));

            }
            AttendedInformationLab.getInstance().deleteAttended();
            // put array inside AttendedInformationLab ( Singleton Pattern) . . . ;
            AttendedInformationLab.getInstance().setAttendedList(list);
            // update user interface after get data . .. ;
            updateUi(AttendedInformationLab.getInstance().get());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // create view holder class for recycler views . . . ;
    private class AttendedHolder extends RecyclerView.ViewHolder{
        TextView textTitle,textDays,textTime;
        ImageView imageCourse;
        public AttendedHolder( View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.attended_title_label);
            textDays=itemView.findViewById(R.id.attended_day_label);
            textTime=itemView.findViewById(R.id.attended_time_label);
            imageCourse=itemView.findViewById(R.id.attended_course_image);
        }
        public void bindViews(AttendedInformation information){
            textTitle.setText(information.getTitle());
            textDays.setText(information.getDays());
            textTime.setText(information.getTime());
            // put image from url to image view using glide library . . . ;
            Glide.with(getActivity())
                    .load(information.getUrl())
                    .placeholder(R.drawable.loading_spinner)
                    .into(imageCourse);
        }

    }
    // create adapter for recycler view to put data inside views . .. ;
    private class AttendedAdapter extends RecyclerView.Adapter<AttendedHolder>{
       Context context;
       List<AttendedInformation>list;

       public AttendedAdapter(Context context,List<AttendedInformation>list){
           this.context=context;
           this.list=list;
       }


        @NonNull
        @Override
        public AttendedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=LayoutInflater.from(context).inflate(R.layout.list_attended,viewGroup,false);
            return new AttendedHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AttendedHolder attendedHolder, int i) {
            AttendedInformation information=list.get(i);
            attendedHolder.bindViews(information);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
