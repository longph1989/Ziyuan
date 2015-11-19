package microbat.handler;

import static sav.commons.TestConfiguration.SAV_COMMONS_TEST_TARGET;
import icsetlv.common.dto.BreakpointData;
import icsetlv.trial.model.Trace;
import icsetlv.variable.TestcasesExecutor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import microbat.util.Settings;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import sav.common.core.SavException;
import sav.common.core.utils.CollectionUtils;
import sav.common.core.utils.JunitUtils;
import sav.commons.TestConfiguration;
import sav.strategies.dto.AppJavaClassPath;
import sav.strategies.dto.BreakPoint;
import sav.strategies.dto.BreakPoint.Variable;
import sav.strategies.dto.BreakPoint.Variable.VarScope;

;

public class StartDebugHandler extends AbstractHandler {

	private TestcasesExecutor tcExecutor;
	private AppJavaClassPath appClasspath;

	protected AppJavaClassPath initAppClasspath() {
		AppJavaClassPath appClasspath = new AppJavaClassPath();
		appClasspath.setJavaHome(TestConfiguration.getJavaHome());
		appClasspath.addClasspath(SAV_COMMONS_TEST_TARGET);
		return appClasspath;
	}

	public void setup() {
		appClasspath = initAppClasspath();

		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace()
				.getRoot();
		IProject myWebProject = myWorkspaceRoot
				.getProject(Settings.projectName);
		String projectPath = myWebProject.getLocationURI().getPath();

		projectPath = projectPath.substring(1, projectPath.length());

//		appClasspath
//				.addClasspath("F://workspace//runtime-debugging//Test//bin//com//test");
		appClasspath
				.addClasspath("F://workspace//runtime-debugging//Test//bin");
		// appClasspath.addClasspath(TestConfiguration.getTestTarget(ICSETLV));
		tcExecutor = new TestcasesExecutor(6);
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		initAppClasspath();
		setup();
		
		List<BreakPoint> breakpoints = new ArrayList<BreakPoint>();
		String clazz = "com.Main";
		BreakPoint bkp1 = new BreakPoint(clazz, null, 16);
		bkp1.addVars(new Variable("c"));
		bkp1.addVars(new Variable("tag", "tag", VarScope.THIS));
		//bkp1.addVars(new Variable("strList", "strList", VarScope.THIS));
		//bkp1.addVars(new Variable("map", "map", VarScope.THIS));
		//bkp1.addVars(new Variable("array", "array", VarScope.THIS));
		//bkp1.addVars(new Variable("org", "org", VarScope.THIS));
		bkp1.addVars(new Variable("output"));
		breakpoints.add(bkp1);
		List<String> junitClassNames = CollectionUtils.listOf("com.test.MainTest");
		List<String> tests;
		try {
			
			File f0 = new File("F://workspace//runtime-debugging//Test//bin");
			//File f1 = new File("E://eclipse//plugins");
			URL[] cp = new URL[1];
			try {
				//cp = new URL{f.toURI().toURL()};
				cp[0] = f0.toURI().toURL();
				//cp[1] = f1.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			URLClassLoader urlcl  = URLClassLoader.newInstance(cp);
			
			tests = JunitUtils.extractTestMethods(junitClassNames, urlcl);
			tcExecutor.setup(appClasspath, tests);
			tcExecutor.run(breakpoints);
			List<BreakpointData> result = tcExecutor.getResult();
			System.out.println(result);
			
			Trace trace = tcExecutor.getTrace();
			System.currentTimeMillis();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SavException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}