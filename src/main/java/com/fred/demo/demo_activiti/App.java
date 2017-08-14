package com.fred.demo.demo_activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;

/**
 * Hello world!
 * 
 */
public class App {

    private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    public static void main(String[] args) {
        Deployment deployment = processEngine.getRepositoryService()
                // 获取流程定义和部署相关的Service
                .createDeployment()
                // 创建部署对象
                .addClasspathResource("MyProcess.bpmn").addClasspathResource("MyProcess.png")
                .deploy();// 完成部署

        System.out.println("部署ID：" + deployment.getId());// 部署ID:1
        System.out.println("部署时间：" + deployment.getDeploymentTime());// 部署时间
    }
}
