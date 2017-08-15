package com.fred.demo.demo_activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AskForLeave {

    private static final Logger LOGGER = LoggerFactory.getLogger(AskForLeave.class);

    private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    private static RepositoryService repositoryService = processEngine.getRepositoryService();
    private static RuntimeService runtimeService = processEngine.getRuntimeService();
    private static TaskService taskService = processEngine.getTaskService();

    public static void main(String[] args) {

        deploy();
        start();
        apply();
        step2Boss();
        step2manager();
    }

    /**
     * 部署流程定义
     */
    public static void deploy() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("leave.bpmn").addClasspathResource("leave.png").deploy();
        LOGGER.info("Deployment id : {}, Deployment Time: {}", deployment.getId(),
                deployment.getDeploymentTime());
    }

    /**
     * 启动一个请假流程
     */
    public static void start() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employeeId", "Fred");
        String processId = runtimeService.startProcessInstanceByKey("leaveProcess", variables)
                .getId();
        LOGGER.info("Start Process ID :" + processId);
    }

    /**
     * 提交请假申请
     */
    public static void apply() {
        LOGGER.info("提交请假申请开始");
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("Fred").list();
        LOGGER.info("任务个数:{}", tasks.size());
        for (Task task : tasks) {
            LOGGER.info("Fred的任务taskname:{}, ,id:{}", task.getName(), task.getId());
            taskService.setVariable(task.getId(), "day", 2);// 设置请假天数
            taskService.complete(task.getId());// 完成任务
            LOGGER.info("请假{}天", 2);
        }
        LOGGER.info("提交请假申请完成");
    }

    public static void step2manager() {
        LOGGER.info("经理组审批流程开始");
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("wangzong").list();// 经理组的任务
        LOGGER.info("经理组的任务数量：", tasks.size());
        for (Task task : tasks) {
            LOGGER.info("经理组的任务taskname:{} ,id:{}", task.getName(), task.getId());
            taskService.claim(task.getId(), "李四");// 申领任务
            taskService.setVariable(task.getId(), "flag", false);// true批准，false不批准
            Object applyUser = taskService.getVariable(task.getId(), "employeeId");
            Object day = taskService.getVariable(task.getId(), "day");
            LOGGER.info("{}请假{}天", applyUser, day);
            taskService.complete(task.getId());// 完成任务
        }
        LOGGER.info("经理组审批流程结束*");
    }

    public static void step2Boss() {
        LOGGER.info("boss组审批流程开始");
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("boss").list();// 总经理组的任务
        LOGGER.info("boss组的任务数量：{}", tasks.size());

        for (Task task : tasks) {
            LOGGER.info("manager的任务taskname:{}, id:{}", task.getName(), task.getId());
            taskService.claim(task.getId(), "王五");// 申领任务
            taskService.setVariable(task.getId(), "flag", true);// true批准,false不批准
            Object applyUser = taskService.getVariable(task.getId(), "employeeId");
            Object day = taskService.getVariable(task.getId(), "day");
            LOGGER.info("{}请假{}天", applyUser, day);
            taskService.complete(task.getId());// 完成任务
        }
        LOGGER.info("总经理组审批流程结束");
    }
}
