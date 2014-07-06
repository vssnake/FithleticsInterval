package vssnake.intervaltraining.model;

/**
 * Created by unai on 25/06/2014.
 */
public class Exercise {
    Long idExercise;
    String mname;
    String mdescription;
    TypeExercise mtypeExercise;

    public enum TypeExercise{
        lifting,
        distance,
        corporal
    }
}
