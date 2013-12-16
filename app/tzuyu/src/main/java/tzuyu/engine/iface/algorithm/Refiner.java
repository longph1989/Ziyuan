/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package tzuyu.engine.iface.algorithm;

import java.util.List;

import refiner.Witness;
import tzuyu.engine.iface.HasTzProject;
import tzuyu.engine.iface.HasTzReport;
import tzuyu.engine.model.Formula;
import tzuyu.engine.model.QueryResult;
import tzuyu.engine.model.QueryTrace;
import tzuyu.engine.model.dfa.DFA;

/**
 * @author LLT
 * 
 */
public interface Refiner extends HasTzReport, HasTzProject {

	/**
	 * @param result
	 * @return
	 */
	Formula refineMembership(QueryResult result);

	/**
	 * @param dfa
	 * @param traces
	 * @return
	 */
	Witness refineCandidate(DFA dfa, List<QueryTrace> traces);

}
