package wordnetapp;

import java.awt.EventQueue;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Main {
	private static final Logger logger = Logger.getLogger(new RuntimeException()
																.getStackTrace()[0].getClassName());
	private static Level loggerLevel = Level.ERROR;
    public static void main(String[] args) {

        setupLogger();
        EventQueue.invokeLater(new Runnable(){
            public void run(){
            	logger.debug("Running WordNet Java App...");
                WordNetAppJFrame w = new WordNetAppJFrame();
                w.setAlwaysOnTop(true);
                w.setVisible(true);
            }
        });
    }
    
    private static void setupLogger(){
    	ConsoleAppender console = new ConsoleAppender();
    	console.setLayout(new PatternLayout("%d [%p|%c|%C{1}] %m%n"));
    	console.activateOptions();
    	Logger.getRootLogger().addAppender(console);
    	Logger.getRootLogger().setLevel(loggerLevel);
    }
}