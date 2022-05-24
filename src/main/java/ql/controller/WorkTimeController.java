/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.util.Calendar;
import java.util.List;
import ql.common.Utils;
import ql.dao.WorkTimeDao;
import ql.model.WorkTime;

/**
 *
 * @author ADMIN
 */
public class WorkTimeController {
    private WorkTimeDao workTimeDao;
    
    public WorkTimeController() {
        workTimeDao = new WorkTimeDao();
    }
    
    public WorkTime getCurrentWorkTime() {
        List<WorkTime> workTimeList = workTimeDao.getWorkTime();
        if (!workTimeList.isEmpty()) {
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            for (WorkTime workTime : workTimeList) {
                int startHour = Utils.getHourFromTime(workTime.getStartTime());
                int endHour = Utils.getHourFromTime(workTime.getEndTime());
                if (startHour >= endHour) {
                    endHour += 24;
                    if (currentHour >= 0 && currentHour < startHour) {
                        currentHour += 24;
                    }
                }
                if (currentHour >= startHour && currentHour <= endHour) {
                    return workTime;
                }
            }
        }
        return null;
    }
}
