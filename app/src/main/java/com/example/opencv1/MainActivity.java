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
        int[] num_st = ft.Search_for_connected_points(ft.list);
        int[] sign = ft.Curvature_calculation_points_counter(ft.list);
        dlN1.setText(String.valueOf(num_st[0]));
        dlN2.setText(String.valueOf(num_st[1]));
        dlN3.setText(String.valueOf(num_st[2]));
        dlN4.setText(String.valueOf(num_st[3]));
        dlN5.setText(String.valueOf(num_st[4]));
        dlN6.setText(String.valueOf(num_st[5]));
        dlN7.setText(String.valueOf(num_st[6]));
        dlN8.setText(String.valueOf(num_st[7]));
        dlvp.setText(String.valueOf((sign[0]+sign[2])));
        dlvg.setText(String.valueOf((sign[1]+sign[3])));
        dlvg90.setText(String.valueOf(sign[1]));
        dlvp90.setText(String.valueOf(sign[0]));
        dlvg135.setText(String.valueOf(sign[3]));
        dlvp135.setText(String.valueOf(sign[2]));
        dlcon.setText(String.valueOf(num_st[0]+num_st[1]+num_st[2]+num_st[3]+num_st[4]+num_st[5]+num_st[6]+num_st[7]));
        cr0.setText(String.valueOf(sign[4]));
        Save_Result sr= new Save_Result();
        String[] specifications = new String[16];
        specifications[0] = "Р”Р»РёРЅС‹ РѕС‚СЂРµР·РєРѕРІ РЅР°РїСЂР°РІР»РµРЅРёСЏ N1: " + String.valueOf(num_st[0])+'\n';
        specifications[1] = "Р”Р»РёРЅС‹ РѕС‚СЂРµР·РєРѕРІ РЅР°РїСЂР°РІР»РµРЅРёСЏ N2: " + String.valueOf(num_st[1])+'\n';
        specifications[2] = "Р”Р»РёРЅС‹ РѕС‚СЂРµР·РєРѕРІ РЅР°РїСЂР°РІР»РµРЅРёСЏ N3: " + String.valueOf(num_st[2])+'\n';
        specifications[3] = "Р”Р»РёРЅС‹ РѕС‚СЂРµР·РєРѕРІ РЅР°РїСЂР°РІР»РµРЅРёСЏ N4: " + String.valueOf(num_st[3])+'\n';
        specifications[4] = "Р”Р»РёРЅС‹ РѕС‚СЂРµР·РєРѕРІ РЅР°РїСЂР°РІР»РµРЅРёСЏ N5: " + String.valueOf(num_st[4])+'\n';
        specifications[5] = "Р”Р»РёРЅС‹ РѕС‚СЂРµР·РєРѕРІ РЅР°РїСЂР°РІР»РµРЅРёСЏ N6: " + String.valueOf(num_st[5])+'\n';
        specifications[6] = "Р”Р»РёРЅС‹ РѕС‚СЂРµР·РєРѕРІ РЅР°РїСЂР°РІР»РµРЅРёСЏ N7: " + String.valueOf(num_st[6])+'\n';
        specifications[7] = "Р”Р»РёРЅС‹ РѕС‚СЂРµР·РєРѕРІ РЅР°РїСЂР°РІР»РµРЅРёСЏ N8: " + String.valueOf(num_st[7])+'\n';
        specifications[8] = "РљРѕР»РёС‡РµСЃС‚РІРѕ С‚РѕС‡РµРє РєРѕРЅС‚СѓСЂР°, СЏРІР»СЏСЋС‰РёРµСЃСЏ С†РµРЅС‚СЂРѕРј РІС‹РїСѓРєР»С‹С… СѓС‡Р°СЃС‚РєРѕРІ +90: " + String.valueOf(sign[0])+'\n';
        specifications[9] = "РљРѕР»РёС‡РµСЃС‚РІРѕ С‚РѕС‡РµРє РєРѕРЅС‚СѓСЂР°, СЏРІР»СЏСЋС‰РёРµСЃСЏ С†РµРЅС‚СЂРѕРј РІРѕРіРЅСѓС‚С‹С… СѓС‡Р°СЃС‚РєРѕРІ -90: " + String.valueOf(sign[1])+'\n';
        specifications[10] = "РљРѕР»РёС‡РµСЃС‚РІРѕ С‚РѕС‡РµРє РєРѕРЅС‚СѓСЂР°, СЏРІР»СЏСЋС‰РёРµСЃСЏ С†РµРЅС‚СЂРѕРј РІС‹РїСѓРєР»С‹С… СѓС‡Р°СЃС‚РєРѕРІ +135: " + String.valueOf(sign[2])+'\n';
        specifications[11] = "РљРѕР»РёС‡РµСЃС‚РІРѕ С‚РѕС‡РµРє РєРѕРЅС‚СѓСЂР°, СЏРІР»СЏСЋС‰РёРµСЃСЏ С†РµРЅС‚СЂРѕРј РІРѕРіРЅСѓС‚С‹С… СѓС‡Р°СЃС‚РєРѕРІ -135: " + String.valueOf(sign[3])+'\n';
        specifications[12] = "РљРѕР»РёС‡РµСЃС‚РІРѕ С‚РѕС‡РµРє РЅСѓР»РµРІРѕР№ РєСЂРёРІРёР·РЅС‹: " + String.valueOf(sign[4])+'\n';
        specifications[13] = "Р”Р»РёРЅР° РєРѕРЅС‚СѓСЂР°: " + String.valueOf(num_st[0]+num_st[1]+num_st[2]+num_st[3]+num_st[4]+num_st[5]+num_st[6]+num_st[7])+'\n';
        specifications[14] = "РћР±С‰Р°СЏ РґР»РёРЅР° РІС‹РїСѓРєР»С‹С… СѓС‡Р°СЃС‚РєРѕРІ РєСЂРёРІРёР·РЅС‹ +90, +135: " + String.valueOf((sign[0]+sign[2]))+'\n';
        specifications[15] = "РћР±С‰Р°СЏ РґР»РёРЅР° РІРѕРіРЅСѓС‚С‹С… СѓС‡Р°СЃС‚РєРѕРІ РєСЂРёРІРёР·РЅС‹ -90, -135: " + String.valueOf((sign[1]+sign[3]))+'\n';
        sr.Save(ft.pm,specifications);
    }
    @Override
    public void onResume() {
        super.onResume();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
    }
}