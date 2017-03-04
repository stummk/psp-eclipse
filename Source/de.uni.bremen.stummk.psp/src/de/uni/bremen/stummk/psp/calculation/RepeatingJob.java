package de.uni.bremen.stummk.psp.calculation;

import org.eclipse.core.runtime.jobs.Job;

/**
* This class implements an job, which will be repeated after a period of milliseconds
* @author Konstantin
*/
public abstract class RepeatingJob extends Job {
  private boolean running = true;
  protected long repeatDelay = 0;

/**
*Constructor
*@param jobName the name of this job
*@param repeatPeriod after how much milli seconds the job will be repeated
*
*/
  public RepeatingJob(String jobName, long repeatPeriod) {
    super(jobName);
    repeatDelay = repeatPeriod;
  }


/**
* return if job should schedule
*/
  public boolean shouldSchedule() {
    return running;
  }

/**
* mark task to stop it
*/
  public void stop() {
    running = false;
  }
}
