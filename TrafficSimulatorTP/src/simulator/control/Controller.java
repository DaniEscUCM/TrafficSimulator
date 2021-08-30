package simulator.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator sim;
	private Factory<Event> fac;
	private File _in;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory)
	{
		if(sim.equals(null) || eventsFactory.equals(null)) {
			//sim.onError("No simulation or event factory has been found");
			throw new IllegalArgumentException("No simulation or event factory has been found");
		}
		else {
			this.sim=sim;
			this.fac=eventsFactory;			
		}
	}
	
	public void setIn(File in) {
		this._in=in;
	}
	
	public File getIn() {
		return this._in;
	}
	
	public void loadEvents() throws Exception  {				
		InputStream in=new FileInputStream(_in.getPath());
		JSONObject jo = new JSONObject(new JSONTokener(in));
		Event e;
		if(jo.has("events")) {
			for(int i=0;i<jo.getJSONArray("events").length();i++) {
				e=fac.createInstance(jo.getJSONArray("events").getJSONObject(i));
				sim.addEvent(e);
			}
		}
		else {
			//sim.onError("The input doesn't match the structure");
			throw new IllegalArgumentException("The input doesn't match the structure");
		}
		in.close();
		
	}
	
	@SuppressWarnings("resource")
	public void run(int n, String _outFile) throws Exception {
		JSONObject aux=new JSONObject();
		if(_outFile!=null) {
			FileOutputStream out= new FileOutputStream(_outFile);	
			JSONObject jo=new JSONObject();			
			jo.put("states", new JSONArray());
			
			for(int i=0;i<n;i++) {
				sim.advance();
				aux=sim.report();
				if(out!=null) {
					jo.getJSONArray("states").put(aux);
				}
			}
			PrintStream p = new PrintStream(out);
			p.print(jo);
		}
		else {
			System.out.println("states");
			for(int i=0;i<n;i++) {
				sim.advance();
				aux=sim.report();
				System.out.println(aux);				
			}
		}
		
	}
	
	public void run(int n) throws Exception {
		for(int i=0;i<n;i++) {
			sim.advance();
		}
	}
	
	public void reset() {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o){
		sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		sim.addEvent(e);
	}
	
}
