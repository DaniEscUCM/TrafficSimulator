package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements TrafficSimObserver {
	
	Controller _ctrl;
	JLabel _info;
	JLabel _event;
	

	StatusBar(Controller ctrl) {
		_ctrl=ctrl;
		
		setPreferredSize(new Dimension(1000, 30));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.add(Box.createRigidArea(new Dimension(10, 30)),BorderLayout.LINE_START) ;
		_info= new JLabel("Time: "+0);
		_info.setFont(new Font("Carlito",Font.PLAIN,15));
		this.add(_info,BorderLayout.LINE_START);
		
		this.add(Box.createRigidArea(new Dimension(10, 30)),BorderLayout.LINE_START) ;
		
		_event=new JLabel();
		_event.setFont(new Font("Carlito",Font.PLAIN,15));
		this.add(_event,BorderLayout.LINE_START);
		
		this.setVisible(true);
		_ctrl.addObserver(this);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_info.setText("Time: "+time);

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_event.setText("");

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_event.setText("Even added ("+e.toString()+")");

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_info.setText("Time: "+time);
		_event.setText("");

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(this, err, "Error Status Bar", JOptionPane.ERROR_MESSAGE);
	}

}
