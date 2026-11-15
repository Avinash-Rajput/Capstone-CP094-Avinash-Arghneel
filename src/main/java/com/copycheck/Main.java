package com.copycheck;

import com.copycheck.ui.Menu;
import java.util.Scanner;

// Starting point of the program.
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(scanner);
        menu.start();
    }
}
