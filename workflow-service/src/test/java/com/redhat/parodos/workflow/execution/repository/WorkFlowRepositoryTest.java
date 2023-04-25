package com.redhat.parodos.workflow.execution.repository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redhat.parodos.project.entity.Project;
import com.redhat.parodos.workflow.definition.entity.WorkFlowDefinition;
import com.redhat.parodos.workflow.enums.WorkFlowStatus;
import com.redhat.parodos.workflow.execution.entity.WorkFlowExecution;
import com.redhat.parodos.workflow.execution.entity.WorkFlowExecutionContext;
import com.redhat.parodos.workflows.work.WorkContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class WorkFlowRepositoryTest {

	@Autowired
	private WorkFlowRepository workFlowRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void injectedComponentsAreNotNull() {
		assertNotNull(workFlowRepository);
		assertNotNull(entityManager);
	}

	@Test
	public void testFindAll() {
		// given
		createWorkFlowExecution();
		createWorkFlowExecution();

		// when
		List<WorkFlowExecution> workFlowExecutions = workFlowRepository.findAll();

		// then
		assertNotNull(workFlowExecutions);
		assertEquals(2, workFlowExecutions.size());
	}

	@Test
	public void testSave() {
		// given
		WorkFlowExecution workFlowExecution = WorkFlowExecution.builder()
				.workFlowDefinitionId(createWorkFlowDefinition().getId()).status(WorkFlowStatus.IN_PROGRESS)
				.projectId(createProject().getId()).build();
		List<WorkFlowExecution> workFlowExecutions = workFlowRepository.findAll();
		assertTrue(workFlowExecutions.isEmpty());

		// when
		WorkFlowExecution flowExecution = workFlowRepository.save(workFlowExecution);

		// then
		flowExecution = workFlowRepository.getById(workFlowExecution.getId());
		assertNotNull(flowExecution);
		assertNotNull(flowExecution.getId());
		assertEquals(workFlowExecution.getWorkFlowDefinitionId(), flowExecution.getWorkFlowDefinitionId());
		assertEquals(workFlowExecution.getProjectId(), flowExecution.getProjectId());
		assertEquals(WorkFlowStatus.IN_PROGRESS, flowExecution.getStatus());
	}

	@Test
	public void testSaveWithExecutionContext() {
		// given
		WorkFlowExecution workFlowExecution = createWorkFlowExecution();
		workFlowExecution = workFlowRepository.save(workFlowExecution);
		WorkContext WorkContext = new WorkContext();
		WorkContext.put("test_key", "test_value");
		WorkFlowExecutionContext workContext = WorkFlowExecutionContext.builder()
				.mainWorkFlowExecution(workFlowExecution).workContext(WorkContext).build();
		workFlowExecution.setWorkFlowExecutionContext(workContext);

		// when
		workFlowRepository.save(workFlowExecution);

		// then
		WorkFlowExecution flowExecution = workFlowRepository.getById(workFlowExecution.getId());
		assertNotNull(flowExecution);
		assertNotNull(flowExecution.getWorkFlowExecutionContext());
		assertNotNull(flowExecution.getWorkFlowExecutionContext().getWorkContext());
		assertEquals("test_value", flowExecution.getWorkFlowExecutionContext().getWorkContext().get("test_key"));
	}

	@Test
	public void testFindByMainWorkFlowExecution() {
		// given
		WorkFlowExecution mainWorkFlowExecution = createWorkFlowExecution();
		WorkFlowExecution workFlowExecution = WorkFlowExecution.builder()
				.workFlowDefinitionId(createWorkFlowDefinition().getId()).status(WorkFlowStatus.IN_PROGRESS)
				.projectId(createProject().getId()).mainWorkFlowExecution(mainWorkFlowExecution).build();
		workFlowExecution = workFlowRepository.save(workFlowExecution);

		// when
		List<WorkFlowExecution> workFlowExecutions = workFlowRepository
				.findByMainWorkFlowExecution(mainWorkFlowExecution);

		// then
		assertNotNull(workFlowExecutions);
		assertEquals(1, workFlowExecutions.size());
		assertEquals(workFlowExecution, workFlowExecutions.get(0));
	}

	@Test
	public void testFindByStatusInAndIsMain() {
		// given
		WorkFlowExecution mainWorkFlowExecution = createWorkFlowExecution();
		WorkFlowExecution workFlowExecution = WorkFlowExecution.builder()
				.workFlowDefinitionId(createWorkFlowDefinition().getId()).status(WorkFlowStatus.IN_PROGRESS)
				.projectId(createProject().getId()).mainWorkFlowExecution(mainWorkFlowExecution).build();
		workFlowRepository.save(workFlowExecution);

		// when
		List<WorkFlowExecution> workFlowExecutions = workFlowRepository
				.findByStatusInAndIsMain(List.of(WorkFlowStatus.IN_PROGRESS));

		// then
		assertNotNull(workFlowExecutions);
		assertEquals(1, workFlowExecutions.size());
		assertEquals(mainWorkFlowExecution, workFlowExecutions.get(0));
	}

	private Project createProject() {
		Project project = Project.builder().name(UUID.randomUUID().toString()).build();
		return entityManager.persist(project);
	}

	private WorkFlowDefinition createWorkFlowDefinition() {
		WorkFlowDefinition workFlowDefinition = WorkFlowDefinition.builder().name(UUID.randomUUID().toString())
				.numberOfWorks(1).parameters("{}").build();
		return entityManager.persist(workFlowDefinition);
	}

	private WorkFlowExecution createWorkFlowExecution() {
		WorkFlowExecution workFlowExecution = WorkFlowExecution.builder()
				.workFlowDefinitionId(createWorkFlowDefinition().getId()).status(WorkFlowStatus.IN_PROGRESS)
				.projectId(createProject().getId()).build();
		return entityManager.persist(workFlowExecution);
	}

}