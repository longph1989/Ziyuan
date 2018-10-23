package learntest.activelearning.core.testgeneration;

import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import icsetlv.common.dto.BreakpointValue;
import learntest.activelearning.core.data.TestInputData;
import microbat.instrumentation.cfgcoverage.graph.Branch;
import sav.strategies.dto.execute.value.ExecValue;
import sav.strategies.dto.execute.value.ExecVar;

/**
 * @author LLT
 *
 */
public class InputData /*implements IInputData*/ {
	private Logger log = LoggerFactory.getLogger(InputData.class);
	private RequestType requestType;
	private JSONObject obj = new JSONObject();

//	@Override
	public void writeData(PrintWriter pw) {
		if (obj == null) {
			return;
		}
		log.debug("write data: {}, {}", requestType, obj);
		pw.println(String.valueOf(requestType));
		pw.println(obj);
//		pw.flush();
	}
	
	public static InputData forBoundaryRemaining(Dataset pathCoverage) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$BOUNDARY_REMAINING;
		inputData.obj.put(JsLabels.PATH_ID, pathCoverage.getId());
		inputData.obj.put(JsLabels.COVERED_DATA_POINTS, pathCoverage.getCoveredData());
		inputData.obj.put(JsLabels.UNCOVERED_DATA_POINTS, pathCoverage.getUncoveredData());
		return inputData;
	}

	public static InputData createStartMethodRequest(String methodId) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$TRAINING;
		inputData.obj.put(JsLabels.METHOD_ID, methodId);
		return inputData;
	}

	public static InputData createBranchRequest(Branch branch) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$TRAINING;
		inputData.obj.put(JsLabels.BRANCH_ID, branch.getBranchID());
		return inputData;
	}
	
	public static InputData createTrainingRequest(Branch branch, List<TestInputData> positiveData, 
			List<TestInputData> negativeData) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$TRAINING;
		inputData.obj.put(JsLabels.BRANCH_ID, branch.getBranchID());
		
		JSONArray positiveArray = transferToJsonArray(positiveData);
		inputData.obj.put(JsLabels.POSITIVE_DATA, positiveArray);
		
		JSONArray negativeArray = transferToJsonArray(negativeData);
		inputData.obj.put(JsLabels.NEGATIVE_DATA, negativeArray);
		
		return inputData;
	}
	
	public static InputData createBoundaryExplorationRequest(Branch branch, List<TestInputData> testData) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$BOUNDARY_EXPLORATION;
		inputData.obj.put(JsLabels.BRANCH_ID, branch.getBranchID());
		
		JSONArray jArray = transferToJsonArray(testData);
		inputData.obj.put(JsLabels.TEST_DATA, jArray);
		
		return inputData;
	}
	
	public static InputData transferToJSON(DataPoints points) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$SEND_LABEL;
		JSONArray array = new JSONArray();
		
		for(int i=0; i<points.values.size(); i++){
			JSONArray point = new JSONArray();
			for(int j=0; j<points.varList.size(); j++){
				ExecVar var = points.getVarList().get(j);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(JsLabels.NAME, var.getVarId());
				jsonObj.put(JsLabels.VALUE, points.values.get(i)[j]);
				jsonObj.put(JsLabels.TYPE, points.varList.get(j).getType());
				boolean label = points.getLabels().get(i);
				jsonObj.put(JsLabels.LABEL, label);
				point.put(jsonObj);
			}
			array.put(point);
		}
		
		inputData.obj.put(JsLabels.RESULT, array);
		
		return inputData;
	}

	private static JSONArray transferToJsonArray(List<TestInputData> positiveData) {
		JSONArray arrayObj = new JSONArray();
		for(TestInputData testInput: positiveData){
			
			BreakpointValue bpv = testInput.getInputValue();
			JSONArray positiveObj = new JSONArray();
			for(int i=0; i<bpv.getChildren().size(); i++){
				ExecValue value = bpv.getChildren().get(i);
				JSONObject param = new JSONObject();
				param.put(JsLabels.TYPE, value.getType());
				param.put(JsLabels.VALUE, value.getStrVal());
				param.put(JsLabels.NAME, value.getVarId());
				
				positiveObj.put(param);
			}
			arrayObj.put(positiveObj);
		}
		return arrayObj;
	}

	public static InputData createInputType(RequestType training) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$TRAINING;
		return inputData;
	}


}
