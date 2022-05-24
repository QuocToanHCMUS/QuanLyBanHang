/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ql.common.Utils;
import ql.dao.CustomerDao;
import ql.dao.InvoiceDao;
import ql.dao.WorkTimeDao;
import ql.dto.ProductInvoiceRequest;
import ql.dto.RevenueStoreDto;
import ql.model.Customer;
import ql.model.Invoice;
import ql.model.User;
import ql.model.WorkTime;


public class InvoiceController {
    private InvoiceDao invoiceDao;
    private WorkTimeDao workTimeDao;
    private CustomerDao customerDao;
    
    public InvoiceController() {
        invoiceDao = new InvoiceDao();
        workTimeDao = new WorkTimeDao();
        customerDao = new CustomerDao();
    }
    
    public double getTotalAmount(int userId) {
        double totalAmount = 0;
        String lastStartWorkTime = null;
        String lastEndWorkTime = null;
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
                if (currentHour >= startHour && currentHour < endHour) {
                    if (workTime.getType() == 1) {
                        WorkTime nightTime = workTimeList.get(2);
                        String yesterday = Utils.getYesterday();
                        String now = Utils.getNow();
                        lastStartWorkTime = yesterday + " " + Utils.getHourFromTime(nightTime.getStartTime()) + ":" + "00" + ":" + "00";
                        lastEndWorkTime = now + " " + Utils.getHourFromTime(nightTime.getEndTime()) + ":" + "00" + ":" + "00";
                    } else if (workTime.getType() == 2) {
                        WorkTime morningTime = workTimeList.get(0);
                        String now = Utils.getNow();
                        lastStartWorkTime = now + " " + Utils.getHourFromTime(morningTime.getStartTime()) + ":" + "00" + ":" + "00";
                        lastEndWorkTime = now + " " + Utils.getHourFromTime(morningTime.getEndTime()) + ":" + "00" + ":" + "00";
                    } else if (workTime.getType() == 3) {
                        WorkTime afternoonTime = workTimeList.get(1);
                        if (currentHour < 24) {
                            String now = Utils.getNow();
                            lastStartWorkTime = now + " " + Utils.getHourFromTime(afternoonTime.getStartTime()) + ":" + "00" + ":" + "00";
                            lastEndWorkTime = now + " " + Utils.getHourFromTime(afternoonTime.getEndTime()) + ":" + "00" + ":" + "00";
                        } else {
                            String yesterday = Utils.getYesterday();
                            lastStartWorkTime = yesterday + " " + Utils.getHourFromTime(afternoonTime.getStartTime()) + ":" + "00" + ":" + "00";
                            lastEndWorkTime = yesterday + " " + Utils.getHourFromTime(afternoonTime.getEndTime()) + ":" + "00" + ":" + "00";
                        }
                    }
                    break;
                }
            }
            totalAmount = invoiceDao.getTotalAmount(userId, lastStartWorkTime, lastEndWorkTime);
        }
        return totalAmount;
    }
    
    public List<Invoice> search(String cusName, String invoiceId) {
        List<Invoice> invoices = invoiceDao.search(cusName, invoiceId);
        return invoices;
    }

    public String getNextInvoiceId() {
        String lastInvoiceId = invoiceDao.getLastInvoiceId();
        if (lastInvoiceId == null) {
            return Invoice.prefixId + "1";
        }
        
        Matcher matcher = Pattern.compile("\\d+").matcher(lastInvoiceId);
        if (!matcher.find()) {
            return Invoice.prefixId + "1";
        }
        int lastId = Integer.parseInt(matcher.group());
        return Invoice.prefixId + (lastId + 1);
    }

    public boolean add(String invoiceNo, User user, String customerName, List<ProductInvoiceRequest> productInvoiceList, double totalPrice, int discount, double discountPrice) {
        Customer customer = new Customer(getNextCusId(), customerName);
        Invoice invoice = new Invoice(invoiceNo, user, customer, new Timestamp(System.currentTimeMillis()), totalPrice, discountPrice, discount);
        return invoiceDao.add(invoice, productInvoiceList);
    }

    public String getNextCusId() {
        String lastCusId = customerDao.getLastCustomerId();
        if (lastCusId == null) {
            return Customer.prefixId + "1";
        }
        
        Matcher matcher = Pattern.compile("\\d+").matcher(lastCusId);
        if (!matcher.find()) {
            return Customer.prefixId + "1";
        }
        int lastId = Integer.parseInt(matcher.group());
        return Customer.prefixId + (lastId + 1);
    }
    
    public boolean remove(String removeInvoiceId, String description) {
        return invoiceDao.remove(removeInvoiceId, description);
    }

    public List<Invoice> findAllByWorkingTime(int userId, WorkTime workTime) {
        String startWorkingTime = "";
        String endWorkingTime = "";
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int startHour = Utils.getHourFromTime(workTime.getStartTime());
        int endHour = Utils.getHourFromTime(workTime.getEndTime());
        if (startHour >= endHour) {
            if (currentHour >= 0 && currentHour < startHour) {
                currentHour += 24;
            }
        }
        if (workTime.getType() == 1 || workTime.getType() == 2) {
            String now = Utils.getNow();
            startWorkingTime = now + " " + Utils.getHourFromTime(workTime.getStartTime()) + ":" + "00" + ":" + "00";
            endWorkingTime = now + " " + Utils.getHourFromTime(workTime.getEndTime()) + ":" + "00" + ":" + "00";
        } else if (workTime.getType() == 3) {
            if (currentHour < 24) {
                String now = Utils.getNow();
                String tomorrow = Utils.getTomorrow();
                startWorkingTime = now + " " + Utils.getHourFromTime(workTime.getStartTime()) + ":" + "00" + ":" + "00";
                endWorkingTime = tomorrow + " " + Utils.getHourFromTime(workTime.getEndTime()) + ":" + "00" + ":" + "00";
            } else {
                String now = Utils.getNow();
                String yesterday = Utils.getYesterday();
                startWorkingTime = yesterday + " " + Utils.getHourFromTime(workTime.getStartTime()) + ":" + "00" + ":" + "00";
                endWorkingTime = now + " " + Utils.getHourFromTime(workTime.getEndTime()) + ":" + "00" + ":" + "00";
            }
        }
        if (startWorkingTime == "" || endWorkingTime == "") {
            return null;
        }
        return invoiceDao.findAllBySaleDate(userId, startWorkingTime, endWorkingTime);
    }
    
    public List<Invoice> revenue(String staffNo, String storeId, Date startDate, Date endDate) {
        return invoiceDao.revenue(staffNo, storeId, startDate, endDate);
    }

    public List<RevenueStoreDto> revenue(String storeId, Date startDate, Date endDate) {
        return invoiceDao.revenue(storeId, startDate, endDate); 
    }
}
