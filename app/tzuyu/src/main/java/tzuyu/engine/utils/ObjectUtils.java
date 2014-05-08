/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package tzuyu.engine.utils;

/**
 * @author LLT
 * 
 */
public class ObjectUtils {

	public static boolean equalsWithNull(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}
	
	public static int compare(int o1, int o2) {
		return (o1 < o2 ? -1 : (o1 == o2 ? 0 : 1));
	}
	
	public static Class<?> getObjClass(Object obj, Class<?> defaultIfNull) {
		if (obj == null) {
			return defaultIfNull;
		}
		if (obj instanceof Class) {
			return (Class<?>) obj;
		}
		return obj.getClass();
	}
	
	public static boolean isPositive(int val) {
		if (val > 0) {
			return true;
		}
		return false;
	}
	
	public static <T> T returnValueOrAlt(T value, T altValue) {
		if (value == null) {
			return altValue;
		}
		return value;
	}
}