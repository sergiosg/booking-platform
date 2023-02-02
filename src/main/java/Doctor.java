import java.util.List;

public class Doctor {
    Long id;
    String name;
    String speciality;
    List<Slot> slots;
    int visitTime; //in minutes
    City city;
}
