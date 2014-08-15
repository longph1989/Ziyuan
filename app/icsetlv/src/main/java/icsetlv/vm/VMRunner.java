/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package icsetlv.vm;

import icsetlv.common.Constants;
import icsetlv.common.exception.IcsetlvException;
import icsetlv.common.utils.CollectionBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sav.common.core.utils.StringUtils;

/**
 * @author LLT
 * 
 */
public class VMRunner {
	private static final String cpToken = "-cp";
	/*
	 * from jdk 1.5, we can use new JVM option: -agentlib 
	 * Benefits of using the new -agentlib args is, it doesn't contain any whitespace, so
	 * you don't need to worry if you need to quote it or not. But if you do
	 * want to use the old flags, be careful about when to quote the value and
	 * when to not quote.
	 */
	private static final String debugToken = "-agentlib:jdwp=transport=dt_socket,suspend=y,address=%s";
	private static final String enableAssertionToken = "-ea";

	public static Process startJVM(VMConfiguration config) throws IcsetlvException {
		if (config.getPort() == -1) {
			throw new IcsetlvException("Cannot find free port to start jvm!");
		}
		
		List<String> commands = CollectionBuilder.init(new ArrayList<String>())
				.add(buildJavaExecArg(config))
				.add(String.format(debugToken, config.getPort()))
				.add(enableAssertionToken)
				.add(cpToken)
				.add(toClasspathStr(config.getClasspaths()))
				.add(config.getLaunchClass())
				.getResult();
		for (String arg : config.getProgramArgs()) {
			commands.add(arg);
		}
		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		processBuilder.redirectErrorStream(true);
		Process process = null;
		try {
			process = processBuilder.start();
		} catch (IOException e) {
			IcsetlvException.rethrow(e, "cannot start jvm process");
		}
		return process;
	}
	
	private static String toClasspathStr(List<String> classpaths) {
		return StringUtils.join(classpaths, File.pathSeparator);
	}

	private static String buildJavaExecArg(VMConfiguration config) {
		return StringUtils.join(Constants.FILE_SEPARATOR, config.getJavaHome(), "bin", "java");
	}
}
