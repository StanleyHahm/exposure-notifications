import java.util.Random;
import java.util.ArrayList;
import java.util.*;

public class Student{
  public int id;
  public int location;
  public boolean covidPositive;
  public boolean inQuarantine;
  public ArrayList<Integer> usedIds;
  public ArrayList<ContactInfo> contactHistory;

  public Student(){
    id = -1;
    location = -1;
    covidPositive = false;
    inQuarantine = false;
    usedIds = new ArrayList<Integer>();
    contactHistory = new ArrayList<ContactInfo>();
  }

  public boolean setLocation(int newLocation){
    if((newLocation >= 0) && (inQuarantine == false)){
      location = newLocation;
      return true;
    }
    else{
      return false;
    }
  }

  public void updateId(){
    Random rand = new Random();

    id = rand.nextInt(Integer.MAX_VALUE);
    usedIds.add(id);
  }

  public boolean addContactInfo(ContactInfo info){
    if((info != null) && (info.isValid() == true)){
      contactHistory.add(info);
      return true;
    }
    else{
      return false;
    }
  }

  public boolean uploadAllUsedIds(Server server){
    if(server != null){
      server.addInfectedIds(usedIds);
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



  public ArrayList<ContactInfo> getRecentPositiveContacts(Server server,
    int fromTime){
      if((server == null) || (fromTime < 0)
        || (server.getInfectedIds() == null)){
          return null;
      }
      ArrayList<ContactInfo> infectedSubListIds = new ArrayList<ContactInfo>();
      ArrayList<Integer> newInfectedIds = server.getInfectedIds();
      for(int i = 0; i < contactHistory.size(); i++){
        if((contactHistory.get(i).used == false)
          && (newInfectedIds.contains(contactHistory.get(i).id))
          && (contactHistory.get(i).time >= fromTime)){
            //System.out.println(Arrays.toString(contactHistory.toArray()));
            infectedSubListIds.add(contactHistory.get(i));
          }
      }
      return infectedSubListIds;
    }


  public int riskCheck(Server server, int fromTime, boolean quarantineChoice){
    ArrayList<ContactInfo> expose = getRecentPositiveContacts(server, fromTime);
    if(expose == null){
      return -1;
    }
    int count = 0;
    for(int i = 0; i < expose.size(); i++){
      if((expose.contains(contactHistory.get(i)))
        && (expose.get(i).distance - contactHistory.get(i).distance <= 1)){
        contactHistory.get(i).used = true;
      }
      if(expose.contains(contactHistory.get(i))){
        count++;
      }
      if(count >= 3){
        contactHistory.get(i).used = true;
      }
      if(contactHistory.get(i).used == true){
        if(quarantineChoice == true){
          this.inQuarantine = true;
        }
        return 1;
      }
    }
    return 0;
  }


  public static void main(String [] args){
    Student test = new Student();
    int newLocation = 10;
    boolean test_location = test.setLocation(newLocation);

    test.updateId();

    //HOW DO I TEST WITH INFO AND CONTACTINFO
    int id = 10;
    int distance = 10;
    int time = 10;
    boolean used = true;
    ContactInfo test2 = new ContactInfo(id, distance, time);
    boolean test_contact = test.addContactInfo(test2);
    //System.out.println(test_contact);

    ArrayList<Integer> infectedIds = new ArrayList<Integer>();
    infectedIds.add(3);
    Server test3 = new Server();
    boolean test_infected = test.uploadAllUsedIds(test3);
    //System.out.println(test_infected);
    boolean test_positive = test.testPositive(test3);
    //System.out.println(test_positive);

    int fromTime = 10;
    Student s = new Student();
    ArrayList<ContactInfo> result = s.getRecentPositiveContacts(test3,10);
    boolean test_s = s.addContactInfo(test2);
    System.out.println(result);
    ArrayList<ContactInfo> result2 = s.getRecentPositiveContacts(test3,10);
    System.out.println(result2);
  }

}
