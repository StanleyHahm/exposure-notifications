import java.util.Random;
import java.util.ArrayList;

public class Server{
  public ArrayList<Integer> infectedIds;

  public Server(){
    this.infectedIds = new ArrayList<Integer>();
  }

  public boolean addInfectedIds(ArrayList<Integer> ids){
    if(ids == null){
      return false;
    }
    for(int i = 0; i < ids.size(); i++){
      //WHY IS MY INFECTEDIDS NULL AND NOT ADDING ANYTHING
      this.infectedIds.add(ids.get(i));
    }
    if(infectedIds.size() == ids.size()){
      return true;
    }
    return false;
  }

  public ArrayList<Integer> getInfectedIds(){
    ArrayList<Integer> newInfectedIds = new ArrayList<Integer>();
    for(int i = 0; i < infectedIds.size(); i++){
      newInfectedIds.add(infectedIds.get(i));
    }
    return newInfectedIds;
  }

  public static void main(String [] args){
    ArrayList<Integer> ids = new ArrayList<Integer>();

    ids.add(0);
    ids.add(1);
    ids.add(null);
    ids.add(10);

    Server test = new Server();
    boolean test_addInfected = test.addInfectedIds(ids);
    //System.out.println(test_addInfected);
    System.out.println(test.getInfectedIds());

  }
}
