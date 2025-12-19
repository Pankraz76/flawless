/*
 * Copyright 2016-2025 DiffPlug
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
package com.diffplug.spotless.kotlin;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.diffplug.spotless.FormatterStep;
import com.diffplug.spotless.ResourceHarness;
import com.diffplug.spotless.SerializableEqualityTester;
import com.diffplug.spotless.StepHarness;
import com.diffplug.spotless.TestProvisioner;

class KtfmtStepTest extends ResourceHarness {
	@Test
	void behavior() throws Exception {
		var step = KtfmtStep.create(TestProvisioner.mavenCentral());
		StepHarness.forStep(step).testResource("kotlin/ktfmt/basic.dirty", "kotlin/ktfmt/basic.clean");
	}

	@Test
	void behaviorWithOptions() {
		var options = new KtfmtStep.KtfmtFormattingOptions();
		options.setMaxWidth(100);
		var step = KtfmtStep.create(KtfmtStep.defaultVersion(), TestProvisioner.mavenCentral(), KtfmtStep.Style.GOOGLE, options);
		StepHarness.forStep(step).testResource("kotlin/ktfmt/basic.dirty", "kotlin/ktfmt/basic.clean");
	}

	@Test
	void dropboxStyle_0_16() throws Exception {
		var options = new KtfmtStep.KtfmtFormattingOptions();
		var step = KtfmtStep.create("0.16", TestProvisioner.mavenCentral(), KtfmtStep.Style.DROPBOX, options);
		StepHarness.forStep(step).testResource("kotlin/ktfmt/basic.dirty", "kotlin/ktfmt/basic-dropboxstyle.clean");
	}

	@Test
	void dropboxStyle_0_18() throws Exception {
		var options = new KtfmtStep.KtfmtFormattingOptions();
		var step = KtfmtStep.create("0.18", TestProvisioner.mavenCentral(), KtfmtStep.Style.DROPBOX, options);
		StepHarness.forStep(step).testResource("kotlin/ktfmt/basic.dirty", "kotlin/ktfmt/basic-dropboxstyle.clean");
	}

	@Test
	void dropboxStyle_0_22() throws Exception {
		var options = new KtfmtStep.KtfmtFormattingOptions();
		var step = KtfmtStep.create("0.22", TestProvisioner.mavenCentral(), KtfmtStep.Style.DROPBOX, options);
		StepHarness.forStep(step).testResource("kotlin/ktfmt/basic.dirty", "kotlin/ktfmt/basic-dropboxstyle.clean");
	}

	@Test
	void dropboxStyle_0_50() throws Exception {
		var options = new KtfmtStep.KtfmtFormattingOptions();
		var step = KtfmtStep.create("0.50", TestProvisioner.mavenCentral(), KtfmtStep.Style.DROPBOX, options);
		StepHarness.forStep(step).testResource("kotlin/ktfmt/basic.dirty", "kotlin/ktfmt/basic-dropboxstyle.clean");
	}

	@Test
	void behaviorWithTrailingCommas() throws Exception {
		var options = new KtfmtStep.KtfmtFormattingOptions();
		options.setTrailingCommaManagementStrategy(KtfmtStep.TrailingCommaManagementStrategy.COMPLETE);
		var step = KtfmtStep.create("0.49", TestProvisioner.mavenCentral(), KtfmtStep.Style.DROPBOX, options);
		StepHarness.forStep(step).testResource("kotlin/ktfmt/trailing-commas.dirty", "kotlin/ktfmt/trailing-commas.clean");
	}

	@Test
	void behaviorWithTrailingCommaManagementStrategyOnlyAdd() {
		var options = new KtfmtStep.KtfmtFormattingOptions();
		options.setTrailingCommaManagementStrategy(KtfmtStep.TrailingCommaManagementStrategy.ONLY_ADD);
		var step = KtfmtStep.create("0.58", TestProvisioner.mavenCentral(), KtfmtStep.Style.KOTLINLANG, options);
		StepHarness.forStep(step).testResource(
				"kotlin/ktfmt/trailing-commas-only-add.dirty",
				"kotlin/ktfmt/trailing-commas-only-add.clean");
	}

	@Test
	void trailingCommaManagementStrategyOnlyAddUnsupportedBefore_0_57() {
		var options = new KtfmtStep.KtfmtFormattingOptions();
		options.setTrailingCommaManagementStrategy(KtfmtStep.TrailingCommaManagementStrategy.ONLY_ADD);
		var step = KtfmtStep.create("0.56", TestProvisioner.mavenCentral(), KtfmtStep.Style.KOTLINLANG, options);

		assertThatThrownBy(() -> StepHarness.forStep(step))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("ONLY_ADD");
	}

	@Test
	void equality() throws Exception {
		new SerializableEqualityTester() {
			String version = "0.18";

			@Override
			protected void setupTest(API api) {
				// same version == same
				api.areDifferentThan();
				// change the version, and it's different
				version = KtfmtStep.defaultVersion();
				api.areDifferentThan();
			}

			@Override
			protected FormatterStep create() {
				var finalVersion = this.version;
				return KtfmtStep.create(finalVersion, TestProvisioner.mavenCentral());
			}
		}.testEquals();
	}
}
