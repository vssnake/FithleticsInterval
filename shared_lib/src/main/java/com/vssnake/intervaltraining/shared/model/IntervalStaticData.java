package com.vssnake.intervaltraining.shared.model;

import android.content.Context;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.vssnake.intervaltraining.shared.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by unai on 27/08/2014.
 */
public class IntervalStaticData {


    private static String TAG = "intervalStaticData";
    private static String keyFile = "intervalData";
    private static IntervalDataManager intervalDataManager;

    public static List<IntervalData> intervalData;

    public static List<IntervalData> getIntervalData(Context context){
        File fileDir =context.getFilesDir();

        if (intervalDataManager == null){
            intervalDataManager = IntervalDataManager.instance(keyFile,context);
            intervalData = intervalDataManager.ReadData();
            if (intervalData == null){
                createPrimaryData(context);
                intervalDataManager.setIntervalData(intervalData);
            }
        }

        return intervalData;
    }

    public static void createPrimaryData(Context context){
        intervalData = new ArrayList<IntervalData>();


        intervalData.add(new IntervalData(
                R.drawable.tabata,
                context.getString(R.string.interval_tabata_name),
                context.getString(R.string.interval_tabata_description),
                8,20,10));

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











    }

    public static ArrayList<ListIntervalData> toList(Context context){
        if (intervalData== null){
            createPrimaryData(context);
        }
        ArrayList<ListIntervalData> data = new ArrayList<ListIntervalData>();

        for (int i = 0;i<intervalData.size();i++){
            IntervalData inter = intervalData.get(i);
            data.add(new ListIntervalData(i,inter.getIcon(),inter.getmName(),
                    inter.getmDescription()));
        }

       return data;
    }
     static class IntervalDataManager{
        private String mNameFile;
        private File mFile;

         private static String TAG = "intervalStaticData";





        private Context mContext;

         IntervalDataManager(String nameFile,Context context){
            this.mContext = context;
             setFile(nameFile);


        }

        public static IntervalDataManager instance(String nameFile,Context context){
            return new IntervalDataManager(nameFile,context);
        }
        public void setFile(String nameFile ){
            if (nameFile != null){
                this.mNameFile = nameFile;
               // mFile  = new File(mContext.getFilesDir(), nameFile);



            }
        }


        public List<IntervalData> ReadData(){

            FileInputStream mInputFile;
            InputStreamReader mReader;
            JsonReader jsonReader;

            try {
                mInputFile = mContext.openFileInput(keyFile);
                mReader = new InputStreamReader(mInputFile,"UTF-8");
                jsonReader = new JsonReader(mReader);
                if (jsonReader != null){
                    try {
                        return readArray(jsonReader);

                    } catch (IOException e) {
                        Log.e(TAG,"onReadData " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                jsonReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        private List<IntervalData> readArray(JsonReader jsonReader) throws IOException {
            List<IntervalData> listIntervals = new ArrayList<IntervalData>();

            jsonReader.beginArray();

            while(jsonReader.hasNext()){
                listIntervals.add(readObject(jsonReader));
            }
            jsonReader.endArray();
            Log.d(TAG,"onReadArray");
            return listIntervals;
        }
        private IntervalData readObject(JsonReader jsonReader) throws IOException {
            IntervalData intervalData;
            String name = "";
            String description = "";
            int icon = 0;
            int timeDoing = 0;
            int timeResting = 0;
            int totalIntervals = 0;
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                String headerName = jsonReader.nextName();
                if (headerName.equals("keyName")){
                    name = jsonReader.nextString();
                }else if (headerName.equals("keyDescription")){
                    description = jsonReader.nextString();
                }else if (headerName.equals("keyIcon")){
                    icon = jsonReader.nextInt();
                }else if (headerName.equals("keyTimeDoing")){
                    timeDoing = jsonReader.nextInt();
                }else if (headerName.equals("keyTimeResting")){
                    timeResting = jsonReader.nextInt();
                }else if (headerName.equals("keyTotalIntervals")){
                    totalIntervals = jsonReader.nextInt();
                }else{
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            IntervalData interval = new IntervalData(icon,name,description,totalIntervals,timeDoing,
                    timeResting);
            Log.d(TAG,"onReadObject"  + name);
            return interval;
        }

        public void setIntervalData(List<IntervalData> listInterval){
            FileOutputStream mOutputFile;
            OutputStreamWriter mWriter;
            JsonWriter jsonWriter;

            try {
                mOutputFile = mContext.openFileOutput(keyFile,Context.MODE_PRIVATE);
                mWriter = new OutputStreamWriter(mOutputFile,"UTF-8");
                jsonWriter = new JsonWriter(mWriter);

                if (jsonWriter != null) {
                    try {
                        jsonWriter.setIndent("   ");
                        writeArray(listInterval,jsonWriter);
                        jsonWriter.close();
                    } catch (IOException e) {
                        Log.e(TAG,"onSetIntervalData " + e.getMessage());
                        e.printStackTrace();
                    }

                }
                jsonWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void writeArray(List<IntervalData> listInterval,JsonWriter jsonWriter) throws IOException {

            int size = listInterval.size();
            jsonWriter.beginArray();
            for (int x = 0; x < size; x++){
                IntervalData intervalData =  listInterval.get(x);
                writeObject(intervalData,jsonWriter);
            }
            jsonWriter.endArray();
            jsonWriter.close();
        }
        private void writeObject(IntervalData intervalData,JsonWriter jsonWriter) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("keyName").value(intervalData.getmName());
            jsonWriter.name("keyDescription").value(intervalData.getmDescription());
            jsonWriter.name("keyIcon").value(intervalData.getIcon());
            jsonWriter.name("keyTimeDoing").value(intervalData.getmTimeDoing());
            jsonWriter.name("keyTimeResting").value(intervalData.getmTimeResting());
            jsonWriter.name("keyTotalIntervals").value(intervalData.getmTotalIntervals());
            jsonWriter.endObject();
        }
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
