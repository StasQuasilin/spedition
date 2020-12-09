package ua.svasilina.spedition.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static android.content.Context.MODE_PRIVATE;

public class StorageUtil {


    private final Context context;

    public StorageUtil(Context context) {
        this.context = context;
    }

    public void saveData(String fileName, String data){
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, MODE_PRIVATE);
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
            gzipOutputStream.write(data.getBytes());
            gzipOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    File[] getFiles(FileFilter fileFilter){
        return context.getFilesDir().listFiles(fileFilter);
    }

    public String readFile(String name) {
        try {
            final FileInputStream fileInputStream = context.openFileInput(name);
            final GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream));
            final StringBuilder builder = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null){
                builder.append(s);
            }
            fileInputStream.close();
            gzipInputStream.close();
            reader.close();
            return builder.toString();
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return null;
    }

    public boolean remove(String name) {
        File file = context.getFileStreamPath(name);
        if (file.exists()){
            return file.delete();
        }
        return false;
    }

    public boolean isSync() {
        final File[] syncs = getFiles(new FileFilter("sync"));
        if (syncs != null){
            return syncs.length > 0;
        }
        return false;
    }
}
