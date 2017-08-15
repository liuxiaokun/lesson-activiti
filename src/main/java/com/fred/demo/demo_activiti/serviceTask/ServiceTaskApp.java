package com.fred.demo.demo_activiti.serviceTask;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fred.demo.demo_activiti.AskForLeave;

public class ServiceTaskApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(AskForLeave.class);

    private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    private static RepositoryService repositoryService = processEngine.getRepositoryService();
    private static RuntimeService runtimeService = processEngine.getRuntimeService();
    private static TaskService taskService = processEngine.getTaskService();
    
    public static void main(String[] args) {

        deploy();
        start();
    }
    
    public static void deploy() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("serviceTask.bpmn").addClasspathResource("serviceTask.png").deploy();
        LOGGER.info("Deployment id : {}, Deployment Time: {}", deployment.getId(),
                deployment.getDeploymentTime());
    }

    /**
     * 启动一个流程
     */
    public static void start() {
        String processId = runtimeService.startProcessInstanceByKey("serviceTask").getId();
        LOGGER.info("Start Process ID :" + processId);
    }

}
