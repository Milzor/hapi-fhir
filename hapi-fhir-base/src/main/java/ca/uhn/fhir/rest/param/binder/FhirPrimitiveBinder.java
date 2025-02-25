/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.param.binder;

import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.util.ReflectionUtil;

public final class FhirPrimitiveBinder extends BaseJavaPrimitiveBinder<IPrimitiveType<?>> {
	
	private Class<IPrimitiveType<?>> myType;

	public FhirPrimitiveBinder(Class<IPrimitiveType<?>> theType) {
		myType = theType;
	}

	@Override
	protected String doEncode(IPrimitiveType<?> theString) {
		return theString.getValueAsString();
	}

	@Override
	protected IPrimitiveType<?> doParse(String theString) {
		IPrimitiveType<?> instance = ReflectionUtil.newInstance(myType);
		instance.setValueAsString(theString);
		return instance;
	}


}
