package com.example.soundtraining;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtils {
    public static int readPointsFromFile(Context context, String fileName) {
        int points = 0;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String pointsString = reader.readLine();
            points = Integer.parseInt(pointsString);
            reader.close();
        } catch (IOException e) {
            savePointsToFile(context, points, fileName);
        }
        return points;
    }

    public static void savePointsToFile(Context context,int points, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(String.valueOf(points));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}