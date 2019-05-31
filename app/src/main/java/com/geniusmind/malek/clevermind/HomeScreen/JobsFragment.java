package com.geniusmind.malek.clevermind.HomeScreen;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.geniusmind.malek.clevermind.ContractUrl;
import com.geniusmind.malek.clevermind.Model.JobsInformation;
import com.geniusmind.malek.clevermind.Model.JobsInformationLab;
import com.geniusmind.malek.clevermind.Model.MySingleton;
import com.geniusmind.malek.clevermind.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<JobsInformation>list;
    private LinearLayout linear_progress;

    public static Fragment newInstance() {
        return new JobsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);
        list=JobsInformationLab.getInstance().get();

        // create object from JobsInformationLab if not exist to initialize data . . . ;

        mRecyclerView=view.findViewById(R.id.jobs_recycler);
        linear_progress=view.findViewById(R.id.jobs_progress);

       // to get data from database and update user interface . . . ;
        if(JobsInformationLab.getInstance().get().size()>0){
            updateUi(JobsInformationLab.getInstance().get());
            HideViews(View.VISIBLE);
        }else {
            getDataFromDatabase();
        }


        return view;
    }

    // create view holder for jobs . . . . ;
    private class JobsHolder extends RecyclerView.ViewHolder {
        private TextView jobsTitle, jobsDescription;
        private ImageView jobsImage;
        private ImageButton jobsBtn;

        public JobsHolder(View itemView) {
            super(itemView);
            jobsTitle = itemView.findViewById(R.id.jobs_title);
            jobsDescription = itemView.findViewById(R.id.jobs_description);
            jobsImage = itemView.findViewById(R.id.jobs_image);
            jobsBtn=itemView.findViewById(R.id.jobs_image_cv);
        }

        public void bind(JobsInformation information) {
            jobsTitle.setText(information.getJobsTitle());
            jobsDescription.setText(information.getJobsDescription());
            // put image from url to image view using glide library . . . ;
            Glide.with(getActivity())
                    .load(information.getJobsImageUrl())
                    .placeholder(R.drawable.loading_spinner)
                    .into(jobsImage);
        }
    }

    // create adapter for recycler view . . . ;
    private class JobsAdapter extends RecyclerView.Adapter<JobsHolder> {
        List<JobsInformation> jobsList = new ArrayList<>();

        public JobsAdapter(List<JobsInformation> list) {
            this.jobsList = list;
        }

        @NonNull
        @Override
        public JobsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_jobs, viewGroup, false);
            return new JobsHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull JobsHolder jobsHolder,final int i) {
            JobsInformation jobsInformation = jobsList.get(i);
            // to bind data inside views . . . ;
            jobsHolder.bind(jobsInformation);
            jobsHolder.jobsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // find attachement and send email . . . . ;
                    sendCvOnEmail(JobsInformationLab.getInstance().get().get(i).getJobsTitle());
                }
            });

        }

        @Override
        public int getItemCount() {
            return jobsList.size();
        }
    }

    // update user interface . . . ;
    private void updateUi(List<JobsInformation>jobs){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new JobsAdapter(jobs));

    }

    private void getDataFromDatabase(){
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.GET, ContractUrl.JOBS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            if(response.contains("no jobs")){
                Toast.makeText(getActivity(), "NO JOBS!", Toast.LENGTH_SHORT).show();
            }else {
                fetchJobsFromJsonResponse(response);
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error",error.getMessage());
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void fetchJobsFromJsonResponse(String response) {
        List<JobsInformation>jobsList=new ArrayList<>();
        try {
            JSONArray root=new JSONArray(response);
            for(int i=0;i<root.length();i++){
                JSONObject object=root.getJSONObject(i);
                String id=object.getString("id");
                String title=object.getString("title");
                String description=object.getString("description");
                String img_url=object.getString("img_url");
                jobsList.add(new JobsInformation(id,img_url,title,description));
            }
            JobsInformationLab.getInstance().deleteJobs();
           JobsInformationLab.getInstance().setJobsList(jobsList);
            updateUi(JobsInformationLab.getInstance().get());
            HideViews(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void HideViews(int Visibility){
        mRecyclerView.setVisibility(Visibility);
        if(Visibility==View.VISIBLE){
            linear_progress.setVisibility(View.GONE);
        }else {
            linear_progress.setVisibility(View.VISIBLE);
        }

    }

    private void sendCvOnEmail(String Title){
        // Todo : send cv to database ...... (update below ) . . . ;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
         // set the type to 'email'
        String to[] = {"malekhamad587@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
         // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, Title);
        startActivity(Intent.createChooser(emailIntent , "Send Via Email ..."));
    }


}
