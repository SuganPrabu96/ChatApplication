package com.sugan.chatapplication.DataStorage.FileStorage.InternalStorage;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sugan on 20/11/15.
 */

// No need to add any permissions for this class

public class ImagesStorage {

    public class ImageClass{

        public Bitmap image;
        public JSONObject fileDetails;

        public ImageClass(Bitmap bitmap, JSONObject fileDetails){
            this.image = bitmap;
            this.fileDetails = fileDetails;
        }

    }

    public static String saveImages(Context context, String fileDir, ArrayList<ImageClass> imagesList){

        int size = imagesList.size();

        ContextWrapper wrapper = new ContextWrapper(context);
        File directory = wrapper.getDir(fileDir, Context.MODE_PRIVATE);

        for(int i=0; i<size; i++) {

            String fileName;
            int quality = 50;

            ImageClass temp = imagesList.get(i);
            JSONObject fileDetails = temp.fileDetails;
            Bitmap image = temp.image;

            try {
                fileName = fileDetails.getString("fileName");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            try {
                quality = fileDetails.getInt("fileQuality");
            } catch (JSONException e) {
                e.printStackTrace();
                quality = 50;
            }
            File file = new File(directory, fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream); //JPEG reduces quality of image but it won't be observable for small resolutions
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return directory.getPath();
    }

     public static Bitmap[] retrieveImages(String fileDir, String[] fileNames){
        int length = fileNames.length;
        Bitmap[] images = new Bitmap[length];
        for(int i=0; i<length; i++) {
            File file = new File(fileDir, fileNames[i]);
            int count = 0;
            try {
                images[count] = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    public static void deleteImages(String fileDir, String[] fileNames){
        int length = fileNames.length;
        for(int i=0; i<length; i++) {
            File file = new File(fileDir, fileNames[i]);
            file.delete();
        }
    }

}
