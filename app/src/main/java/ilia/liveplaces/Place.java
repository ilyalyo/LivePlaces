package ilia.liveplaces;

public class Place {
    long id;
    int inst_id;
    String name;

    public Place(){}

    public Place(int inst_id, String name){
        this.inst_id = inst_id;
        this.name = name;
    }

    public void setId(long id){
        this.id = id;
    }
    public void setInstId(int inst_id){
        this.inst_id = inst_id;
    }
    public void setName(String name){
        this.name = name;
    }

    public long getId(){
        return this.id;
    }

    public int getInstId(){
        return this.inst_id;
    }

    public String getName(){
        return this.name;
    }

    public String ToString(){
        return this.name;
    }
}
