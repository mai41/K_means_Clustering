/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
Mai Rady Hassan Mohamed - 20170302 -IS-DS-4
 */
package k_means_assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;
import java.math.*;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author pc
 */
public class Assignment2 {

    public ArrayList generateRandom(String k, int upperBound, int col) {
        Random rand = new Random();
        ArrayList<String> centroid = new ArrayList<String>();
        for (int i = 0; i < Float.parseFloat(k); i++) {
            for (int j = 1; j < col; j++) {
                float random = rand.nextInt(upperBound);
                centroid.add(Float.toString(random));
            }
        }
        return centroid;
    }

    public ArrayList getMean(ArrayList<String> items) throws IOException, BiffException {
        File excel = new File("C:\\Users\\pc\\Downloads\\DataMining\\Assignment2\\Assignment2\\Sales.xls");
        ArrayList<String> content = new ArrayList<>();
        ArrayList<Float> means = new ArrayList<>();
        Workbook workbook = Workbook.getWorkbook(excel);
        Sheet sheet = workbook.getSheet(0);
        int row = sheet.getRows();
        int col = sheet.getColumns();
        if (items.isEmpty()) {
            means.add(Float.NaN);
        }
        if (items.size() == 1) {
            for (int i = 1; i < row; i++) {
                for (int j = 1; j < col; j++) {
                    Cell c = sheet.getCell(j, i);
                    if (Integer.parseInt(items.get(0)) == i) {
                        means.add(Float.parseFloat(c.getContents()));
                    }
                }
            }
        } else {
            for (int i = 1; i < col; i++) {
                float mean = 0, m = 0;
                for (int j = 1; j < row; j++) {
                    Cell c = sheet.getCell(i, j);
                    for (int x = 0; x < items.size(); x++) {
                        if (Integer.parseInt(items.get(x)) == j) {
                            content.add(c.getContents());
                        }
                    }
                }
                for (int j = 0; j < content.size(); j++) {
                    m += Integer.parseInt(content.get(j));
                }
                mean = m / content.size();
                means.add(mean);
                content.clear();
            }
        }
        return means;
    }

    public ArrayList getClusters(ArrayList<String> centroid, String k) throws IOException, BiffException {
        File excel = new File("C:\\Users\\pc\\Downloads\\DataMining\\Assignment2\\Assignment2\\Sales.xls");
        ArrayList<String> items = new ArrayList<String>();
        ArrayList<String> distances = new ArrayList<String>();
        ArrayList<String> clusters = new ArrayList<String>();
        Workbook workbook = Workbook.getWorkbook(excel);
        Sheet sheet = workbook.getSheet(0);
        int row = sheet.getRows();
        int col = sheet.getColumns();
        float manhattan = 0;
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                Cell c = sheet.getCell(j, i);
                items.add(c.getContents());
            }
            //System.out.println("Rows: " + items);
            for (int x = 0; x < Integer.parseInt(k) * (col - 1); x += items.size()) {
                for (int j = 0; j < items.size(); j++) {
                    manhattan += Math.abs(Float.parseFloat(centroid.get(j + x)) - Float.parseFloat(items.get(j)));
                }
                //System.out.println("distance: " + manhattan);
                distances.add(Float.toString(manhattan));
                manhattan = 0;
            }
            //System.out.println(distances);
            float minDis = Float.parseFloat(distances.get(0)), index = 0;
            for (int j = 0; j < distances.size(); j++) {
                if (minDis > Float.parseFloat(distances.get(j))) {
                    index = j;
                }
            }
            clusters.add(Float.toString(index + 1));
            distances.clear();
            items.clear();
        }
        return clusters;
    }

    public void Sales() throws FileNotFoundException, IOException, BiffException {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter number of clusters: ");
        String k = in.nextLine();
        int upperBound = 0;
        File excel = new File("C:\\Users\\pc\\Downloads\\DataMining\\Assignment2\\Assignment2\\Sales.xls");
        ArrayList<String> centroid = new ArrayList<String>();
        ArrayList<String> items = new ArrayList<String>();
        ArrayList<String> items2 = new ArrayList<String>();
        ArrayList<String> clusters = new ArrayList<String>();
        ArrayList<String> semiClusters = new ArrayList<String>();
        ArrayList<String> means = new ArrayList<String>();
        ArrayList<Integer> outlier = new ArrayList<Integer>();
        Workbook workbook = Workbook.getWorkbook(excel);
        Sheet sheet = workbook.getSheet(0);
        int row = sheet.getRows();
        int col = sheet.getColumns();
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                Cell c = sheet.getCell(j, i);
                if (upperBound < Integer.parseInt(c.getContents())) {
                    upperBound = Integer.parseInt(c.getContents());
                }
            }
        }
        centroid = generateRandom(k, upperBound, col);
        // System.out.println(centroid);
        clusters = getClusters(centroid, k);
        //System.out.println(clusters);

        for (int j = 0; j < Integer.parseInt(k); j++) {
            for (int i = 0; i < clusters.size(); i++) {
                if (Float.parseFloat(clusters.get(i)) == j + 1) {
                    semiClusters.add(Integer.toString(i + 1));
                    items.add(Integer.toString(i + 1));
                }
            }
            items.add("|");
            System.out.println("Cluster" + (j + 1) + ": " + semiClusters);
            ArrayList<Float> m = new ArrayList<>();
            m = getMean(semiClusters);
            for (int i = 0; i < m.size(); i++) {
                means.add(Float.toString(m.get(i)));
            }
            semiClusters.clear();
        }
        //System.out.println(means);
        clusters.clear();
        clusters = getClusters(means, k);
        //System.out.println(clusters);
        System.out.println();
        for (int j = 0; j < Integer.parseInt(k); j++) {
            for (int i = 0; i < clusters.size(); i++) {
                if (Float.parseFloat(clusters.get(i)) == j + 1) {
                    semiClusters.add(Integer.toString(i + 1));
                    items2.add(Integer.toString(i + 1));
                }
            }
            items2.add("|");
            System.out.println("Cluster" + (j + 1) + ": " + semiClusters);
            ArrayList<Float> m = new ArrayList<>();
            m = getMean(semiClusters);
            for (int i = 0; i < m.size(); i++) {
                means.add(Float.toString(m.get(i)));
            }
            semiClusters.clear();
        }
        while (!(items.equals(items2))) {
            items = items2;
            items2.clear();
            //System.out.println(means);
            clusters.clear();
            clusters = getClusters(means, k);
            //System.out.println(clusters);
            System.out.println();
            for (int j = 0; j < Integer.parseInt(k); j++) {
                for (int i = 0; i < clusters.size(); i++) {
                    if (Float.parseFloat(clusters.get(i)) == j + 1) {
                        semiClusters.add(Integer.toString(i + 1));
                        items2.add(Integer.toString(i + 1));
                    }
                }
                items2.add("|");
                System.out.println("Cluster" + (j + 1) + ": " + semiClusters);
                float r = semiClusters.size() / col;
                if (r < 1.0) {
                    outlier.add(j + 1);
                }
                ArrayList<Float> m = new ArrayList<>();
                m = getMean(semiClusters);
                for (int i = 0; i < m.size(); i++) {
                    means.add(Float.toString(m.get(i)));
                }
                semiClusters.clear();
            }
            for (int i = 0; i < outlier.size(); i++) {
                System.out.println("Cluster" + outlier.get(i) + " is Outlier");
            }

        }
    }

}
