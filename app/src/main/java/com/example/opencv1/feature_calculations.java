package com.example.opencv1;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import java.util.ArrayList;

/**
 * Клас для работы с изображениями
 */
public class feature_calculations extends AppCompatActivity {
    public Bitmap pm;
    /**
     * Лист точек, содержащий координаты точек контура
     */
    public ArrayList<Point> list = new ArrayList<Point>();

    /**
     * Поиск контура
     * @param img изображение представленное в матричном виде
     */
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
        Converters.Mat_to_vector_Point(contours.get(maxValIdx), list);
        Bitmap resultBitmap = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img, resultBitmap);
        pm=resultBitmap;
    }

    /** Поиск связных точек контура
     * @param list лиск координат точек контура
     * @return количество точек в направлении N1 - N8
     */
    public static int[]  Search_for_connected_points(ArrayList<Point> list){
        int dlN1=0;
        int dlN2=0;
        int dlN3=0;
        int dlN4=0;
        int dlN5=0;
        int dlN6=0;
        int dlN7=0;
        int dlN8=0;
        for(int i = 0; i<=list.size()-1;i++)
        {
            if (i==list.size()-1)
            {
                if(list.get(i).x<list.get(0).x&&list.get(i).y==list.get(0).y)
                {
                    dlN1++;
                }
                if(list.get(i).x<list.get(0).x&&list.get(i).y<list.get(0).y)
                {
                    dlN2++;
                }
                if(list.get(i).x==list.get(0).x&&list.get(i).y<list.get(0).y)
                {
                    dlN3++;
                }
                if(list.get(i).x>list.get(0).x&&list.get(i).y<list.get(0).y)
                {
                    dlN4++;
                }
                if(list.get(i).x>list.get(0).x&&list.get(i).y==list.get(0).y)
                {
                    dlN5++;
                }
                if(list.get(i).x>list.get(0).x&&list.get(i).y>list.get(0).y)
                {
                    dlN6++;
                }
                if(list.get(i).x==list.get(0).x&&list.get(i).y>list.get(0).y)
                {
                    dlN7++;
                }
                if(list.get(i).x<list.get(0).x&&list.get(i).y>list.get(0).y)
                {
                    dlN8++;
                }
            }
            else {
                if (list.get(i).x < list.get(i + 1).x && list.get(i).y == list.get(i + 1).y) {
                    dlN1++;
                }
                if (list.get(i).x < list.get(i + 1).x && list.get(i).y < list.get(i + 1).y) {
                    dlN2++;
                }
                if (list.get(i).x == list.get(i + 1).x && list.get(i).y < list.get(i + 1).y) {
                    dlN3++;
                }
                if (list.get(i).x > list.get(i + 1).x && list.get(i).y < list.get(i + 1).y) {
                    dlN4++;
                }
                if (list.get(i).x > list.get(i + 1).x && list.get(i).y == list.get(i + 1).y) {
                    dlN5++;
                }
                if (list.get(i).x > list.get(i + 1).x && list.get(i).y > list.get(i + 1).y) {
                    dlN6++;
                }
                if (list.get(i).x == list.get(i + 1).x && list.get(i).y > list.get(i + 1).y) {
                    dlN7++;
                }
                if (list.get(i).x < list.get(i + 1).x && list.get(i).y > list.get(i + 1).y) {
                    dlN8++;
                }
            }
        }
        int[] dln1_dln8 = new int[8];
        dln1_dln8[0]=dlN1;
        dln1_dln8[1]=dlN2;
        dln1_dln8[2]=dlN3;
        dln1_dln8[3]=dlN4;
        dln1_dln8[4]=dlN5;
        dln1_dln8[5]=dlN6;
        dln1_dln8[6]=dlN7;
        dln1_dln8[7]=dlN8;
        return dln1_dln8;
    }

    /**Вычисление кривизны точек контура
     * @param list лиск координат точек контура
     * @return количество точек с углами 0,90,-90,135,-135
     */
    public static int[] Curvature_calculation_points_counter(ArrayList<Point> list)
    {
        int dlvp90=0;
        int dlvg90=0;
        int dlvp135=0;
        int dlvg135=0;
        int cr0=0;
        for(int z = 0; z<list.size()-1;z++) {
            if(z==0) {
                //g2
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y) {
                    cr0++;//0
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y) {
                    cr0++;//0
                }
                //g1
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    cr0++;//0
                }
                if (list.get(z).x == list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;//0
                }
                //g3
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    cr0++;//0
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;//0
                }
                //g4
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    cr0++;//0
                }
                if (list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;//0
                }

                //90
                //g5
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1&& list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1&& list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g6
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g7
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g8
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }

                //135
                //g9
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g10
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g11
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g12
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g13
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++; //РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++; //РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g14
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && (list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && (list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g15
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g16
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
            }
            else if (z==list.size()-1){
                //g2
                if (list.get(z).x < list.get(0).x && list.get(z).y == list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y) {
                    cr0++;//0
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y == list.get(0).y) {
                    cr0++;//0
                }
                //g1
                if (list.get(z).x == list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;//0
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(0).x && list.get(z).y > list.get(0).y) {
                    cr0++;//0
                }
                //g3
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;//0
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y) {
                    cr0++;//0
                }
                //g4
                if (list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;//0
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y) {
                    cr0++;//0
                }
                //90
                //g5
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1&& list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1&& list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g6
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g7
                if (list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g8
                if (list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //135
                //g9
                if (list.get(z).x == list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g10
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g11
                if (list.get(z).x < list.get(0).x && list.get(z).y == list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g12
                if (list.get(z).x > list.get(0).x && list.get(z).y == list.get(0).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g13
                if (list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++; //РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++; //РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g14
                if (list.get(z).x == list.get(0).x && list.get(z).y < list.get(0).y && (list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && (list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g15
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y == list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g16
                if (list.get(z).x < list.get(0).x && list.get(z).y == list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
            }
            else{
                //g2
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y) {
                    cr0++;//0
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y) {
                    cr0++;//0
                }
                //g1
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;//0
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;//0
                }
                //g3
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;//0
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;//0
                }
                //g4
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;//0
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;//0
                }

                //90
                //g5
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1&& list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g6
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g7
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g8
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp90++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg90++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //135
                //g9
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                //g10
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g11
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-2||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g12
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+2||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-2||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g13
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++; //РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g14
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && (list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && (list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y+1 && list.get(r).x != list.get(z).x+1||list.get(r).y == list.get(z).y-1 && list.get(r).x != list.get(z).x-1||list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x+1||list.get(r).x > list.get(z).x-1||list.get(r).x > list.get(z).x) {
                                dlvp135++; //РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g15
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                // g16
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x+1 && list.get(r).y != list.get(z).y+1||list.get(r).x == list.get(z).x-1 && list.get(r).y != list.get(z).y-1||list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y+1||list.get(r).y > list.get(z).y-1||list.get(r).y > list.get(z).y) {
                                dlvp135++;//РїРѕР»РѕР¶РёС‚РµР»СЊРЅРѕР¶
                                break;
                            } else {
                                dlvg135++;//РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕ
                                break;
                            }
                        }
                    }
                }
            }
        }
        int[] signs = new int[5];
        signs[0] = dlvp90;
        signs[1] = dlvg90;
        signs[2] = dlvp135;
        signs[3] = dlvg135;
        signs[4] = cr0;
        return signs;
    }
}