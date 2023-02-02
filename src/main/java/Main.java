import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


// I assume:
//  - Date range to propose alternatives: 7 days
//  - world population: 10.000B
//  - number of big cities = 10K
//  - city population ~ 1M
//  - doctors per city ~ 8K
//  - specialities per country ~ 50
//  - slots per doctor per week ~ 20
//  So database functions should return number of elements:
//      findDoctorSlotsByCityAndSpeciality -> (8k / 50) * 20 = 3200
//      findDoctorAppointmentsByCityAndSpecialityAndDateRange -> (8k / 50) * 20 = 3200
//  So calculateAvailableAppointments cost -> 3200 * 20. It's x20 because to find appointmens I use a map (it should be ~ 20 appointments per week per doctor)
//  All figures should be reduced a lot since not everyone uses the platform. Just showing as a demo.
//  The only difference with data model attached is I've deleted the countries and used specialities within Doctor object to simplify

//THIS IS PSEUDOCODE. NOT FOLLOWING JAVA BEST PRACTICES. NOT WORKING.
public class Main {

    final Long DAYS = 7L;

    //Suggest to the user alternatives during next 7 days for same city and speciality
    public List<Appointment> suggestAlternatives(Appointment appointment) {

        List<Slot> slots =
                findDoctorSlotsByCityAndSpeciality(appointment.doctor.city, appointment.doctor.speciality);

        Map<Integer, List<Appointment>> appointments =
                findDoctorAppointmentsByCityAndSpecialityAndDateRange(appointment.doctor.city, appointment.doctor.speciality, DAYS);

        return calculateAvailableAppointments(slots, appointments);
    }

    private List<Appointment> calculateAvailableAppointments(List<Slot> slots, Map<Integer, List<Appointment>> appointmentsMap){
        List<Appointment> result = new ArrayList<>();
        for(Slot slot: slots){
            if( isSlotFree(slot, appointmentsMap) ){
                result.add(buildAppointMent(slot));
            }
        }
        return result;
    }

    private boolean isSlotFree(Slot slot, Map<Integer, List<Appointment>> appointmentsMap){
        return !appointmentsMap
                .get(slot.doctor.id)
                .stream()
                .anyMatch(a -> overlaps(slot, a));
    }

    private boolean overlaps(Slot slot, Appointment appointment ){

        final int fromDayMinutes = toDayMinutes(appointment.datetime);
        final int toDayMinutes = fromDayMinutes + appointment.visitSize;

        final int endSlotTime = slot.time + slot.doctor.visitTime;

        return slot.weekDay == toWeekDay(appointment.datetime)
                && (
                    (fromDayMinutes < slot.time && slot.time < toDayMinutes)
                    ||
                    (fromDayMinutes < endSlotTime && endSlotTime < toDayMinutes)
        );

    }

    private Appointment buildAppointMent(Slot slot){
        Appointment result = new Appointment();
        result.visitSize = slot.doctor.visitTime; //Doctor's visitTime may change. But we set one for the appointment
        result.doctor = slot.doctor;
        result.datetime = toDate(slot.weekDay);
        return result;
    }

    private Date addMinutes(final Date startDate, int minutes){
        return new Date(startDate.getTime() + (minutes * 60 * 1000));
    }

    private int toWeekDay(Date date){
        //TODO: Return weed day. From 1..7
        return 0;
    }

    private int toDayMinutes(Date date){
        //TODO: Return minute day. From 0..1440 (1440=24 hours * 60 min)
        return 0;
    }

    private Date toDate(int weekDay){
        //TODO:
        return null;
    }


    private List<Slot> findDoctorSlotsByCityAndSpeciality(City city, String speciality){
        /* JUST PSEUDOCODE. Represents DB call. Could be cached
                "select s.* " +
                "from slot s " +
                "inner join doctor d on d.id = s.doctor_id " +
                "where d.speciality = :speciality and d.city = :city ";
        */
        return null;
    }

    private Map<Integer, List<Appointment>> findDoctorAppointmentsByCityAndSpecialityAndDateRange(
            City city, String speciality, Long days){
        /* JUST PSEUDOCODE. Represents DB
            select a.*
            from appointments a
            inner join doctor d on d.id = a.doctor_id
            where d.speciality = :speciality and d.city = :city  a.date BETWEEN now() and now() + :days
         */
        //DATAbase result converted to map before returning.
        return null;
    }


}
