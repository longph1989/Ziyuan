package learntest.activelearning.plugin.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import learntest.activelearning.core.distribution.RandomTestDistributionRunner;
import learntest.activelearning.core.settings.LearnTestResources;
import learntest.activelearning.core.settings.LearntestSettings;
import learntest.activelearning.plugin.ActiveLearntestPlugin;
import learntest.core.commons.data.classinfo.MethodInfo;
import learntest.plugin.LearnTestConfig;
import learntest.plugin.LearntestLogger;
import learntest.plugin.handler.TestableMethodCollector;
import learntest.plugin.handler.filter.classfilter.ITypeFilter;
import learntest.plugin.handler.filter.classfilter.TestableClassFilter;
import learntest.plugin.handler.filter.methodfilter.IMethodFilter;
import learntest.plugin.handler.filter.methodfilter.NestedBlockChecker;
import learntest.plugin.handler.filter.methodfilter.TestableMethodFilter;
import learntest.plugin.handler.gentest.GentestSettings;
import learntest.plugin.utils.IProjectUtils;
import learntest.plugin.utils.IResourceUtils;
import learntest.plugin.utils.IStatusUtils;
import learntest.plugin.utils.LearnTestUtil;
import sav.settings.SAVTimer;
import sav.strategies.dto.AppJavaClassPath;

public class TestcasesDistributionHandler extends AbstractHandler implements IHandler {
	private static final Logger log = LoggerFactory.getLogger(TestcasesDistributionHandler.class);
	private List<IMethodFilter> methodFilters;
	private List<ITypeFilter> classFilters;
	
	public TestcasesDistributionHandler() {
		initFilters();
	}
	
	private void initFilters() {
		methodFilters = new ArrayList<IMethodFilter>();
		methodFilters.add(new TestableMethodFilter());
		methodFilters.add(new NestedBlockChecker());
//		methodFilters.add(new MethodNameFilter(LearntestConstants.EXCLUSIVE_METHOD_FILE_NAME, false));
//		methodFilters.add(new MethodNameFilter(LearntestConstants.SKIP_METHOD_FILE_NAME, false));
		classFilters = Arrays.asList(new TestableClassFilter());
	}

	@Override
	public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
		Job job = new Job("Run Random Testcases For Distribution") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					execute(monitor);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					monitor.done();
				}
				return IStatusUtils.afterRunning(monitor);
			}

		};
		job.schedule();

		return null;
	}

	protected void execute(IProgressMonitor monitor) throws Exception {
		LearnTestConfig config = LearnTestConfig.getInstance();
		AppJavaClassPath appClasspath = GentestSettings.getConfigAppClassPath(config);
		LearntestLogger.initLog4j(config.getProjectName());
		LearnTestResources resources = new LearnTestResources();
		resources.setMicrobatInstrumentationJarPath(IResourceUtils.getResourceAbsolutePath(ActiveLearntestPlugin.PLUGIN_ID, 
				"microbat_instrumentator.jar"));
		resources.setSavJunitRunnerJarPath(IResourceUtils.getResourceAbsolutePath(ActiveLearntestPlugin.PLUGIN_ID, 
				"sav.testrunner.jar"));
		LearntestSettings learntestSettings = new LearntestSettings(resources);
		IJavaProject project = IProjectUtils.getJavaProject(config.getProjectName());
//		SAVTimer.enableExecutionTimeout = true;
		SAVTimer.exeuctionTimeout = 50000000;
		run(project, appClasspath, learntestSettings, monitor);
	}
	
	private void run(IJavaProject project, AppJavaClassPath appClasspath, LearntestSettings learntestSettings, IProgressMonitor monitor) {
		final List<IPackageFragmentRoot> roots = IProjectUtils.getSourcePkgRoots(project);
		try {
			for (IPackageFragmentRoot root : roots) {
				for (IJavaElement element : root.getChildren()) {
					if (element instanceof IPackageFragment) {
						run((IPackageFragment) element, appClasspath, learntestSettings, monitor);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run(IPackageFragment pkg, AppJavaClassPath appClasspath, LearntestSettings learntestSettings, IProgressMonitor monitor)
			throws JavaModelException {
		for (IJavaElement javaElement : pkg.getChildren()) {
			if (javaElement instanceof IPackageFragment) {
				run((IPackageFragment) javaElement, appClasspath, learntestSettings, monitor);
			} else if (javaElement instanceof ICompilationUnit) {
				ICompilationUnit icu = (ICompilationUnit) javaElement;
				CompilationUnit cu = LearnTestUtil.convertICompilationUnitToASTNode(icu);
				boolean valid = true;
				for (ITypeFilter classFilter : classFilters) {
					if (!classFilter.isValid(cu)) {
						valid = false;
						continue;
					}
				}
				if (!valid) {
					continue;
				}
				TestableMethodCollector collector = new TestableMethodCollector(cu, methodFilters);
				cu.accept(collector);
				List<MethodInfo> validMethods = collector.getValidMethods();
				run(validMethods, cu, appClasspath, learntestSettings, monitor);
			}
		}
		log.info("package : {} ", pkg.getElementName());
	}

	private void run(List<MethodInfo> validMethods, CompilationUnit cu, AppJavaClassPath appClasspath,
			LearntestSettings learntestSettings, IProgressMonitor monitor) {
		for (MethodInfo method : validMethods) {
			RandomTestDistributionRunner distributionRunner = new RandomTestDistributionRunner();
			try {
				distributionRunner.run(appClasspath, method, learntestSettings);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
