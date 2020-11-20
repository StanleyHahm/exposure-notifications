/**
*  NAME: Stanley Hahm
*  ID: A14609365
*  EMAIL: sthahm@ucsd.edu
*
*  For in this code, we are trying to create a class that would take in
*  all the variables and just set them to the appropriate variable so
*  that other classes can use this and test for exposure.
*  We're using a new concept of multiple classes.
*/
import java.util.Random;
import java.util.ArrayList;

/**
*  This class uses multiples methods. It sets the parameters to their
*  variables and confirms if they're valid. The instance variable id
*  is the ID of the infected student. Distance is the distance between
*  that student and the student of the one receiving the value. And time
*  is when contact happened.
*/

public class ContactInfo{

  public int id;
  public int distance;
  public int time;
  public boolean used;

  /**
  *  Takes in parameters and assigns it to variables using 'this'
  *
  *  @param id ID of the infected student
  *  @param distance length between two students
  *  @param time when contact happened
  */
  public ContactInfo(int id, int distance, int time){
    //uses this to make sure we send the values to the class variables
    this.used = false;
    this.id = id;
    this.distance = distance;
    this.time = time;
  }

  /**
  *  Checks if all parameters in previous method are valid
  *
  *  @return true, false based on if parameters are correct
  */
  public boolean isValid(){
    if((id >= 0) && (distance >= 0) && (time >= 0)){
      return true;
    }
    else{
      return false;
    }
  }
}
