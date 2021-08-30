package simulator.model;

public class CityRoad extends Road {

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int resul=super.getTotalContamination();
		if(super.getEnvironment()==Weather.WINDY||super.getEnvironment()==Weather.STORM) {
			resul-=10;
		}
		else {
			resul-=2;
		}
		
		if(resul<0) {
			resul=0;
		}
		super.setTotalContamination(resul);
	}

	@Override
	void updateSpeedLimit() {
		super.setAcSpeed(getMaxSpeed());
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (int)(((11.0-v.getContaminationGrate())/11.0)*super.getAcSpeed());
	}

}
