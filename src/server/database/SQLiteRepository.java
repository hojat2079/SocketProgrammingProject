package server.database;

import server.database.entity.Category;
import server.database.entity.Database;
import server.database.entity.Post;
import server.database.entity.User;

import java.sql.SQLException;
import java.util.List;

public class SQLiteRepository implements Repository {

    Database appDatabase;

    public SQLiteRepository(Database database) {
        this.appDatabase = database;
    }

    @Override
    public List<Category> getCategory() throws SQLException {
        return appDatabase.getCategory();
    }

    @Override
    public List<Post> getMyPost(String username) throws SQLException {
        return appDatabase.getMyPost(username);
    }

    @Override
    public int removePost(String username, String postId) throws SQLException {
        return appDatabase.removePost(username,postId);
    }

    @Override
    public List<Post> getAllPost() throws SQLException {
        return appDatabase.getAllPost();
    }

    public boolean login(String username, String password) throws SQLException {
        return appDatabase.findUser(username, password);
    }

    @Override
    public int register(User user) throws SQLException {
        return appDatabase.register(user);
    }

    @Override
    public int sendPost(String username, int categoryId, String title, String desc) throws SQLException {
        return appDatabase.sendPost(username, categoryId, title, desc);
    }

}
