package com.fred.demo.demo_activiti.serviceTask;

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

        LOGGER.info("ServiceTask1 end");

    }

}
