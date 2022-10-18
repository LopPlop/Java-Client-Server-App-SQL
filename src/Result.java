import java.io.Serializable;

public class Result implements Serializable {
    private int id, fish_count, time;
    private String username;

    public Result(){

    }
    public Result(int id, String name, int fish_count, int time){
        this.id = id;
        this.username = name;
        this.fish_count = fish_count;
        this.time = time;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setFish_count(int fish_count){
        this.fish_count = fish_count;
    }
    public void setTime(int time){
        this.time = time;
    }
    public void setUsername(String name){
        this.username = name;
    }
    public int getId(){
        return id;
    }
    public int getFish_count(){
        return fish_count;
    }
    public int getTime(){
        return time;
    }
    public String getUsername(){
        return username;
    }
    public void print(){
        System.out.println(id + ")"+" Username: " + username + "; fish count: " + fish_count + " time: " + time);
    }
}
