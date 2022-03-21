package server;

import server.database.Repository;
import server.database.SQLiteJDBC;
import server.database.SQLiteRepository;
import server.database.entity.Category;
import server.database.entity.Database;
import server.database.entity.Post;
import server.database.entity.User;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.List;

import static server.util.Constant.*;

// server.Server class
class Server {

    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            while (true) {
                Socket client = server.accept();

                ClientHandler clientSock
                        = new ClientHandler(client);

                new Thread(clientSock).start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        Database database = new SQLiteJDBC();
        Repository repository = new SQLiteRepository(database);
        PrintWriter out = null;
        BufferedReader in = null;

        public ClientHandler(Socket socket) throws SQLException, SocketException {
            socket.setSoTimeout(1000000);
            this.clientSocket = socket;
            database.setupDatabase();
            //just one time!
            database.insertSampleCategory();
        }

        public void run() {
            try {

                out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.contains(login)) {
                        resultLogin(line);
                    } else if (line.contains(register)) {
                        register(line);
                    } else if (line.contains(categoryName)) {
                        resultGetCategory();
                    } else if (line.contains(sendPost)) {
                        resultSendPost(line);
                    } else if (line.contains(showMyPost)) {
                        resultShowMyPost(line);
                    } else if (line.contains(showAllPost)) {
                        resultShowAllPosts();
                    } else if (line.contains(deleteMyPost)) {
                        resultDeleteMyPost(line);
                    }
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void resultDeleteMyPost(String line) throws SQLException {
            StringBuilder username = new StringBuilder();
            StringBuilder postID = new StringBuilder();
            for (int i = 12; i < line.length(); i++) {
                for (int j = i; line.charAt(j) != ' '; j++, i++) {
                    username.append(line.charAt(j));
                    i=j;
                }
                for (int j = i + 1; j < line.length(); j++, i++) {
                    postID.append(line.charAt(j));
                }
            }
            int removeResult = repository.removePost
                    (username.toString(), postID.toString());
            if (removeResult < 1) {
                out.println("Error in Removed your post! ");
            } else out.println("Remove Post is SuccessFully!");
            out.flush();
        }

        private void resultShowAllPosts() throws SQLException {
            StringBuilder line = new StringBuilder(showAllPost);
            List<Post> posts = repository.getAllPost();
            for (Post post : posts) {
                String temp = post.getId() + " " + post.getTitle()
                        + " " + post.getDetails() + " " +
                        post.getDate() + " " + post.getCategory().getTitle() +
                        " " + post.getCategory().getParent() + " " + post.getUser().getName() + ",";
                line.append(temp);
            }
            if (posts.isEmpty()) {
                out.println("not founded All post!");
                out.flush();
                return;
            }
            out.println(line.toString());
            out.flush();
        }

        private void resultShowMyPost(String usernameLine) throws SQLException {
            StringBuilder username = new StringBuilder();
            for (int i = 10; i < usernameLine.length(); i++) {
                username.append(usernameLine.charAt(i));
            }
            StringBuilder line = new StringBuilder(showMyPost);
            List<Post> posts = repository.getMyPost(username.toString());
            for (Post post : posts) {
                String temp = post.getId() + " " + post.getTitle()
                        + " " + post.getDetails() + " " +
                        post.getDate() + " " + post.getCategory().getTitle() +
                        " " + post.getCategory().getParent() + ",";
                line.append(temp);
            }
            if (posts.isEmpty()) {
                out.println("not founded your post!");
                out.flush();
                return;
            }
            out.println(line.toString());
            out.flush();
        }

        private void resultSendPost(String line) throws SQLException {
            StringBuilder username = new StringBuilder();
            StringBuilder title = new StringBuilder();
            StringBuilder desc = new StringBuilder();
            StringBuilder categoryId = new StringBuilder();
            System.out.println(line);
            for (int i = 8; i < line.length(); i++) {
                for (int j = i; line.charAt(j) != ' '; j++, i++) {
                    title.append(line.charAt(j));
                }
                for (int j = i + 1; line.charAt(j) != ' '; j++, i++) {
                    desc.append(line.charAt(j));
                    i = j;
                }
                for (int j = i + 1; line.charAt(j) != ' '; j++, i++) {
                    categoryId.append(line.charAt(j));
                    i = j;
                }
                for (int j = i + 1; line.charAt(j) != ' '; j++, i++) {
                    username.append(line.charAt(j));
                    i = j;
                }
            }
            if (repository.sendPost(username.toString(), Integer.parseInt(categoryId.toString())
                    , title.toString(), desc.toString()) > 0) {
                out.println("PostedAdded!");
            } else out.println("PostNotAdded!");
            out.flush();
        }

        private void register(String line) throws SQLException {
            StringBuilder username = new StringBuilder();
            StringBuilder password = new StringBuilder();
            StringBuilder name = new StringBuilder();
            System.out.println(line);
            int index = line.indexOf(' ');
            int lastIndex = line.lastIndexOf(' ');
            for (int i = 8; i < index; i++) {
                name.append(line.charAt(i));
            }
            for (int i = index + 1; i < lastIndex; i++) {
                username.append(line.charAt(i));
            }

            for (int i = lastIndex + 1; i < line.length(); i++) {
                password.append(line.charAt(i));
            }
            User user = new User();
            user.setName(name.toString());
            user.setUsername(username.toString());
            user.setPassword(password.toString());
            if (repository.register(user) > 0)
                out.println("Welcome!");
            else out.println("username is exit!");
            out.flush();
        }

        private void resultLogin(String line) throws SQLException {
            StringBuilder username = new StringBuilder();
            StringBuilder password = new StringBuilder();
            System.out.println(line);
            int index = line.indexOf(' ');
            for (int i = 5; i < index; i++) {
                username.append(line.charAt(i));
            }
            for (int i = index + 1; i < line.length(); i++) {
                password.append(line.charAt(i));
            }
            if (repository.login(username.toString(), password.toString()))
                out.println("Welcome!");
            else out.println("User not Founded");
            out.flush();
        }

        private void resultGetCategory() throws SQLException {
            StringBuilder line = new StringBuilder(categoryName);
            List<Category> categories = repository.getCategory();
            for (Category category : categories) {
                String temp = category.getTitle() + " " + category.getParent() + ",";
                line.append(temp);
            }
            out.println(line.toString());
            out.flush();
        }
    }
}
