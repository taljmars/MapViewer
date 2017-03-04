package main.java.tester;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan("main.java.gui.core.mapTree")
@ComponentScan("main.java.gui.core.mapViewer")
@Import(MapView.class)
@Configuration
public class AppConfig {
	
	public static final AppConfig loader = new AppConfig();
	public static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	
	public Object load(String url) {
		try (InputStream fxmlStream = AppConfig.class.getResourceAsStream(url)) 
		{
			FXMLLoader loader = new FXMLLoader();
			URL location = getClass().getResource(url);
            loader.setLocation(location);
			loader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> clazz) {
					System.err.println("Try to get bean name " + clazz );
					return context.getBean(clazz);
				}
			});
			
			return loader.load(fxmlStream);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}
}
