package com.example.opencv1;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Save_Result {
    File directory;
    File directoryfile;
    File file;
    public  void Save(Bitmap bm,String[] specifications){
        CreateDirectory();
        CreateDirectoryFile();
        try {
            file = new File(directoryfile.getPath() + "/" + "photo_" + System.currentTimeMillis() + ".jpg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Bitmap bitmap = bm;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            String FILENAME ="photo_" + System.currentTimeMillis() + ".txt";
            File Text = new File(directoryfile.getPath(),FILENAME);
            BufferedWriter bw = new BufferedWriter(new FileWriter(Text));
            for(int i=0;i<specifications.length;i++)
                bw.write(specifications[i]);
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateDirectory() {
        directory = new File(Environment.getExternalStorageDirectory(), "РџСЂРёР·РЅР°РєРё РѕР±СЉРµРєС‚РѕРІ");
        if (!directory.exists())
            directory.mkdirs();
    }
    public void CreateDirectoryFile() {
        directoryfile = new File(Environment.getExternalStorageDirectory()+"/РџСЂРёР·РЅР°РєРё РѕР±СЉРµРєС‚РѕРІ", "photo_" + System.currentTimeMillis());
        if (!directoryfile.exists())
            directoryfile.mkdirs();
    }
}
