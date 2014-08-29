package vssnake.intervaltraining.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import vssnake.intervaltraining.R;

/**
 * Created by unai on 27/08/2014.
 */
public class IntervalStaticData {

    public static ArrayList<IntervalData> intervalData;
    public static void createData(Context context){
        intervalData = new ArrayList<IntervalData>();


        intervalData.add(new IntervalData(
                R.drawable.hiitalactic_easy,
                context.getString(R.string.interval_HiitAlactic_Beginner_name),
                context.getString(R.string.interval_HiitAlactic_Beginner_description),
                3,20,90));
        intervalData.add(new IntervalData(
                R.drawable.hiitalactic_easy_med,
                context.getString(R.string.interval_HiitAlactic_Begin_Intermediate_name),
                context.getString(R.string.interval_HiitAlactic_Begin_Intermediate_description),
                6,20,90));
        intervalData.add(new IntervalData(
                R.drawable.hiitalactic_med,
                context.getString(R.string.interval_HiitAlactic_Intermediate_name),
                context.getString(R.string.interval_HiitAlactic_Intermediate_description),
                6,20,60));
        intervalData.add(new IntervalData(
                R.drawable.hiitalactic_med_hard,
                context.getString(R.string.interval_HiitAlactic_Inter_Advanced_name),
                context.getString(R.string.interval_HiitAlactic_Inter_Advanced_description),
                5,20,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitalactic_hard,
                context.getString(R.string.interval_HiitAlactic_Advanced_name),
                context.getString(R.string.interval_HiitAlactic_Advanced_description),
                8,20,30));

        intervalData.add(new IntervalData(
                R.drawable.hiitendurance_easy,
                context.getString(R.string.interval_HiitEndurance_Beginner_name),
                context.getString(R.string.interval_HiitEndurance_Beginner_description),
                3,50,90));
        intervalData.add(new IntervalData(
                R.drawable.hiitendurance_easy_med,
                context.getString(R.string.interval_HiitEndurance_Begin_Intermediate_name),
                context.getString(R.string.interval_HiitEndurance_Begin_Intermediate_description),
                6,50,90));
        intervalData.add(new IntervalData(
                R.drawable.hiitendurance_med,
                context.getString(R.string.interval_HiitEndurance_Intermediate_name),
                context.getString(R.string.interval_HiitEndurance_Intermediate_description),
                6,50,60));
        intervalData.add(new IntervalData(
                R.drawable.hiitendurance_med_hard,
                context.getString(R.string.interval_HiitEndurance_Inter_Advanced_name),
                context.getString(R.string.interval_HiitEndurance_Inter_Advanced_description),
                9,50,60));
        intervalData.add(new IntervalData(
                R.drawable.hiitendurance_hard,
                context.getString(R.string.interval_HiitEndurance_Advanced_name),
                context.getString(R.string.interval_HiitEndurance_Advanced_description),
                12,50,60));



        intervalData.add(new IntervalData(
                R.drawable.hiitpower_easy,
                context.getString(R.string.interval_HiitPower_Beginner_name),
                context.getString(R.string.interval_HiitPower_Beginner_description),
                3,5,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitpower_easy_med,
                context.getString(R.string.interval_HiitPower_Begin_Intermediate_name),
                context.getString(R.string.interval_HiitPower_Begin_Intermediate_description),
                5,5,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitpower_med,
                context.getString(R.string.interval_HiitPower_Intermediate_name),
                context.getString(R.string.interval_HiitPower_Intermediate_description),
                9,5,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitpower_med_hard,
                context.getString(R.string.interval_HiitPower_Inter_Advanced_name),
                context.getString(R.string.interval_HiitPower_Inter_Advanced_description),
                12,5,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitpower_hard,
                context.getString(R.string.interval_HiitPower_Advanced_name),
                context.getString(R.string.interval_HiitPower_Advanced_description),
                15,5,30));




        intervalData.add(new IntervalData(
                R.drawable.hiitsprint_easy,
                context.getString(R.string.interval_HiitSprint_Beginner_name),
                context.getString(R.string.interval_HiitSprint_Beginner_description),
                3,10,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitsprint_easy_med,
                context.getString(R.string.interval_HiitSprint_Begin_Intermediate_name),
                context.getString(R.string.interval_HiitSprint_Begin_Intermediate_description),
                5,10,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitsprint_med,
                context.getString(R.string.interval_HiitSprint_Intermediate_name),
                context.getString(R.string.interval_HiitSprint_Intermediate_description),
                7,10,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitsprint_med_hard,
                context.getString(R.string.interval_HiitSprint_Inter_Advanced_name),
                context.getString(R.string.interval_HiitSprint_Inter_Advanced_description),
                9,10,30));
        intervalData.add(new IntervalData(
                R.drawable.hiitsprint_hard,
                context.getString(R.string.interval_HiitSprint_Advanced_name),
                context.getString(R.string.interval_HiitSprint_Advanced_description),
                11,10,30));


        intervalData.add(new IntervalData(
                R.drawable.tabata,
                context.getString(R.string.interval_tabata_name),
                context.getString(R.string.interval_tabata_description),
                8,20,10));









    }

