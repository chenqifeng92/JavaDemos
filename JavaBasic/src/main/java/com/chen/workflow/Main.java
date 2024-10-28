package com.chen.workflow;

public class Main {

    /*实现一个任务工作流引擎，可以支持定义不同类型的任务步骤，并能处理任务的流转。
    每个任务步骤应该具有以下属性：
    a. 步骤ID（Step ID）：唯一标识任务步骤。
    b. 步骤名称（Step Name）：描述任务步骤的名称。
    c. 前置条件（Preconditions）：指定该任务步骤可执行的前置任务步骤。如果前置任务步骤未完成，则该步骤不能执行。
    d. 后续步骤（Next Steps）：指定在当前任务步骤完成后，应该执行的下一个任务步骤。
    实现任务执行功能，即按照任务工作流定义的步骤执行任务，并输出任务的流转日志。*/

    public static void main(String[] args) {
        WorkflowEngine workflowEngine = new WorkflowEngine();

        Step step1 = new Step("1", "Start Task");
        Step step2 = new Step("2", "Process Task");
        Step step3 = new Step("3", "End Task");

        step2.addPrecondition("1"); // step2 需要 step1 完成
        step3.addPrecondition("2"); // step3 需要 step2 完成

        step1.addNextStep("2");
        step2.addNextStep("3");

        workflowEngine.addStep(step1);
        workflowEngine.addStep(step2);
        workflowEngine.addStep(step3);

        // 开始工作流，执行第一个步骤
        try {
            workflowEngine.execute("1");
        } catch (WorkflowException e) {
            System.err.println("Workflow execution failed: " + e.getMessage());
        }
    }
}
