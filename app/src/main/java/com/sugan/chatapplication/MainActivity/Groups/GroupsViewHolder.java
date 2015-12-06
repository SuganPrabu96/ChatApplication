package com.sugan.chatapplication.MainActivity.Groups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.docbizz.doctors.R;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class GroupsViewHolder extends RecyclerView.ViewHolder{

    private Context context;
    public TextView hospitalName, speciality, experience;
    public ImageView hospitalImage;
    public LinearLayout jobOpeningsLayout;
    public Button applyButton;
    public GroupsViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        jobOpeningsLayout = (LinearLayout) itemView.findViewById(R.id.job_openings_linear_layout);
        hospitalName = (TextView) itemView.findViewById(R.id.job_openings_jobs_hospitals_name);
        speciality = (TextView) itemView.findViewById(R.id.job_openings_jobs_speciality);
        experience = (TextView) itemView.findViewById(R.id.job_openings_jobs_experience);
        hospitalImage = (ImageView) itemView.findViewById(R.id.job_openings_jobs_hospitals_icon);
        applyButton = (Button) itemView.findViewById(R.id.job_openings_jobs_apply_btn);
    }
}
