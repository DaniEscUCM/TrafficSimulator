package simulator.model;

public class InterCityRoad extends Road {

	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		switch (super.getEnvironment()) {
		case SUNNY:
			super.setTotalContamination((int)(((100.0-2)/100.0)*super.getTotalContamination()));
			break;
		case CLOUDY:
			super.setTotalContamination((int)(((100.0-3)/100.0)*super.getTotalContamination()));	
			break;
		case RAINY:
			super.setTotalContamination((int)(((100.0-10)/100.0)*super.getTotalContamination()));
			break;
		case WINDY:
			super.setTotalContamination((int)(((100.0-15)/100.0)*super.getTotalContamination()));
			break;
		case STORM:
			super.setTotalContamination((int)(((100.0-20)/100.0)*super.getTotalContamination()));
			break;
		}
		
	}

	@Override
	void updateSpeedLimit() {
		if(super.isContAlarm()) {
			super.setAcSpeed((int)(super.getMaxSpeed()*0.5));
		}
		else {
			super.setAcSpeed(getMaxSpeed());
		}

	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(super.getEnvironment()!=Weather.STORM) {
			return super.getAcSpeed();
		}
		else {
			return (int)(super.getAcSpeed()*0.8);
		}
		
	}

}
