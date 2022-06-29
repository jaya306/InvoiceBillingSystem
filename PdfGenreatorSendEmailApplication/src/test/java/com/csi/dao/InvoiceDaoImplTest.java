package com.csi.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.csi.model.Invoice;
import com.csi.model.Product;
import com.csi.repo.InvoiceRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceDaoImplTest {

    @Autowired
    InvoiceDaoImpl invoiceDaoImpl;

    @MockBean
    InvoiceRepo invoiceRepoImpl;

    @Test
    public void saveDataTest() throws IOException {


        List<Product> productList = new LinkedList<>();

        Product product = new Product(1, "HP Laptop", 1, 50000.23);
        Product product1 = new Product(2, "Samsung Mobile", 2, 20000.12);
        productList.add(product);
        productList.add(product1);
        Date dob = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            dob = simpleDateFormat.parse("09-09-2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Invoice invoice = new Invoice(1, "CustName", 987456321, productList, dob, dob);

        Invoice invoice1=invoiceDaoImpl.saveInvoice(invoice);

        verify(invoiceRepoImpl, times(1)).save(invoice1);


    }

    @Test
    public void getAllDataTest() {
        List<Product> productList = new LinkedList<>();

        Product product = new Product(1, "HP Laptop", 1, 50000.23);
        Product product1 = new Product(2, "Samsung Mobile", 2, 20000.12);
        productList.add(product);
        productList.add(product1);
        Date dob = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            dob = simpleDateFormat.parse("09-09-2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        when(invoiceRepoImpl.findAll()).thenReturn(Stream.of(new Invoice(1, "CustName", 987456321, productList, dob, dob), new Invoice(1, "CustName", 987456321, productList, dob, dob)).collect(Collectors.toList()));

        assertEquals(2, invoiceDaoImpl.getAllData().size());

    }

    @Test
    public void updateDataTest() throws IOException {
        List<Product> productList = new LinkedList<>();

        Product product = new Product(1, "HP Laptop", 1, 50000.23);
        Product product1 = new Product(2, "Samsung Mobile", 2, 20000.12);
        productList.add(product);
        productList.add(product1);
        Date dob = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            dob = simpleDateFormat.parse("09-09-2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Invoice invoice = new Invoice(1, "CustName", 987456321, productList, dob, dob);

        invoiceDaoImpl.updateInvoice(invoice);

        verify(invoiceRepoImpl, times(1)).save(invoice);
    }

    @Test
    public void deleteDataByIdTest() {
        List<Product> productList = new LinkedList<>();

        Product product = new Product(1, "HP Laptop", 1, 50000.23);
        Product product1 = new Product(2, "Samsung Mobile", 2, 20000.12);
        productList.add(product);
        productList.add(product1);
        Date dob = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            dob = simpleDateFormat.parse("09-09-2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Invoice invoice = new Invoice(1, "CustName", 987456321, productList, dob, dob);

        invoiceDaoImpl.deleteInvoiceById(invoice.getId());

        verify(invoiceRepoImpl, times(1)).deleteById(invoice.getId());
    }


}