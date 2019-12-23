package com.example.opencv1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
/**
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private static int TAKE_PICTURE_REQUEST = 1;
    private static final String TAG = "epp";
    private ImageView imageView;
    private Bitmap thumbnailBitmap;
    private TextView dlN1;
    private TextView dlN2;
    private TextView dlN3;
    private TextView dlN4;
    private TextView dlN5;
    private TextView dlN6;
    private TextView dlN7;
    private TextView dlN8;
    private TextView dlvp90;
    private TextView dlvg90;
    private TextView dlvp135;
    private TextView dlvg135;
    private TextView dlcon;
    private TextView cr0;
    private TextView dlvp;
    private TextView dlvg;
    private Button bt_con;
    private Button bt_x;
    public feature_calculations ft = new feature_calculations();
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        /**
         * Проверка работы Opencv
         * @param status флаг проверки
         */
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
    /**
     * инициализация компонентов
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        dlN1 = findViewById(R.id.dlN1);
        dlN2 = findViewById(R.id.dlN2);
        dlN3 = findViewById(R.id.dlN3);
        dlN4 = findViewById(R.id.dlN4);
        dlN5 = findViewById(R.id.dlN5);
        dlN6 = findViewById(R.id.dlN6);
        dlN7 = findViewById(R.id.dlN7);
        dlN8 = findViewById(R.id.dlN8);
        dlvg90 = findViewById(R.id.dlvg90);
        dlvp90 = findViewById(R.id.dlvp90);
        dlvg135 = findViewById(R.id.dlvg135);
        dlvp135 = findViewById(R.id.dlvp135);
        dlcon = findViewById(R.id.dlcon);
        cr0 = findViewById(R.id.cr0);
        dlvp=findViewById(R.id.odlpv);
        dlvg=findViewById(R.id.odlpg);
        bt_con=findViewById(R.id.bt_con);
        bt_con.setClickable(false);
        bt_x=findViewById(R.id.bt_x);
        bt_x.setClickable(false);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Вывод статуса работы Opencv
     */
    private void initializeOpenCVDependencies() {
        try {
            Toast.makeText(this, "openCv successfully loaded", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Ok");
        } catch (Exception e) {
            Toast.makeText(this, "openCv cannot be loaded", Toast.LENGTH_LONG).show();
            Log.i(TAG, "NO");
        }
    }

    /**
     * Открытие изображения
     * @param view
     */
    public void onClick_CreateImg(View view)
    {
        create_img();
        bt_con.setClickable(true);
    }

    /**
     * Характеристики контура
     * @param view
     */
    public void onClick_Сaharact_Img(View view)
    {
        charact_img();
    }

    /**
     * Контур изображения
     * @param view
     */
    public void onClick_Search_Counter(View view)
    {
        counter();
        bt_x.setClickable(true);
    }

    /**
     * Отображения работы Opencv при открытии камеры
     */
    private void create_img(){
        getThumbnailPicture();
        if(OpenCVLoader.initDebug()){
            Toast.makeText(this, "openCv successfully loaded", Toast.LENGTH_LONG).show();
            Log.i(TAG, "OK");
        }else{
            Toast.makeText(this, "openCv cannot be loaded", Toast.LENGTH_LONG).show();
            Log.i(TAG, "NO");
        }
    }

    /**
     * Отображение контура изображения
     */
    private void counter(){
        Mat ImageMat = new Mat(thumbnailBitmap.getHeight(), thumbnailBitmap.getWidth(), CvType.CV_8U, new Scalar(4));
        Bitmap myBitmap32 = thumbnailBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, ImageMat);
        ser_contr(ImageMat);
    }

    /**
     * Получение изображения с камеры
     * @param requestCode флаг проверки ответа камеры
     * @param resultCode флаг проверки получения изображения
     * @param data изображение полученное с камеры
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    Bundle extras = data.getExtras();
                    thumbnailBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(thumbnailBitmap);
                }
            }
        }
    }

    /**
     * Открытие камеры
     */
    private void getThumbnailPicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }

    /**
     * Поиск контура
     * @param img изображение в матричном виде
     */
    public void ser_contr(Mat img)
    {
        ft.Search_contr(img);
        imageView.setImageBitmap(ft.pm);
    }

    /**
     * Получение чарактеристик контура
     */
    public void charact_img(){
        ft.Search_for_connected_points();
        ft.Curvature_calculation_points_counter();
        dlN1.setText(String.valueOf(ft.dlN1));
        dlN2.setText(String.valueOf(ft.dlN2));
        dlN3.setText(String.valueOf(ft.dlN3));
        dlN4.setText(String.valueOf(ft.dlN4));
        dlN5.setText(String.valueOf(ft.dlN5));
        dlN6.setText(String.valueOf(ft.dlN6));
        dlN7.setText(String.valueOf(ft.dlN7));
        dlN8.setText(String.valueOf(ft.dlN8));
        dlvp.setText(String.valueOf((ft.dlvp90+ft.dlvp135)));
        dlvg.setText(String.valueOf((ft.dlvg90+ft.dlvg135)));
        dlvg90.setText(String.valueOf(ft.dlvg90));
        dlvp90.setText(String.valueOf(ft.dlvp90));
        dlvg135.setText(String.valueOf(ft.dlvg135));
        dlvp135.setText(String.valueOf(ft.dlvp135));
        dlcon.setText(String.valueOf(ft.dlcon));
        cr0.setText(String.valueOf(ft.cr0));
    }
    @Override
    public void onResume() {
        super.onResume();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
    }

}