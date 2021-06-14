package entities;

public class EducationalMaterialEntity {
    private final int id;
    private final String type;
    private String name;
    private final String path;

    public EducationalMaterialEntity(int id, String type, String name, String path) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
    }
}
