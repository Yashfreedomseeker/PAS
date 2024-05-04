/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pack;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import javax.mail.*;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Folder;
import javax.mail.Store;


/**
 *
 * @author Yash
 */
public class connect {
    static Connection conn = null;
    signedUp h = new signedUp();
    Loginform l = new Loginform();
    public static Connection connectdb(){//Establishes the database connection
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas","root","1234");
            conn.setAutoCommit(false);
            System.out.println("Connection succeeded!");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }  
        return conn;
    }
    
    public void signup(String n, String first, String last, int mobileno, String email, String pwd, String status){
        try{
            PreparedStatement st1 = conn.prepareStatement("INSERT INTO users(nic, fname, lname, mobileno, email, pwd, pstatus) values(?,?,?,?,?,?,?);");
            st1.setString(1, n);
            st1.setString(2, first);
            st1.setString(3, last);
            st1.setInt(4, mobileno);
            st1.setString(5, email);
            st1.setString(6, pwd); 
            st1.setString(7, status);
            
            st1.executeUpdate();
            conn.commit();
            JOptionPane.showMessageDialog(null, "Sign Up successfully done!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(), "failed", JOptionPane.INFORMATION_MESSAGE);
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback changes on error
                } 
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void adsignup(String name, int mobileno, String email, String pwd){
        try{
            PreparedStatement st1 = conn.prepareStatement("INSERT INTO admins(passwd, aName, mobileNo, email) values(?,?,?,?);");
            st1.setString(1, pwd);
            st1.setString(2, name);
            st1.setInt(3, mobileno);
            st1.setString(4, email); 
            
            st1.executeUpdate();
            conn.commit();
            JOptionPane.showMessageDialog(null, "Sign Up successfully done!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(), "failed", JOptionPane.INFORMATION_MESSAGE);
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback changes on error
                } 
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public static String passwordHash(String pw){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(pw.getBytes());
            byte[] rbt = md.digest();
            StringBuilder sb = new StringBuilder();
            
            for (byte b: rbt){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    
    public void image(String user, JLabel theimage) { // Add a JLabel parameter to set the image to
        try {
            String s = "SELECT image FROM application WHERE nic = ?;"; // Corrected SQL query
            PreparedStatement pst = conn.prepareStatement(s);
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Get the image as a byte array
                byte[] imgBytes = rs.getBytes("image");
                // Convert byte array to ImageIcon and set it to the JLabel
                ImageIcon image = new ImageIcon(imgBytes);
                // Assuming the image might be too large, let's scale it
                Image img = image.getImage().getScaledInstance(theimage.getWidth(), theimage.getHeight(), Image.SCALE_SMOOTH);
                theimage.setIcon(new ImageIcon(img));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void login(String user, String pass){
        try{
            String search = "SELECT nic, pwd FROM users WHERE nic = ?;";
            PreparedStatement pst = conn.prepareStatement(search);
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){ // Check if the result set is not empty
                String dbUser = rs.getString("nic");
                String dbPass = rs.getString("pwd");
                String hashedPass = passwordHash(pass); 

                if(dbUser.equals(user) && dbPass.equals(hashedPass)){
                    JOptionPane.showMessageDialog(null, "Login successfully done!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    l.dispose();
                    h.home(user);
                    h.appst(user);
                    h.displayapp(user);
                    h.setVisible(true);
                } 
                else {
                    JOptionPane.showMessageDialog(null, "Password is incorrect!", "Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            } 
            else {
                JOptionPane.showMessageDialog(null, "Username is incorrect!", "Failed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //this should work but somehow idk what's going on. the problem is prolly lying with nic. the database doesn't get updated
    public void eligibilityup(String user){
        try{
            String updatesql = "UPDATE users SET pstatus = ? WHERE nic = ?;";
            PreparedStatement pst = conn.prepareStatement(updatesql);
            pst.setString(1, "Eligible");
            pst.setString(2, user);
            System.out.println(pst);
            pst.executeUpdate();
            conn.commit();
            JOptionPane.showMessageDialog(null,"eligibility update success", "successful", JOptionPane.INFORMATION_MESSAGE);

        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(), "failed", JOptionPane.ERROR_MESSAGE);

        }
        
    }
    public void adlogin(int user, String pass){
        try{
            String search = "SELECT adminId, passwd FROM admins WHERE adminId = ?;";
            PreparedStatement pst = conn.prepareStatement(search);
            pst.setInt(1, user);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){ // Check if the result set is not empty
                String dbUser = rs.getString("adminId");
                String dbPass = rs.getString("passwd");
                String hashedPass = passwordHash(pass); 
                String use = String.valueOf(user);

                if(dbUser.equals(use) && dbPass.equals(hashedPass)){
                    JOptionPane.showMessageDialog(null, "Login successfully done!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    adminframe k = new adminframe();
                    k.setVisible(true);
                    k.adminname(user);
                } 
                else {
                    JOptionPane.showMessageDialog(null, "Password is incorrect!", "Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            } 
            else {
                JOptionPane.showMessageDialog(null, "Username is incorrect!", "Failed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    public byte[] insertImg(String flnme){
        byte [] imge = null; 
        try{
            File image = new File(flnme); //get a new file object and store the file path
            FileInputStream fis = new FileInputStream(image); //turn the path into bytes
            ByteArrayOutputStream bos = new ByteArrayOutputStream(); //getting a new byte array output stream object
            byte[] buff = new byte[1024]; //buffer creation of size 1024bytes
            for(int num; (num=fis.read(buff))!=-1;){ //reading the fis and store in the buffer using 1024 chunks
                bos.write(buff, 0,num); //the chunks are written into bos
            }
            imge = bos.toByteArray(); //byte array is obtained from the ByteArrayOutputStream object and written into img
        }
        catch(IOException e){ //catch if there is any input output exceptions
            JOptionPane.showMessageDialog(null, e);
        }
        catch(NullPointerException e){//catch if there are any null pointers
            JOptionPane.showMessageDialog(null,e.getMessage());
            imge = null;
        }
        return imge;//return img as final output
    
    }
    
    public void submitData(String nic, byte[] image, String tos,String totd,String apptype, int trno, String expire, String s, String f,String l,String ad,String d,String db,int bc,String dis,String p,String sex,String prof,int dualno,String Fnat,int fpassn,int tt,String ml){
        try{
            conn.setAutoCommit(false);
            String sql1 = "INSERT INTO application (nic, appType, serviceType, travelDocType, surname, fname, lname, pAdress, district, dob, bcno, bDistrict, bPlace, sex, profession, image, mobileno, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            String sql2 = "INSERT INTO dualcitizen (dualcitNo, frnationality, frpassNo, nic) VALUES (?,?,?,?);";
            String sql3 = "INSERT INTO renewals (nic, passportNo, expdate) VALUES (?,?,?);";
            PreparedStatement st1 = conn.prepareStatement(sql1);
            st1.setString(1, nic);
            st1.setString(2, apptype);
            st1.setString(3, tos);
            st1.setString(4, totd);
            st1.setString(5, s);
            st1.setString(6, f);
            st1.setString(7, l);
            st1.setString(8, ad);
            st1.setString(9, d);
            st1.setString(10, db);
            st1.setInt(11, bc);
            st1.setString(12, dis);
            st1.setString(13, p);
            st1.setString(14, sex);
            st1.setString(15, prof);
            st1.setBytes(16, image);
            st1.setInt(17, tt);
            st1.setString(18, ml);
            PreparedStatement st2 = conn.prepareStatement(sql2);
            st2.setInt(1, dualno);
            st2.setString(2, Fnat);
            st2.setInt(3, fpassn);
            st2.setString(4, nic);
            PreparedStatement st3 = conn.prepareStatement(sql3);
            st3.setString(1, nic);
            st3.setInt(2, trno);
            st3.setString(3, expire);
            
            st1.executeUpdate();
            st2.executeUpdate();
            st3.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "submission successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            h.appst(nic);
            //h.applicationstatus(nic);
        }
        catch(SQLException e){
             try {
                conn.rollback(); // Rollback the transaction in case of an error
                JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
            } 
             catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Rollback Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        finally {
            try {
                conn.setAutoCommit(true); // Restore auto-commit mode
            } 
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "AutoCommit Mode Restoration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    
    }
    public void appointment(String id, String time, String date){
        try{
            String sql = "INSERT INTO appointment(nic, appDate, appTime) VALUES(?,?,?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, id);
            st.setString(2, date);
            st.setString(3, time);
            st.executeUpdate();
            JOptionPane.showMessageDialog(null, "submission successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(SQLException e){
             try {
                conn.rollback(); // Rollback the transaction in case of an error
                JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
            } 
             catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Rollback Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        finally {
            try {
                conn.setAutoCommit(true); // Restore auto-commit mode
            } 
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "AutoCommit Mode Restoration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void updateAppointment(String id, String day, String time) {
        try {
            String updatesql1 = "UPDATE appointment SET ";
            boolean needComma = false;

            if (day != null && !day.isEmpty() && !"day".equals(day)) {
                updatesql1 += "appDate = ?";
                needComma = true;
            }
            if (time != null && !time.isEmpty() && !"time".equals(time)) { 
                if (needComma) updatesql1 += ",";
                updatesql1 += " appTime = ?";
            }
            updatesql1 += " WHERE nic = ?;";

            try (PreparedStatement pstmt = conn.prepareStatement(updatesql1)) {
                int index = 1;
                if (day != null && !day.isEmpty() && !"day".equals(day)) {
                    pstmt.setString(index++, day);
                }
                if (time != null && !time.isEmpty() && !"time".equals(time)) {
                    pstmt.setString(index++, time);
                }
                pstmt.setString(index, id);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Appointment Changed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                conn.commit();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Change failed!", JOptionPane.ERROR_MESSAGE);
        }
    }
   

    public void updateProfile(String n, String first, String last, int mobileNo, String Email, String pawd) {
    try {
        StringBuilder updatesql1 = new StringBuilder("UPDATE users SET ");
        boolean needComma = false;

        if (first != null && !first.isEmpty() && !"first".equals(first)) {
            updatesql1.append("fname = ?");
            needComma = true;
        }
        if (last != null && !last.isEmpty() && !"last".equals(last)) {
            if (needComma) updatesql1.append(",");
            updatesql1.append(" lname = ?");
            needComma = true; 
        }
        if (mobileNo != 0) {
            if (needComma) updatesql1.append(",");
            updatesql1.append(" mobileno = ?");
            needComma = true; 
        }
        if (Email != null && !Email.isEmpty() && !"Email".equals(Email)) {
            if (needComma) updatesql1.append(",");
            updatesql1.append(" email = ?");
            needComma = true;
        }
        if (pawd != null && !pawd.isEmpty() && !"pawd".equals(pawd)) {
            if (needComma) updatesql1.append(",");
            updatesql1.append(" pwd = ?");
            
        }
        updatesql1.append(" WHERE nic = ?;");

        try (PreparedStatement pstmt = conn.prepareStatement(updatesql1.toString())) {
            int index = 1;
            if (first != null && !first.isEmpty() && !"first".equals(first)) 
                pstmt.setString(index++, first);
            if (last != null && !last.isEmpty() && !"last".equals(last)) 
                pstmt.setString(index++, last);
            if (mobileNo != 0) 
                pstmt.setInt(index++, mobileNo); 
            if (Email != null && !Email.isEmpty() && !"Email".equals(Email)) 
                pstmt.setString(index++, Email);
            if (pawd != null && !pawd.isEmpty() && !"pawd".equals(pawd)) 
                pstmt.setString(index++, pawd);
            
            pstmt.setString(index, n);
            System.out.println(updatesql1);
            System.out.println(pstmt);
            pstmt.executeUpdate();
            conn.commit();
            JOptionPane.showMessageDialog(null, "Profile Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException e) {
        try {
            conn.rollback();
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getMessage(), "Change failed!", JOptionPane.ERROR_MESSAGE);
    }
}

    
    public void GenerateReport(String apptype, String toTs, int tno, String expire, String id, String sur, String first, String last, String pa, String di, String sex, String date, String b, String proF, int dno, int fpassn, String fnat) throws IOException, DocumentException{
        String report = "C:\\Users\\wasan\\Downloads\\reportfile\\"+id+".pdf";
        Document doc = new Document();
        FileOutputStream fos = null;
            try {
                File neW = new File(report);
                fos = new FileOutputStream(neW);
            } 
            catch (FileNotFoundException ex) {
                Logger.getLogger(connect.class.getName()).log(Level.SEVERE, null, ex);
            }
        try{
            PdfWriter.getInstance(doc, fos);
            doc.open();
            Paragraph ph = new Paragraph();
            ph.setAlignment(Element.ALIGN_CENTER);
            Font fn = new Font();
            fn.setColor(255, 0, 255);
            fn = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            fn.setStyle(Font.BOLD);
            fn.setSize(35);
            ph.add("Passport Application Report");
            
            Paragraph p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            Font f = new Font();
            f.setColor(0, 0, 0);
            f = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            f.setStyle(Font.BOLD);
            f.setSize(20); 
            p.add("Applicant Type : "+apptype+"\n"+"\n");
            p.add("Type of Travel Document : "+toTs+"\n"+"\n");
            p.add("Existing Passport no. : "+tno+"\n"+"\n");
            p.add("Expire date : "+expire+"\n"+"\n");
            p.add("NIC : "+id+"\n"+"\n");
            p.add("Full Name : "+sur+" "+first+" "+last+"\n"+"\n");
            p.add("Permanant Address : "+pa+"\n"+"\n");
            p.add("District : "+di+"\n"+"\n");
            p.add("Sex : "+sex+"\n"+"\n");
            p.add("Date of Birth : "+date+"\n"+"\n");
            p.add("Birth Certificate No. : "+b+"\n"+"\n");
            p.add("Profession : "+proF+"\n"+"\n");
            p.add("Dual Citizenship No. : "+dno+"\n"+"\n");
            p.add("Foriegn Paassport No. : "+fpassn+"\n"+"\n");
            p.add("Foreign Nationality : "+fnat+"\n"+"\n");
            doc.add(ph);
            doc.add(p);
            doc.close();

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        
        }
    
    }
    
    public void tempPdf(String user){
        String report = "C:\\Users\\wasan\\Downloads\\userReportFile\\"+user+".pdf";
        Document doc = new Document();
        FileOutputStream fos = null;
            try {
                File neW = new File(report);
                fos = new FileOutputStream(neW);
            } 
            catch (FileNotFoundException ex) {
                Logger.getLogger(connect.class.getName()).log(Level.SEVERE, null, ex);
            }
        try{
            PdfWriter.getInstance(doc, fos);
            doc.open();
            Paragraph ph = new Paragraph();
            ph.setAlignment(Element.ALIGN_CENTER);
            Font fn = new Font();
            fn.setColor(255, 0, 255);
            fn = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            fn.setStyle(Font.BOLD);
            fn.setSize(35);
            ph.add("Passport Application Report");
            
            Paragraph p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            Font f = new Font();
            f.setColor(0, 0, 0);
            f = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            f.setStyle(Font.BOLD);
            f.setSize(20);
            
            String search1 = "SELECT * FROM application a INNER JOIN renewals r ON a.nic = r.nic INNER JOIN dualcitizen d ON a.nic = d.nic WHERE a.nic = ?;";
            PreparedStatement pst = conn.prepareStatement(search1);
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){ // Check if the result set is not empty
                p.add("Applicant Type : "+rs.getString("appType")+"\n"+"\n");
                p.add("Type of Travel Document : "+rs.getString("travelDocType")+"\n"+"\n");
                p.add("Existing Passport no. : "+rs.getInt("passportNo")+"\n"+"\n");
                p.add("Expire date : "+rs.getString("expdate")+"\n"+"\n");
                p.add("NIC : "+rs.getString("nic")+"\n"+"\n");
                p.add("Full Name : "+rs.getString("surname")+" "+rs.getString("fname")+" "+rs.getString("lname")+"\n"+"\n");
                p.add("Permanant Address : "+rs.getString("pAdress")+"\n"+"\n");
                p.add("District : "+rs.getString("district")+"\n"+"\n");
                p.add("Sex : "+rs.getString("sex")+"\n"+"\n");
                p.add("Date of Birth : "+rs.getString("dob")+"\n"+"\n");
                p.add("Birth Certificate No. : "+rs.getInt("bcno")+"\n"+"\n");
                p.add("Profession : "+rs.getString("profession")+"\n"+"\n");
                p.add("Dual Citizenship No. : "+rs.getInt("dualcitNo")+"\n"+"\n");
                p.add("Foriegn Paassport No. : "+rs.getInt("frpassNo")+"\n"+"\n");
                p.add("Foreign Nationality : "+rs.getString("frnationality")+"\n"+"\n");
            }
            doc.add(ph);
            doc.add(p);
            doc.close();
            JOptionPane.showMessageDialog(null,"report generation successful", "success", JOptionPane.INFORMATION_MESSAGE);

        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
    
    }
    
    public void sendmail(String user){
        try{
            Properties pr = new Properties();
            pr.put("mail.smtp.auth", "true");
            pr.put("mail.smtp.starttls.enable", "true"); // Use STARTTLS
            pr.put("mail.smtp.host", "smtp.gmail.com");
            pr.put("mail.smtp.port", "587"); // Port for STARTTLS
            pr.put("mail.smtp.ssl.protocols", "TLSv1.2");
            pr.put("mail.transport.protocol", "smtp");
            pr.put("mail.smtp.ssl.trust", "*");
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
            System.setProperty("javax.net.debug", "ssl:handshake");


            Session sess = Session.getInstance(pr, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication("trashcan21st@gmail.com","undc oboo ytfh nxwb");
                }
            });

            Message mess = new MimeMessage(sess);
            mess.setSubject("mailing the system generated report of "+ user);

            Address adressto = new InternetAddress("ralilypotter000@gmail.com");
            mess.setRecipient(Message.RecipientType.TO, adressto);

            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart att = new MimeBodyPart();
            att.attachFile(new File("C:\\Users\\wasan\\Downloads\\reportfile\\"+user+".pdf"));

            MimeBodyPart mbodypart = new MimeBodyPart();
            mbodypart.setContent("<h3>The application report of user "+user+" is sent herewith<h3>","text/html");

            multipart.addBodyPart(mbodypart);
            multipart.addBodyPart(att);

            mess.setContent(multipart);
            Transport.send(mess);
            JOptionPane.showMessageDialog(null, "Mail to RA & Police sent successfully!", "success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(MessagingException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void userMail(String mail, String user){
        try{
            Properties pr = new Properties();
            pr.put("mail.smtp.auth", "true");
            pr.put("mail.smtp.starttls.enable", "true"); // Use STARTTLS
            pr.put("mail.smtp.host", "smtp.gmail.com");
            pr.put("mail.smtp.port", "587"); // Port for STARTTLS
            pr.put("mail.smtp.ssl.protocols", "TLSv1.2");
            pr.put("mail.transport.protocol", "smtp");
            pr.put("mail.smtp.ssl.trust", "*");
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
            System.setProperty("javax.net.debug", "ssl:handshake");


            Session sess = Session.getInstance(pr, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication("trashcan21st@gmail.com","undc oboo ytfh nxwb");
                }
            });

            Message mess = new MimeMessage(sess);
            mess.setSubject("Your Report"+ user);

            Address adressto = new InternetAddress("wasanayasho@gmail.com");
            mess.setRecipient(Message.RecipientType.TO, adressto);

            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart att = new MimeBodyPart();
            att.attachFile(new File("C:\\Users\\wasan\\Downloads\\reportfile\\"+user+".pdf"));

            MimeBodyPart mbodypart = new MimeBodyPart();
            mbodypart.setContent("<h3>This is your filled application which will be directed to police and Regional Administrator<h3>","text/html");

            multipart.addBodyPart(mbodypart);
            multipart.addBodyPart(att);

            mess.setContent(multipart);
            Transport.send(mess);
            JOptionPane.showMessageDialog(null, "Mail sent successfully!", "success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(MessagingException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void notification(String userEmail, String appPassword) {
        Properties properties = new Properties();
        System.setProperty("javax.net.debug", "all");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        Session emailSession = Session.getInstance(properties);

        try {
            // Connect to the email store
            Store store = emailSession.getStore("imaps");
            store.connect(userEmail, appPassword);

            // Access the Inbox folder
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // Fetch the latest 3 messages
            Message[] messages = emailFolder.getMessages();
            System.out.println("Total Messages: " + messages.length);
            for (int i = 0; i < Math.min(3, messages.length); i++) {
                Message message = messages[messages.length - 1 - i];
                System.out.println("---------------------------------");
                System.out.println("Email " + (i + 1) + ":");
                System.out.println("Subject: " + message.getSubject());
                //System.out.println("From: " + String.join(", ", message.getFrom()));
                System.out.println("Sent Date: " + message.getSentDate());
                // Be cautious with getContent(), it can return complex data structures for multipart emails
                System.out.println("Content Preview: " + message.getContent().toString().substring
                       (0, Math.min(message.getContent().toString().length(), 50)) + "...");
            }

            // Close connections
            emailFolder.close(false);
            store.close();
            } 
        catch (NoSuchProviderException e) {
                e.printStackTrace();
            } 
        catch (MessagingException e) {
                e.printStackTrace();
            } 
        catch (IOException e) {
                e.printStackTrace();
            }
    }
    /*
    public void checkUnreadMails(String user, String password) {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        Session emailSession = Session.getDefaultInstance(properties);
        
        try {
            // Connect to the email store
            Store store = emailSession.getStore("imaps");
            store.connect(user, password);

            // Access the Inbox folder
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // Fetch unread messages
            Message[] unreadMessages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            System.out.println("You have " + unreadMessages.length + " unread emails.");

            // Optionally, print details of each unread message
            for (Message message : unreadMessages) {
                System.out.println("---------------------------------");
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Sent Date: " + message.getSentDate());
            }

            // Close the folder and store
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }*/

    void appst(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    
    
}
