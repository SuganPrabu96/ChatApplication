package com.sugan.chatapplication.MainActivity.Groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.docbizz.doctors.Activities.HospitalProfile;
import com.app.docbizz.doctors.Activities.MainActivity;
import com.app.docbizz.doctors.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import DatabaseFiles.JobsApplied.JobsStorage;
import JobOpenings.JobsViewPager.JobsNew;
import util.BitmapOptimalLoader;
import util.ConnectionDetector;
import util.ImageFileAccess;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class GroupsRecyclerAdapter extends RecyclerView.Adapter<GroupsViewHolder>{

    private ArrayList<GroupsItem> jobs;
    private Context context;

    public GroupsRecyclerAdapter(ArrayList<GroupsItem> jobs, Context context) {
        this.jobs = jobs;
        this.context = context;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.job_openings_recycler_view,
                        viewGroup,
                        false);

        return new GroupsViewHolder(context, itemView);
    }

    @Override
    public void onBindViewHolder(final GroupsViewHolder viewHolder, final int position) {
        //viewHolder. = names.get(position);
        final GroupsItem item = jobs.get(position);
        viewHolder.hospitalName.setText(item.getHospitalName());
        viewHolder.experience.setText(item.getExperience());
        viewHolder.speciality.setText(item.getSpeciality());

        Bitmap defaultImg1, defaultImg2, defaultImg3;

        defaultImg1 = BitmapOptimalLoader.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.hospi1, viewHolder.hospitalImage);
        defaultImg2 = BitmapOptimalLoader.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.hospi2, viewHolder.hospitalImage);
        defaultImg3 = BitmapOptimalLoader.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.hospi3, viewHolder.hospitalImage);

        final DatabaseFiles.JobOpenings.JobsStorage db = new DatabaseFiles.JobOpenings.JobsStorage(context);

        int t = position%3;

        Bitmap cachedImage = MainActivity.jobOpeningsCache.getBitmapFromCache(item.getJobId());

        if(cachedImage!=null){
            viewHolder.hospitalImage.setImageBitmap(cachedImage);
        }

        if(!item.getImagePath().equals("")){
            try {
                File file = new File(item.getImagePath());
                Picasso.with(context).load(file).into(viewHolder.hospitalImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else {

            if (t == 0)
                viewHolder.hospitalImage.setImageBitmap(defaultImg1);
            else if (t == 1)
                viewHolder.hospitalImage.setImageBitmap(defaultImg2);
            else
                viewHolder.hospitalImage.setImageBitmap(defaultImg3);
        }

        try {
            ConnectionDetector detector = new ConnectionDetector(context);
            if(detector.isConnectingToInternet()) {

                Picasso.with(context).load(item.getHospitalProPicURL()).into(viewHolder.hospitalImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) viewHolder.hospitalImage.getDrawable()).getBitmap();
                        MainActivity.jobOpeningsCache.storeBitmapInCache(item.getJobId(),bitmap);
                        String path = ImageFileAccess.saveImage(context, bitmap, "JobsApplied", item.getJobId());
                        db.addImageDetails(item.getJobId(), path);
                        item.setImagePath(path);
                        MainActivity.jobOpenings.set(position, item);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }catch(Exception e){
            if(!item.getImagePath().equals("")){
                try {
                    File file = new File(item.getImagePath());
                    Picasso.with(context).load(file).into(viewHolder.hospitalImage);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }

            else {
                if (t == 0)
                    viewHolder.hospitalImage.setImageBitmap(defaultImg1);
                else if (t == 1)
                    viewHolder.hospitalImage.setImageBitmap(defaultImg2);
                else
                    viewHolder.hospitalImage.setImageBitmap(defaultImg3);
            }
        }


        viewHolder.jobOpeningsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HospitalProfile.class);
                intent.putExtra("hospitalName", item.getHospitalName());
                intent.putExtra("hospitalDescription", item.getHospitalDescription());
                intent.putExtra("hospitalPhone", item.getHospitalPhone());
                intent.putExtra("hospitalLocation", item.getHospitalLocation());
                intent.putExtra("hospitalProfilePic", item.getHospitalProPicURL());
                intent.putExtra("hospitalJobsCount",item.getHospitalJobsCount());
                intent.putExtra("hospitalUpdatesCount",item.getHospitalUpdatesCount());
                intent.putExtra("imagePath",item.getImagePath());
                context.startActivity(intent);
            }
        });

        viewHolder.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.arg1 = position;
                JobsNew.applyForJobHandler.sendMessage(msg);
                Log.d("Inside","applyBtn");
            }
        });

    }

    public int getItemCount() {
        return jobs == null ? 0 : jobs.size();
    }

    public void add(GroupsItem item){
        jobs.add(item);
        notifyItemInserted(jobs.indexOf(item));
    }
}
