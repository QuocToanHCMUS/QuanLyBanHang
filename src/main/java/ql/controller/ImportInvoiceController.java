/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.controller;

import java.util.List;
import ql.dao.ImportInvoiceDao;
import ql.model.DetailImportInvoice;
import ql.model.ImportInvoice;
import ql.model.User;

/**
 *
 * @author ADMIN
 */
public class ImportInvoiceController {
    private ImportInvoiceDao importInvoiceDao;
    
    public ImportInvoiceController() {
        importInvoiceDao = new ImportInvoiceDao();
    }
    
    public List<DetailImportInvoice> search(User user, String id) {
        return importInvoiceDao.search(user.getStore().getId(), user.getId(), id);
    }
}
