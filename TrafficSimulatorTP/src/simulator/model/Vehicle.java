package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle> {
	
	private List<Junction> itinerary;
	private int maxSpeed;
	private int acSpeed;
	private VehicleStatus status;
	private Road imIn;
	private int location;
	private int contaminationGrate;
	private int contaminationTotal;
	private int totalDistance;
	private int lastJunction;//indice ultima interseccion en la que ha estado, -1 si no ha estado en ninguna
	
	Vehicle(String id, int maxSpeed, int contClass,	List<Junction> itinerary) {
		super(id);
		if(maxSpeed>=0) {
			if(contClass>=0 && contClass<11) {
				if(itinerary.size()>=2) {
					this.itinerary=Collections.unmodifiableList(new ArrayList<>(itinerary));
					this.maxSpeed=maxSpeed;
					this.acSpeed=0;
					this.status=VehicleStatus.PENDING;
					this.imIn=null;
					this.totalDistance=0;
					this.contaminationGrate=contClass;
					this.contaminationTotal=0;
					this.lastJunction=-1;
				}else {
					throw new IllegalArgumentException("Itinerary must have at least 2 junctions");
				}
			}
			else {
				throw new IllegalArgumentException("Contamination class must be between 0 and 10");
			}
		}
		else {
			throw new IllegalArgumentException("Maximum speed must be positive");
		}
	}
	
	void setSpeed(int s) {
		if(s>=0) {
			if(this.status==VehicleStatus.TRAVELING) {
				if(s<maxSpeed) {
					this.acSpeed=s;
				}else  {
					this.acSpeed=maxSpeed;
				}
			}
		}
		else {
			throw new IllegalArgumentException("Only positive speeds acepted");
		}
	}
	
	void setContaminationClass(int c) {
		if(c>=0 && c<=10) {
			this.contaminationGrate=c;
		}
		else {
			throw new IllegalArgumentException("Contamination class must be between 0 and 10");
		}
	}

	@Override
	void advance(int time) {
		if(this.status==VehicleStatus.TRAVELING) {
			int oldLocation=this.location;
			if(this.location+this.acSpeed<this.imIn.getLenght()) {
				this.location +=this.acSpeed;
			}else {
				this.imIn.getDestiny().enter(this);
				this.status=VehicleStatus.WAITING;
				location=this.imIn.getLenght();
				acSpeed=0;
			}
			int aux=(this.location - oldLocation)*contaminationGrate;
			this.contaminationTotal+=aux ;
			this.totalDistance+=this.location - oldLocation;
			this.imIn.addContamination(aux);
		}
	}
	
	void moveToNextRoad() {
		if(this.status==VehicleStatus.PENDING) {
			location=0;
			this.imIn=this.itinerary.get(0).roadTo(itinerary.get(1));
			lastJunction=1;
			this.imIn.enter(this);
			this.status=VehicleStatus.TRAVELING;
		}
		else if(this.status==VehicleStatus.WAITING) {
			this.imIn.exit(this);
			location=0;
			if(lastJunction+1<itinerary.size()) {
				this.imIn=this.itinerary.get(this.lastJunction).roadTo(itinerary.get(lastJunction+1));
				this.imIn.enter(this);
				this.status=VehicleStatus.TRAVELING;
				this.lastJunction++;
			}else {
				this.status=VehicleStatus.ARRIVED;
			}
		}
		else {
			throw new IllegalArgumentException("Vehicle status must be WAITING or PENDING to move to next junction");
		}
	}

	@Override
	public JSONObject report() {
		JSONObject vehi = new JSONObject();		
		vehi.put("id", super._id);
		vehi.put("speed", this.acSpeed);
		vehi.put("distance", this.totalDistance);
		vehi.put("co2", this.contaminationTotal);
		vehi.put("class",this.contaminationGrate);
		vehi.put("status", this.status);
		if(this.status!=VehicleStatus.PENDING && this.status!=VehicleStatus.ARRIVED) {
			vehi.put("road", this.imIn);
			vehi.put("location", this.location);
		}
		return vehi;
	}

	public Road getImIn() {
		return imIn;
	}

	@Override
	public int compareTo(Vehicle v) {
		return this.location>v.location?-1:this.location<v.location?1:0;
	}

	public int getLocation() {
		return location;
	}

	public int getAcSpeed() {
		return acSpeed;
	}

	public int getContaminationGrate() {
		return contaminationGrate;
	}

	public VehicleStatus getStatus() {
		return status;
	}
	
	public String getId() {
		return super._id;
	}
	
	public List<Junction> getItinerary() {		
		return Collections.unmodifiableList(new ArrayList<>(itinerary));
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getContaminationTotal() {
		return contaminationTotal;
	}

	public int getTotalDistance() {
		return totalDistance;
	}
	
	
	
	
}
