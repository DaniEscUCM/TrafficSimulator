package simulator.model;

public class NewRoadEvent extends Event 
{ 
	private String id;
	private String srcJun;
	private String destJunc;
	private int length; 
	private int co2Limit;
	private int maxSpeed;
	private Weather weather;
	
	public NewRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.id=id; 
		this.srcJun=srcJun;
		this.destJunc=destJunc;
		this.length=length;
		this.co2Limit=co2Limit;
		this.maxSpeed=maxSpeed;
		this.weather=weather;
		
	}

	@Override
	void execute(RoadMap map) {	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSrcJun() {
		return srcJun;
	}

	public void setSrcJun(String srcJun) {
		this.srcJun = srcJun;
	}

	public String getDestJunc() {
		return destJunc;
	}

	public void setDestJunc(String destJunc) {
		this.destJunc = destJunc;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getCo2Limit() {
		return co2Limit;
	}

	public void setCo2Limit(int co2Limit) {
		this.co2Limit = co2Limit;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}
	
	

}
