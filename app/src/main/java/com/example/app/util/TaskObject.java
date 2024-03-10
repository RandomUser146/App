package com.example.app.util;

public class TaskObject{
    public int taskid;
    public int task = -1;
    public int amount = 0;
    public long scheduledTime = 0;
    public int taskStatus = 0;
    private int index = -1;

    public TaskObject(int taskID, int taskAmount, long scheduledTime, int task){
        this.taskid = taskID;
        this.amount = taskAmount;
        this.scheduledTime = scheduledTime;
        this.task = task;
    }

    public TaskObject() {

    }

    public void setIndex(int index) {
        this.index = index;
    }
}
