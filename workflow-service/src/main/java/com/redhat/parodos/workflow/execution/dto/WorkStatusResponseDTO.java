/*
 * Copyright (c) 2022 Red Hat Developer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.parodos.workflow.execution.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.parodos.workflow.enums.WorkStatus;
import com.redhat.parodos.workflow.enums.WorkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Workflow response dto
 *
 * @author Richard Wang (Github: richardw98)
 * @author Annel Ketcha (Github: anludke)
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkStatusResponseDTO {

	private String name;

	private WorkType type;

	private WorkStatus status;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<WorkStatusResponseDTO> works;

}