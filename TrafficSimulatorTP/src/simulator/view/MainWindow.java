package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import simulator.control.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	private Controller _ctrl;
	
	private JMenuBar menuBar; 
	private JMenu menu;
	
	private ControlPanel tool;
	
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
		Image iconoPropio = Toolkit.getDefaultToolkit().getImage("resources/icons/lights.png");
		setIconImage(iconoPropio);
	}
	
	private void initGUI() {
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		menuBar = new JMenuBar(); 
		iniMenu();
		this.setJMenuBar(menuBar); 	
		
		
		tool=new ControlPanel(_ctrl);
		mainPanel.add(tool,BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		// tables
		JPanel eventsView =
		createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 400));
		tablesPanel.add(eventsView);
		
		JPanel vehiclesTable =
		createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		tablesPanel.add(vehiclesTable);
		
		JPanel roadsTable =
		createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		tablesPanel.add(roadsTable);
		
		JPanel junctionsTable =
		createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		tablesPanel.add(junctionsTable);
		
		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 200));
		mapsPanel.add(mapView);
		
		JPanel roadsView = createViewPanel(new MapByRoadComponent(_ctrl), "Map by road");
		mapView.setPreferredSize(new Dimension(500, 200));
		mapsPanel.add(roadsView);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				tool.exit();
				super.windowClosing(e);
			}
		});
		this.pack();
		this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		p.setBorder(BorderFactory.createTitledBorder(b,title));
		p.add(new JScrollPane(c));
		return p;
	}

	private void iniMenu() {
		//----------------------------------------------------MENU---------------------------------------------
		menu = new JMenu("MENU");
		menu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menu);
		
		JMenuItem File=new JMenuItem("File");
		File.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				tool.loadFile();				
			}
		});
		File.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,ActionEvent.SHIFT_MASK));
		menu.add(File);
				
		JMenuItem Co2 = new JMenuItem("Co2");
		Co2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				tool.setCo2();					
			}
		});
		Co2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.SHIFT_MASK));
		menu.add(Co2);
		
		JMenuItem Weather = new JMenuItem("Weather");
		Weather.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				tool.setWeather();
			}
		});
		Weather.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.SHIFT_MASK));
		menu.add(Weather);		

		JMenuItem Reset = new JMenuItem("Reset");
		Reset.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				tool.reset();			
			}
		});
		Reset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.SHIFT_MASK));
		menu.add(Reset);
		
		JMenuItem Exit = new JMenuItem("Exit");
		Exit.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				tool.exit();			
			}
		});
		Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.SHIFT_MASK));
		menu.add(Exit);	
		
		//---------------------------------------------------SIM-----------------------------------------------
		JMenu sim = new JMenu("SIM");
		sim.setMnemonic(KeyEvent.VK_S);
		menuBar.add(sim);
		
		JMenuItem Play = new JMenuItem("Play");
		Play.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				tool.play();			
			}
		});
		Play.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.SHIFT_MASK));
		sim.add(Play);
		
		JMenuItem Stop = new JMenuItem("Stop");
		Stop.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				tool.stop();			
			}
		});
		Stop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,ActionEvent.SHIFT_MASK));
		sim.add(Stop);
			
		
	}
	
	void setEnabledMenu(boolean b) {
		menu.setEnabled(b);
	}
}
