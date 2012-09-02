package csci498.wlandini.lunchlist;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;

public class LunchListActivity extends Activity {
    List<Restaurant> model = new ArrayList<Restaurant>();
    List<String> addresses = new ArrayList<String>();
    AutoCompleteTextView address;
	ArrayAdapter<Restaurant> adapter = null;
	ArrayAdapter<String> adapter2 = null;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        ListView list = (ListView)findViewById(R.id.restaurants);
        AutoCompleteTextView address = (AutoCompleteTextView)findViewById(R.id.addr);
        //Spinner s = (Spinner)findViewById(R.id.restaurants);
        adapter = new ArrayAdapter<Restaurant>(this,android.R.layout.simple_list_item_1,model);
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,addresses);
        //adapter = new ArrayAdapter<Restaurant>(this,android.R.layout.simple_spinner_dropdown_item,model);
        list.setAdapter(adapter);
        address.setAdapter(adapter2);
        //s.setAdapter(adapter);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener(){
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			EditText name = (EditText)findViewById(R.id.name);
			//EditText address = (EditText)findViewById(R.id.addr);
			AutoCompleteTextView address = (AutoCompleteTextView)findViewById(R.id.addr);
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
			adapter.add(r);
			adapter2.add(r.getAddress());
		}
	};
}