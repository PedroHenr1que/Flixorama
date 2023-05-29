package menu;

import Entities.*;
import Enums.CountryEnum;
import Enums.GenderEnum;
import com.rabbitmq.client.Channel;
import connection.ConnectionManager;
import exchanges.FanoutExchange;
import exchanges.HeadersExchange;
import msg.HeadersMessage;

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
        System.out.println("##################################");
        System.out.println("# 1 - Producer");
        System.out.println("# 2 - Consumer");
        System.out.println("# 3 - Audit");
        System.out.print("# What are you? ");
        int choice = scanner.nextInt();
        System.out.println("##################################");


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
        System.out.print("# Whats your name? ");
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
            System.out.print("# Content title: ");
            String contentName = scanner.nextLine();
            msg.setTitle(contentName);

            System.out.print("# Description: ");
            String contentDescription = scanner.nextLine();
            msg.setContent(contentDescription);

            List<GenderEnum> genderEnums = MenuApplication.getGenders();
            msg.setHeaders(genderEnums);

            System.out.println("##################################");

            try {
                p.sendMessage(msg);
                System.out.print("# Another content(y/n)? ");
                newContent = scanner.nextLine().equals("y");

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void auditMenu(Audit a) {

        System.out.println("##################################");

        try {
            a.declareQueue();
            a.declareBinding();
            a.receiveMessage();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    private static CountryEnum getCountry() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("# Which country are you at? ");
        List<CountryEnum> countryEnums = Arrays.stream(CountryEnum.values()).toList();
        for (int i = 0; i < countryEnums.size(); i++) {
            System.out.println("# " + (i+1) + " - " + countryEnums.get(i));
        }
        System.out.print("# Choose: ");
        int choice = scanner.nextInt() - 1;
        return countryEnums.get(choice);
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
            System.out.print("# Choose(-1 to stop choosing): ");
            choice = scanner.nextInt();

            if (choice != -1) {
                contentGenders.add(genderEnums.get(choice - 1));
            }
        } while (choice != -1);

        return contentGenders;
    }

}
