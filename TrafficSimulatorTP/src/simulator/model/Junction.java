package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private List<Road> inRoads;
	private Map<Junction,Road> outRoads;
	private List<List<Vehicle >> waitingCars;
	//private Map<Road,List<Vehicle>> queue;
	private int  greenLight=-1;
	private int lastGreenLight=0;
	private LightSwitchingStrategy lsStrategy; 
	private DequeuingStrategy dqStrategy;
	private int xCoor;
	private int yCoor;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		if(!lsStrategy.equals(null) &&!dqStrategy.equals(null)) {
			if(xCoor>=0 && yCoor>=0) {
				this.lsStrategy=lsStrategy;
				this.dqStrategy=dqStrategy;
				this.xCoor=xCoor;
				this.yCoor=yCoor;
				this.inRoads= new LinkedList<Road>();
				this.outRoads=new HashMap<Junction, Road>();
				this.waitingCars=new LinkedList<List<Vehicle>>();
			}else{//exception of negative value
				throw new IllegalArgumentException("Coordenates must be positive (" + xCoor + ", " + yCoor + ")");
			}
		}else{//exception of null value
			throw new IllegalArgumentException("No null value acepted");
		}
	}
	
	void addIncommingRoad(Road r) {
		if(!this.inRoads.contains(r)) {
			if(r.getDestiny().equals(this)) {
				this.inRoads.add(r);
				List<Vehicle> l=new LinkedList<Vehicle>();
				this.waitingCars.add(l);
			}else {
				throw new IllegalArgumentException("This road doesn't end in this junction");
			}
		}
	}
	
	void addOutGoingRoad(Road r) {
		if(!outRoads.containsKey(r.getDestiny()) && r.getOrigin().equals(this)) {
			outRoads.put(r.getDestiny(), r);
		}
		else {
			throw new IllegalArgumentException ("Wrong destiny junction or origin junction");
		}
	}
	
	void enter(Vehicle v) {
		waitingCars.get(inRoads.indexOf(v.getImIn())).add(v);
	}
	
	Road roadTo(Junction j) {
		return outRoads.get(j);
	}

	@Override
	void advance(int time) {
		if(greenLight>-1 && !waitingCars.get(greenLight).isEmpty()) {
			List<Vehicle>lis=this.dqStrategy.dequeue(waitingCars.get(greenLight));
			for(Vehicle i:lis) {
				i.moveToNextRoad();
				waitingCars.get(greenLight).remove(i);
			}
		}
		int aux=this.lsStrategy.chooseNextGreen(inRoads, waitingCars, greenLight, lastGreenLight, time);
		if(aux!=greenLight) {
			greenLight=aux;
			lastGreenLight=time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject junc = new JSONObject();
		junc.put("id",super._id);
		junc.put("green", greenLight==-1 ?"none":inRoads.get(greenLight)._id);
		JSONArray jaux = new JSONArray();		
		for(int i=0;i<waitingCars.size();i++) {
			JSONObject jhelp= new JSONObject();
			jhelp.put("road",inRoads.get(i)._id);
			jhelp.put("vehicles", idVehi(i));
			jaux.put(jhelp);
		}
		junc.put("queues", jaux);
		return junc;
	}
	
	private List<String> idVehi(int j){
		List<String>resul=new LinkedList<String>(); 
		for(Vehicle i:waitingCars.get(j)) {
			resul.add(i._id);
		}
		return resul;
	}
	

	public int getGreenLight() {
		return greenLight;
	}

	public List<Road> getInRoads() {
		return inRoads;
	}

	//COORDENADAS PARA GUI
	public int getxCoor() {
		return xCoor;
	}

	void setxCoor(int xCoor) {
		this.xCoor = xCoor;
	}

	public int getyCoor() {
		return yCoor;
	}

	void setyCoor(int yCoor) {
		this.yCoor = yCoor;
	}	
//COORDENADAS PARA GUI

	public String getId() {
		return super._id;
	}

	public List<List<Vehicle>> getWaitingCars() {
		return Collections.unmodifiableList(new ArrayList<>(waitingCars));
	}
	
	
	
}
