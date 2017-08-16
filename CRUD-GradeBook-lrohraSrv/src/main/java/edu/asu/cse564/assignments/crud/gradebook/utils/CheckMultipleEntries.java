/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.assignments.crud.gradebook.utils;

import edu.asu.cse564.assignments.crud.gradebook.lrohrasrv.model.GradeBook;
import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author laveeshrohra
 */
public class CheckMultipleEntries {
    private static final Logger LOG = LoggerFactory.getLogger(CheckMultipleEntries.class);
    
    public static int checkEntryExists(HashMap<Integer, GradeBook> entries, GradeBook entry){
        LOG.info("Checking if entry exists");
        
        Iterator<Integer> it = entries.keySet().iterator();
        while(it.hasNext()){
            int taskId = it.next();
            GradeBook task = entries.get(taskId);
            if(task.getWorkTask().equals(entry.getWorkTask()) && task.getStudentId().equals(entry.getStudentId())){
                LOG.debug("Duplicate entry!");
                return taskId;
            }
        }
        return -1;
    }
}
