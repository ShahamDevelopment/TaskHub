package me.stav.taskhub.utilities;

public class Item {
    private final int id;
    private String name;
    private String description;
    private String creator;

    public Item(int id, String name, String description, String creator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
