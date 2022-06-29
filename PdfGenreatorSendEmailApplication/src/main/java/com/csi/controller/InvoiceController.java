package com.csi.controller;


import com.csi.Exception.InvoiceNotFound;
import com.csi.email.Mail;
import com.csi.model.Invoice;
import com.csi.service.InvoiceServiceImpl;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoiceapi")
public class InvoiceController {

    @Autowired
    InvoiceServiceImpl invoiceServiceImpl;

    @PostMapping("/saveInvoiceData")
    public Invoice saveInvoice(@Valid @RequestBody Invoice invoice) throws IOException {
        return invoiceServiceImpl.saveInvoice(invoice);
    }

    @PostMapping("/generatePdfSendMail/{invoiceId}")
    public void sendEmailnew(@RequestBody Mail mail, @PathVariable int invoiceId) throws DocumentException, IOException {
        invoiceServiceImpl.sendEmail(mail, invoiceId);

    }

    @DeleteMapping("/deleteInvoiceById/{invoiceId}")
    public String deleteDataById(@PathVariable int invoiceId) throws InvoiceNotFound {
         invoiceServiceImpl.getDataById(invoiceId).orElseThrow(() -> new InvoiceNotFound("Invoice not found with Id " + invoiceId));
            invoiceServiceImpl.deleteInvoiceById(invoiceId);


        return "Invoice Data of invoice Id " + invoiceId + " deleted successfully";
    }

    @GetMapping("/getAllInvoices")
    public List<Invoice> getAllInvoicesList() {
        return invoiceServiceImpl.getAllData();
    }

    @GetMapping("/getDataById/{invoiceId}")
    public Optional<Invoice> getDataById(@PathVariable int invoiceId) {
        return invoiceServiceImpl.getDataById(invoiceId);
    }

    @PutMapping("/updateInvoiceById/{invoiceId}")
    public Invoice updateInvoiceById(@PathVariable int invoiceId, @RequestBody Invoice invoice) throws InvoiceNotFound, IOException {

        Invoice invoice1 = invoiceServiceImpl.getDataById(invoiceId).orElseThrow(() -> new InvoiceNotFound("Invoice not found with Id " + invoiceId));
        invoice1.setCustomerName(invoice.getCustomerName());
        invoice1.setCustomerContactNumber(invoice.getCustomerContactNumber());
        invoice1.setInvoiceDate(invoice.getInvoiceDate());
        invoice1.setDueDate(invoice.getDueDate());
        invoice1.setProducts(invoice.getProducts());


        return invoiceServiceImpl.updateInvoice(invoice1);
    }

}
