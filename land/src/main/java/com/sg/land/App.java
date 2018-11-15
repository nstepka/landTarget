/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.land;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author nstep
 */
public class App {

    public static void main(String[] args) {

        UserIO io = new UserIOConsoleImpl();  //easy tool to allow user to interact JVM
        
        int fieldCounter = 1;
        List<Integer> plantableAreas = new ArrayList<Integer>();
        int field[][] = new int[400][600];
        io.print("Please enter the barren land area in this format ");
        String input = io.readString("{48 192 351 207}, {48 392 351 407}, {120 52 135 547}, {260 52 275 547}");
        try {
            input = input.replace("{", "").replace("}", "");
            List<String> cordinates = Arrays.asList(input.split(","));  //splits all the locations at the ,
            for (String cordinate : cordinates) {
                cordinate = cordinate.trim(); //gets rid of the spaces on the sides at 
                                      //the start of every loop keeping the 
                                      //inner spaces to be allowed to be read
                if (cordinate.split("\\s+").length != 4) {  //checks to make sure the format is right
                                                        // if there are not 4 corindates the location
                                                        // can not be true.
                    throw new NumberFormatException();
                }
                int x1 = Integer.valueOf(cordinate.split("\\s+")[0]);  //assign to each postion BUTTOM RIGHT, TOP LEFT
                int y1 = Integer.valueOf(cordinate.split("\\s+")[1]);   //""
                int x2 = Integer.valueOf(cordinate.split("\\s+")[2]);   //""
                int y2 = Integer.valueOf(cordinate.split("\\s+")[3]);   //""
                for (int i = x1; i <= x2; i++) {  //i counts until it gets to x2 and then it doesnt need to count any more
                    for (int j = y1; j <= y2; j++) {  // j counts until it equals y1 then its done.
                        field[i][j] = -1;  //assigns a -1 value to bad land
                                           //will be used later to validate land not scanned yet.
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid data");
            System.exit(1);
        }


        while (true) {
            boolean foundUnfilledRegion = false;
            int fillX = -1; 
            int fillY = -1;
            for (int i = 0; i < field.length; i++) { //iterate through the field and check for uncounted fertile areas
                for (int j = 0; j < field[0].length; j++) {
                    if (field[i][j] == 0) {
                        foundUnfilledRegion = true;
                        fillX = i;
                        fillY = j;
                    }
                    
                }
            }
            if (foundUnfilledRegion == false) {
                break; //we've accounted for every bit of previously uncounted fertile regions
            }
            int totalSquareMeters = 0;  //seeting up counter of good land to add to to the list 
            int h = field[0].length;
            int w = field.length;

            if (fillX < 0 || fillX > (w - 1) || fillY < 0 || fillY > (h - 1)) { //invalid input
                return;
            }

            Stack<Point> stack = new Stack<Point>();
            stack.push(new Point(fillX, fillY));
            int x1;
            int y1;

            while (!stack.isEmpty()) {
                Point p = stack.pop();
                x1 = p.x;
                y1 = p.y;
                   
                if (y1 < 0 || y1 > (h - 1) || x1 < 0 || x1 > (w - 1)) { 
                    continue;  //moves ahead if the numbers are bad and pops to the next grid.
                }

                if (field[x1][y1] == 0) {
                    field[x1][y1] = fieldCounter; //assign a number to the field for counting.
                    stack.push(new Point(x1 + 1, y1));  //adding stacks to get all the locations
                    stack.push(new Point(x1 - 1, y1));  //is there a method to do this?
                    stack.push(new Point(x1, y1 + 1));
                    stack.push(new Point(x1, y1 - 1));  
                }
            }

            for (int i = 0; i < field.length; i++) {  //length of 400 to start then changes number to new field
                for (int j = 0; j < field[0].length; j++) { //length of 600 to start ""  """ """ 
                    if (field[i][j] == fieldCounter) {  //if the number matches this itteration count a sq meter
                        totalSquareMeters++;
                    }
                }
            }
            plantableAreas.add(totalSquareMeters);  //adds sq feet of block of good land to the list
            fieldCounter++;  //increase sq meeter so we can loop around again and assign those locations a value.
        }
        Collections.sort(plantableAreas);  //sorys all the good fields and puts them from smallest to largest.
        for (Integer plantedArea : plantableAreas) {
            System.out.print(plantedArea + " ");
        }
    }

}
