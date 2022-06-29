package com.csi.dao;


import com.csi.email.Mail;
import com.csi.model.Invoice;
import com.csi.model.Product;
import com.csi.repo.InvoiceRepo;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Component
public class InvoiceDaoImpl {

    @Autowired
    InvoiceRepo invoiceRepoImpl;


    @Autowired
    JavaMailSender mailSender;

    public Invoice saveInvoice(Invoice invoice) throws IOException {

         Invoice invoice1=invoiceRepoImpl.save(invoice);
         pdfGeneration(invoice.getId());
         return invoice1;

    }

    public List<Invoice> getAllData() {
        return invoiceRepoImpl.findAll();
    }

    public Optional<Invoice> getDataById(int invoiceId) {
        return invoiceRepoImpl.findById(invoiceId);
    }

    public void sendEmail(Mail mail, int invoiceId) throws IOException {
        String filename = "D:\\BillingSys\\invoicefile" + invoiceId + ".pdf";
        File filenew= new File(filename);

        // If file doesn't exists, then create it

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(mail.getMailFrom());
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());
            mimeMessageHelper.setCc(mail.getCc());
            mimeMessageHelper.setBcc(mail.getBcc());


            if (filenew.exists()) {

                FileSystemResource fileSystem
                        = new FileSystemResource(filenew);
                mimeMessageHelper.addAttachment(fileSystem.getFilename(),
                        fileSystem);

            } else {
                System.out.println("File not Exist");
            }

            mailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void pdfGeneration(int invoiceId) throws IOException {
        double totalamount = 0.0;
        double sgstamount = 0.0;
        double cgstamount = 0.0;
        double finalAmount = 0.0;



        Optional<Invoice> invoices = invoiceRepoImpl.findById(invoiceId);
        String filename = "D:\\BillingSys\\invoicefile" + invoiceId + ".pdf";

        Invoice invoice = invoices.get();
        File file = new File(filename);

        // If file doesn't exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        } else {
            System.out.println("File Already Exist");
        }
        //The creation of a table in a PDF using Java is done by installing the document class.
        // While instantiating this class, pass a PdfDocument object as a parameter to its constructor.
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(25);

        Font fontAboveFiels = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontAboveFiels.setSize(18);

        Paragraph paragraph = new Paragraph("Invoice", fontAboveFiels);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        Paragraph paragraphinvoicid = new Paragraph("Customer Id: " + String.valueOf(invoice.getId()), fontAboveFiels);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraphinvoicid);

        Paragraph paragraphname = new Paragraph("Customer Name: " + invoice.getCustomerName(), fontAboveFiels);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraphname);

        Paragraph paragraphnumber = new Paragraph("Contact Number: " + String.valueOf(invoice.getCustomerContactNumber()), fontAboveFiels);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraphnumber);

        Paragraph invoiceDataParagraph = new Paragraph("Invoice date: " + String.valueOf(invoice.getInvoiceDate()), fontAboveFiels);
        invoiceDataParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(invoiceDataParagraph);

        Paragraph dueDataParagraph = new Paragraph("Due date: " + String.valueOf(invoice.getDueDate()), fontAboveFiels);
        dueDataParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(dueDataParagraph);

        Paragraph divider = new Paragraph("-----------------------------------------------------------------", fontTiltle);

        divider.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(divider);

        PdfPTable table = new PdfPTable(4);

        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3, 3, 3});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        font.setSize(18);

        cell.setPhrase(new Phrase("ID", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Product Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Product Qty", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Product Price", font));
        table.addCell(cell);

        for (Product product : invoice.getProducts()) {
            // Adding student id
            Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            font1.setColor(CMYKColor.BLACK);
            font1.setSize(15);
            PdfPCell cell1 = new PdfPCell();
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setPhrase(new Phrase(String.valueOf(product.getProductsId()), font1));
            table.addCell(cell1);

            double totalPrice = 0.0;
            totalPrice = product.getProductQty() * product.getProcustPrice();
            totalamount = totalamount + totalPrice;

            cell1.setPhrase(new Phrase(product.getProductName(), font1));
            table.addCell(cell1);

            cell1.setPhrase(new Phrase(String.valueOf(product.getProductQty()), font1));
            table.addCell(cell1);

            cell1.setPhrase(new Phrase(String.valueOf(product.getProcustPrice()), font1));
            table.addCell(cell1);

        }

        sgstamount = (totalamount * 9) / 100;
        cgstamount = (totalamount * 9) / 100;

        finalAmount = totalamount + sgstamount + cgstamount;


        DecimalFormat df = new DecimalFormat("####0.00");
        Paragraph totalparagraph = new Paragraph("Total: " + df.format(totalamount) + "\n\n" + "SGST (9%): " + df.format(sgstamount) + "\n\n" + "CGST (9%): " + df.format(cgstamount) + "\n\n" + "Final Amount: " + df.format(finalAmount));

        totalparagraph.getFont().setStyle(Font.BOLD);
        cell = new PdfPCell(totalparagraph);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        document.add(table);
        document.close();


    }

    public void deleteInvoiceById(int invoiceId) {
        invoiceRepoImpl.deleteById(invoiceId);
    }

    public Invoice updateInvoice(Invoice invoice) throws IOException {
        Invoice invoice1=invoiceRepoImpl.save(invoice);
        pdfGeneration(invoice.getId());
        return invoiceRepoImpl.save(invoice1);
    }
}