    public static ArrayList<ListIntervalData> toList(Context context){
        if (intervalData== null){
            createData(context);
        }
        ArrayList<ListIntervalData> data = new ArrayList<ListIntervalData>();

        for (int i = 0;i<intervalData.size();i++){
            IntervalData inter = intervalData.get(i);
            data.add(new ListIntervalData(i,inter.getIcon(),inter.getmName(),
                    inter.getmDescription()));
        }

       return data;
    }
    public static class IntervalData{
        private int mIcon;
        private String mName;
        private String mDescription;
        private int mTotalIntervals;
        private int mTimeDoing;
        private int mTimeResting;

        public IntervalData(int icon, String name,String description,int totalIntervals,
                            int timeDoing,
                             int timeResting){
            mIcon = icon;
            mName = name;
            mDescription = description;
            mTotalIntervals = totalIntervals;
            mTimeDoing = timeDoing;
            mTimeResting = timeResting;
        }

        public String getmName() {
            return mName;
        }

        public String getmDescription() {
            return mDescription;
        }

        public int getmTotalIntervals() {
            return mTotalIntervals;
        }

        public int getmTimeDoing() {
            return mTimeDoing;
        }

        public int getmTimeResting() {
            return mTimeResting;
        }

        public int getIcon() {
            return mIcon;
        }
    }

    /**
     * Created by unai on 26/08/2014.
     */
    public static class ListIntervalData implements Parcelable {

        private int mIcon;
        private String mName;
        private String mDescription;
        private long mID;

        public ListIntervalData(int ID,int icon, String name, String description){
            setID(ID);
            setIcon(icon);
            setName(name);
            setDescription(description);
        }
        public ListIntervalData(Parcel in){
            setID(in.readLong());
            setIcon(in.readInt());
            setName(in.readString());
            setDescription(in.readString());
        }

        @Override
        public int describeContents() {

            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(getID());
            dest.writeInt(getIcon());
            dest.writeString(getName());
            dest.writeString(getDescription());

        }

        public static final Parcelable.Creator<ListIntervalData> CREATOR =
                new Parcelable.Creator<ListIntervalData>() {

                    public ListIntervalData createFromParcel(Parcel in) {
                        return new ListIntervalData(in);
                    }

                    public ListIntervalData[] newArray(int size) {
                        return new ListIntervalData[size];
                    }
                };

        public int getIcon() {
            return mIcon;
        }

        public void setIcon(int mIcon) {
            this.mIcon = mIcon;
        }

        public String getName() {
            return mName;
        }

        public void setName(String mName) {
            this.mName = mName;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String mDescription) {
            this.mDescription = mDescription;
        }

        public long getID() {
            return mID;
        }

        public void setID(long mID) {
            this.mID = mID;
        }
    }
}
