package simulator.factories;

import org.json.JSONObject;
import simulator.model.Event;

public abstract class NewRoadEventBuilder extends Builder<Event> {
	
	private int time;
	private String id;
	private String srcJun;
	private String destJunc;
	private int length; 
	private int co2Limit;
	private int maxSpeed;
	private String weather;

	NewRoadEventBuilder(String type) {
		super(type);
	}
	
	protected boolean isCorrect(JSONObject data) {
		boolean resul=data.has("time")
				&&data.has("id")
				&&data.has("src")
				&&data.has("dest")
				&&data.has("length")
				&&data.has("co2limit")
				&&data.has("maxspeed")
				&&data.has("weather");
		if(resul) {
			this.time=data.getInt("time"); 
			this.id=data.getString("id");
			this.srcJun=data.getString("src");
			this.destJunc=data.getString("dest");
			this.length=data.getInt("length") ;
			this.co2Limit=data.getInt("co2limit");
			this.maxSpeed=data.getInt("maxspeed");
			this.weather=data.getString("weather").toUpperCase();
			
		}
		return resul;
	}
	

	int getTime() {
		return time;
	}
	
	String getId() {
		return id;
	}

	String getSrcJun() {
		return srcJun;
	}

	String getDestJunc() {
		return destJunc;
	}

	int getLength() {
		return length;
	}

	int getCo2Limit() {
		return co2Limit;
	}

	int getMaxSpeed() {
		return maxSpeed;
	}

	String getWeather() {
		return weather;
	}
	
	

}
