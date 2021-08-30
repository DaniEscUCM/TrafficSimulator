package simulator.model;
import java.util.ArrayList;
import java.util.Collections;
/*
import java.util.ArrayList;
import java.util.Collections;*/
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject {
	
	private Junction origin;
	private Junction destiny;
	private int length;
	private int maxSpeed;
	private int acSpeed;
	private int contAlarm;
	private Weather environment;
	private int totalContamination;
	private List<Vehicle> _queue;
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,int contLimit, int length, Weather weather) {
		super(id);
		if(maxSpeed>0 && length>0) {
			if(contLimit>=0) {
				if(!srcJunc.equals(null)&&!destJunc.equals(null)&&!weather.equals(null)) {
					this.origin=srcJunc;
					this.destiny=destJunc;
					this.length=length;
					this.maxSpeed=maxSpeed;
					acSpeed=maxSpeed;
					contAlarm=contLimit;
					environment=weather;
					totalContamination=0;
					_queue=new SortedArrayList<Vehicle>();
					
				}else {
					throw new IllegalArgumentException("The values of source junction, destiny junction and weather are obligatory");
				}
				
			}else {
				throw new IllegalArgumentException("Contamination limit must be bigger or equal than cero");
			}
			
		}else {
			throw new IllegalArgumentException("Maximum speed and road lenght must be positive");
		}
	}
	
	void enter(Vehicle v) {
		if(v.getLocation()==0 && v.getAcSpeed()==0) {
			this._queue.add(v);
		}else {
			throw new IllegalArgumentException("Location and speed to entry the road must be zero");
		}
	}
	
	void exit(Vehicle v) {
		this._queue.remove(v);
	}
	
	void addContamination(int c) {
		if(c>=0) {
			totalContamination+=c;
		}
		else {
			throw new IllegalArgumentException("Negative contamination value");
		}
	}
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);

	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle i: this._queue) {
			i.setSpeed(calculateVehicleSpeed(i));
			i.advance(time);
		}
	}

	@Override
	public JSONObject report() {
		JSONObject junc = new JSONObject();
		junc.put("id",super._id);
		junc.put("speedlimit", this.acSpeed);
		junc.put("weather", this.environment );	
		junc.put("co2",this.totalContamination);
		List<String> list=new LinkedList<String>();//array de identificadores
		for(Vehicle i:this._queue) {
			list.add(i._id);		
		}
		junc.put("vehicles", list);
		return junc;
	}

	public Junction getOrigin() {
		return origin;
	}

	public Junction getDestiny() {
		return destiny;
	}

	int getLenght() {
		return length;
	}

	public Weather getEnvironment() {
		return environment;
	}

	public void setEnvironment(Weather environment) {
		this.environment = environment;
	}

	public int getTotalContamination() {
		return totalContamination;
	}

	public void setTotalContamination(int totalContamination) {
		this.totalContamination = totalContamination;
	}
	
	public boolean isContAlarm() {
		return contAlarm<totalContamination;
	}

	public int getAcSpeed() {
		return acSpeed;
	}

	public void setAcSpeed(int acSpeed) {
		this.acSpeed = acSpeed;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getContAlarm() {
		return contAlarm;
	}

	public int getLength() {
		return length;
	}
	
	
	
	

	public List<Vehicle> get_queue() {		
		return Collections.unmodifiableList(new ArrayList<>(_queue));
	}

	public String getId() {
		return super._id;
	}

	

}
