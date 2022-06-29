package com.csi.service;


import com.csi.dao.InvoiceDaoImpl;
import com.csi.email.Mail;
import com.csi.model.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InvoiceServiceImpl {

    @Autowired
    InvoiceDaoImpl invoiceDaoImpl;



    public Invoice saveInvoice(Invoice invoice) throws IOException {
        return invoiceDaoImpl.saveInvoice(invoice);
    }



    public List<Invoice> getAllData(){
        return invoiceDaoImpl.getAllData();
    }

    @Cacheable(value = "invoiceId")
    public Optional<Invoice> getDataById(int invoiceId){
        log.info("*******************try to fetch*****************");
        return invoiceDaoImpl.getDataById(invoiceId);
    }

    public void sendEmail(Mail mail,int invoiceId) throws IOException{
        invoiceDaoImpl.sendEmail(mail,invoiceId);
    }

    public void deleteInvoiceById(int invoiceId){
        invoiceDaoImpl.deleteInvoiceById(invoiceId);
    }

    public Invoice updateInvoice(Invoice invoice) throws IOException {
        return invoiceDaoImpl.updateInvoice(invoice);
    }
}
