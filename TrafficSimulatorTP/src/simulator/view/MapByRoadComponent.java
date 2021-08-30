package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class MapByRoadComponent extends JComponent implements TrafficSimObserver {
	
	Controller _ctrl;
	RoadMap _map;
	private Image _car;
	
	MapByRoadComponent(Controller ctrl){
		_ctrl=ctrl;
		_ctrl.addObserver(this);
		setPreferredSize (new Dimension (300, 200));
		this.setBackground(Color.WHITE);
		_car=loadImage("car.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.setColor(Color.WHITE);
		g.clearRect(0, 0, getWidth(), getHeight());
		
		if (_map == null || _map.getRoads().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
		
	}
	
	private void drawMap(Graphics2D g) {
		int i=0;
		for (Road r: _map.getRoads()) {
			int x1=50,x2=getWidth()-100, y=(i+1)*50;
			//Line
			g.setColor(Color.BLACK);
			g.drawLine(x1, y, x2,y);
			//Origin Junction
			g.setColor(Color.BLUE);
			g.fillOval(x1, y-5,10,10);
			g.setColor(new Color(200, 100, 0));
			g.drawString(r.getOrigin().getId(),x1, y - 6);
			//Destiny Junction
			g.setColor(Color.RED);
			g.fillOval(x2,y-5,10,10);
			g.setColor(new Color(200, 100, 0));
			g.drawString(r.getDestiny().getId(),x2, y - 6);
			//Cars
			drawCars(x1, x2, y, r, g);	
			//Road identifier
			g.setColor(Color.BLACK);
			g.drawString(r.getId(),x1-16, y);
			//Weather image
			g.drawImage(weatherImage(r),x2+16,y-16,32,32,this);
			//Contamination image
			int C=(int) Math.floor(Math.min((double) r.getTotalContamination()/(1.0 + (double) r.getContAlarm()),1.0) / 0.19);
			Image cont=loadImage("cont_"+C+".png");
			g.drawImage(cont,x2+64,y-16,32,32,this);
			
			i++;
		}
	}
	
	private Image weatherImage(Road r) {
		Image we=null;
		switch (r.getEnvironment()) {
			case SUNNY:
				we=loadImage("sun.png");
				break;
			case CLOUDY:
				we=loadImage("cloud.png");
				break;
			case RAINY:
				we=loadImage("rain.png");
				break;
			case WINDY:
				we=loadImage("wind.png");
				break;
			case STORM:
				we=loadImage("storm.png");
			break;
				default:
				we=null;
				break;
		}
		return we;
	}

	private void drawCars(int x1, int x2, int y,Road r,Graphics2D g) {
		for(Vehicle v :r.get_queue()) {
			int x= x1 + (int) ((x2 - x1) * ((double)v.getLocation() / (double) r.getLength()));
			g.drawImage(_car,x,y-10,16,16,this);
			int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContaminationGrate()));
			g.setColor(new Color(0, vLabelColor, 0));
			g.drawString(v.getId(),x, y - 6);
		}	
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);

	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(this,err, "Error Road Map", JOptionPane.ERROR_MESSAGE);
	}

}
