package de.uni.bremen.stummk.psp.test.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.launcher.JUnitMigrationDelegate;
import org.eclipse.jdt.internal.junit.launcher.TestKindRegistry;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.LoCCounter;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.calculation.TestReportGenerator;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.DBConnection;
import de.uni.bremen.stummk.psp.utility.DataIO;

/**
 * This class should be executed as JUNIT PLUGIN TEST. Tests the functionality of the LoCCounter,
 * automatic Compile Listener and Test Report generator
 * 
 * @author Konstantin
 *
 */
@SuppressWarnings("restriction")
public class CalculationComponentsTest {
  private static final String projectName = "JAVATESTPROJECT";
  private IJavaProject javaProject;

  @BeforeClass
  public static void startUp() {
    // set up database
    DBConnection.init();

    if (Manager.getInstance().containsProject(projectName)) {
      Manager.getInstance().deleteCompleteProject(projectName);
    }
  }

  @Before
  public void setUp() throws Exception {
    // create java project on start up
    waitForJobs();

    try {
      createJavaProject();
    } catch (Exception e) {

    }

    waitForJobs();
    delay(3000);
  }

  /**
   * Tests the line of code counter. Counts the lines of codes from the generated classes
   */
  @Test
  public void testCount() {
    try {
      assertEquals(6, LoCCounter.count(projectName));
    } catch (Exception e) {

    }
  }

  /**
   * Tests the test report generator. Runs a test on the generated MainTest.class and checks if time
   * record has been generated
   */
  @SuppressWarnings("restriction")
  @Test
  public void testTestGenerator() {
    try {
      LocalDate date = LocalDate.now();
      Project p = new Project(projectName, date);
      ProjectPlanSummary pps = new ProjectPlanSummary(p);
      PSPProject pspp = new PSPProject(p, pps);
      Manager.getInstance().saveBackupProject(pspp);

      DataIO.saveToFile(projectName, pspp, null);

      ILaunchConfigurationType configType = DebugPlugin.getDefault().getLaunchManager()
          .getLaunchConfigurationType(JUnitLaunchConfigurationConstants.ID_JUNIT_APPLICATION);
      ILaunchConfigurationWorkingCopy wc = null;

      wc = configType.newInstance(null,
          DebugPlugin.getDefault().getLaunchManager().generateLaunchConfigurationName("JUNIT 4"));
      wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "");
      wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_KEEPRUNNING, false);
      wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER, javaProject.getHandleIdentifier());
      wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_RUNNER_KIND,
          TestKindRegistry.getContainerTestKindId(javaProject));
      JUnitMigrationDelegate.mapResources(wc);

      TestReportGenerator trg = new TestReportGenerator();
      JUnitCore.addTestRunListener(trg);
      wc.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor(), true);

      assertNotNull(trg.getTimeRecord());

      JUnitCore.removeTestRunListener(trg);
    } catch (Exception e) {

    }
  }

  private void delay(long waitTimeMillis) {
    // delay given milliseconds if waiting for job
    Display display = Display.getCurrent();

    if (display != null) {
      long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
      while (System.currentTimeMillis() < endTimeMillis) {
        if (!display.readAndDispatch())
          display.sleep();
      }
      display.update();
    }

    else {
      try {
        Thread.sleep(waitTimeMillis);
      } catch (InterruptedException e) {
      }
    }
  }

  public void waitForJobs() {
    // wait for Jobs
    while (!Job.getJobManager().isIdle())
      delay(1000);
  }

  @After
  public void cleanUp() {
    // cleans up the database
    waitForJobs();
    Manager.getInstance().deleteCompleteProject(projectName);
  }

  private void createJavaProject() {
    // generates a java project in the workspace for test use only
    // generates two classes: Main and MainTest
    try {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject project = root.getProject(projectName);
      if (!project.exists()) {
        project.create(null);
      }
      if (!project.isOpen()) {
        project.open(null);
      }
      IProjectDescription description = project.getDescription();
      description.setNatureIds(new String[] {JavaCore.NATURE_ID});
      project.setDescription(description, null);
      javaProject = JavaCore.create(project);
      IFolder binFolder = project.getFolder("bin");
      binFolder.create(false, true, null);
      javaProject.setOutputLocation(binFolder.getFullPath(), null);
      List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
      IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
      LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
      for (LibraryLocation element : locations) {
        entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
      }
      // add libs to project class path
      javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
      IFolder sourceFolder = project.getFolder("src");
      sourceFolder.create(false, true, null);
      IPackageFragmentRoot packageRoot = javaProject.getPackageFragmentRoot(sourceFolder);
      IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
      IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 2];
      System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
      newEntries[oldEntries.length] = JavaCore.newSourceEntry(packageRoot.getPath());
      newEntries[(oldEntries.length + 1)] =
          JavaCore.newContainerEntry(new Path("org.eclipse.jdt.junit.JUNIT_CONTAINER/4"));
      javaProject.setRawClasspath(newEntries, null);
      IPackageFragment pack =
          javaProject.getPackageFragmentRoot(sourceFolder).createPackageFragment("main", false, null);
      StringBuffer buffer = new StringBuffer();
      buffer.append("package " + pack.getElementName() + ";\n");
      buffer.append("\n");
      buffer.append("/** \n *TestClass \n */");
      buffer.append("\n");
      buffer.append("public class Main { \n");
      buffer.append("public static void main(String[] args) { \n");
      buffer.append("System.out.println(" + "5" + "); \n");
      buffer.append("} \n");
      buffer.append("\n");
      buffer.append("//Commentar \n");
      buffer.append("} \n");
      ICompilationUnit cu = pack.createCompilationUnit("Main.java", buffer.toString(), false, null);

      StringBuffer buf = new StringBuffer();
      buf.append("package " + pack.getElementName() + ";\n");
      buf.append("\n");
      buf.append("import static org.junit.Assert.assertEquals;");
      buf.append("\n");
      buf.append("import org.junit.Test;");
      buf.append("\n");
      buf.append("public class MainTest { \n");
      buf.append("@Test");
      buf.append("\n");
      buf.append("public void testMain() { \n");
      buf.append("assertEquals(1, 1); \n");
      buf.append("} \n");
      buf.append("} \n");
      ICompilationUnit c = pack.createCompilationUnit("MainTest.java", buf.toString(), false, null);
    } catch (CoreException | NoClassDefFoundError | IllegalStateException e) {
    }
  }

}
