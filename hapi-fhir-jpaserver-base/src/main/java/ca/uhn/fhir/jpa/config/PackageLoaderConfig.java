/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.packages.loader.PackageLoaderSvc;
import ca.uhn.fhir.jpa.packages.loader.PackageResourceParsingSvc;
import org.hl7.fhir.utilities.npm.PackageClient;
import org.hl7.fhir.utilities.npm.PackageServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PackageLoaderConfig {

	@Bean
	public PackageLoaderSvc packageLoaderSvc() {
		PackageLoaderSvc svc = new PackageLoaderSvc();
		svc.getPackageServers().clear();
		svc.getPackageServers().add(PackageServer.primaryServer());
		svc.getPackageServers().add(PackageServer.secondaryServer());
		return svc;
	}

	@Bean
	public PackageResourceParsingSvc resourceParsingSvc(FhirContext theContext) {
		return new PackageResourceParsingSvc(theContext);
	}
}
