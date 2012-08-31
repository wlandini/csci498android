package csci498.wlandini.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

public class LunchListActivity extends Activity {
    Restaurant r = new Restaurant();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener(){
		
		public void onClick(View v) {
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			//This is how you create the radio buttons without using the xml file
//			RadioButton rb1 = new RadioButton(null);
//			RadioButton rb2 = new RadioButton(null);
//			RadioButton rb3 = new RadioButton(null);
//			rb1.setText("Take-Out");
//			rb2.setText("Sit-Down");
//			rb3.setText("Delivery");
//			types.addView(rb1);
//			types.addView(rb2);
//			types.addView(rb3);
			
			switch(types.getCheckedRadioButtonId()){
				case R.id.sit_down:
					r.setType("sit_down");
					break;
				
				case R.id.take_out:
					r.setType("take_out");
					break;
					
				case R.id.delivery:
					r.setType("delivery");
					break;
			}
			
		}
	};
}