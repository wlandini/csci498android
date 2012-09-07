package csci498.wlandini.lunchlist;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TabActivity;
import android.widget.TabHost;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView;

public class LunchListActivity extends TabActivity {
    List<Restaurant> model = new ArrayList<Restaurant>();
    List<String> addresses = new ArrayList<String>();
    AutoCompleteTextView adressACTV;
	RestaurantAdapter adapter = null;
	ArrayAdapter<String> adapter2 = null;
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        name = (EditText)findViewById(R.id.name);
        address = (EditText)findViewById(R.id.addr);
        types = (RadioGroup)findViewById(R.id.types);
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        ListView list = (ListView)findViewById(R.id.restaurants);
        list.setOnItemClickListener(onListClick);
        AutoCompleteTextView adressACTV = (AutoCompleteTextView)findViewById(R.id.addr);
        adapter = new RestaurantAdapter();
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,addresses);
        list.setAdapter(adapter);
        adressACTV.setAdapter(adapter2);
        //s.setAdapter(adapter);
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        getTabHost().addTab(spec);
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
    }
    
    private View.OnClickListener onSave = new View.OnClickListener(){
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			EditText name = (EditText)findViewById(R.id.name);
			AutoCompleteTextView adressACTV = (AutoCompleteTextView)findViewById(R.id.addr);
			r.setName(name.getText().toString());
			r.setAddress(adressACTV.getText().toString());
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
		private ImageView icon = null;
		
		RestaurantHolder(View row){
			name = (TextView)row.findViewById(R.id.title);
			address = (TextView)row.findViewById(R.id.address);
			icon = (ImageView)row.findViewById(R.id.icon);
		}
		void populateFrom(Restaurant r){
			name.setText(r.getName());
			address.setText(r.getAddress());
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
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener(){
		public void onItemClick(AdapterView<?> parent,View view, int position, long id){
			Restaurant r = model.get(position);
			name.setText(r.getName());
			address.setText(r.getAddress());
			getTabHost().setCurrentTab(1);
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
}