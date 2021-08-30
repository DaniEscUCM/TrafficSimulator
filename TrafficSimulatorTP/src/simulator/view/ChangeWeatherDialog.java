package simulator.view;

import javax.swing.JFrame;

import simulator.model.Weather;

public class ChangeWeatherDialog extends ToolDialog<String,Weather> {
private static final long serialVersionUID = 1L;
	
	private static String titulo="Change Road Weather";
	private static String texto="Schedule event to change the weather of a road after a given number of simulation ticks from now";
	private static String box="Weather";

	public ChangeWeatherDialog(JFrame frame, ControlPanel cont) {
		super(titulo, texto, box, Weather.values(), frame,cont);
	}

	@Override
	public void action() {
		_cont.changeWeather();	
	}

}
