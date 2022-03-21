package server.database;

import server.database.entity.Category;
import server.database.entity.Post;
import server.database.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface Repository {
    List<Category> getCategory() throws SQLException;

    List<Post> getMyPost(String username) throws SQLException;

    int removePost(String username,String postId) throws SQLException;

    List<Post> getAllPost() throws SQLException;

    boolean login(String username, String password) throws SQLException;

    int register(User user) throws SQLException;

    int sendPost(String username, int categoryId, String title,String desc) throws SQLException;
}
