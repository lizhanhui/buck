/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.ocaml;

import com.facebook.buck.rules.AbstractBuildRule;
import com.facebook.buck.rules.AddToRuleKey;
import com.facebook.buck.rules.BuildContext;
import com.facebook.buck.rules.BuildRuleParams;
import com.facebook.buck.rules.BuildableContext;
import com.facebook.buck.step.Step;
import com.facebook.buck.step.fs.MkdirStep;
import com.google.common.collect.ImmutableList;

import java.nio.file.Path;

public class OcamlMLCompile extends AbstractBuildRule {
  @AddToRuleKey
  private final OcamlMLCompileStep.Args args;

  public OcamlMLCompile(
      BuildRuleParams params,
      OcamlMLCompileStep.Args args) {
    super(params);
    this.args = args;
  }

  @Override
  public ImmutableList<Step> getBuildSteps(
      BuildContext context,
      BuildableContext buildableContext) {
    for (Path artifact : args.getAllOutputs()) {
      buildableContext.recordArtifact(artifact);
    }
    return ImmutableList.of(
      new MkdirStep(getProjectFilesystem(), args.output.getParent()),
      new OcamlMLCompileStep(
          getProjectFilesystem().getRootPath(), context.getSourcePathResolver(), args)
    );
  }

  @Override
  public Path getPathToOutput() {
    return args.output;
  }

  @Override
  public boolean isCacheable() {
    // Intermediate OCaml rules are not cacheable because the compiler is not deterministic.
    return false;
  }
}
