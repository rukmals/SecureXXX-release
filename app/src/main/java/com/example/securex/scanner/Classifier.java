package com.example.securex.scanner;

import android.content.res.AssetFileDescriptor;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Classifier {

    private Interpreter interpreter;
    private final Interpreter.Options options = new Interpreter.Options();
    AppCompatActivity activity;
    private static  Classifier classifier;
    ByteBuffer permissions;
    List<String> all_permissions;

    private Classifier(AppCompatActivity activity){
        this.activity=activity;
        try {
            interpreter = new Interpreter(loadModelFile(), options);
            loadPermissions();
            Log.d("PER",Integer.toString(all_permissions.size()));
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    public static Classifier getInstance(AppCompatActivity activity){
        if(classifier==null){
            classifier = new Classifier(activity);
        }
        return classifier;
    }

    public int predict(List<String> permissionsList){
        float[][] mResult = new float[1][2];
        permissions = ByteBuffer.allocateDirect(8*328);
        permissions.rewind();

        for(int i =0 ; i<328;i++){
            if(permissionsList.contains(all_permissions.get(i))){
                permissions.putInt(1);
            }
            else{
                permissions.putInt(0);
            }
        }
        interpreter.run(permissions,mResult);
        float[] probs = mResult[0];
        return argmax(probs);




    }


    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("permission.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    private void loadPermissions(){
        all_permissions = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(activity.getAssets().open("permissions.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                all_permissions.add(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    private static int argmax(float[] probs) {
        int maxIdx = -1;
        float maxProb = 0.0f;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb) {
                maxProb = probs[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }
}
