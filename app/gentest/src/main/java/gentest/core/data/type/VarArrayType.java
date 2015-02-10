/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package gentest.core.data.type;

import java.lang.reflect.Type;

import sav.common.core.SavRtException;
import sav.common.core.utils.SignatureUtils;

/**
 * @author LLT
 *
 */
public class VarArrayType implements IType {
	private IType componentType;
	private Class<?> rawType;
	
	public VarArrayType(IType componentType) {
		this.componentType = componentType;
	}

	@Override
	public boolean isArray() {
		return true;
	}
	
	@Override
	public IType getComponentType() {
		return componentType;
	}

	@Override
	public Type getType() {
		return componentType.getType();
	}

	@Override
	public Class<?> getRawType() {
		if (rawType == null) {
			String className = "";
			try {
				if (componentType.isArray()) {
					className = "[" + componentType.getRawType().getName();
				} else {
					className = "[" + SignatureUtils.getSignature(
									componentType.getRawType()).replace('/', '.');
				}
				rawType = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new SavRtException("class not found - " + className);
			}
		}
		return rawType;
	}

	@Override
	public IType resolveType(Class<?> a) {
		return componentType.resolveType(a);
	}

	@Override
	public IType resolveType(Type type) {
		return componentType.resolveType(type);
	}

	@Override
	public IType[] resolveType(Type[] type) {
		return componentType.resolveType(type);
	}

	@Override
	public IType resolveSubType(Class<?> a) {
		return componentType.resolveSubType(a);
	}

	@Override
	public String toString() {
		return "VarArrayType [componentType=" + componentType + "]";
	}
	
}
