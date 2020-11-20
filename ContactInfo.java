import java.util.Random;
import java.util.ArrayList;

public class ContactInfo{

  public int id;
  public int distance;
  public int time;
  public boolean used;

  public ContactInfo(int id, int distance, int time){
    this.used = false;
    this.id = id;
    this.distance = distance;
    this.time = time;
  }

  public boolean isValid(){
    if((id >= 0) && (distance >= 0) && (time >= 0)){
      return true;
    }
    else{
      return false;
    }
  }

  public static void main (String[] args){
    int id = 10;
    int distance = 0;
    int time = 10;
    boolean used = false;
    ContactInfo test = new ContactInfo(id, distance, time);
    //test.ContactInfo(id, distance, time);
    boolean output = test.isValid();
    System.out.println(output);
  }
}
