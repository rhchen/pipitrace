package net.sf.pipitrace.app;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import net.izhtw.rcp.explorer.master.ApplicationWorkbenchAdvisor;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class PipiApplicationWorkbenchAdvisor extends ApplicationWorkbenchAdvisor {
	
	private static final String PERSPECTIVE_ID = "org.eclipse.ui.resourcePerspective";
	//private static final String PERSPECTIVE_ID = "org.eclipse.rcp.explorer.resourcePerspective";

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new PipiApplicationWorkbenchWindowAdvisor(configurer);
	}

}