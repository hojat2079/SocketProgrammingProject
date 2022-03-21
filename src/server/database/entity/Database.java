package server.database.entity;

import java.sql.SQLException;
import java.util.List;

public interface Database {
    void setupDatabase();

    int register(User user) throws SQLException;

    List<Post> getMyPost(String username) throws SQLException;

    List<Post> getAllPost() throws SQLException;

    boolean findUser(String username, String password) throws SQLException;

    void insertSampleCategory() throws SQLException;

    List<Category> getCategory() throws SQLException;

    int removePost(String username, String postId) throws SQLException;

    int sendPost(String username, int categoryId, String title, String desc) throws SQLException;
}
