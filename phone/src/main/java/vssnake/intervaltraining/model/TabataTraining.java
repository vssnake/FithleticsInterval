package vssnake.intervaltraining.model;

import java.util.List;

/**
 * Created by unai on 25/06/2014.
 */
public class TabataTraining extends IntervalTraining {

    boolean addExercise(Exercise exercise){
        if (intervalExercises.size() < 8) {
            intervalExercises.add(new IntervalExercises(TypeInterval.duration, exercise.idExercise, 20, 10));
            return true;
        }else{
           return false;
        }
    }
    void addExercise(List<Exercise> exercises){

    }
}
