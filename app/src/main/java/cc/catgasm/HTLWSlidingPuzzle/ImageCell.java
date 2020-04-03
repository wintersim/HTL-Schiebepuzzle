package cc.catgasm.HTLWSlidingPuzzle;

public class ImageCell {
    private String path;
    private String name;
    private int id;

    public ImageCell(String path, String name, int id) {
        this.id = id;
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
