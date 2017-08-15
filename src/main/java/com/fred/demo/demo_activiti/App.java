package com.fred.demo.demo_activiti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 * 
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    public static void main(String[] args) throws Exception {
        process();
        viewPic();

    }

    private static void process() {
        // 部署
        Deployment deployment = processEngine.getRepositoryService()
                // 获取流程定义和部署相关的Service
                .createDeployment()
                // 创建部署对象
                .addClasspathResource("MyProcess.bpmn").addClasspathResource("MyProcess.png")
                .deploy();// 完成部署

        LOGGER.info("部署ID:{}, 部署时间：{}", deployment.getId(), deployment.getDeploymentTime());// 部署ID:1

        // 启动
        String processDefinitionKey = "myProcess";// 流程定义的key,也就是bpmn中存在的ID

        ProcessInstance pi = processEngine.getRuntimeService()// 管理流程实例和执行对象，也就是表示正在执行的操作
                .startProcessInstanceByKey(processDefinitionKey);// //按照流程定义的key启动流程实例

        LOGGER.info("流程实例ID：{}", pi.getId());// 流程实例ID：101
        LOGGER.info("流程实例ID：{}", pi.getProcessInstanceId());// 流程实例ID：101
        LOGGER.info("流程实例ID：{}", pi.getProcessDefinitionId());// myMyHelloWorld:1:4

        // 查看当前任务办理人的个人任务
        String assignee = "zhangsan";// 当前任务办理人
        List<Task> tasks = processEngine.getTaskService()// 与任务相关的Service
                .createTaskQuery()// 创建一个任务查询对象
                .taskAssignee(assignee).list();

        if (tasks != null && tasks.size() > 0) {

            for (Task task : tasks) {
                LOGGER.info("任务ID:{}", task.getId());
                LOGGER.info("任务的办理人:{}", task.getAssignee());
                LOGGER.info("任务名称:{}", task.getName());
                LOGGER.info("任务的创建时间:{}", task.getCreateTime());
                LOGGER.info("流程实例ID:{}", task.getProcessInstanceId());

                // 完成任务
                String taskID = task.getId();
                processEngine.getTaskService().complete(taskID);
                LOGGER.info("完成任务：{}", taskID);
            }
        }

        //
    }

    
    // 取出流程图，感觉没啥用，工程里面有
    public static void viewPic() throws IOException {
        /** 将生成图片放到文件夹下 */
        String deploymentId = "5001";
        // 获取图片资源名称
        List<String> list = processEngine.getRepositoryService()//
                .getDeploymentResourceNames(deploymentId);
        // 定义图片资源的名称
        String resourceName = "";
        if (list != null && list.size() > 0) {
            for (String name : list) {
                if (name.indexOf(".png") >= 0) {
                    resourceName = name;
                }
            }
        }

        // 获取图片的输入流
        InputStream in = processEngine.getRepositoryService()//
                .getResourceAsStream(deploymentId, resourceName);

        // 将图片生成到D盘的目录下
        File file = new File("D:/" + resourceName);
        // 将输入流的图片写到D盘下
        FileUtils.copyInputStreamToFile(in, file);
    }
    
}
