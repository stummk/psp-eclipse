package de.uni.bremen.stummk.psp.control;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * The perspective of the psp
 * 
 * @author Konstantin
 *
 */
public class PSPPerspective implements IPerspectiveFactory {

  private static final String BOTTOM = "bottom";

  @Override
  public void createInitialLayout(IPageLayout myLayout) {
    myLayout.addView("org.eclipse.jdt.ui.PackageExplorer", IPageLayout.LEFT, 0.20f, myLayout.getEditorArea());

    IFolderLayout bot = myLayout.createFolder(BOTTOM, IPageLayout.BOTTOM, 0.60f, myLayout.getEditorArea());
    bot.addView(Constants.ID_TASK_OVERVIEW);
    bot.addView(Constants.RESOURCE_SUMMARY_ID);
    bot.addView(IPageLayout.ID_PROBLEM_VIEW);
    bot.addView("org.eclipse.ui.console.ConsoleView");
  }

}
