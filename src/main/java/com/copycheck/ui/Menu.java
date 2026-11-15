package com.copycheck.ui;

import java.util.Scanner;

// The console menu. This is the only class that talks to the user.
public class Menu {

    private final Scanner scanner;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    // Shows the menu until the user chooses Exit.
    public void start() {
        System.out.println("CopyCheck - menu coming in CC-08");
    }
}
