package server.database.entity;

public class User {
    private String username;
    private String name;
    private String password;

    public User() {
    }

    public User(String name) {
        this.name = name;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username.length() > 3 && username.length() < 33)
            this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.length() > 0 && name.length() < 31)
            this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() > 6)
            this.password = password;
    }
}
