/*
 * Copyright 2016 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.spotless.maven;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Paths;

import org.codehaus.plexus.resource.ResourceManager;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class FileLocatorTest {

	private final ResourceManager resourceManager = mock(ResourceManager.class);
	private final FileLocator fileLocator = new FileLocator(resourceManager);

	@Test
	public void locateEmptyString() {
		assertNull(fileLocator.locateFile(""));
	}

	@Test
	public void locateNull() {
		assertNull(fileLocator.locateFile(null));
	}

	@Test
	public void locateValidFile() throws Exception {
		String path = Paths.get("tmp", "configs", "my-config.xml").toString();
		File tmpOutputFile = new File("tmp-my-config.xml");
		when(resourceManager.getResourceAsFile(any(), any())).thenReturn(tmpOutputFile);

		File locatedFile = fileLocator.locateFile(path);

		assertEquals(tmpOutputFile, locatedFile);

		ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
		verify(resourceManager).getResourceAsFile(eq(path), argCaptor.capture());
		assertThat(argCaptor.getValue()).startsWith("my-config").endsWith(".xml");
	}
}
