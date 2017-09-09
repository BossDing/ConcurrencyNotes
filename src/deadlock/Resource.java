package deadlock;

public class Resource {
    private int id;
    public Resource(int id) { this.id = id; }
    public int getId() { return id; }
    public String toString(){ return ""+id; };
}
