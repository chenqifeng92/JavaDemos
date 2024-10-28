package com.chen.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowEngine {
    private Map<String, Step> steps;
    private List<String> completedSteps;

    public WorkflowEngine() {
        steps = new HashMap<>();
        completedSteps = new ArrayList<>();
    }

    public void addStep(Step step) {
        steps.put(step.getStepId(), step);
    }

    /**
     * 执行指定的任务步骤
     */
    public void execute(String stepId) throws WorkflowException {
        Step step = steps.get(stepId);
        if (step != null) {
            if (step.canExecute(completedSteps)) {
                System.out.println("Executing step: " + step.getStepName());
                completedSteps.add(stepId);
                for (String nextStepId : step.getNextSteps()) {
                    execute(nextStepId);
                }
                logExecution(step);
            } else {
                throw new WorkflowException("Cannot execute step: " + step.getStepName() + ". Precondition not met.");
            }
        } else {
            throw new WorkflowException("Step not found: " + stepId);
        }
    }

    private void logExecution(Step step) {
        System.out.println("Completed step: " + step.getStepName());
    }
}
