package csci498.wlandini.lunchlist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LunchListActivity extends Activity {
    private static final int DATE_DIALOG_ID = 0;
    private static boolean DATE_SET = false;
	List<Restaurant> model = new ArrayList<Restaurant>();
    List<String> addresses = new ArrayList<String>();
    AutoCompleteTextView adressACTV;
	RestaurantAdapter adapter = null;
	ArrayAdapter<String> adapter2 = null;
	EditText name = null;
	EditText address = null;
	EditText date = null;
	RadioGroup types = null;
	ViewFlipper vf = null;
	private int mYear;
	private int mDay;
	private int mMonth;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        vf = (ViewFlipper)findViewById(R.id.viewflipper);
        //Set view to main.
        vf.setDisplayedChild(0);
        name = (EditText)findViewById(R.id.name);
        address = (EditText)findViewById(R.id.addr);
        date = (EditText)findViewById(R.id.date);
        types = (RadioGroup)findViewById(R.id.types);
        //Create save button
        Button save = (Button)findViewById(R.id.save);
        //Create button to flip to other view
        Button flipButton = (Button)findViewById(R.id.flipView);
        Button flipButton2 = (Button)findViewById(R.id.flipView2);
        flipButton2.setOnClickListener(onDetails);
        Button setDate = (Button)findViewById(R.id.setDate);
        setDate.setOnClickListener(onSetDate);
        flipButton.setOnClickListener(onList);
        save.setOnClickListener(onSave);
        ListView list = (ListView)findViewById(R.id.restaurants);
        list.setOnItemClickListener(onListClick);
        AutoCompleteTextView adressACTV = (AutoCompleteTextView)findViewById(R.id.addr);
        adapter = new RestaurantAdapter();
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,addresses);
        list.setAdapter(adapter);
        adressACTV.setAdapter(adapter2);
    }
    
    //When list button is clicked change to the list view. 
    private View.OnClickListener onList = new View.OnClickListener() {
		public void onClick(View v) {
			vf.setDisplayedChild(1);
		}
	};
	
	private View.OnClickListener onDetails = new View.OnClickListener() {
		public void onClick(View v) {
			vf.setDisplayedChild(0);	
		}
	};
	
	private View.OnClickListener onSetDate = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(DATE_DIALOG_ID);
			DATE_SET = true;
		}
	};
	
    private View.OnClickListener onSave = new View.OnClickListener(){
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			EditText name = (EditText)findViewById(R.id.name);
			AutoCompleteTextView adressACTV = (AutoCompleteTextView)findViewById(R.id.addr);
			r.setName(name.getText().toString());
			r.setAddress(adressACTV.getText().toString());
			if(DATE_SET == false){
				Calendar c = Calendar.getInstance();
				r.setDate(Integer.toString(c.MONTH) + "/" +  Integer.toString(c.DAY_OF_MONTH) + "/" + Integer.toString(c.YEAR));
			}
			else{
				r.setDate(Integer.toString(mMonth) + "/" + Integer.toString(mDay) + "/" + Integer.toString(mYear));
			}
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
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
	
	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter(){
			super(LunchListActivity.this,android.R.layout.simple_list_item_1,model);
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			View row = convertView;
			RestaurantHolder holder = null;
			if(row == null){
				LayoutInflater inflater = getLayoutInflater();
				if(getItemViewType(position) == 0){
					row = inflater.inflate(R.layout.row0, null);
				}
				else if(getItemViewType(position) == 1){
					row = inflater.inflate(R.layout.row1, null);
				}
				else{
					row = inflater.inflate(R.layout.row2, null);
				}
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			}
			else{
				holder = (RestaurantHolder)row.getTag();
			}
			holder.populateFrom(model.get(position));
			return(row);
		}
		
		public int getItemViewType(int position){
			if(model.get(position).getType() == "sit_down"){
				return 0;
			}
			else if(model.get(position).getType() == "take_out"){
				return 1;
			}
			else{
				return 2;			
			}
		}
		
		public int getViewTypeCount(){
			return 3;
		}
	}
	
	static class RestaurantHolder{
		private TextView name = null;
		private TextView address = null;
		private TextView date = null;
		private ImageView icon = null;
		
		RestaurantHolder(View row){
			name = (TextView)row.findViewById(R.id.title);
			address = (TextView)row.findViewById(R.id.address);
			date = (TextView)row.findViewById(R.id.date);
			icon = (ImageView)row.findViewById(R.id.icon);
		}
		void populateFrom(Restaurant r){
			name.setText(r.getName());
			address.setText(r.getAddress());
			date.setText(r.getDate());
			if(r.getType().equals("sit_down")){
				icon.setImageResource(R.drawable.ball_red);
				name.setTextColor(Color.RED);
			}
			else if(r.getType().equals("take_out")){
				icon.setImageResource(R.drawable.ball_yellow);
				name.setTextColor(Color.YELLOW);
			}
			else{
				icon.setImageResource(R.drawable.ball_green);
				name.setTextColor(Color.GREEN);
			}
		}
	}
	
	//When clicking an item in the list do this
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener(){
		public void onItemClick(AdapterView<?> parent,View view, int position, long id){
			Restaurant r = model.get(position);
			name.setText(r.getName());
			address.setText(r.getAddress());
			vf.setDisplayedChild(0);
			if(r.getType().equals("sit_down")){
				types.check(R.id.sit_down);
			}
			else if(r.getType().equals("take_out")){
				types.check(R.id.take_out);
			}
			else{
				types.check(R.id.delivery);
			}
		}
	};
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;  
                }
            };
        	@Override
        	protected Dialog onCreateDialog(int id) {
        		switch (id) {
        		case DATE_DIALOG_ID:
        			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
        					mDay);
        		}
        		return null;
        	}
}