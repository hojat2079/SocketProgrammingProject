package server.database.entity;

public class Category {
    private int id;
    private String title;
    private String parent;

    public Category(int id, String title, String parent) {
        this.id = id;
        this.title = title;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
