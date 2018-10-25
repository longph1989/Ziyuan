package sav.strategies.dto.execute.value;

public class ExecVarHelper {
	private ExecVarHelper(){}
	
	public static String getArrayElementID(String arrayVarId, int[] location) {
		StringBuilder sb = new StringBuilder(arrayVarId);
		for (int i = 0; i < location.length; i++) {
			sb.append("[").append(location[i]).append("]");
		}
		return sb.toString();
	}
	
	public static String getArrayElementID(String varID, int idx) {
		return getArrayChildID(varID, String.valueOf(idx));
	}
	
	public static String getArrayChildID(String varID, String childCode) {
		return String.format("%s[%s]", varID, childCode);
	}

	public static String getFieldId(String parentId, String fieldName) {
		return String.format("%s.%s", parentId, fieldName);
	}

	public static String getStringChildId(String varId, String childCode) {
		return String.format("%s{%s}", varId, childCode);
	}
}
