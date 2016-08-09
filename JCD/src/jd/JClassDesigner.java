package jd;

import java.util.Locale;
import static javafx.application.Application.launch;
import jd.data.DataManager;
import jd.file.FileManager;
import jd.gui.Workspace;
import jf.AppTemplate;
import jf.components.AppComponentsBuilder;
import jf.components.AppDataComponent;
import jf.components.AppFileComponent;
import jf.components.AppWorkspaceComponent;

/**
 * This class serves as the application class for our UML Generator(JClass designer) program. 
 * 
 * @author Aditya Potdar
 */
public class JClassDesigner extends AppTemplate {
    /**
     * This builder provides methods for properly setting up all
     * the custom objects needed to run this application. Note that
     * by swapping out these components we could have a very dijferent
     * program that did something completely dijferent.
     * 
     * @return The builder object that knows how to create the proper
     * components for this custom application.
     */
    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
	return new AppComponentsBuilder() {
	    /**
	     * Makes the returns the data component for the app.
	     * 
	     * @return The component that will manage all data
	     * updating for this application.
	     * 
	     * @throws Exception An exception may be thrown should
	     * data updating fail, which can then be customly handled.
	     */
	    @Override
	    public AppDataComponent buildDataComponent() throws Exception {
		return new DataManager(JClassDesigner.this);
	    }

	    /**
	     * Makes the returns the file component for the app.
	     * 
	     * @return The component that will manage all file I/O
	     * for this application.
	     * 
	     * @throws Exception An exception may be thrown should
	     * file I/O updating fail, which can then be customly handled.
	     */
	    @Override
	    public AppFileComponent buildFileComponent() throws Exception {
		return new FileManager();
	    }

	    /**
	     * Makes the returns the workspace component for the app.
	     * 
	     * @return The component that serve as the workspace region of
	     * the User Interface, managing all controls therein.
	     * 
	     * @throws Exception An exception may be thrown should
	     * UI updating fail, which can then be customly handled.
	     */
	    @Override
	    public AppWorkspaceComponent buildWorkspaceComponent() throws Exception {
		return new Workspace(JClassDesigner.this);
	    }
	};
    }
    
    /**
     * This is where program execution begins. 
     */
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
}
