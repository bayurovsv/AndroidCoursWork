package com.example.opencv1;

import android.graphics.Bitmap;
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
public class feature_calculations {
    /**
     * Количество точек N1
     */
    public int dlN1=0;
    /**
     * Количество точек N2
     */
    public int dlN2=0;
    /**
     * Количество точек N3
     */
    public int dlN3=0;
    /**
     * Количество точек N4
     */
    public int dlN4=0;
    /**
     * Количество точек N5
     */
    public int dlN5=0;
    /**
     * Количество точек N6
     */
    public int dlN6=0;
    /**
     * Количество точек N7
     */
    public int dlN7=0;
    /**
     * Количество точек N8
     */
    public int dlN8=0;
    /**
     * Количество выпуклых точек +90
     */
    public int dlvp90=0;
    /**
     * Количество вогнутых точек -90
     */
    public int dlvg90=0;
    /**
     * Количество выпуклых точек +135
     */
    public int dlvp135=0;
    /**
     * Количество вогнутых точек -135
     */
    public int dlvg135=0;
    /**
     * Длина контура
     */
    public double dlcon=0;
    /**
     * Количество точек с кривизной 0
     */
    public int cr0=0;
    /**
     * Bitmap для передачи изображения в основной класс
     */
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

    /**
     * Поиск связных точек контура
     */
    public void Search_for_connected_points(){
        for(int i = 0; i<list.size()-1;i++)
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
    }

    /**
     * Вычисление кривизны точек контура
     */
    public void Curvature_calculation_points_counter()
    {
        for(int z = 0; z<list.size();z++)
        {
            if(z==0)
            {
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y) {
                    cr0++;
                }
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    cr0++;
                }
                if (list.get(z).x == list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;
                }
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    cr0++;
                }
                if (list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                // g12
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x < list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && (list.get(z).x > list.get(list.size()-1).x && list.get(z).y > list.get(list.size()-1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && (list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(list.size()-1).x && list.get(z).y < list.get(list.size()-1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(list.size()-1).x && list.get(z).y == list.get(list.size()-1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
            }
            else if (z==list.size()-1){
                if (list.get(z).x < list.get(0).x && list.get(z).y == list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y == list.get(0).y) {
                    cr0++;
                }
                if (list.get(z).x == list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(0).x && list.get(z).y > list.get(0).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y) {
                    cr0++;
                }
                if (list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {

                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(0).x && list.get(z).y == list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(0).x && list.get(z).y == list.get(0).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x < list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(0).x && list.get(z).y > list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(0).x && list.get(z).y < list.get(0).y && (list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && (list.get(z).x > list.get(0).x && list.get(z).y > list.get(0).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(0).x && list.get(z).y < list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y == list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(0).x && list.get(z).y == list.get(0).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(0).x && list.get(z).y < list.get(0).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
            }
            else{
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y) {
                    cr0++;
                }
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;
                }
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    cr0++;
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    cr0++;
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp90++;
                            } else {
                                dlvg90++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {

                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x < list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x < list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x == list.get(z - 1).x && list.get(z).y > list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x == list.get(z + 1).x && list.get(z).y > list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && (list.get(z).x > list.get(z - 1).x && list.get(z).y > list.get(z - 1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x == list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && (list.get(z).x > list.get(z + 1).x && list.get(z).y > list.get(z + 1).y)) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).y == list.get(z).y && list.get(r).x != list.get(z).x) {
                            if (list.get(r).x > list.get(z).x) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y < list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y == list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y < list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y == list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z + 1).x && list.get(z).y == list.get(z + 1).y && list.get(z).x > list.get(z - 1).x && list.get(z).y < list.get(z - 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
                if (list.get(z).x < list.get(z - 1).x && list.get(z).y == list.get(z - 1).y && list.get(z).x > list.get(z + 1).x && list.get(z).y < list.get(z + 1).y) {
                    for (int r = 0; r < list.size(); r++) {
                        if (list.get(r).x == list.get(z).x && list.get(r).y != list.get(z).y) {
                            if (list.get(r).y > list.get(z).y) {
                                dlvp135++;
                            } else {
                                dlvg135++;
                            }
                        }
                    }
                }
            }
        }
        dlcon=(1*(dlN1+dlN3+dlN5+dlN7)+Math.sqrt(2*(dlN2+dlN4+dlN6+dlN8)));
    }
}