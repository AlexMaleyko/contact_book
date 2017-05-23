package com.itechart.maleiko.contact_book.business.service;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Alexey on 10.04.2017.
 */
public class QuartzEmailSenderTrigger {
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(QuartzEmailSenderTrigger.class);
    public void sendBirthdayEmails() {
        try{
            JobDetail job = JobBuilder.newJob(BirthdayMailJob.class)
                    .withIdentity("birthdayJob", "contactsbook").build();
            Trigger trigger = newTrigger()
                    .withIdentity("BirthdayTrigger", "contactsbook")
                    .withSchedule(cronSchedule("0 0/1 * 1/1 * ? *")/*dailyAtHourAndMinute(0, 0)*/)
                    .forJob("birthdayJob", "contactsbook")
                    .build();


            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }catch(SchedulerException se) {
            LOGGER.error("{}", se.getMessage());
        }
    }
}
