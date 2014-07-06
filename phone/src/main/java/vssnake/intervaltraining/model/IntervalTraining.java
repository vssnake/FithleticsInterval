package vssnake.intervaltraining.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unai on 25/06/2014.
 */
public class IntervalTraining extends TrainingBase{

    List<IntervalExercises> intervalExercises;
    public IntervalTraining(){
        intervalExercises = new ArrayList<IntervalExercises>();
    }
    public enum TypeInterval{
        duration,
        repetitions
    }

    class IntervalExercises{
        public IntervalExercises(TypeInterval intervalType,Long idExercise ,int duration,int rest){
            this.intervalType = intervalType;
            this.idExercise = idExercise;
            this.duration = duration;
            this.rest = rest;
        }

        TypeInterval intervalType;
        Long idExercise;
        int duration; //In seconds
        int rest; // In secibds

    }


}
