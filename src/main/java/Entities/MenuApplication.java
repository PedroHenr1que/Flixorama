package Entities;

import Enums.CountryEnum;
import Enums.GenderEnum;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class MenuApplication {
    public static void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            FanoutExchange.declareExchange();
            HeadersExchange.declareExchange();
            Channel channel = ConnectionManager.getConnection().createChannel();
            channel.exchangeBind("flix-exchange", "flix-fanout-exchange", "");
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
                producerMenu(p);
            }
            case 2 -> {
                Consumer c = new Consumer();
                consumerMenu(c);
            }
            case 3 -> {
                Audit a = new Audit();
                auditMenu(a);
            }
        }
    }

    private static void consumerMenu(Consumer c) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("##################################");
        System.out.println("# Whats your name? ");

        c.setConsumerName(scanner.nextLine());

        CountryEnum countryEnum = getCountry();
        c.setCountry(countryEnum);

        List<GenderEnum> genderEnums = getGenders();
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
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void auditMenu(Audit a) {
        try {
            a.declareQueue();
            a.declareBinding();
            a.receiveMessage();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("##################################");

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

}
