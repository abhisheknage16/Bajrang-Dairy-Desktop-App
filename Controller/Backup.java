package Controller;

import DB.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Backup
{
    @FXML
    private Label lblWarn, lblTitle, lblnotice, lbldate;
    @FXML
    private DatePicker date;
    @FXML
    private Button btnBackup, btnReset;

    public void initialize()
    {
        if(Global.lang)
        {
            lblTitle.setText("Backup Software Data");
            lblnotice.setText("Backups are a must when your computer is in danger.\n" +
                    "Before installing any software or antivirus, you need to backup.\n" +
                    "Should you have any problems contact the company.");
            lbldate.setText("Date :");
            btnBackup.setText("Backup");
            btnReset.setText("Reset");
            date.setPromptText("xx-yy-zzzz");

        }
        try
        {
            lblWarn.setTextFill(Paint.valueOf(Global.labelColor1));
        }
        catch (Exception ignored)
        {
        }
        date.setValue(LocalDate.now());

        btnReset.setOnAction(event -> {
            lblWarn.setText("");
            date.setValue(LocalDate.now());
        });

        btnBackup.setOnAction(event -> createBackup());

    }

    private void createBackup()
    {
        try {
//            Create Workbook
            Workbook workbook = new HSSFWorkbook();

            Session session = Global.getSession();
            Transaction transaction = session.beginTransaction();

//            i maintain the row count
            int i = 0;

//            Backup Details in first Sheet
            {
//            Create Sheet and give name of table (Class Name) to sheet
                Sheet spreadsheet = workbook.createSheet("Backup Details");
//            Insert column names at first row
                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("Date");
                row.createCell(1).setCellValue("Time");

                row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue(String.valueOf(LocalDate.now()));
                row.createCell(1).setCellValue(String.valueOf(LocalTime.now()));

            }

//            User Table
            {
//            Create Sheet and give name of table (Class Name) to sheet
                Sheet spreadsheet = workbook.createSheet("User");
//            Insert column names at first row
                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("name");
                row.createCell(1).setCellValue("pass");
                row.createCell(2).setCellValue("qname");
                row.createCell(3).setCellValue("ans");
                row.createCell(4).setCellValue("date");
                row.createCell(5).setCellValue("time");

//                get Database table data, iterate that data and store into row wise in excel
                List<User> userList = session.createQuery("from DB.User").list();
                for (DB.User user : userList)
                {
                    row = spreadsheet.createRow(i++);
                    row.createCell(0).setCellValue(user.getName());
                    row.createCell(1).setCellValue(user.getPass());
                    row.createCell(2).setCellValue(user.getQname());
                    row.createCell(3).setCellValue(user.getAns());
                    row.createCell(4).setCellValue(String.valueOf(user.getDate()));
                    row.createCell(5).setCellValue(String.valueOf(user.getTime()));
                }
            }

//            Expense
            {
                Sheet spreadsheet = workbook.createSheet("Expense");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("name");
                row.createCell(2).setCellValue("date");
                row.createCell(3).setCellValue("ttl");
                row.createCell(4).setCellValue("d");

                List<Expense> expenseList = session.createQuery("from DB.Expense").list();
                for (DB.Expense expense : expenseList)
                {
                    row = spreadsheet.createRow(i++);
                    row.createCell(0).setCellValue(expense.getId());
                    row.createCell(1).setCellValue(expense.getName());
                    row.createCell(2).setCellValue(String.valueOf(expense.getDate()));
                    row.createCell(3).setCellValue(expense.getTtl());
                    row.createCell(4).setCellValue(expense.getD());


                }
            }


//            Details
            {
                Sheet spreadsheet = workbook.createSheet("Details");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("left");
                row.createCell(2).setCellValue("center");
                row.createCell(3).setCellValue("top");
                row.createCell(4).setCellValue("label");
                row.createCell(5).setCellValue("d");

                List<Details> detailsList = session.createQuery("from DB.Details").list();
                for (DB.Details details : detailsList)
                {
                    row = spreadsheet.createRow(i++);
                    row.createCell(0).setCellValue(details.getId());
                    row.createCell(1).setCellValue(details.getLeft());
                    row.createCell(2).setCellValue(details.getCenter());
                    row.createCell(3).setCellValue(details.getTop());
                    row.createCell(4).setCellValue(details.getLabel());
                    row.createCell(5).setCellValue(details.getD());
                }
            }

//            Customer
            {
                Sheet spreadsheet = workbook.createSheet("Customer");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("name");
                row.createCell(2).setCellValue("addr");
                row.createCell(3).setCellValue("accno");
                row.createCell(4).setCellValue("ifsc");
                row.createCell(5).setCellValue("sno");
                row.createCell(6).setCellValue("mob");
                row.createCell(7).setCellValue("pending");
                row.createCell(8).setCellValue("deposite");
                row.createCell(9).setCellValue("hafta");
                row.createCell(10).setCellValue("date");
                row.createCell(11).setCellValue("status");
                row.createCell(12).setCellValue("d");

                List<Customer> customerList = session.createQuery("from DB.Customer").list();
                for (DB.Customer customer : customerList)
                {
                    row = spreadsheet.createRow(i++);
                    row.createCell(0).setCellValue(customer.getId());
                    row.createCell(1).setCellValue(customer.getName());
                    row.createCell(2).setCellValue(customer.getAddr());
                    row.createCell(3).setCellValue(customer.getAccno());
                    row.createCell(4).setCellValue(customer.getIfsc());
                    row.createCell(5).setCellValue(customer.getSno());
                    row.createCell(6).setCellValue(customer.getMob());
                    row.createCell(7).setCellValue(customer.getPending());
                    row.createCell(8).setCellValue(customer.getDeposite());
                    row.createCell(9).setCellValue(customer.getHafta());
                    row.createCell(10).setCellValue(String.valueOf(customer.getDate()));
                    row.createCell(11).setCellValue(customer.getStatus());
                    row.createCell(12).setCellValue(customer.getD());
                }
            }

//            Message
            {
                Sheet spreadsheet = workbook.createSheet("Message");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("mobile");
                row.createCell(2).setCellValue("textmsg");
                row.createCell(3).setCellValue("date");
                row.createCell(4).setCellValue("time");
                row.createCell(5).setCellValue("d");

                List<Message> messsageList = session.createQuery("from DB.Message").list();
                for (Message messsage : messsageList)
                {
                    row = spreadsheet.createRow(i++);
                    row.createCell(0).setCellValue(messsage.getId());
                    row.createCell(1).setCellValue(messsage.getMobile());
                    row.createCell(2).setCellValue(messsage.getTextmsg());
                    row.createCell(3).setCellValue(String.valueOf(messsage.getDate()));
                    row.createCell(4).setCellValue(String.valueOf(messsage.getTime()));
                    row.createCell(5).setCellValue(messsage.getD());
                }
            }

//            Rate
            {
                Sheet spreadsheet = workbook.createSheet("Rate");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("cur");
                row.createCell(2).setCellValue("name");
                row.createCell(3).setCellValue("orate");
                row.createCell(4).setCellValue("nrate");
                row.createCell(5).setCellValue("osrate");
                row.createCell(6).setCellValue("nsrate");
                row.createCell(7).setCellValue("odegree");
                row.createCell(8).setCellValue("ndegree");
                row.createCell(9).setCellValue("ofat");
                row.createCell(10).setCellValue("nfat");
                row.createCell(11).setCellValue("sdate");
                row.createCell(12).setCellValue("edate");
                row.createCell(13).setCellValue("d");

                List<Rate> rateListe = session.createQuery("from DB.Rate").list();
                for (DB.Rate rate : rateListe)
                {
                    row = spreadsheet.createRow(i++);

                    row.createCell(0).setCellValue(rate.getId());
                    row.createCell(1).setCellValue(rate.getCur());
                    row.createCell(2).setCellValue(rate.getName());
                    row.createCell(3).setCellValue(rate.getOrate());
                    row.createCell(4).setCellValue(rate.getNrate());
                    row.createCell(5).setCellValue(rate.getOsrate());
                    row.createCell(6).setCellValue(rate.getNsrate());
                    row.createCell(7).setCellValue(rate.getOdegree());
                    row.createCell(8).setCellValue(rate.getNdegree());
                    row.createCell(9).setCellValue(rate.getOfat());
                    row.createCell(10).setCellValue(rate.getNfat());
                    row.createCell(11).setCellValue(String.valueOf(rate.getSdate()));
                    row.createCell(12).setCellValue(String.valueOf(rate.getEdate()));
                    row.createCell(13).setCellValue(rate.getD());
                }
            }

//            CowMilk
            {
                Sheet spreadsheet = workbook.createSheet("CowMilk");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("me");
                row.createCell(2).setCellValue("customer");
                row.createCell(3).setCellValue("date");
                row.createCell(4).setCellValue("time");
                row.createCell(5).setCellValue("cdegree");
                row.createCell(6).setCellValue("cfat");
                row.createCell(7).setCellValue("crate");
                row.createCell(8).setCellValue("degree");
                row.createCell(9).setCellValue("fat");
                row.createCell(10).setCellValue("gfat");
                row.createCell(11).setCellValue("rate");
                row.createCell(12).setCellValue("totalmilk");
                row.createCell(13).setCellValue("totalamt");
                row.createCell(14).setCellValue("d");
                row.createCell(15).setCellValue("snf");

                List<CowMilk> cowMilkList = session.createQuery("from DB.CowMilk").list();
                for (DB.CowMilk cowMilk : cowMilkList)
                {
                    row = spreadsheet.createRow(i++);

                    row.createCell(0).setCellValue(cowMilk.getId());
                    row.createCell(1).setCellValue(cowMilk.getMe());
                    row.createCell(2).setCellValue(cowMilk.getCustomer().getId());
                    row.createCell(3).setCellValue(String.valueOf(cowMilk.getDate()));
                    row.createCell(4).setCellValue(String.valueOf(cowMilk.getTime()));
                    row.createCell(5).setCellValue(cowMilk.getCdegree());
                    row.createCell(6).setCellValue(cowMilk.getCfat());
                    row.createCell(7).setCellValue(cowMilk.getCrate());
                    row.createCell(8).setCellValue(cowMilk.getDegree());
                    row.createCell(9).setCellValue(cowMilk.getFat());
                    row.createCell(10).setCellValue(cowMilk.getGfat());
                    row.createCell(11).setCellValue(cowMilk.getRate());
                    row.createCell(12).setCellValue(cowMilk.getTotalmilk());
                    row.createCell(13).setCellValue(cowMilk.getTotalamt());
                    row.createCell(14).setCellValue(cowMilk.getD());
                    row.createCell(15).setCellValue(cowMilk.getSnf());
                }
            }

//            BuffaloMilk
            {
                Sheet spreadsheet = workbook.createSheet("BuffaloMilk");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("me");
                row.createCell(2).setCellValue("customer");
                row.createCell(3).setCellValue("date");
                row.createCell(4).setCellValue("time");
                row.createCell(5).setCellValue("cdegree");
                row.createCell(6).setCellValue("cfat");
                row.createCell(7).setCellValue("crate");
                row.createCell(8).setCellValue("degree");
                row.createCell(9).setCellValue("fat");
                row.createCell(10).setCellValue("gfat");
                row.createCell(11).setCellValue("rate");
                row.createCell(12).setCellValue("totalmilk");
                row.createCell(13).setCellValue("totalamt");
                row.createCell(14).setCellValue("d");

                List<BuffaloMilk> buffaloMilkList = session.createQuery("from DB.BuffaloMilk").list();
                for (DB.BuffaloMilk buffaloMilk : buffaloMilkList)
                {
                    row = spreadsheet.createRow(i++);

                    row.createCell(0).setCellValue(buffaloMilk.getId());
                    row.createCell(1).setCellValue(buffaloMilk.getMe());
                    row.createCell(2).setCellValue(buffaloMilk.getCustomer().getId());
                    row.createCell(3).setCellValue(String.valueOf(buffaloMilk.getDate()));
                    row.createCell(4).setCellValue(String.valueOf(buffaloMilk.getTime()));
                    row.createCell(5).setCellValue(buffaloMilk.getCdegree());
                    row.createCell(6).setCellValue(buffaloMilk.getCfat());
                    row.createCell(7).setCellValue(buffaloMilk.getCrate());
                    row.createCell(8).setCellValue(buffaloMilk.getDegree());
                    row.createCell(9).setCellValue(buffaloMilk.getFat());
                    row.createCell(10).setCellValue(buffaloMilk.getGfat());
                    row.createCell(11).setCellValue(buffaloMilk.getRate());
                    row.createCell(12).setCellValue(buffaloMilk.getTotalmilk());
                    row.createCell(13).setCellValue(buffaloMilk.getTotalamt());
                    row.createCell(14).setCellValue(buffaloMilk.getD());
                }
            }

//            MilkSale
            {
                Sheet spreadsheet = workbook.createSheet("MilkSale");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("me");
                row.createCell(2).setCellValue("type");
                row.createCell(3).setCellValue("cname");
                row.createCell(4).setCellValue("rate");
                row.createCell(5).setCellValue("total");
                row.createCell(6).setCellValue("ttlmilk");
                row.createCell(7).setCellValue("date");
                row.createCell(8).setCellValue("time");

                List<DB.MilkSale> milkSaleList = session.createQuery("from DB.MilkSale").list();
                for (DB.MilkSale milkSale : milkSaleList)
                {
                    row = spreadsheet.createRow(i++);

                    row.createCell(0).setCellValue(milkSale.getId());
                    row.createCell(1).setCellValue(milkSale.getMe());
                    row.createCell(2).setCellValue(milkSale.getType());
                    row.createCell(3).setCellValue(milkSale.getCname());
                    row.createCell(4).setCellValue(milkSale.getRate());
                    row.createCell(5).setCellValue(milkSale.getTotal());
                    row.createCell(6).setCellValue(milkSale.getTtlmilk());
                    row.createCell(7).setCellValue(String.valueOf(milkSale.getDate()));
                    row.createCell(8).setCellValue(String.valueOf(milkSale.getTime()));
                }
            }

//            Bill
//            {
//                Sheet spreadsheet = workbook.createSheet("Bill");
//
//                i = 0;
//                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
//                row.createCell(0).setCellValue("id");
//                row.createCell(1).setCellValue("bno");
//                row.createCell(2).setCellValue("date");
//                row.createCell(3).setCellValue("sdate");
//                row.createCell(4).setCellValue("edate");
//                row.createCell(5).setCellValue("bttlltr");
//                row.createCell(6).setCellValue("bttlgfat");
//                row.createCell(7).setCellValue("bttlamt");
//                row.createCell(8).setCellValue("cttlltr");
//                row.createCell(9).setCellValue("cttlgfat");
//                row.createCell(10).setCellValue("cttlamt");
//                row.createCell(11).setCellValue("ttlltr");
//                row.createCell(12).setCellValue("total");
//                row.createCell(13).setCellValue("hafta");
//                row.createCell(14).setCellValue("paid");
//                row.createCell(15).setCellValue("cno");
//                row.createCell(16).setCellValue("customer");
//                row.createCell(17).setCellValue("d");
//
//                List<DB.Bill> billList = session.createQuery("from DB.Bill").list();
//                for (DB.Bill bill : billList)
//                {
//                    row = spreadsheet.createRow(i++);
//
//                    row.createCell(0).setCellValue(bill.getId());
//                    row.createCell(1).setCellValue(bill.getBno());
//                    row.createCell(2).setCellValue(String.valueOf(bill.getDate()));
//                    row.createCell(3).setCellValue(String.valueOf(bill.getSdate()));
//                    row.createCell(4).setCellValue(String.valueOf(bill.getEdate()));
//                    row.createCell(5).setCellValue(bill.getBttlltr());
//                    row.createCell(6).setCellValue(bill.getBttlgfat());
//                    row.createCell(7).setCellValue(bill.getBttlamt());
//                    row.createCell(8).setCellValue(bill.getCttlltr());
//                    row.createCell(9).setCellValue(bill.getCttlgfat());
//                    row.createCell(10).setCellValue(bill.getCttlamt());
//                    row.createCell(11).setCellValue(bill.getTtlltr());
//                    row.createCell(12).setCellValue(bill.getTotal());
//                    row.createCell(13).setCellValue(bill.getHafta());
//                    row.createCell(14).setCellValue(bill.getPaid());
//                    row.createCell(15).setCellValue(bill.getCno());
//                    row.createCell(16).setCellValue(bill.getCustomer().getName());
//                    row.createCell(17).setCellValue(bill.getD());
//                }
//            }

//            Bill Prod
//            {
//                Sheet spreadsheet = workbook.createSheet("BillProd");
//
//                i = 0;
//                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
//                row.createCell(0).setCellValue("id");
//                row.createCell(1).setCellValue("bno");
//                row.createCell(2).setCellValue("srno");
//                row.createCell(3).setCellValue("date");
//                row.createCell(4).setCellValue("bltr");
//                row.createCell(5).setCellValue("bfat");
//                row.createCell(6).setCellValue("bgfat");
//                row.createCell(7).setCellValue("bttl");
//                row.createCell(8).setCellValue("cltr");
//                row.createCell(9).setCellValue("cfat");
//                row.createCell(10).setCellValue("cgfat");
//                row.createCell(11).setCellValue("cttl");
//                row.createCell(12).setCellValue("bill");
//                row.createCell(13).setCellValue("d");
//
//                List<DB.BillProd> billList = session.createQuery("from DB.BillProd").list();
//                for (DB.BillProd bill : billList)
//                {
//                    row = spreadsheet.createRow(i++);
//
//                    row.createCell(0).setCellValue(bill.getId());
//                    row.createCell(1).setCellValue(bill.getBno());
//                    row.createCell(2).setCellValue(bill.getSrno());
//                    row.createCell(3).setCellValue(String.valueOf(bill.getDate()));
//                    row.createCell(4).setCellValue(bill.getBltr());
//                    row.createCell(5).setCellValue(bill.getBfat());
//                    row.createCell(6).setCellValue(bill.getBgfat());
//                    row.createCell(7).setCellValue(bill.getBttl());
//                    row.createCell(8).setCellValue(bill.getCltr());
//                    row.createCell(9).setCellValue(bill.getCfat());
//                    row.createCell(10).setCellValue(bill.getCgfat());
//                    row.createCell(11).setCellValue(bill.getCttl());
//                    row.createCell(12).setCellValue(bill.getBill().getCno());
//                    row.createCell(13).setCellValue(bill.getD());
//                }
//            }

//            BillProduct
//            {
//                Sheet spreadsheet = workbook.createSheet("BillProduct");
//
//                i = 0;
//                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
//                row.createCell(0).setCellValue("id");
//                row.createCell(1).setCellValue("bill");
//                row.createCell(2).setCellValue("buffaloMilk");
//                row.createCell(3).setCellValue("cowMilk");
//                row.createCell(4).setCellValue("d");
//
//                List<DB.BillProduct> billProductList = session.createQuery("from DB.BillProduct").list();
//                for (DB.BillProduct billProduct : billProductList)
//                {
//                    row = spreadsheet.createRow(i++);
//
//                    row.createCell(0).setCellValue(billProduct.getId());
//                    row.createCell(1).setCellValue(billProduct.getBill().getBno());
//                    row.createCell(2).setCellValue(billProduct.getBuffaloMilk().getTotalmilk());
//                    row.createCell(3).setCellValue(billProduct.getCowMilk().getTotalmilk());
//                    row.createCell(4).setCellValue(billProduct.getD());
//                }
//            }

//            CustomerPaymentCredit
            {
                Sheet spreadsheet = workbook.createSheet("CustomerPaymentCredit");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("customer");
                row.createCell(2).setCellValue("date");
                row.createCell(3).setCellValue("remark");
                row.createCell(4).setCellValue("amount");
                row.createCell(5).setCellValue("credit");
                row.createCell(6).setCellValue("deposite");
                row.createCell(7).setCellValue("d");

                List<DB.CustomerPaymentCredit> cpcl = session.createQuery("from DB.CustomerPaymentCredit").list();
                for (DB.CustomerPaymentCredit cpc : cpcl)
                {
                    row = spreadsheet.createRow(i++);

                    row.createCell(0).setCellValue(cpc.getId());
                    if(cpc.getCustomer() != null)
                        row.createCell(1).setCellValue(cpc.getCustomer().getName());
                    row.createCell(2).setCellValue(String.valueOf(cpc.getDate()));
                    row.createCell(3).setCellValue(cpc.getRemark());
                    row.createCell(4).setCellValue(cpc.getAmount());
                    row.createCell(5).setCellValue(cpc.getCredit());
                    row.createCell(6).setCellValue(cpc.getDeposite());
                    row.createCell(7).setCellValue(cpc.getD());
                }
            }

//            CustomerPaymentDeposite
            {
                Sheet spreadsheet = workbook.createSheet("CustomerPaymentDeposite");

                i = 0;
                org.apache.poi.ss.usermodel.Row row = spreadsheet.createRow(i++);
                row.createCell(0).setCellValue("id");
                row.createCell(1).setCellValue("customer");
                row.createCell(2).setCellValue("date");
                row.createCell(3).setCellValue("remark");
                row.createCell(4).setCellValue("amount");
                row.createCell(5).setCellValue("credit");
                row.createCell(6).setCellValue("deposite");
                row.createCell(7).setCellValue("bill");
                row.createCell(8).setCellValue("d");

                List<DB.CustomerPaymentDeposite> cpdl = session.createQuery("from DB.CustomerPaymentDeposite").list();
                for (DB.CustomerPaymentDeposite cpd : cpdl)
                {
                    row = spreadsheet.createRow(i++);

                    row.createCell(0).setCellValue(cpd.getId());
                    if(cpd.getCustomer() != null)
                        row.createCell(1).setCellValue(cpd.getCustomer().getName());
                    row.createCell(2).setCellValue(String.valueOf(cpd.getDate()));
                    row.createCell(3).setCellValue(cpd.getRemark());
                    row.createCell(4).setCellValue(cpd.getAmount());
                    row.createCell(5).setCellValue(cpd.getCredit());
                    row.createCell(4).setCellValue(cpd.getDeposite());
                    if(cpd.getBill() != null)
                        row.createCell(4).setCellValue(cpd.getBill().getBno());
                    row.createCell(5).setCellValue(cpd.getD());
                }
            }




            transaction.commit();
            session.close();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("सेव्ह करण्यासाठी फाईल निर्दिष्ट करा..!!");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File (*.xls)", "*.xls");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                try
                {
                    FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
                    workbook.write(fileOut);
                    fileOut.close();
                }
                catch (IOException ignored)
                {
                }
            }
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
    }

}
