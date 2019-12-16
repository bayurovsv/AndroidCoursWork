package com.example.opencv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static int TAKE_PICTURE_REQUEST = 1;
    private static final String TAG = "epp";
    private ImageView imageView;
    private Bitmap thumbnailBitmap;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.create_img :
                create_img();
                return true;
            case R.id.binaris:
                b_img();
                return true;
            case R.id.counter:
                counter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initializeOpenCVDependencies() {

        try {
            Toast.makeText(this, "openCv successfully loaded", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Работает");
        } catch (Exception e) {
            Toast.makeText(this, "openCv cannot be loaded", Toast.LENGTH_LONG).show();
            Log.i(TAG, "нет");
        }
    }
    public void onClick_CreateImg(View view)
    {
        create_img();
    }
    private void create_img(){
        getThumbnailPicture();
        if(OpenCVLoader.initDebug()){
            Toast.makeText(this, "openCv successfully loaded", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Работает");
            Mat img = new Mat();
        }else{
            Toast.makeText(this, "openCv cannot be loaded", Toast.LENGTH_LONG).show();
            Log.i(TAG, "нет");
        }
    }
    private void b_img(){
        Mat ImageMat = new Mat(thumbnailBitmap.getHeight(), thumbnailBitmap.getWidth(), CvType.CV_8U, new Scalar(4));
        Bitmap myBitmap32 = thumbnailBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, ImageMat);
        toBlack_and_While(ImageMat);
    }
    private void counter(){
        Mat ImageMat = new Mat(thumbnailBitmap.getHeight(), thumbnailBitmap.getWidth(), CvType.CV_8U, new Scalar(4));
        Bitmap myBitmap32 = thumbnailBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, ImageMat);
        Search_contr(ImageMat);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    thumbnailBitmap = data.getParcelableExtra("data");
                    imageView.setImageBitmap(thumbnailBitmap);
                }
            }
        }
    }
    private void getThumbnailPicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }
public void Search_contr(Mat img){
    Mat imgGray = new Mat();
    Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
    Mat edges = new Mat();
    Imgproc.Canny(imgGray, edges, 80, 200);
    Mat edgesCopy = edges.clone();
    ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    Mat hierarchy = new Mat();
    Imgproc.findContours(edgesCopy, contours, hierarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
    double maxVal = 0;
    int maxValIdx = 0;
    for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++)
    {
        double contourArea = Imgproc.contourArea(contours.get(contourIdx));
        if (maxVal < contourArea)
        {
            maxVal = contourArea;
            maxValIdx = contourIdx;
        }
    }
    Imgproc.drawContours(img, contours, maxValIdx, new Scalar(255,0,0));
    ArrayList<Point> list = new ArrayList<Point>();
    Converters.Mat_to_vector_Point(contours.get(maxValIdx), list); System.out.println(list); // [{0.0, 0.0}, {1.0, 1.0}]

    contours.get(maxValIdx).fromArray();
    Toast.makeText(this, ""+contours.get(maxValIdx), Toast.LENGTH_LONG).show();

    System.out.println(Imgproc.contourArea(contours.get(maxValIdx)));
    System.out.println(Imgproc.arcLength(new MatOfPoint2f(contours.get(maxValIdx).toArray()), true));

    Bitmap resultBitmap = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
    Utils.matToBitmap(img, resultBitmap);
    imageView.setImageBitmap(resultBitmap);
}
    public void toBlack_and_While(Mat img) {
        Mat image = new Mat();
        Imgproc.cvtColor(img, image,Imgproc.COLOR_BGR2GRAY);
        Mat image_binary=new Mat();
        Imgproc.threshold(image,image_binary,80,200,Imgproc.THRESH_BINARY);
        Bitmap resultBitmap = Bitmap.createBitmap(image_binary.cols(), image_binary.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image_binary, resultBitmap);
        thumbnailBitmap= resultBitmap;
        imageView.setImageBitmap(resultBitmap);
    }
    @Override
    public void onResume() {
        super.onResume();
       // OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
    }
}
