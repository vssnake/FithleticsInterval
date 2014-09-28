package vssnake.intervaltraining.interval;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.utils.StacData;

public class IntervalEditorActivity extends Activity {

    Button btnSave;
    Button btnCancel;
    TextView txtName;
    TextView txtDescription;
    TextView txtRestTime;
    TextView txtEffortTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_editor);
        btnSave = (Button) findViewById(R.id.intervalEditor_btnOK);
        btnCancel = (Button) findViewById(R.id.intervalEditor_btnBack);
        txtName = (TextView) findViewById(R.id.interval_editor_name);
        txtDescription= (TextView) findViewById(R.id.interval_editor_description);
        txtRestTime= (TextView) findViewById(R.id.intervalEditor_secondsRest);
        txtEffortTime= (TextView) findViewById(R.id.intervalEditor_secondsEffort);
        initialize();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interval_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initialize(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    Intent intent = IntervalEditorActivity.this.getIntent();
                    intent.putExtra(StacData.IntervalEditor.nameKey,txtName.getText());
                    intent.putExtra(StacData.IntervalEditor.descriptionKey,txtDescription.getText());
                    intent.putExtra(StacData.IntervalEditor.secondsEffortKey,txtEffortTime.getText());
                    intent.putExtra(StacData.IntervalEditor.secondsRestKey,txtRestTime.getText());
                    IntervalEditorActivity.this.setResult(RESULT_OK,intent);
                    finish();
                }else{
                    CharSequence text = "Failed!";
                    Toast.makeText(IntervalEditorActivity.this,text,Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean check(){
        boolean checked = true;
        String name = txtName.getText().toString();
        String description = txtDescription.getText().toString();
        String effort = txtEffortTime.getText().toString();
        String rest = txtRestTime.getText().toString();
        if (name.isEmpty()){
            txtName.setError("Name not should be blank");

            checked = false;
        }else{
            txtName.setError(null);
        }
        if (description.isEmpty()){
           // txtDescription.setBackgroundColor(0xffffc330);
            txtDescription.setError("Description not should be blank");
            checked = false;

        }else{

           txtDescription.setError(null);

        }
        if (effort.isEmpty()){

            txtEffortTime.setError("Effort Time not should be blank");
            checked = false;
        }else{
            txtEffortTime.setError(null);
        }
        if (rest.isEmpty()){

            txtRestTime.setError("Rest Time not should be blank");
            checked = false;
        }else{
            txtRestTime.setError(null);
        }
        return checked;
    }
}
