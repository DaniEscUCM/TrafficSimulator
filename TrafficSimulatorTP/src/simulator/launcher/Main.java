package simulator.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.factories.*;
import simulator.control.Controller;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;


//Daniela Alejandra Escobar Suarez
public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = "GUI";
	private static Factory<Event> _eventsFactory = null;
	private static int _ticks=0;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);
			parseModeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Number of iterations").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Change from GUI mode and CONSOLE mode, by default it's GUI").build());
		

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTicksOption(CommandLine line) {
		if (line.hasOption("t")) {
			_ticks=Integer.parseInt(line.getOptionValue("t"));
		}
		else {
			_ticks=_timeLimitDefaultValue;
		}
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		String m;
		if (line.hasOption("m")) {
			m=(line.getOptionValue("m")).toUpperCase();
			if(m.equals("CONSOLE") || m.equals("GUI")) {
				_mode=m;
			}
			else {
				throw new ParseException("UNKNOWN MODE");
			}
		}
	}

	private static void initFactories() {
		List<Builder<Event>> builders=new LinkedList<Builder<Event>>();
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		builders.add(new NewCityRoadEventBuilder());
		builders.add(new NewInterCityEventBuilder());
		builders.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
		builders.add(new NewVehicleEventBuilder());
		builders.add(new SetContClassEventBuilder());
		builders.add(new SetWeatherEventBuilder());
		
		_eventsFactory=new BuilderBasedFactory<Event>(builders);

	}

	private static void startBatchMode() throws Exception {
		TrafficSimulator sim=new TrafficSimulator();
		Controller controller=new Controller(sim, _eventsFactory);
		//FileInputStream in= new FileInputStream(_inFile);
		if (_inFile == null ) {
			throw new ParseException("An events file is missing");
		}
		controller.setIn(new File(_inFile));
		controller.loadEvents();		
		controller.run(_ticks,_outFile);
		
		System.out.println("CONSOLE FINISHED");
		
	}
	
	private static void startGUIMode() throws Exception {
		TrafficSimulator sim=new TrafficSimulator();
		Controller controller=new Controller(sim, _eventsFactory);
		if(_inFile!=null) {
			//FileInputStream in= new FileInputStream(_inFile);
			controller.setIn(new File(_inFile));
			controller.loadEvents();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
			}
		});

	}

	private static void start(String[] args) throws IOException, Exception {
		initFactories();
		parseArgs(args);
		if(_mode.equals("CONSOLE")) {
			startBatchMode();
		}
		else {
			startGUIMode();
		}
		
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
