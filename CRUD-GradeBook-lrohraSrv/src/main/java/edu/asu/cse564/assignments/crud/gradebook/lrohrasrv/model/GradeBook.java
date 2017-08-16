/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.assignments.crud.gradebook.lrohrasrv.model;

/**
 *
 * @author laveeshrohra
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GradeBook {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradeBook.class);

    @JsonProperty("id")
    private int taskId;
    
    @JsonProperty("workTask")
    private String workTask;
    
    @JsonProperty("grade")
    private String grade;
    
    @JsonProperty("feedback")
    private String feedback;
    
    @JsonProperty("studentId")
    private String studentId;
    
    @JsonProperty("appeal")
    private boolean appeal = false;
    
    public GradeBook(){
        LOG.info("Creating a gradebook entry");
    }
    
    public void setTaskId(int id){
        LOG.info("Setting task id: {}", id);
        this.taskId = id;
    }
    
    public void setWorkTask(String task){
        LOG.info("Setting work task: {}", task);
        this.workTask = task;
    }
    
    public void setGrade(String grade){
        LOG.info("Setting grade: {}", grade);
        this.grade = grade;
    }
    
    public void setFeedback(String feedback){
        LOG.info("Setting feedback: {}", feedback);
        this.feedback = feedback;
    }
    
    public void setStudentId(String id){
        LOG.info("Setting Student ID: {}", id);
        this.studentId = id;
    }
    
    public void setAppeal(boolean appeal){
        LOG.info("Setting Appealed Status: {}", appeal);
        this.appeal = appeal;
    }
    
    public int getId(){
        LOG.info("Getting ID: {}", this.taskId);
        return this.taskId;
    }
    
    public String getWorkTask(){
        LOG.info("Getting work task: {}", this.workTask);
        return this.workTask;
    }
    
    public String getGrade(){
        LOG.info("Getting grade: {}", this.grade);
        return this.grade;
    }
    
    public String getFeedback(){
        LOG.info("Getting feedback: {}", this.feedback);
        return this.feedback;
    }
    
    public String getStudentId(){
        LOG.info("Getting Student ID: {}", this.studentId);
        return this.studentId;
    }
    
    public boolean getAppeal(){
        LOG.info("Getting Appeal Status: {}", this.appeal);
        return this.appeal;
    }
}
