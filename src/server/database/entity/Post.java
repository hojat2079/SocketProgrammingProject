package server.database.entity;

public class Post {
    private int id;
    private User user;
    private String title;
    private String details;
    private Category category;
    private String date;

    public int getId() {
        return id;
    }

    public Post(int id, String title, String details, Category category, String date) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.category = category;
        this.date = date;
    }
    public Post(int id, String title, String details, Category category, String date,User user) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.category = category;
        this.date = date;
        this.user=user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.length() > 3 && title.length() < 65)
            this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        if (details.length() > 10 && details.length() < 1025)
            this.details = details;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
