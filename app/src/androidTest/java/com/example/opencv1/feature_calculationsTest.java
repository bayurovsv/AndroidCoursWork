package com.example.opencv1;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.opencv.core.Point;
import java.util.ArrayList;

public class feature_calculationsTest {
    private Point Point1;
    private Point Point2;
    private Point Point3;
    private Point Point4;
    private Point Point5;
    private Point Point6;
    private Point Point7;
    private Point Point8;
    private Point Point9;
    private Point Point10;
    private Point Point11;
    private Point Point12;
    private Point Point13;
    private Point Point14;
    private Point Point15;
    private Point Point16;
    private Point Point17;
    private Point Point18;
    private Point Point19;
    private Point Point20;
    private Point Point21;
    private Point Point22;
    private Point Point23;
    private Point Point24;
    private Point Point25;
    private Point Point26;
    private Point Point27;
    private Point Point28;
    private Point Point29;
    private Point Point30;
    private Point Point31;
    private Point Point32;
    private Point Point33;
    private Point Point34;
    private Point Point35;
    private Point Point36;
    private Point Point37;
    private Point Point38;
    private Point Point39;
    private Point Point40;


    @Before
    public void setUp(){
        Point1 = new Point(30,36);
        Point2 = new Point(29,35);
        Point3 = new Point(29,34);
        Point4 = new Point(30,33);
        Point5 = new Point(31,33);
        Point6 = new Point(32,33);
        Point7 = new Point(33,33);
        Point8 = new Point(34,34);
        Point9 = new Point(35,33);
        Point10 = new Point(36,33);
        Point11 = new Point(37,33);
        Point12 = new Point(38,34);
        Point13 = new Point(39,35);
        Point14 = new Point(39,36);
        Point15 = new Point(39,37);
        Point16 = new Point(40,38);
        Point17 = new Point(39,39);
        Point18 = new Point(39,40);
        Point19 = new Point(39,41);
        Point20 = new Point(40,42);
        Point21 = new Point(40,43);
        Point22 = new Point(39,44);
        Point23 = new Point(38,44);
        Point24 = new Point(37,45);
        Point25 = new Point(36,44);
        Point26 = new Point(35,44);
        Point27 = new Point(34,44);
        Point28 = new Point(33,45);
        Point29 = new Point(32,45);
        Point30 = new Point(31,44);
        Point31 = new Point(31,43);
        Point32 = new Point(30,42);
        Point33 = new Point(29,43);
        Point34 = new Point(28,42);
        Point35 = new Point(28,41);
        Point36 = new Point(29,40);
        Point37 = new Point(30,40);
        Point38 = new Point(31,39);
        Point39 = new Point(30,38);
        Point40 = new Point(29,37);
    }
    @Test
    public void search_for_connected_points() {
        ArrayList<Point> list = new ArrayList<Point>();
        list.add(Point1);
        list.add(Point2);
        list.add(Point3);
        list.add(Point4);
        list.add(Point5);
        list.add(Point6);
        list.add(Point7);
        list.add(Point8);
        list.add(Point9);
        list.add(Point10);
        list.add(Point11);
        list.add(Point12);
        list.add(Point13);
        list.add(Point14);
        list.add(Point15);
        list.add(Point16);
        list.add(Point17);
        list.add(Point18);
        list.add(Point19);
        list.add(Point20);
        list.add(Point21);
        list.add(Point22);
        list.add(Point23);
        list.add(Point24);
        list.add(Point25);
        list.add(Point26);
        list.add(Point27);
        list.add(Point28);
        list.add(Point29);
        list.add(Point30);
        list.add(Point31);
        list.add(Point32);
        list.add(Point33);
        list.add(Point34);
        list.add(Point35);
        list.add(Point36);
        list.add(Point37);
        list.add(Point38);
        list.add(Point39);
        list.add(Point40);
        int[] expected = feature_calculations.Search_for_connected_points(list);
        int[] actual = new int[8];
        actual[0]=6;
        actual[1]=5;
        actual[2]=5;
        actual[3]=5;
        actual[4]=4;
        actual[5]=7;
        actual[6]=3;
        actual[7]=5;
        Assert.assertArrayEquals(expected,actual);
    }

    @Test
    public void curvature_calculation_points_counter() {
        ArrayList<Point> list = new ArrayList<Point>();
        list.add(Point1);
        list.add(Point2);
        list.add(Point3);
        list.add(Point4);
        list.add(Point5);
        list.add(Point6);
        list.add(Point7);
        list.add(Point8);
        list.add(Point9);
        list.add(Point10);
        list.add(Point11);
        list.add(Point12);
        list.add(Point13);
        list.add(Point14);
        list.add(Point15);
        list.add(Point16);
        list.add(Point17);
        list.add(Point18);
        list.add(Point19);
        list.add(Point20);
        list.add(Point21);
        list.add(Point22);
        list.add(Point23);
        list.add(Point24);
        list.add(Point25);
        list.add(Point26);
        list.add(Point27);
        list.add(Point28);
        list.add(Point29);
        list.add(Point30);
        list.add(Point31);
        list.add(Point32);
        list.add(Point33);
        list.add(Point34);
        list.add(Point35);
        list.add(Point36);
        list.add(Point37);
        list.add(Point38);
        list.add(Point39);
        list.add(Point40);
        int[] expected = feature_calculations.Curvature_calculation_points_counter(list);
        int[] actual = new int[5];
        actual[0]=2;//+90
        actual[1]=5;//-90
        actual[2]=14;//+135
        actual[3]=10;//-135
        actual[4]=8;//0
        Assert.assertArrayEquals(expected,actual);
    }
}