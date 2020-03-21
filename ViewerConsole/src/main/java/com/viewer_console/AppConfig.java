package com.viewer_console;

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

@ComponentScan("com.mapviewer.gui.core.layers")
@ComponentScan("com.mapviewer.gui.core.mapTree")
@ComponentScan("com.mapviewer.gui.core.mapViewer")
@Import(MapView.class)
@Configuration
public class AppConfig {
	
	public static final AppConfig loader = new AppConfig();
	public static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


	private FXMLLoader getFXMLLoaderForUrl(String url) {
		FXMLLoader fxmlloader = new FXMLLoader();
		URL location = getClass().getResource(url);
		fxmlloader.setLocation(location);
		fxmlloader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> clazz) {
				System.out.print("Fetch bean name '" + clazz + "' ");
				Object obj = context.getBean(clazz);
				if (obj != null)
					System.out.println("[SUCCESS :'" + obj + "']");
				else
					System.err.println("[FAIL]");
				return obj;
			}
		});

		return fxmlloader;
	}

	public Object load(String url) {
		try {
			InputStream fxmlStream = this.getClass().getClassLoader().getResourceAsStream(url);
			FXMLLoader fxmlLoader = getFXMLLoaderForUrl(url);
			return fxmlLoader.load(fxmlStream);
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
