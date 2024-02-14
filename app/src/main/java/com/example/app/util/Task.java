package com.example.app.util;

public class Task {
    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    private String taskName;
    private String taskDescription;
    private String taskDate;
    private String taskTime;
    private String taskStatus;

    public Task(String taskName, String taskDescription, String taskDate, String taskTime, String taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskStatus = taskStatus;
    }

    public Task(){}

    public Task setTaskDescription(String taskDescription){
        this.taskDescription = taskDescription;
        return this;
    }

    public Task setTaskName(String taskName){
        this.taskName = taskName;
        return this;
    }

    public Task setTaskDate(String taskDate){
        this.taskDate = taskDate;
        return this;
    }

    public Task setTaskTime(String taskTime){
        this.taskTime = taskTime;
        return this;
    }

    public Task setTaskStatus(String taskStatus){
        this.taskStatus = taskStatus;
        return this;
    }

}
