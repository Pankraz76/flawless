/*
 * Copyright 2016-2024 DiffPlug
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
package com.diffplug.spotless.java;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.diffplug.spotless.FormatterStep;
import com.diffplug.spotless.ResourceHarness;
import com.diffplug.spotless.SerializableEqualityTester;
import com.diffplug.spotless.StepHarness;

class ImportOrderStepTest extends ResourceHarness {
	@Test
	void sortImportsDefault() {
		var step = ImportOrderStep.forJava().createFrom();
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedImports.test", "java/importsorter/JavaCodeSortedImportsDefault.test");
	}

	@Test
	void sortImportsFromArray() {
		var step = ImportOrderStep.forJava().createFrom("java", "javax", "org", "\\#com");
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedImports.test", "java/importsorter/JavaCodeSortedImports.test");
	}

	@Test
	void sortImportsFromArrayWithSubgroups() {
		var step = ImportOrderStep.forJava().createFrom("java|javax", "org|\\#com", "\\#");
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedImportsSubgroups.test", "java/importsorter/JavaCodeSortedImportsSubgroups.test");
	}

	@Test
	void sortImportsFromArrayWithDuplicateEntries() {
		var step = ImportOrderStep.forJava().createFrom("java|javax", "java", "org|\\#com", "\\#");
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedImportsSubgroups.test", "java/importsorter/JavaCodeSortedImportsSubgroups.test");
	}

	@Test
	void sortImportsFromArrayWithSubgroupsLeadingCatchAll() {
		var step = ImportOrderStep.forJava().createFrom("\\#|");
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedImportsSubgroups.test", "java/importsorter/JavaCodeSortedImportsSubgroupsLeadingCatchAll.test");
	}

	@Test
	void sortImportsFromFile() {
		var step = ImportOrderStep.forJava().createFrom(createTestFile("java/importsorter/import.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedImports.test", "java/importsorter/JavaCodeSortedImports.test");
	}

	@Test
	void sortImportsUnmatched() {
		var step = ImportOrderStep.forJava().createFrom(createTestFile("java/importsorter/import_unmatched.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedImportsUnmatched.test", "java/importsorter/JavaCodeSortedImportsUnmatched.test");
	}

	@Test
	void sortImportsWildcardsLast() {
		var step = ImportOrderStep.forJava().createFrom(true, true, null, null);
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedImports.test", "java/importsorter/JavaCodeSortedImportsWildcardsLast.test");
	}

	@Test
	void removeDuplicates() {
		var step = ImportOrderStep.forJava().createFrom(createTestFile("java/importsorter/import_unmatched.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeSortedDuplicateImportsUnmatched.test", "java/importsorter/JavaCodeSortedImportsUnmatched.test");
	}

	@Test
	void removeComments() {
		var step = ImportOrderStep.forJava().createFrom(createTestFile("java/importsorter/import.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeImportComments.test", "java/importsorter/JavaCodeSortedImports.test");
	}

	@Test
	void misplacedImports() {
		var step = ImportOrderStep.forJava().createFrom(createTestFile("java/importsorter/import.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedMisplacedImports.test", "java/importsorter/JavaCodeSortedMisplacedImports.test");
	}

	@Test
	void empty() {
		var step = ImportOrderStep.forJava().createFrom(createTestFile("java/importsorter/import.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeEmptyFile.test", "java/importsorter/JavaCodeEmptyFile.test");
	}

	@Test
	void lexicographicSort() {
		var step = ImportOrderStep.forJava().createFrom(false, false, null, null, createTestFile("java/importsorter/import.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedSemanticSort.test", "java/importsorter/JavaCodeSortedLexicographic.test");
	}

	@Test
	void semanticSort() {
		var step = ImportOrderStep.forJava().createFrom(false, true, Set.of("com.sun.jna.platform.win32.COM"),
				Set.of("com.example.b"), createTestFile("java/importsorter/import.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/JavaCodeUnsortedSemanticSort.test", "java/importsorter/JavaCodeSortedSemanticSort.test");
	}

	@Test
	void groovyImports() {
		var step = ImportOrderStep.forGroovy().createFrom(createTestFile("java/importsorter/import.properties"));
		StepHarness.forStep(step).testResource("java/importsorter/GroovyCodeUnsortedMisplacedImports.test", "java/importsorter/GroovyCodeSortedMisplacedImports.test");
	}

	@Test
	void doesntThrowIfImportOrderIsntSerializable() {
		ImportOrderStep.forJava().createFrom("java", "javax", "org", "\\#com");
	}

	@Test
	void equality() throws Exception {
		new SerializableEqualityTester() {
			String[] imports = {};

			@Override
			protected void setupTest(API api) {
				// same version == same
				api.areDifferentThan();
				// change the version, and it's different
				imports = new String[]{"a"};
				api.areDifferentThan();
				// change the version, and it's different
				imports = new String[]{"b"};
				api.areDifferentThan();
				// change the version, and it's different
				imports = new String[]{"a", "b"};
				api.areDifferentThan();
			}

			@Override
			protected FormatterStep create() {
				return ImportOrderStep.forJava().createFrom(imports);
			}
		}.testEquals();
	}

}
