package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements TrafficSimObserver {
	
	private Controller _ctrl;
	
	private JToolBar toolBar;
	
	private JButton _files;
	private JButton _co2Class;
	private JButton _weather;
	private JToggleButton _play;
	private JButton _stop;
	
	private JSpinner _ticks;
	private JSpinner _speed;
	
	private JButton _reset;
	private JButton _exit;
	
	private boolean _stopped;
	
	private ToolDialog<String,Integer> CO2;
	private ToolDialog<String,Weather> weather;
	
	private JFileChooser fc;
	
	private int actTicks;
	
	private List<String> vehi;
	private List<String> road;

	
	public ControlPanel(Controller ctrl){
		try {			
			_ctrl=ctrl;
			_ctrl.addObserver(this);

			toolBar= new JToolBar();
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(toolBar,BorderLayout.LINE_START);
			
			//-----------------------------------------------------DIALOGOS-----------------------------------------------------------
			vehi= new ArrayList<String>();
			road= new ArrayList<String>();			
			
			CO2=new ChangeCO2Dialog((JFrame) SwingUtilities.getWindowAncestor(this), this);			
			
			weather= new ChangeWeatherDialog((JFrame) SwingUtilities.getWindowAncestor(this), this);
			
			//------------------------------------------------------BOTONES------------------------------------------------------------
			
			//FILES
			 fc = new JFileChooser() {
				 @Override
		            protected javax.swing.JDialog createDialog(java.awt.Component parent) throws java.awt.HeadlessException {
		                javax.swing.JDialog dialog = super.createDialog(parent);

		                Image iconoPropio = Toolkit.getDefaultToolkit().getImage("resources/icons/lights.png");
		        		dialog.setIconImage(iconoPropio);

		                return dialog;

		            }
			 };
			 fc.setCurrentDirectory(new File("resources\\examples"));
			 //C:\\Users\\dani_\\Documents\\INFOR\\2doCurso\\2DOCUATRI\\TP2\\TrafficSimulatorTP\\
			
			_files= new JButton();
			_files.setIcon(new ImageIcon("resources/icons/open.png"));
			_files.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loadFile();
				}
			});
			_files.setToolTipText("LOAD FILE (L)");
			toolBar.add(_files, BorderLayout.LINE_START);
			
			toolBar.addSeparator();
			
			//CO2CLASS
			_co2Class= new JButton();
			_co2Class.setIcon(new ImageIcon("resources/icons/co2class.png"));
			_co2Class.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					CO2.setVisible(true);
				}
	
			});
			_co2Class.setToolTipText("CHANGE CAR CO2 (C)");
			toolBar.add(_co2Class, BorderLayout.LINE_START);
			
			//WEATHER
			_weather= new JButton();
			_weather.setIcon(new ImageIcon("resources/icons/weather.png"));
			_weather.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					weather.setVisible(true);
				}
			});
			_weather.setToolTipText("CHANGE ROAD WEATHER (W)");
			toolBar.add(_weather, BorderLayout.LINE_START);
			
			toolBar.addSeparator();
			
			//PLAY
			_play=new JToggleButton();
			_play.setIcon(new ImageIcon("resources/icons/run.png"));
			_play.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						_stopped=false;
						run_sim((Integer)_ticks.getValue());
						enableToolBar(false);
					}
					else {
						_stopped=true;
						enableToolBar(true);
					}
				}
			});
			_play.setToolTipText("START SIM (P)");
			toolBar.add(_play);
			
			//STOP
			_stop=new JButton();
			_stop.setIcon(new ImageIcon("resources/icons/stop.png"));			
			_stop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					stop();
				}
			});
			_stop.setToolTipText("STOP SIM (T)");
			toolBar.add(_stop, BorderLayout.LINE_START);
			
			//TICK-S--------------------SPINNER-------------------------
			_ticks = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
			_ticks.setMaximumSize(new Dimension(100,50));
			toolBar.add( new JLabel("Ticks: "));
			toolBar.add(_ticks, BorderLayout.LINE_START);
			
			toolBar.addSeparator();
			
			//VELOCIDAD-----------------SPINNER------------------------
			_speed = new JSpinner(new SpinnerNumberModel(500, 0, 999, 1));
			_speed.setMaximumSize(new Dimension(100,50));
			_speed.setToolTipText("SET SIM SPEED [0-999]");
			toolBar.add( new JLabel("Speed: "));
			toolBar.add(_speed, BorderLayout.LINE_START);
			
			//RESET
			toolBar.add(Box.createGlue());
			
			_reset=new JButton();
			_reset.setIcon(new ImageIcon("resources/icons/reset.png"));
			_reset.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					reset();
				}
			});
			_reset.setToolTipText("RESET SIM (R)");
			toolBar.add(_reset, BorderLayout.LINE_END);
			
			//EXIT
			
			toolBar.addSeparator();
			
			_exit=new JButton();
			_exit.setIcon(new ImageIcon("resources/icons/exit.png"));
			_exit.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					exit();
				}
			});
			_exit.setToolTipText("CLOSE THE SIM (E)");
			toolBar.add(_exit, BorderLayout.LINE_END);
					
		
		}catch(Exception ex) {
			onError(ex.getMessage());
		}
	}
	
	//---------------------------------------------------------FUNCIONES-------------------------------------------------------
	
	//LOADS DESDE FILES Y ROADMAP
	void loadFile() {
		try {
			int returnVal = fc.showOpenDialog((JFrame) SwingUtilities.getWindowAncestor(this));
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();			
				this._ctrl.reset();
				_ctrl.setIn(file);
				this._ctrl.loadEvents();
			}	
		}catch(Exception e){
			onError(e.getMessage());
		}
	}
	
	void loadChangeCo2(List<Vehicle> vehicle) {
		try {
			vehi.clear();
			for(Vehicle v: vehicle) {
				vehi.add(v.getId());
			}
			CO2.setList(vehi);
		}catch(Exception e){
			onError(e.getMessage());
		}
	}
	
	void loadWeather(List<Road> roads) {
		try {
			road.clear();
			for(Road r: roads) {
				road.add(r.getId());
			}
			weather.setList(road);
		}catch(Exception e){
			onError(e.getMessage());
		}
	}
	
	//ACCIONES DE TOOLDIALOG
	void changeCo2() {	
		if(CO2.getSelected()) {
			try {
				List<Pair<String,Integer>> car=new LinkedList<Pair<String, Integer>>(); 
				
				for(int v=0;v<CO2.getNumListIndices();v++) {
					car.add(new Pair<String, Integer>(vehi.get(v), (Integer) CO2.getSelectedbox()));
				}
				
				NewSetContClassEvent even= new NewSetContClassEvent(actTicks+(int) CO2.getTicks(), car);
				
				_ctrl.addEvent(even);
			}catch(Exception e){
				onError(e.getMessage());
			}
		}				
	}	
	
	void changeWeather() {
		if(weather.getSelected()) {
			try {
				List<Pair<String,Weather>> ws=new LinkedList<Pair<String,Weather>>();
				
				for(int r=0;r<weather.getNumListIndices();r++) {
					ws.add(new Pair<String, Weather>(road.get(r), weather.getSelectedbox()));
				}
				
				SetWeatherEvent even=new SetWeatherEvent(actTicks+(int) weather.getTicks(), ws);
				_ctrl.addEvent(even);
			}catch(Exception e){
				onError(e.getMessage());
			}
		}
	}
	
	//ACCIONES DE BOTONES
	void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
				Thread.sleep(1000-(int)_speed.getValue());
			} catch (Exception e) {
				_stopped = true;
				this.onError(e.getMessage());				
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {					
					run_sim(n - 1);
				}
			});
		} else {
			stop();
		}
	}
	
	void enableToolBar(boolean b) {
		_files.setEnabled(b);
		_co2Class.setEnabled(b);
		_weather.setEnabled(b);
		_exit.setEnabled(b);
		_ticks.setEnabled(b);
		_speed.setEnabled(b);
		_reset.setEnabled(b);
		((MainWindow) SwingUtilities.getWindowAncestor(this)).setEnabledMenu(b);
	}

	void stop() {
		_play.setSelected(false);
	}
	
	void play() {
		_play.setSelected(!_play.isSelected());
	}
	
	void reset() {
		_ctrl.reset();
		try {
			_ctrl.loadEvents();
		} catch (Exception e) {
			onError(e.getMessage());
		}
		
	}
	
	void exit() {
		Object[] options = {"OK","Cancelar"};
		int n = JOptionPane.showOptionDialog((JFrame) SwingUtilities.getWindowAncestor(this),
		 "You're about to exit Traffic Simulator", "EXIT",
		 JOptionPane.OK_CANCEL_OPTION,
		 JOptionPane.WARNING_MESSAGE,
		 null,
		 options,
		 options[1]); 
		if(n==0) {
			System.exit(0);
		}
	}
	
	void setWeather() {
		weather.setVisible(true);
	}
	
	void setCo2() {
		CO2.setVisible(true);
	}
	
	int getTicks() {
		return (int) _ticks.getValue();
	}

	//-----------------------------------------FUNCIONES SIMULATOR----------------------------------------------
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		loadChangeCo2(map.getVehicles());
		loadWeather(map.getRoads());
		actTicks=time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		loadChangeCo2(map.getVehicles());
		loadWeather(map.getRoads());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}
	
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_ticks.setValue(10);
		_speed.setValue(500);
		vehi.clear();
		CO2.setList(vehi);
		CO2.reset();
		road.clear();
		weather.setList(road);
		weather.reset();
		_stopped=true;
	}	

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(this, err, "Error Control Panel", JOptionPane.ERROR_MESSAGE);
	}

}
