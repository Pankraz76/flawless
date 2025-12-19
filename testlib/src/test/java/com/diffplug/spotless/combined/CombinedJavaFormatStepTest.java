/*
 * Copyright 2023-2025 DiffPlug
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
package com.diffplug.spotless.combined;

import static com.diffplug.spotless.TestProvisioner.mavenCentral;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.diffplug.spotless.FormatterStep;
import com.diffplug.spotless.ResourceHarness;
import com.diffplug.spotless.StepHarness;
import com.diffplug.spotless.generic.EndWithNewlineStep;
import com.diffplug.spotless.generic.FenceStep;
import com.diffplug.spotless.generic.IndentStep;
import com.diffplug.spotless.generic.TrimTrailingWhitespaceStep;
import com.diffplug.spotless.java.GoogleJavaFormatStep;
import com.diffplug.spotless.java.ImportOrderStep;
import com.diffplug.spotless.java.RemoveUnusedImportsStep;
import com.diffplug.spotless.yaml.SerializeToByteArrayHack;

public class CombinedJavaFormatStepTest extends ResourceHarness {

	@Test
	void checkIssue1679() {
		var gjf = GoogleJavaFormatStep.create(GoogleJavaFormatStep.defaultVersion(), "AOSP", mavenCentral());
		var indentWithSpaces = IndentStep.Type.SPACE.create();
		var importOrder = ImportOrderStep.forJava().createFrom();
		var removeUnused = RemoveUnusedImportsStep.create(mavenCentral());
		var trimTrailing = TrimTrailingWhitespaceStep.create();
		var endWithNewLine = EndWithNewlineStep.create();
		var toggleOffOnPair = FenceStep.named(FenceStep.defaultToggleName()).openClose("formatting:off", "formatting:on");
		try (var formatter = StepHarness.forSteps(
				toggleOffOnPair.preserveWithin(List.of(
						new SerializeToByteArrayHack(),
						gjf,
						indentWithSpaces,
						importOrder,
						removeUnused,
						trimTrailing,
						endWithNewLine)))) {
			formatter.testResource("combined/issue1679.dirty", "combined/issue1679.clean");
		}
	}
}
