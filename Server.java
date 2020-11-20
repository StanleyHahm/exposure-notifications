/**
*  NAME: Stanley Hahm
*  ID: A14609365
*  EMAIL: sthahm@ucsd.edu
*
*  For in this code, we are trying to set up ArrayLists of the infected
*  students' IDs. We are simply using a new concept of multiple classes.
*/
import java.util.Random;
import java.util.ArrayList;

/**
*  This class uses multiples methods. It takes in an ArrayList of
*  infected individuals and outputs a new ArrayList of those people.
*  An important instance variable is infectedIds where it initializes
*  an Integer ArrayList.
*/
public class Server{
  public ArrayList<Integer> infectedIds;

  /**
  *  Makes new integer ArrayList called infectedId
  */
  public Server(){
    this.infectedIds = new ArrayList<Integer>();
  }

  /**
  *  Adds all infected IDs into a new ArrayList called infectedIds
  *
  *  @param ids the arraylist of all infected people
  *  @return true, false based on if the adding every component of the
  *  array went successfully.
  */
  public boolean addInfectedIds(ArrayList<Integer> ids){
    //makes sure we don't receive a null parameter
    if(ids == null){
      return false;
    }
    for(int i = 0; i < ids.size(); i++){
      this.infectedIds.add(ids.get(i));
    }
    if(infectedIds.size() == ids.size()){
      return true;
    }
    return false;
  }

  /**
  *  Deep copies all of the infected ID arraylist into a new infected
  *  IDs arraylist.
  *
  *  @return newInfectedIds arraylist of the infected IDs
  */
  public ArrayList<Integer> getInfectedIds(){
    ArrayList<Integer> newInfectedIds = new ArrayList<Integer>();
    for(int i = 0; i < infectedIds.size(); i++){
      //copies each element at a time to newInfectedIds
      newInfectedIds.add(infectedIds.get(i));
    }
    return newInfectedIds;
  }
}
