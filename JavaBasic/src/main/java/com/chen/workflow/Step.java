package com.chen.workflow;

import java.util.ArrayList;
import java.util.List;

public class Step {
    private String stepId;
    private String stepName;
    private List<String> preconditions;
    private List<String> nextSteps;

    public Step(String stepId, String stepName) {
        this.stepId = stepId;
        this.stepName = stepName;
        this.preconditions = new ArrayList<>();
        this.nextSteps = new ArrayList<>();
    }

    public void addPrecondition(String precondition) {
        preconditions.add(precondition);
    }

    public void addNextStep(String nextStep) {
        nextSteps.add(nextStep);
    }

    public String getStepId() {
        return stepId;
    }

    public String getStepName() {
        return stepName;
    }

    public List<String> getPreconditions() {
        return preconditions;
    }

    public List<String> getNextSteps() {
        return nextSteps;
    }

    /**
     * 判断该步骤是否可以执行
     */
    public boolean canExecute(List<String> completedSteps) {
        return completedSteps.containsAll(preconditions);
    }
}
