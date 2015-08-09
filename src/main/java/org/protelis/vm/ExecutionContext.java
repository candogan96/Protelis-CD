/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
/**
 * 
 */
package org.protelis.vm;

import it.unibo.alchemist.model.interfaces.IPosition;
import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.vm.util.CodePath;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Danilo Pianini
 *
 */
public interface ExecutionContext {

	/**
	 * @param id
	 *            stack frame type
	 */
	void newCallStackFrame(byte... id);
	
	/**
	 * returns from the last frame.
	 */
	void returnFromCallFrame();
	
	/**
	 * Pushes multiple variables.
	 * 
	 * @param map the variables to push
	 */
	void putMultipleVariables(Map<FasterString, ?> map);
	
	/**
	 * @param name
	 *            variable name
	 * @param value
	 *            variable value
	 * @param canShadow
	 *            if no other variable with the same name exists, this parameter
	 *            is irrelevant. Otherwise, if true, the previous variable will
	 *            be shadowed. If false, the variable will be overridden (with
	 *            possible side effects upon return) instead.
	 */
	void putVariable(FasterString name, Object value, boolean canShadow);

	ExecutionContext restrictDomain(Field f);
	
	/**
	 * @return The unique identifier for the device where execution is taking place.
	 */
	DeviceUID getDeviceUID();

	/**
	 * @return the current time, absolute
	 */
	Number getCurrentTime();
	
	/**
	 * @return the current time, relative to last round
	 */
	Number getDeltaTime();
	
	/**
	 * @param target
	 *            the device to compute the distance to
	 * @return the distance to the device
	 */
	double distanceTo(DeviceUID target);
	
	IPosition getDevicePosition();

	/**
	 * Builds a new {@link Field}, fetching data from all the aligned neighbors.
	 * A neighbor is considered to be aligned it it has reached the exact same
	 * {@link CodePath}. The field will always contain at least one value,
	 * namely the value of the local device.
	 * 
	 * @param localValue
	 *            a {@link Supplier} which will be used to compute the local
	 *            value for this field
	 * @return a new {@link Field} containing the local device value and the
	 *         values for any of the aligned neighbors
	 */
	<T> Field buildField(Function<T, ?> computeValue, T localValue);

	double nextRandomDouble();

	Object getVariable(FasterString name);
	
	/**
	 * @param id
	 *            the variable name
	 * @return true if the variable is present
	 */
	boolean hasEnvironmentVariable(final String id);
	
	/**
	 * @param id
	 *            the variable name
	 * @return the value of the variable if present, false otherwise
	 */
	Object getEnvironmentVariable(final String id);

	/**
	 * @param id
	 *            the variable name
	 * @param defaultValue
	 *            a parameterizable default value
	 * @return the value of the variable if present, defaultValue otherwise
	 */
	Object getEnvironmentVariable(final String id, final Object defaultValue);

	/**
	 * @param id
	 *            the variable name
	 * @param v
	 *            the value that should be associated
	 * @return
	 */
	boolean putEnvironmentVariable(final String id, final Object v);
	
	/**
	 * @param id
	 *            the variable name
	 * @return
	 */
	Object removeEnvironmentVariable(final String id);
	
	void setup();

	void commit();

	void setAvailableFunctions(Map<FasterString, FunctionDefinition> knownFunctions);
	
}
