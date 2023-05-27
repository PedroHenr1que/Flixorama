package Entities;

import Enums.CountryEnum;
import Enums.GenderEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class MenuApplication {
    public static void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            HeadersExchange.declareExchange();
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("What are you?");
        System.out.println("1 - Producer");
        System.out.println("2 - Consumer");
        System.out.println("3 - Audit");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                Producer p = new Producer();
                MenuApplication.producerMenu(p);
            }
            case 2 -> {
                Consumer c = new Consumer();
                MenuApplication.consumerMenu(c);
            }
            case 3 -> {
                System.out.println("audit");
                Audit a = new Audit();
            }
        }
    }

    private static void consumerMenu(Consumer c) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("##################################");

        CountryEnum countryEnum = MenuApplication.getCountry();
        c.setCountry(countryEnum);

        List<GenderEnum> genderEnums = MenuApplication.getGenders();
        c.setQueueHeaders(genderEnums);

        try {
            c.declareQueue();
            c.declareBinding();
            c.receiveMessage();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("##################################");
    }

    private static void producerMenu(Producer p) {
        Scanner scanner = new Scanner(System.in);

        //country
        System.out.println("##################################");
        CountryEnum countryEnum = MenuApplication.getCountry();
        p.setCountry(countryEnum);

        //content
        boolean newContent = true;
        while (newContent) {
            MenuApplication.clear();
            HeadersMessage msg = new HeadersMessage();
            msg.setCountry(countryEnum);
            System.out.println("##################################");
            System.out.println("# Content title: ");
            String contentName = scanner.nextLine();
            msg.setTitle(contentName);

            System.out.println("# Description: ");
            String contentDescription = scanner.nextLine();
            msg.setContent(contentDescription);

            List<GenderEnum> genderEnums = MenuApplication.getGenders();
            msg.setHeaders(genderEnums);

            System.out.println("##################################");

            try {
                p.sendMessage(msg);
                System.out.println("# Another content(y/n)? ");
                newContent = scanner.next().equals("y");
                MenuApplication.clear();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static CountryEnum getCountry() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("# Which country are you at? ");
        List<CountryEnum> countryEnums = Arrays.stream(CountryEnum.values()).toList();
        for (int i = 0; i < countryEnums.size(); i++) {
            System.out.println("# " + (i+1) + " - " + countryEnums.get(i));
        }

        return countryEnums.get(scanner.nextInt() - 1);
    }

    private static List<GenderEnum> getGenders() {
        Scanner scanner = new Scanner(System.in);
        List<GenderEnum> genderEnums = Arrays.stream(GenderEnum.values()).toList();
        List<GenderEnum> contentGenders = new ArrayList<>();
        int choice;

        do {
            System.out.println("# Chose content genders: ");
            for (int i = 0; i < genderEnums.size(); i++) {
                System.out.println("# " + (i+1) + " - " + genderEnums.get(i));
            }
            System.out.println("# Choose(-1 to exit): ");
            choice = scanner.nextInt();
            if (choice != -1) {
                contentGenders.add(genderEnums.get(choice - 1));
            }
        } while (choice != -1);

        return contentGenders;
    }
    private static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ignored) {}
    }

}
