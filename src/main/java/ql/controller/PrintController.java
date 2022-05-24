/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ql.common.Utils;
import ql.dao.InvoiceDao;
import ql.dao.ProductDao;
import ql.dao.StoreDao;
import ql.dto.RevenueStoreDto;
import ql.model.DetailImportInvoice;
import ql.model.ImportInvoice;
import ql.model.Invoice;
import ql.model.Product;
import ql.model.Staff;
import ql.model.User;

/**
 *
 * @author ADMIN
 */
public class PrintController {
    
    private InvoiceDao invoiceDao;
    private ProductDao productDao;
    private StoreDao storeDao;
    
    public PrintController() {
        invoiceDao = new InvoiceDao();
        productDao = new ProductDao();
        storeDao = new StoreDao();
    }

    public boolean printStoreRevenueExcel(File file, List<RevenueStoreDto> invoiceList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet");
        String[] titles = {"STT", "Store name", "Store address", "Amount"};
        int rowNum = 0;
        int colNum = 0;
        Row row = sheet.createRow(rowNum++);
        for (String str : titles) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(str);
        }
        int stt = 1;
        double totalAmount = 0;
        for (RevenueStoreDto revenue : invoiceList) {
            totalAmount += revenue.getTotalAmount();
            row = sheet.createRow(rowNum++);
            colNum = 0;
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(stt++);
            
            cell = row.createCell(colNum++);
            cell.setCellValue(revenue.getStoreName());

            cell = row.createCell(colNum++);
            cell.setCellValue(revenue.getStoreAddress());

            cell = row.createCell(colNum++);
            cell.setCellValue(revenue.getTotalAmount());
        }
        row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(colNum++);
        cell.setCellValue("Tổng doanh thu là " + Utils.formatCurrency(totalAmount));
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean printReportRevenueExcel(File file, List<Invoice> invoiceList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet");
        String[] titles = {"Mã hợp đồng", "Mã nhân viên", "Tên nhân viên", "Ngày bán", "Tổng tiền"};
        int rowNum = 0;
        int colNum = 0;
        Row row = sheet.createRow(rowNum++);
        for (String str : titles) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(str);
        }
        double totalAmount = 0;
        for (Invoice invoice : invoiceList) {
            totalAmount += invoice.getDiscountAmount();
            row = sheet.createRow(rowNum++);
            colNum = 0;
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(invoice.getId());
            
            Staff staff = (Staff) invoice.getUser();
            cell = row.createCell(colNum++);
            cell.setCellValue(staff.getStaffNo());

            cell = row.createCell(colNum++);
            cell.setCellValue(staff.getName());
            
            cell = row.createCell(colNum++);
            cell.setCellValue(Utils.formatDate(invoice.getSaleDate()));

            cell = row.createCell(colNum++);
            cell.setCellValue(invoice.getDiscountAmount());
        }
        row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(colNum++);
        cell.setCellValue("Tổng doanh thu là " + Utils.formatCurrency(totalAmount));
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean readDataFromFile(File file, User user) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = mySheet.iterator();
            rowIterator.hasNext();
            rowIterator.next();
            rowIterator.hasNext();
            rowIterator.next();
            rowIterator.hasNext();
            rowIterator.next();
            ImportInvoice importInvoice = new ImportInvoice();
            List<DetailImportInvoice> detailImportInvoices = new ArrayList<>();
            DataFormatter dataFormatter= new DataFormatter();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0) == null || row.getCell(0).getCellTypeEnum() == CellType.BLANK) {
                    Cell cell = row.getCell(4);
                    dataFormatter.formatCellValue(cell);
                    String totalInvoiceAmount = String.valueOf(cell.getNumericCellValue());
                    String tmpTotalInvoiceAmount = totalInvoiceAmount.replace(".", "");
                    String nextImportInvoice = invoiceDao.getNextImportInvoiceId();
                    importInvoice.setId(nextImportInvoice);
                    importInvoice.setImportInvoiceDate(new Date());
                    importInvoice.setTotalPrice(Double.valueOf(tmpTotalInvoiceAmount.substring(0, tmpTotalInvoiceAmount.length() - 4)));
                    break;
                }
                Cell cell1 = row.getCell(0);
                dataFormatter.formatCellValue(cell1);
                String productId = cell1.getStringCellValue();
                
                Cell cell2 = row.getCell(1);
                dataFormatter.formatCellValue(cell2);
                String productName = cell2.getStringCellValue();
                
                Cell cell3 = row.getCell(2);
                dataFormatter.formatCellValue(cell3);
                double quantity = cell3.getNumericCellValue();
                
                Cell cell4 = row.getCell(3);
                dataFormatter.formatCellValue(cell4);
                String importProductPrice = String.valueOf(cell4.getNumericCellValue());
                
                Cell cell5 = row.getCell(4);
                dataFormatter.formatCellValue(cell5);
                String totalPrice = String.valueOf(cell5.getNumericCellValue());
                
                Product p = new Product(productId, productName);
                String tempImportPrice = importProductPrice.replace(".", "");
                p.setImportPrice(Double.valueOf(tempImportPrice.substring(0, tempImportPrice.length() - 4)));
                
                String tmpTotalPrice = totalPrice.replace(".", "");
                detailImportInvoices.add(new DetailImportInvoice(importInvoice, p, (int) quantity, Double.valueOf(tmpTotalPrice.substring(0, tmpTotalPrice.length() - 4))));
            }
            
            return invoiceDao.addImportInvoice(detailImportInvoices, user);
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(PrintController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
