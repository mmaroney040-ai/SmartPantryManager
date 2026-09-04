package za.ac.richfield.smartpantry;

public class Recipe {
    private long id;
    private String name;
    private String method;
    public Recipe(long id, String name, String method) { this.id=id; this.name=name; this.method=method; }
    public long getId(){ return id; }
    public String getName(){ return name; }
    public String getMethod(){ return method; }
}
