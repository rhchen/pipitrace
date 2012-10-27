package net.sf.pipitrace.app;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import net.izhtw.rcp.explorer.master.Application;

/**
 * This class controls all aspects of the application's execution
 */
public class PipiApplication extends Application {

	// The plug-in ID
	public static final String PLUGIN_ID = "net.sf.pipitrace.app";

	@Override
	public Object start(IApplicationContext context) {
		
		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new PipiApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
		
	}

	
}
