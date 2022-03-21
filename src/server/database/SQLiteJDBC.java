package server.database;

import server.database.entity.Category;
import server.database.entity.Database;
import server.database.entity.Post;
import server.database.entity.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static server.util.Constant.*;

public class SQLiteJDBC implements Database {
    Connection c = null;
    Statement stmt = null;

    @Override
    public void setupDatabase() {
        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + USER_TABLE +
                    " (" + USERNAME +
                    " TEXT PRIMARY KEY NOT NULL, "
                    + NAME_USER +
                    " TEXT NOT NULL, " +
                    PASSWORD + " TEXT NOT NULL);";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE +
                    " (" + ID_CATEGORY +
                    " INTEGER PRIMARY KEY NOT NULL, "
                    + NAME_CATEGORY +
                    " TEXT NOT NULL, " +
                    PARENT + " TEXT NOT NULL);";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS " + POST_TABLE +
                    " (" + ID_POST +
                    " INTEGER PRIMARY KEY NOT NULL , "
                    + TITLE +
                    " TEXT NOT NULL, " +
                    DESC + " TEXT NOT NULL, " + USERNAME + " TEXT NOT NULL, "
                    + ID_CATEGORY + " INTEGER NOT NULL, " + DATE + " TEXT NOT NULL );";
            stmt.executeUpdate(sql);
            stmt.close();
//            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    @Override
    public int register(User user) throws SQLException {
        ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) AS total FROM " + USER_TABLE + " WHERE " + USERNAME + "= '" + user.getUsername() + "';");
        int size = rs.getInt("total");
        if (size > 0)
            return -1;
        stmt = c.createStatement();
        String sql;
        sql = "INSERT INTO " + USER_TABLE + " (" + USERNAME + "," + NAME_USER + "," +
                PASSWORD + " ) " + "VALUES " +
                " ('" + user.getUsername() + "', '" + user.getName() + "', '"
                + user.getPassword() + "');";
        int success = stmt.executeUpdate(sql);
        stmt.close();
        return success;
    }

    @Override
    public List<Post> getMyPost(String username) throws SQLException {
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT POST.ID_POST,POST.TITLE," +
                        "POST.DESC,POST.DATE,CATEGORY.NAME_CATEGORY," +
                        "CATEGORY.PARENT, CATEGORY.ID_CATEGORY " +
                        "FROM POST " +
                        "INNER JOIN CATEGORY " +
                        "ON POST.ID_CATEGORY =CATEGORY.ID_CATEGORY WHERE POST.USERNAME = '" + username + "';"
        );
        List<Post> list = new ArrayList<>();
        while (rs.next()) {
            int idPost = rs.getInt(ID_POST);
            int idCategory = rs.getInt(ID_CATEGORY);
            String title = rs.getString(TITLE);
            String desc = rs.getString(DESC);
            String date = rs.getString(DATE);
            String nameCategory = rs.getString(NAME_CATEGORY);
            String parent = rs.getString(PARENT);
            list.add(new Post(idPost, title, desc,
                    new Category(idCategory, nameCategory, parent),
                    date));
        }
        rs.close();
        stmt.close();
        return list;
    }

    @Override
    public List<Post> getAllPost() throws SQLException {
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT POST.ID_POST,POST.TITLE," +
                        "POST.DESC,POST.DATE,CATEGORY.NAME_CATEGORY," +
                        "CATEGORY.PARENT, 'USER'.NAME_USER" +
                        ", CATEGORY.ID_CATEGORY " +
                        "FROM POST " +
                        "INNER JOIN CATEGORY " +
                        "ON POST.ID_CATEGORY =CATEGORY.ID_CATEGORY" +
                        " INNER JOIN 'USER' ON 'USER'.USERNAME=POST.USERNAME;"
        );
        List<Post> list = new ArrayList<>();
        while (rs.next()) {
            int idPost = rs.getInt(ID_POST);
            int idCategory = rs.getInt(ID_CATEGORY);
            String title = rs.getString(TITLE);
            String desc = rs.getString(DESC);
            String date = rs.getString(DATE);
            String nameCategory = rs.getString(NAME_CATEGORY);
            String parent = rs.getString(PARENT);
            String name = rs.getString(NAME_USER);
            list.add(new Post(idPost, title, desc,
                    new Category(idCategory, nameCategory, parent),
                    date, new User(name)));
        }
        rs.close();
        stmt.close();
        return list;
    }

    @Override
    public boolean findUser(String username, String password) throws SQLException {
        ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) AS total FROM " + USER_TABLE + " WHERE " + USERNAME + "= '" + username + "' AND " + PASSWORD + " = '" + password + "';");
        int size = rs.getInt("total");
        return size != 0;
    }

    @Override
    public void insertSampleCategory() throws SQLException {
        ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) AS total FROM " + USER_TABLE + ";");
        int size = rs.getInt("total");
        if (!(size > 0)) {
            stmt = c.createStatement();
            String sql = "INSERT INTO CATEGORY (ID_CATEGORY,NAME_CATEGORY,PARENT) " +
                    "VALUES (1, 'Politic','News');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO CATEGORY (ID_CATEGORY,NAME_CATEGORY,PARENT) " +
                    "VALUES (2, 'Film','Media');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO CATEGORY (ID_CATEGORY,NAME_CATEGORY,PARENT) " +
                    "VALUES (3, 'Sport','News');";
            stmt.executeUpdate(sql);


            sql = "INSERT INTO CATEGORY (ID_CATEGORY,NAME_CATEGORY,PARENT) " +
                    "VALUES (4, 'Science','News');";
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @Override
    public List<Category> getCategory() throws SQLException {
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM CATEGORY;");
        List<Category> list = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt(ID_CATEGORY);
            String name = rs.getString(NAME_CATEGORY);
            String parent = rs.getString(PARENT);
            list.add(new Category(id, name, parent));
        }
        rs.close();
        stmt.close();
        return list;
    }

    @Override
    public int removePost(String username, String postId) throws SQLException {
        stmt = c.createStatement();
        int postID=Integer.parseInt(postId);
        String sql;
        sql="DELETE FROM POST WHERE ID_POST='" + postID + "' AND USERNAME= '" + username + "' ;";
        int rs = stmt.executeUpdate(sql);
        stmt.close();
        return rs;
    }

    @Override
    public int sendPost(String username, int categoryId, String title, String desc) throws SQLException {
        String sql, date = formatDate();
        stmt = c.createStatement();
        sql = "INSERT INTO " + POST_TABLE + " (" + TITLE + "," + DESC + "," +
                USERNAME + "," + ID_CATEGORY + "," + DATE + " ) " + "VALUES " +
                " ('" + title + "', '" + desc + "', '"
                + username + "', '" + categoryId + "', '" + date + "' );";
        int success = stmt.executeUpdate(sql);
        stmt.close();
        return success;
    }

    String formatDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}