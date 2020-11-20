/**
*  NAME: Stanley Hahm
*  ID: A14609365
*  EMAIL: sthahm@ucsd.edu
*
*  For in this code, we are trying to make a class that would do
*  take care of the other functions that the previous classes couldn't.
*  It would manage changing the location, ID, and exposure of all the
*  students.
*/
import java.util.Random;
import java.util.ArrayList;
import java.util.*;

/**
*  This class sets the new location of each student. It also can update
*  the ID of every student. It would then proceed to check if there are
*  positive cases and get the recent contacts of the postive patient.
*  It would then assess if the student would be need to quarantine or not
*  based on their exposure to the infected student.
*/
public class Student{
  public int id;
  public int location;
  public boolean covidPositive;
  public boolean inQuarantine;
  public ArrayList<Integer> usedIds;
  public ArrayList<ContactInfo> contactHistory;

  /**
  *  Just sets the variables to initial values or booleans. Also sets
  *  two variables (usedIds & contactHistory) to ArrayLists.
  */
  public Student(){
    id = -1;
    location = -1;
    covidPositive = false;
    inQuarantine = false;
    usedIds = new ArrayList<Integer>();
    contactHistory = new ArrayList<ContactInfo>();
  }

  /**
  *  Checks if location is valid and then if it is, sets the location
  *  variable to the new updated one.
  *
  *  @param newLocation the updated location of the student
  *  @return true, false based on if the newlocation is valid and if
  *  the student is not in quarantine.
  */
  public boolean setLocation(int newLocation){
    //inQuarantine has to be false or else if it was true then location
    //would go nowhere
    if((newLocation >= 0) && (inQuarantine == false)){
      location = newLocation;
      return true;
    }
    else{
      return false;
    }
  }

  /**
  *  Creates a new random ID for the students
  */
  public void updateId(){
    Random rand = new Random();

    id = rand.nextInt(Integer.MAX_VALUE);
    usedIds.add(id);
  }

  /**
  *  Adds in the new info using the class ContactInfo
  *
  *  @param info includes all the parameters of ContactInfo class
  *  @return true, false based on if info is valid
  */
  public boolean addContactInfo(ContactInfo info){
    //has to confirm info is valid using the isValid method
    //within ContactInfo class
    if((info != null) && (info.isValid() == true)){
      contactHistory.add(info);
      return true;
    }
    else{
      return false;
    }
  }

  /**
  *  Updates IDs w/ infected IDs using class Server
  *
  *  @param server includes all the parameters of Server class
  *  @return true, false based on if server is valid
  */
  public boolean uploadAllUsedIds(Server server){
    if(server != null){
      server.addInfectedIds(usedIds);
      //checks to see if the addInfectedIds method in the Server class worked
      if(server.addInfectedIds(usedIds) == true){
        return true;
      }
      else{
        return false;
      }
    }
    else{
      return false;
    }
  }

  /**
  *  Sets covidPositive and inQuarantine to true because student is positive.
  *  And uses prior method to update the server of all infected IDs
  *
  *  @param info includes all the parameters of ContactInfo class
  *  @return true, false based on if uploadAllUsedIds returns true
  */
  public boolean testPositive(Server server){
    covidPositive = true;
    inQuarantine = true;
    if(this.uploadAllUsedIds(server) == true){
      return true;
    }
    else{
      return false;
    }
  }

  /**
  *  Checks through all server IDs and picks out all infected IDs and
  *  puts them into a sublist.
  *
  *  @param server includes all the parameters of Server class
  *  @param fromTime includes how long it's been since in contact w/
  *  infected patient
  *  @return infectedSubListIds an arraylist from ContactInfo class that
  *  returns all the parameters of that class of that infected student
  */

  public ArrayList<ContactInfo> getRecentPositiveContacts(Server server,
    int fromTime){
      //prechecks to make sure variables are valid
      if((server == null) || (fromTime < 0)
        || (server.getInfectedIds() == null)){
          return null;
      }
      //this is a new ArrayList using ContactInfo
      ArrayList<ContactInfo> infectedSubListIds = new ArrayList<ContactInfo>();
      //this makes a new variable that gets the return Arraylist from
      //getInfectedIds
      ArrayList<Integer> newInfectedIds = server.getInfectedIds();
      for(int i = 0; i < contactHistory.size(); i++){
        if((contactHistory.get(i).used == false)
          && (newInfectedIds.contains(contactHistory.get(i).id))
          && (contactHistory.get(i).time >= fromTime)){
            infectedSubListIds.add(contactHistory.get(i));
          }
      }
      return infectedSubListIds;
    }

  /**
  *  Gets the recent positive contacts and checks if student has been
  *  potentially exposed or infected enough to the infected ID to the point
  *  where they have to quarantine.
  *
  *  @param server includes all the parameters of Server class
  *  @param fromTime includes how long it's been since in contact w/
  *  infected patient
  *  @param quarantineChoice this is if they have to quarantine or not
  *  @return 1, 0 based if the student has to quarantine or not
  */
  public int riskCheck(Server server, int fromTime, boolean quarantineChoice){
    //expose is the name of the arraylist that gets the return from
    //getRecentPositiveContacts
    ArrayList<ContactInfo> expose = getRecentPositiveContacts(server, fromTime);
    if(expose == null){
      return -1;
    }
    //this counts if the amount of times the student got exposed to infected
    //student
    int count_exposures = 0;
    for(int i = 0; i < expose.size(); i++){
      if((expose.contains(contactHistory.get(i)))
        && (expose.get(i).distance - contactHistory.get(i).distance <= 1)){
        contactHistory.get(i).used = true;
      }
      if(expose.contains(contactHistory.get(i))){
        count_exposures++;
      }
      //if the count exposures get to 3 or more, then the contactHistory used
      //becomes true
      if(count_exposures >= 3){
        contactHistory.get(i).used = true;
      }
      //if the contactHistory used is true, and if the quarantineChoice is true
      //then they must be in quarantine so inQuarantine is true
      if(contactHistory.get(i).used == true){
        if(quarantineChoice == true){
          this.inQuarantine = true;
        }
        return 1;
      }
    }
    //defaults to returning 0 if student isn't exposed to anything
    return 0;
  }
}
