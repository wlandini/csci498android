package csci498.wlandini.lunchlist;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import csci498.wlandini.lunchlist.R.drawable;

public class LunchListActivity extends TabActivity {
  List<Restaurant> model=new ArrayList<Restaurant>();
  RestaurantAdapter adapter=null;
  EditText name = null;
  EditText address = null;
  EditText notes = null;
  RadioGroup types = null;
  Restaurant current = null;
  TabHost th;
  int progress = 0;
  Handler h;
  boolean isDetails = false;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_PROGRESS);
    setContentView(R.layout.main);
    LinkedBlockingQueue lbq = new LinkedBlockingQueue();
    h = new Handler();
    name=(EditText)findViewById(R.id.name);
    address=(EditText)findViewById(R.id.addr);
    notes=(EditText)findViewById(R.id.notes);
    types=(RadioGroup)findViewById(R.id.types);
    Button save=(Button)findViewById(R.id.save);
    th = getTabHost();
    th.setOnTabChangedListener(new OnTabChangeListener() {
        public void onTabChanged(String arg0) { 
        	if(th.getCurrentTab() == 0){
        		isDetails = false;
        	}
        	else{
        		isDetails = true;
        	}
        }     
    });  
    
    save.setOnClickListener(onSave);
    
    ListView list = (ListView)findViewById(R.id.restaurants);
    
    adapter=new RestaurantAdapter();
    list.setAdapter(adapter);
    
    TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
    spec.setContent(R.id.restaurants);
    spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
    getTabHost().addTab(spec);
    spec=getTabHost().newTabSpec("tag2");
    spec.setContent(R.id.details);
    spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
    getTabHost().addTab(spec);
    getTabHost().setCurrentTab(0);
 
    list.setOnItemClickListener(onListClick);
  }
  
  private Runnable longTask = new Runnable(){
	  public void run(){
		  for(int i = 0; i < 20; i++){
			  doSomeLongWork(500);
		  }
		  h.post(new Runnable(){
			  public void run(){
				  setProgressBarVisibility(false);
				  if(getTabHost().getCurrentTab() == 0){
					  getTabHost().setCurrentTab(1);
				  }
				  else if(getTabHost().getCurrentTab() == 1){
					  getTabHost().setCurrentTab(0);
				  }
			  }
		  });
//		  runOnUiThread(new Runnable(){
//			  public void run(){
//				  setProgressBarVisibility(false);
//				  if(getTabHost().getCurrentTab() == 0){
//					  getTabHost().setCurrentTab(1);
//				  }
//				  else if(getTabHost().getCurrentTab() == 1){
//					  getTabHost().setCurrentTab(0);
//				  }
//			  }
//		  });
	  }
  };
  
  private void doSomeLongWork(final int incr){
	  h.post(new Runnable(){
		  public void run(){
			  progress += incr;
			  setProgress(progress);
		  }
	  });
//	  runOnUiThread(new Runnable(){
//		  public void run(){
//			  progress += incr;
//			  setProgress(progress);
//		  }
//	  });
	  SystemClock.sleep(250);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
	if(isDetails){
		new MenuInflater(this).inflate(R.menu.option, menu);
	}
	else{
		new MenuInflater(this).inflate(R.menu.option2, menu);
	}
  
    return(super.onCreateOptionsMenu(menu));
  }
  
  public boolean onPrepareOptionsMenu(Menu menu){
	  menu.clear();
	  if(isDetails){
			new MenuInflater(this).inflate(R.menu.option, menu);
		}
		else{
			new MenuInflater(this).inflate(R.menu.option2, menu);
		}
	  return(super.onCreateOptionsMenu(menu));
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.toast) {
      String message = "No restaurant selected";
      
      if (current != null) {
        message=current.getNotes();
      }
      new AlertDialog.Builder(this).setTitle("Notes on restaurant").setMessage(message).setNeutralButton("Close",null).show();
      //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
      return(true);
    }
    else if(item.getItemId() == R.id.item1){
    	getTabHost().setCurrentTab(0);
    }
    else if(item.getItemId() == R.id.item2){
    	getTabHost().setCurrentTab(1);
    }
    else if(item.getItemId() == R.id.run){
    	setProgressBarVisibility(true);
    	progress = 0;
    	new Thread(longTask).start();
    }
    return(super.onOptionsItemSelected(item));
  }
  
  private View.OnClickListener onSave=new View.OnClickListener() {
    public void onClick(View v) {
      current = new Restaurant();
      current.setName(name.getText().toString());
      current.setAddress(address.getText().toString());
      current.setNotes(notes.getText().toString());
      
      switch (types.getCheckedRadioButtonId()) {
        case R.id.sit_down:
          current.setType("sit_down");
          break;
          
        case R.id.take_out:
          current.setType("take_out");
          break;
          
        case R.id.delivery:
          current.setType("delivery");
          break;
      }
      
      adapter.add(current);
    }
  };
  
  private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {
    public void onItemClick(AdapterView<?> parent,
                             View view, int position,
                             long id) {
      current=model.get(position);
      
      name.setText(current.getName());
      address.setText(current.getAddress());
      notes.setText(current.getNotes());
      
      if (current.getType().equals("sit_down")) {
        types.check(R.id.sit_down);
      }
      else if (current.getType().equals("take_out")) {
        types.check(R.id.take_out);
      }
      else {
        types.check(R.id.delivery);
      }
      
      getTabHost().setCurrentTab(1);
    }
  };
  
  class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    RestaurantAdapter() {
      super(LunchListActivity.this, R.layout.row, model);
    }
    
    public View getView(int position, View convertView,
                        ViewGroup parent) {
      View row=convertView;
      RestaurantHolder holder=null;
      
      if (row==null) {                          
        LayoutInflater inflater=getLayoutInflater();
        
        row=inflater.inflate(R.layout.row, parent, false);
        holder=new RestaurantHolder(row);
        row.setTag(holder);
      }
      else {
        holder=(RestaurantHolder)row.getTag();
      }
      
      holder.populateFrom(model.get(position));
      
      return(row);
    }
  }
  
  static class RestaurantHolder {
    private TextView name=null;
    private TextView address=null;
    private ImageView icon=null;
    
    RestaurantHolder(View row) {
      name=(TextView)row.findViewById(R.id.title);
      address=(TextView)row.findViewById(R.id.address);
      icon=(ImageView)row.findViewById(R.id.icon);
    }
    
    void populateFrom(Restaurant r) {
      name.setText(r.getName());
      address.setText(r.getAddress());
  
      if (r.getType().equals("sit_down")) {
        icon.setImageResource(R.drawable.ball_red);
      }
      else if (r.getType().equals("take_out")) {
        icon.setImageResource(R.drawable.ball_yellow);
      }
      else {
        icon.setImageResource(R.drawable.ball_green);
      }
    }
  }
}