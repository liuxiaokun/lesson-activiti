package com.fred.demo.demo_activiti.serviceTask;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fred.demo.demo_activiti.AskForLeave;

public class ServiceTask1 implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(AskForLeave.class);

    public void execute(DelegateExecution execution) {
        LOGGER.info("ServiceTask1 start");

        LOGGER.info(this.toString());
        Map<String, Object> variables = execution.getVariables();
        LOGGER.info("variables:{}", variables);

        LOGGER.info("ServiceTask1 end");

    }

}
