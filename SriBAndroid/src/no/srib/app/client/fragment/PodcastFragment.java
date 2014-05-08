package no.srib.app.client.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.srib.R;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.model.ProgramName;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class PodcastFragment extends Fragment {

	private PodcastMetadataDownloader podcastMetadataDownloader;
	private Spinner spinner = null;
	private GridView podcastGridView = null;
	private GridArrayAdapter gridViewAdapter = null;
	private StableArrayAdapter adapter = null;
	private ObjectMapper MAPPER = null;
	private HttpAsyncTask programTask;
	HttpAsyncTask podcastTask;


	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_podcast, container,
				false);
		
		TextView textView1 = (TextView) rootView
				.findViewById(R.id.label_program_name);
		textView1.setText("Program navn:");
		MAPPER = new ObjectMapper();
		
		spinner = (Spinner) rootView.findViewById(R.id.spinner1);
		podcastGridView = (GridView) rootView.findViewById(R.id.gridView_podcastList);
		
		programTask = new HttpAsyncTask(new GetProgramNames());
		podcastTask  = new HttpAsyncTask(new GetAllPodcast());
		
		String programTaskUrl = getResources().getString(R.string.getAllProgramNames);
		String podcastTaskUrl = getResources().getString(R.string.getAllPodcast);
		
		Log.i("Debug",programTaskUrl + " " + podcastTaskUrl);
		
		podcastTask.execute(podcastTaskUrl);
		programTask.execute(programTaskUrl);
		
		return rootView;
		
		
	}
	
	
	
	
	
	public interface PodcastMetadataDownloader{
		
		void update();
	}
	
	
	
	private class GetProgramNames implements HttpResponseListener{

		@Override
		public void onResponse(String response) {
			
			
			
			List<ProgramName> list = null;
			
			if(response != null){
			try {
				list = MAPPER.readValue(response, new TypeReference<List<ProgramName>>() {
				});
				
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			if(list != null){
			
			adapter = new StableArrayAdapter(getActivity(),list);
			
		
		
			//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
			spinner.setAdapter(adapter);
			spinner.setSelection(0, false);
			spinner.setOnItemSelectedListener(new ListViewItemClickListener());			
			
		
			}
			
		}
		
		
		
	}
	
	private class GetAllPodcast implements HttpResponseListener{

		@Override
		public void onResponse(String response) {
			List<Podcast> podcastList = null;
			Log.i("Debug","Podcast JSON downloaded " + response);
			if(response != null){
				try {
					podcastList  = MAPPER.readValue(response, new TypeReference<List<Podcast>>() {
					});
					
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			if(podcastList != null){
				
				gridViewAdapter = new GridArrayAdapter(podcastList, getActivity());
				podcastGridView.setAdapter(gridViewAdapter);
				podcastGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
				podcastGridView.setOnItemClickListener(new GridViewItemClickListener());
				
			}
			
		}
		
		
		
	}
	
	private class ListViewItemClickListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HttpAsyncTask podcast = new HttpAsyncTask(new GetAllPodcast());
			String url = getResources().getString(R.string.getAllPodcast);
			
			Log.i("deb",arg0.getItemIdAtPosition(arg2) + " ");
			podcast.execute(url + "/" + arg0.getItemIdAtPosition(arg2));
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}
		
		
		
		
	}
	
	private class GridViewItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			long id = arg0.getItemIdAtPosition(arg2);
			Log.i("debug","id er" + id);
		}

		
		
	}
	
	private class GridArrayAdapter extends BaseAdapter{

		List<Podcast> podcastList = new ArrayList<Podcast>();
		LayoutInflater inflater;
		Context context;
	
		
		public GridArrayAdapter(List<Podcast> list, Context context){
			this.podcastList = list;
			this.context = context;
			inflater = LayoutInflater.from(this.context);
		}
		
		@Override
		public int getCount() {
		
			return podcastList.size();
		}

		@Override
		public Podcast getItem(int position) {
			
			return podcastList.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return podcastList.get(position).getRefnr();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i("debug","denne blir kjørt");
			
			for(Podcast a : podcastList){
				Log.i("debug","podcast " + a.getProgram() + "  " + a.getRefnr() + " " + a.getCreatedate());
			}
			
			convertView = inflater.inflate(R.layout.podcast_grid_item, null);
			Podcast podcast = podcastList.get(position);
			TextView programNameTextView = (TextView) convertView.findViewById(R.id.label_gridViewItem_programname);
			String programName = podcast.getProgram();
			programNameTextView.setText(programName);
			
			TextView programNameDate = (TextView) convertView.findViewById(R.id.label_gridViewItem_date);
			int date = podcast.getCreatedate();
			programNameDate.setText(String.valueOf(date));
			
			
			return convertView;
		}
		
		
	}
	
	private class StableArrayAdapter extends BaseAdapter {

		List<ProgramName> programList = new ArrayList<ProgramName>();
		LayoutInflater inflater;
		Context context;
		
		public StableArrayAdapter(Context context,
				List<ProgramName> objects) {
			this.programList = (ArrayList<ProgramName>) objects;
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return programList.size();
		}

		@Override
		public ProgramName getItem(int position) {
		
			return programList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return programList.get(position).getDefnr();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			convertView = inflater.inflate(R.layout.podcastlist, null);
			
			TextView text = (TextView) convertView.findViewById(R.id.label_spinnerItem_name);
			text.setText(programList.get(position).getName());
			
			
			return convertView;
		}

		

	
	}
	
	
	
	
	
	
	
}


