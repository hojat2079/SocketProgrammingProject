package client;

import client.util.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static client.util.Constant.*;

public class Client {

    Scanner sc;
    PrintWriter out;
    BufferedReader in;
    String username;
    private boolean isLogin = false;

    Client() {
        startSocket();
    }

    private void startSocket() {
        try (Socket socket = new Socket(Constant.serverName, Constant.portName)) {

            // writing to server
            out = new PrintWriter(socket.getOutputStream(), true);

            // reading from server
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            // object of scanner class
            sc = new Scanner(System.in);

            loop:
            while (true) {
                int item = showMenu();
                switch (item) {
                    case 1:
                        showCategory();
                        break;
                    case 2:
                        sendPost();
                        break;
                    case 3:
                        showMyPost();
                        break;
                    case 4:
                        deleteMyPost();
                        break;
                    case 5:
                        showAllPost();
                        break;
                    case 6:
                        exit();
                        break loop;
                    default:
                        break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exit() {
        sc.close();
    }

    private void showMyPost() throws IOException {
        out.println(showMyPost + username);
        String result = in.readLine();
        if (result.contains("not founded your post")) {
            System.out.println("not founded your post!");
            return;
        }
        printFormattedPostsResult(result);
    }

    private void printFormattedPostsResult(String result) {
        int index = 0;
        for (int i = 10; i < result.length(); i++) {
            StringBuilder postId = new StringBuilder();
            StringBuilder postTitle = new StringBuilder();
            StringBuilder desc = new StringBuilder();
            StringBuilder date = new StringBuilder();
            StringBuilder categoryTitle = new StringBuilder();
            StringBuilder parent = new StringBuilder();
            System.out.println("MyPosts : ");
            for (int j = i; result.charAt(j) != ' '; j++, i++) {
                postId.append(result.charAt(j));
            }
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                postTitle.append(result.charAt(j));
                i = j;
            }
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                desc.append(result.charAt(j));
                i = j;
            }
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                date.append(result.charAt(j));
                i = j;
            }
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                date.append(result.charAt(j));
                i = j;
            }
            date.append(' ');
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                categoryTitle.append(result.charAt(j));
                i = j;
            }
            for (int j = i + 1; result.charAt(j) != ','; j++, i++) {
                parent.append(result.charAt(j));
                i = j;
            }
            index++;
            i++;
            System.out.println(index + ". postId : " + postId.toString()
                    + ", postTitle : " + postTitle.toString() +
                    ", desc : " + desc.toString() +
                    ", date : " + date.toString() +
                    ", titleCategory : " + categoryTitle.toString() +
                    ", parentCategory : " + parent.toString());
        }
    }

    private void deleteMyPost() throws IOException {
        System.out.print("Enter post id : ");
        String id=sc.next();
        while (id.trim().isEmpty()){
            id=sc.next();
        }

        out.println(deleteMyPost + username + " " + id);
        String result = in.readLine();
        if (result.contains("Error in Removed your post")){
            System.out.println("Error in Removed your post!");
            return;
        }
        System.out.println("Remove Post is SuccessFully!");
    }

    private void showAllPost() throws IOException {
        out.println(showAllPost);
        String result = in.readLine();
        if (result.contains("not founded All post")) {
            System.out.println("not founded All post!");
            return;
        }
        printFormattedAllPostsResult(result);
    }

    private void printFormattedAllPostsResult(String result) {
        int index = 0;
        for (int i = 10; i < result.length(); i++) {
            StringBuilder postId = new StringBuilder();
            StringBuilder postTitle = new StringBuilder();
            StringBuilder desc = new StringBuilder();
            StringBuilder date = new StringBuilder();
            StringBuilder categoryTitle = new StringBuilder();
            StringBuilder parent = new StringBuilder();
            StringBuilder owner = new StringBuilder();
            System.out.println("MyPosts : ");
            for (int j = i; result.charAt(j) != ' '; j++, i++) {
                postId.append(result.charAt(j));
            }
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                postTitle.append(result.charAt(j));
                i = j;
            }
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                desc.append(result.charAt(j));
                i = j;
            }
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                date.append(result.charAt(j));
                i = j;
            }
            date.append(' ');
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                date.append(result.charAt(j));
                i = j;
            }

            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                categoryTitle.append(result.charAt(j));
                i = j;
            }
            for (int j = i + 1; result.charAt(j) != ' '; j++, i++) {
                parent.append(result.charAt(j));
                i = j;
            }
            for (int j = i + 1; result.charAt(j) != ','; j++, i++) {
                owner.append(result.charAt(j));
                i = j;
            }
            index++;
            i++;
            System.out.println(index + ". postId : " + postId.toString()
                    + ", postTitle : " + postTitle.toString() +
                    ", desc : " + desc.toString() +
                    ", date : " + date.toString() +
                    ", titleCategory : " + categoryTitle.toString() +
                    ", parentCategory : " + parent.toString() +
                    ", Owner : " + owner.toString());
        }
    }

    private void sendPost() throws IOException {
        String title;
        String desc;
        int category;
        System.out.print("title : ");
        title = sc.next();
        while (!(title.length() > 3 && title.length() < 65)) {
            System.out.println("len title must 3<title.len<65");
            title = sc.next();
        }
        System.out.print("desc : ");
        desc = sc.next();
        while (!(desc.length() > 9 && desc.length() < 1025)) {
            System.out.println("len desc must 9<desc.len<1025");
            desc = sc.next();
        }
        System.out.print("category(Enter(1-4)) : ");
        category = sc.nextInt();
        while (!(category == 1 || category == 2 || category == 3 || category == 4)) {
            System.out.println("len category must 0<category<5");
            category = sc.nextInt();
        }
        out.println(sendPost + title + " " + desc + " " + String.valueOf(category) + " " + username + " ");
        out.flush();
        String result = in.readLine();
        if (result.contains("PostedAdded!")) {
            System.out.println("Posted Added!");
        } else
            System.out.println("Posted not Added! Error!");
    }

    private void showCategory() throws IOException {
        out.println(categoryName);
        String result = in.readLine();
        printFormattedCategoriesResult(result);
    }

    private void printFormattedCategoriesResult(String result) {
        int index = 0;
        System.out.println("Categories : ");
        for (int i = 12; i < result.length(); i++) {
            StringBuilder name = new StringBuilder();
            StringBuilder parent = new StringBuilder();
            for (int j = i; result.charAt(j) != ' '; j++, i++) {
                name.append(result.charAt(j));
            }
            for (int j = i + 1; result.charAt(j) != ','; j++, i++) {
                parent.append(result.charAt(j));
            }
            index++;
            i++;
            System.out.println(index + ". name : " + name.toString() + ", parent : " + parent.toString());
        }
    }

    public int showMenu() throws IOException {
        if (!isLogin) {
            int result = showLoginMenu();
            if (result == -1) return 6;
            while (result != 1) {
                result = showLoginMenu();
            }
        }
        isLogin = true;
        int menuItem;
        System.out.println("1.show Category \n2.Send Post\n3.show My Post\n4.Delete My Post" +
                "\n5.show last posts\n6.Exit");
        menuItem = sc.nextInt();
        return menuItem;
    }

    private int showLoginMenu() throws IOException {
        System.out.println("1.Login \n2.Register\n3.Exit");
        int menuItem = sc.nextInt();
        if (menuItem == 3) {
            sc.close();
            exit();
        }
        if (!(menuItem == 1 || menuItem == 2))
            return 0;
        if (menuItem == 1 && login()) return 1;
        if (menuItem == 2 && register()) return 1;
        return 0;
    }

    private boolean register() throws IOException {
        String username;
        String password;
        String name;
        System.out.print("name : ");
        name = sc.next();
        while (!(name.length() > 0 && name.length() < 31)) {
            System.out.println("len name must 0<name.len<31");
            name = sc.next();
        }
        System.out.print("username : ");
        username = sc.next();
        while (!(username.length() > 3 && username.length() < 33)) {
            System.out.println("len username must 3<username.len<33");
            username = sc.next();
        }
        this.username = username;
        System.out.print("password : ");
        password = sc.next();
        while (!(password.length() > 6)) {
            System.out.println("len password must 6<name.len");
            password = sc.next();
        }
        out.println(register + name + " " + username + " " + password);
        String result = in.readLine();
        if (result.contains("Welcome")) {
            System.out.println("Welcome!");
            return true;
        }
        System.out.println("username is exit!");
        return false;
    }

    private boolean login() throws IOException {
        String username;
        String password;
        System.out.print("username : ");
        username = sc.next();
        System.out.print("password : ");
        password = sc.next();
        out.println(login + username + " " + password);
        this.username = username;
        String result = in.readLine();
        if (result.contains("not Founded")) {
            System.out.println("User not Founded");
            return false;
        }
        System.out.println("Welcome");
        return true;
    }
}

class Main {
    public static void main(String[] args) {
        Client client = new Client();
    }
}

