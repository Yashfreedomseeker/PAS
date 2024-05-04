/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pack;

import com.itextpdf.text.DocumentException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;



/**
 *
 * @author Yash
 */

public class signedUp extends javax.swing.JFrame {

    /**
     * Creates new form signedUp
     */
    Connection conn = null;
    String filename;
    String dateString;
    String expDateString;
    String DateString;
    String x;
    
    public signedUp() {
        initComponents();
        home.repaint();
        home.revalidate();
        conn = connect.connectdb();
        scroll.setVerticalScrollBar(new ScrollBarCustom());
        ScrollBarCustom sp = new ScrollBarCustom();
        sp.setOrientation(JScrollBar.HORIZONTAL);
        scroll.setHorizontalScrollBar(sp);
        
        home.setVisible(true);
        Applicationpg1.setVisible(false);
        Applicationpg2.setVisible(false);
        duelcitizen.setEnabled(false);
        appointmentpanel.setVisible(false);
        timePicker1.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent s){
                x = timePicker1.getSelectedTime();
                selctedtime.setText(x); 
            }
        });
        
        dateselcted.addPropertyChangeListener("date", evt ->{  
            java.util.Date date = dateselcted.getDate();
            if(date != null){
                SimpleDateFormat form = new SimpleDateFormat(dateselcted.getDateFormatString());
                DateString = form.format(date);
                datedisplay.setText(DateString);
                }    
        });
        dob.addPropertyChangeListener("date", evt ->{  
        java.util.Date date = dob.getDate();
            if(date != null){
                SimpleDateFormat form = new SimpleDateFormat(dob.getDateFormatString());
                dateString = form.format(date); 
            }    
        });
        exp.addPropertyChangeListener("date", evt ->{  
        java.util.Date date = exp.getDate();
            if(date != null){
                SimpleDateFormat form = new SimpleDateFormat(exp.getDateFormatString());
                expDateString = form.format(date); 
            }    
        });
        
        /*addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                toggleScrollPane();
            }
        });*/
         
    }
    
//    public JLabel lhome(){
//        add(theimage);
//        return theimage;
//    }
//    
//    public void image(String user, JLabel theimage) { // Add a JLabel parameter to set the image to
//        try {
//            String s = "SELECT image FROM application WHERE nic = ?;"; // Corrected SQL query
//            PreparedStatement pst = conn.prepareStatement(s);
//            pst.setString(1, user);
//            ResultSet rs = pst.executeQuery();
//            if (rs.next()) {
//                // Get the image as a byte array
//                byte[] imgBytes = rs.getBytes("image");
//                // Convert byte array to ImageIcon and set it to the JLabel
//                ImageIcon imge = new ImageIcon(imgBytes);
//                // Assuming the image might be too large, let's scale it
//                Image img = imge.getImage().getScaledInstance(theimage.getWidth(), theimage.getHeight(), Image.SCALE_SMOOTH);
//                theimage.setIcon(new ImageIcon(img));
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
     
    public void home(String user) {
        try {
            String search = "SELECT nic, fname, lname, mobileno, email, pstatus FROM users WHERE nic = ?;";
            PreparedStatement pst = conn.prepareStatement(search);
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) { // Use if instead of while if you're expecting one record
                idno.setText(rs.getString("nic"));
                idNo.setText(rs.getString("nic"));
                String f = rs.getString("fname");
                String l = rs.getString("lname");
                String fl = f+" "+l;
                fname.setText(f);
                lname.setText(l);
                first.setText(fl);
                // Convert int to String for the mobile number
                mobno.setText(Integer.toString(rs.getInt("mobileno")));
                telno.setText(Integer.toString(rs.getInt("mobileno")));
                maill.setText(rs.getString("email"));
                email.setText(rs.getString("email"));
                statuss.setText(rs.getString("pstatus")); 
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage()); // Properly handle exception
        }
    }
    
    
    public void appst(String user){
        try{
            String search = "SELECT appType FROM application WHERE nic = ?;";
            PreparedStatement pst = conn.prepareStatement(search);
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();
            if(search.isBlank()){
                appstatus.setText("New Applicant");
            }  
            else{
                if (rs.next()){
                    appstatus.setText(rs.getString("appType")+" Applicant");
                }
            }
        }
        catch(SQLException e){
                System.out.println(e.getMessage());
        }
    
    }
    //take care of this too..check every line.
    public void applicationstatus(String user) {
        String selectQuery = "SELECT u.nic FROM application a,users u WHERE u.nic = a.nic AND u.nic = ?;";
        String updateQuery = "UPDATE users SET pstatus = ? WHERE nic = ?;";

        try (PreparedStatement pst1 = conn.prepareStatement(selectQuery)) {
            pst1.setString(1, user);
            try (ResultSet rs = pst1.executeQuery()) {
                if (rs.next()) { // User found in 'application'
                    try (PreparedStatement up = conn.prepareStatement(updateQuery)) {
                        up.setString(1, "applied");
                        up.setString(2, user);
                        up.executeUpdate();
                        statuss.setText("applied");
                        conn.commit();
                        System.out.println(selectQuery);
                        System.out.println(updateQuery);
                        JOptionPane.showMessageDialog(null, "Your application status updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                }
            }
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     public void displayapp(String user){
        try{
            String selectQuery = "SELECT appDate, appTime FROM appointment WHERE nic = ?;";
            PreparedStatement pst1 = conn.prepareStatement(selectQuery);
            pst1.setString(1, user);
            ResultSet rs = pst1.executeQuery();
            if(rs.next()){
                datedisplay.setText(rs.getString("appDate"));
                selctedtime.setText(rs.getString("appTime"));
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Display failed!", JOptionPane.ERROR_MESSAGE);
        }  
    }
     
    /*
    private void toggleScrollPane() {
        if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            // Frame is maximized, remove the JScrollPane
            remove(scroll);
            add(jPanel1);
        } else {
            // Frame is not maximized, ensure JScrollPane is added
            remove(jPanel1);
            add(scroll);
        }
        validate(); // Revalidate the layout
        repaint(); // Repaint the frame
    }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tos = new javax.swing.ButtonGroup();
        totd = new javax.swing.ButtonGroup();
        dualcit = new javax.swing.ButtonGroup();
        sex = new javax.swing.ButtonGroup();
        appType = new javax.swing.ButtonGroup();
        menu = new javax.swing.JPanel();
        Applicationtab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Appointments = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        Hometab = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        theimage = new javax.swing.JLabel();
        idno = new javax.swing.JLabel();
        statuss = new javax.swing.JLabel();
        appstatus = new javax.swing.JLabel();
        mobno = new javax.swing.JLabel();
        maill = new javax.swing.JLabel();
        first = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        pdf = new javax.swing.JButton();
        loadimage = new javax.swing.JButton();
        appointmentpanel = new javax.swing.JPanel();
        updatedate = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        timePicker1 = new com.raven.swing.TimePicker();
        dateselcted = new com.toedter.calendar.JDateChooser();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        datedisplay = new javax.swing.JLabel();
        selctedtime = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        confirmdate = new javax.swing.JButton();
        updateDate = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        Applicationpg2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        bcno = new javax.swing.JTextField();
        bDis = new javax.swing.JTextField();
        bPlace = new javax.swing.JTextField();
        male = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        job = new javax.swing.JTextField();
        dualyes = new javax.swing.JRadioButton();
        dualno = new javax.swing.JRadioButton();
        duelcitizen = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        fn = new javax.swing.JTextField();
        fpn = new javax.swing.JTextField();
        dzn = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        submit = new javax.swing.JButton();
        dob = new com.toedter.calendar.JDateChooser();
        prev = new javax.swing.JButton();
        telno = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        Applicationpg1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        onedayserv = new javax.swing.JRadioButton();
        normalserv = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        all = new javax.swing.JRadioButton();
        me = new javax.swing.JRadioButton();
        emc = new javax.swing.JRadioButton();
        idc = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        passportno = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        sur = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        address = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        district = new javax.swing.JTextField();
        nextpg = new javax.swing.JButton();
        image = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        newPass = new javax.swing.JRadioButton();
        renewPass = new javax.swing.JRadioButton();
        exp = new com.toedter.calendar.JDateChooser();
        idNo = new javax.swing.JLabel();
        fname = new javax.swing.JLabel();
        lname = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menu.setBackground(new java.awt.Color(0, 109, 119));

        Applicationtab.setBackground(new java.awt.Color(131, 197, 190));
        Applicationtab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ApplicationtabMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel1.setText("Apply for Passport");

        javax.swing.GroupLayout ApplicationtabLayout = new javax.swing.GroupLayout(Applicationtab);
        Applicationtab.setLayout(ApplicationtabLayout);
        ApplicationtabLayout.setHorizontalGroup(
            ApplicationtabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ApplicationtabLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        ApplicationtabLayout.setVerticalGroup(
            ApplicationtabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ApplicationtabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        Appointments.setBackground(new java.awt.Color(131, 197, 190));
        Appointments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AppointmentsMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel4.setText("Appointments");

        javax.swing.GroupLayout AppointmentsLayout = new javax.swing.GroupLayout(Appointments);
        Appointments.setLayout(AppointmentsLayout);
        AppointmentsLayout.setHorizontalGroup(
            AppointmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AppointmentsLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        AppointmentsLayout.setVerticalGroup(
            AppointmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AppointmentsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        Hometab.setBackground(new java.awt.Color(131, 197, 190));
        Hometab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HometabMouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel10.setText("Home");

        javax.swing.GroupLayout HometabLayout = new javax.swing.GroupLayout(Hometab);
        Hometab.setLayout(HometabLayout);
        HometabLayout.setHorizontalGroup(
            HometabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HometabLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HometabLayout.setVerticalGroup(
            HometabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HometabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Applicationtab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Appointments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Hometab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(Hometab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(Applicationtab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(Appointments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(251, Short.MAX_VALUE))
        );

        jPanel1.setAlignmentX(1.0F);

        home.setBackground(new java.awt.Color(237, 246, 249));
        home.setAutoscrolls(true);

        theimage.setText("image");

        idno.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        idno.setText("NIC");

        statuss.setFont(new java.awt.Font("Century Gothic", 0, 36)); // NOI18N
        statuss.setText("passport status");

        appstatus.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        appstatus.setText("new applicant/ renewable applicant");

        mobno.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        mobno.setText("mobile number");

        maill.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        maill.setText("email");

        first.setFont(new java.awt.Font("Century Gothic", 1, 45)); // NOI18N
        first.setText("Name ");

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Edit Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        pdf.setBackground(new java.awt.Color(0, 102, 102));
        pdf.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        pdf.setForeground(new java.awt.Color(255, 255, 255));
        pdf.setText("Report");
        pdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdfActionPerformed(evt);
            }
        });

        loadimage.setBackground(new java.awt.Color(0, 102, 102));
        loadimage.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        loadimage.setForeground(new java.awt.Color(255, 255, 255));
        loadimage.setText("Load the image");
        loadimage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadimageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeLayout.createSequentialGroup()
                        .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(first, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(homeLayout.createSequentialGroup()
                                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(idno)
                                    .addComponent(appstatus)
                                    .addComponent(statuss)
                                    .addGroup(homeLayout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(44, 44, 44)
                                        .addComponent(pdf, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(homeLayout.createSequentialGroup()
                        .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(maill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(homeLayout.createSequentialGroup()
                                .addComponent(mobno, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)))
                        .addGap(279, 279, 279)))
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(loadimage, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(theimage, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(275, 275, 275))
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(first, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(idno)
                        .addGap(18, 18, 18)
                        .addComponent(mobno)
                        .addGap(18, 18, 18)
                        .addComponent(maill)
                        .addGap(18, 18, 18)
                        .addComponent(appstatus)
                        .addGap(18, 18, 18)
                        .addComponent(statuss, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pdf, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(homeLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(theimage, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(loadimage, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(142, Short.MAX_VALUE))
        );

        appointmentpanel.setBackground(new java.awt.Color(237, 246, 249));

        updatedate.setBackground(new java.awt.Color(196, 224, 215));
        updatedate.setEnabled(false);
        updatedate.setMaximumSize(new java.awt.Dimension(500, 300));
        updatedate.setPreferredSize(new java.awt.Dimension(629, 365));

        jLabel3.setFont(new java.awt.Font("BIZ UDPGothic", 1, 18)); // NOI18N
        jLabel3.setText("Select the date and time for manual document verfication appointment");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        timePicker1.setForeground(new java.awt.Color(11, 116, 114));

        jLabel34.setFont(new java.awt.Font("Corbel Light", 0, 18)); // NOI18N
        jLabel34.setText("Select Date");

        jLabel35.setFont(new java.awt.Font("Corbel Light", 0, 18)); // NOI18N
        jLabel35.setText("Select Time");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(null);

        jLabel32.setFont(new java.awt.Font("Corbel Light", 0, 24)); // NOI18N
        jLabel32.setText("Your selected date and time would be");
        jPanel2.add(jLabel32);
        jLabel32.setBounds(20, 20, 358, 30);

        datedisplay.setFont(new java.awt.Font("Corbel Light", 0, 24)); // NOI18N
        datedisplay.setForeground(new java.awt.Color(0, 153, 153));
        datedisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        datedisplay.setText("date");
        jPanel2.add(datedisplay);
        datedisplay.setBounds(130, 80, 130, 30);

        selctedtime.setFont(new java.awt.Font("Corbel Light", 0, 24)); // NOI18N
        selctedtime.setForeground(new java.awt.Color(0, 153, 153));
        selctedtime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selctedtime.setText("time");
        jPanel2.add(selctedtime);
        selctedtime.setBounds(140, 140, 110, 30);

        jLabel36.setFont(new java.awt.Font("Corbel Light", 0, 24)); // NOI18N
        jLabel36.setText("On");
        jPanel2.add(jLabel36);
        jLabel36.setBounds(180, 50, 30, 30);

        jLabel37.setFont(new java.awt.Font("Corbel Light", 0, 24)); // NOI18N
        jLabel37.setText("At");
        jPanel2.add(jLabel37);
        jLabel37.setBounds(180, 110, 30, 30);

        confirmdate.setBackground(new java.awt.Color(0, 102, 102));
        confirmdate.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        confirmdate.setForeground(new java.awt.Color(255, 255, 255));
        confirmdate.setText("Confirm the Appointment");
        confirmdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmdateActionPerformed(evt);
            }
        });

        updateDate.setBackground(new java.awt.Color(0, 102, 102));
        updateDate.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        updateDate.setForeground(new java.awt.Color(255, 255, 255));
        updateDate.setText("Change the Appointment");
        updateDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateDateActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Corbel Light", 0, 18)); // NOI18N
        jLabel33.setText("Want to Change the appointment date and time?");

        javax.swing.GroupLayout updatedateLayout = new javax.swing.GroupLayout(updatedate);
        updatedate.setLayout(updatedateLayout);
        updatedateLayout.setHorizontalGroup(
            updatedateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updatedateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1059, Short.MAX_VALUE))
            .addGroup(updatedateLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(updatedateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(updatedateLayout.createSequentialGroup()
                        .addGroup(updatedateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(dateselcted, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(timePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(updatedateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(updatedateLayout.createSequentialGroup()
                                .addGap(128, 128, 128)
                                .addComponent(confirmdate))
                            .addGroup(updatedateLayout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(updatedateLayout.createSequentialGroup()
                                .addGap(128, 128, 128)
                                .addComponent(updateDate)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        updatedateLayout.setVerticalGroup(
            updatedateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatedateLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(updatedateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(updatedateLayout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateselcted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, updatedateLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(confirmdate, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel33)
                        .addGap(18, 18, 18)
                        .addComponent(updateDate, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout appointmentpanelLayout = new javax.swing.GroupLayout(appointmentpanel);
        appointmentpanel.setLayout(appointmentpanelLayout);
        appointmentpanelLayout.setHorizontalGroup(
            appointmentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appointmentpanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(updatedate, javax.swing.GroupLayout.DEFAULT_SIZE, 1065, Short.MAX_VALUE)
                .addGap(54, 54, 54))
        );
        appointmentpanelLayout.setVerticalGroup(
            appointmentpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, appointmentpanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(updatedate, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                .addGap(39, 39, 39))
        );

        Applicationpg2.setBackground(new java.awt.Color(237, 246, 249));

        jLabel14.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel14.setText("Date of Birth");

        jLabel15.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel15.setText("Birth Certificate No.");

        jLabel16.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel16.setText("Birth District");

        jLabel17.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel17.setText("Place of Birth");

        jLabel18.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel18.setText("Sex");

        jLabel19.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel19.setText("Profession");

        jLabel20.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel20.setText("Dual Citizenship");

        jLabel22.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel22.setText("Mobile No.");

        jLabel23.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel23.setText("Email Address");

        sex.add(male);
        male.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        male.setSelected(true);
        male.setText("Male");

        sex.add(female);
        female.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        female.setText("Female");

        dualcit.add(dualyes);
        dualyes.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        dualyes.setText("Yes");
        dualyes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dualyesActionPerformed(evt);
            }
        });

        dualcit.add(dualno);
        dualno.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        dualno.setText("No");
        dualno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dualnoActionPerformed(evt);
            }
        });

        duelcitizen.setBackground(new java.awt.Color(196, 224, 215));

        jLabel24.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel24.setText("Foreign Nationality ");

        jLabel21.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel21.setText("Dual Citizenship Number ");

        fn.setEnabled(false);

        fpn.setEnabled(false);

        dzn.setEnabled(false);

        jLabel25.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel25.setText("Foreign passport No. ");

        javax.swing.GroupLayout duelcitizenLayout = new javax.swing.GroupLayout(duelcitizen);
        duelcitizen.setLayout(duelcitizenLayout);
        duelcitizenLayout.setHorizontalGroup(
            duelcitizenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(duelcitizenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(duelcitizenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(duelcitizenLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(64, 64, 64)
                        .addComponent(fpn, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
                    .addGroup(duelcitizenLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(36, 36, 36)
                        .addComponent(dzn))
                    .addGroup(duelcitizenLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(76, 76, 76)
                        .addComponent(fn)))
                .addContainerGap())
        );
        duelcitizenLayout.setVerticalGroup(
            duelcitizenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(duelcitizenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(duelcitizenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(dzn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(duelcitizenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(fn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(duelcitizenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(fpn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        submit.setBackground(new java.awt.Color(0, 102, 102));
        submit.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        submit.setForeground(new java.awt.Color(255, 255, 255));
        submit.setText("Submit");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        prev.setBackground(new java.awt.Color(0, 102, 102));
        prev.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        prev.setForeground(new java.awt.Color(255, 255, 255));
        prev.setText("Previous");
        prev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevActionPerformed(evt);
            }
        });

        telno.setText("MobileNo.");

        email.setText("Email");

        javax.swing.GroupLayout Applicationpg2Layout = new javax.swing.GroupLayout(Applicationpg2);
        Applicationpg2.setLayout(Applicationpg2Layout);
        Applicationpg2Layout.setHorizontalGroup(
            Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Applicationpg2Layout.createSequentialGroup()
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(Applicationpg2Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(duelcitizen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Applicationpg2Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Applicationpg2Layout.createSequentialGroup()
                                .addComponent(prev, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Applicationpg2Layout.createSequentialGroup()
                                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(56, 56, 56)
                                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(telno, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(Applicationpg2Layout.createSequentialGroup()
                                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(Applicationpg2Layout.createSequentialGroup()
                                            .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel14)
                                                .addComponent(jLabel15)
                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(32, 32, 32))
                                        .addGroup(Applicationpg2Layout.createSequentialGroup()
                                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGap(43, 43, 43)))
                                    .addGroup(Applicationpg2Layout.createSequentialGroup()
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(43, 43, 43)))
                                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(Applicationpg2Layout.createSequentialGroup()
                                        .addComponent(dualyes)
                                        .addGap(69, 69, 69)
                                        .addComponent(dualno))
                                    .addComponent(bPlace)
                                    .addComponent(bDis)
                                    .addComponent(bcno)
                                    .addGroup(Applicationpg2Layout.createSequentialGroup()
                                        .addComponent(male)
                                        .addGap(52, 52, 52)
                                        .addComponent(female, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(job, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dob, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE))))))
                .addContainerGap(478, Short.MAX_VALUE))
        );
        Applicationpg2Layout.setVerticalGroup(
            Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Applicationpg2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(dob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(bcno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(bDis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(bPlace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(male)
                    .addComponent(female))
                .addGap(18, 18, 18)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(job, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(dualyes)
                    .addComponent(dualno))
                .addGap(18, 18, 18)
                .addComponent(duelcitizen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(telno))
                .addGap(18, 18, 18)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(email))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Applicationpg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prev, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        Applicationpg1.setBackground(new java.awt.Color(237, 246, 249));

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel5.setText("Type of Service");

        tos.add(onedayserv);
        onedayserv.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        onedayserv.setText("One Day");

        tos.add(normalserv);
        normalserv.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        normalserv.setText("Normal");
        normalserv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normalservActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel2.setText("Type of Travel Document");

        totd.add(all);
        all.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        all.setText("All Countries");

        totd.add(me);
        me.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        me.setText("Middle East Countries");

        totd.add(emc);
        emc.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        emc.setText("Emergency Certificate");

        totd.add(idc);
        idc.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        idc.setText("Identity Certificate");
        idc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idcActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel6.setText("Present Travel Document No. (If any)");

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel7.setText("NIC no.");

        passportno.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel8.setText("Surname");

        sur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                surActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel9.setText("First name");

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel11.setText("Last name");

        jLabel12.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel12.setText("Permanent Address");

        address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel13.setText("District");

        district.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                districtActionPerformed(evt);
            }
        });

        nextpg.setBackground(new java.awt.Color(0, 102, 102));
        nextpg.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        nextpg.setForeground(new java.awt.Color(255, 255, 255));
        nextpg.setText("Next");
        nextpg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextpgActionPerformed(evt);
            }
        });

        image.setBackground(new java.awt.Color(0, 102, 102));
        image.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        image.setForeground(new java.awt.Color(255, 255, 255));
        image.setText("Upload your Passport Image Here");
        image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel27.setText("Expiry date");

        jLabel28.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel28.setText("Applying for");

        appType.add(newPass);
        newPass.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        newPass.setText("New document");
        newPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPassActionPerformed(evt);
            }
        });

        appType.add(renewPass);
        renewPass.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        renewPass.setText("Renewal");
        renewPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewPassActionPerformed(evt);
            }
        });

        idNo.setText("NIC");

        fname.setText("FirstName");

        lname.setText("LastName");

        javax.swing.GroupLayout Applicationpg1Layout = new javax.swing.GroupLayout(Applicationpg1);
        Applicationpg1.setLayout(Applicationpg1Layout);
        Applicationpg1Layout.setHorizontalGroup(
            Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Applicationpg1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Applicationpg1Layout.createSequentialGroup()
                        .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Applicationpg1Layout.createSequentialGroup()
                                .addComponent(all)
                                .addGap(33, 33, 33)
                                .addComponent(me))
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel28)
                            .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Applicationpg1Layout.createSequentialGroup()
                                    .addComponent(newPass)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(renewPass))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Applicationpg1Layout.createSequentialGroup()
                                    .addComponent(normalserv)
                                    .addGap(42, 42, 42)
                                    .addComponent(onedayserv))))
                        .addGap(18, 18, 18)
                        .addComponent(emc)
                        .addGap(18, 18, 18)
                        .addComponent(idc))
                    .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(Applicationpg1Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(Applicationpg1Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(district, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Applicationpg1Layout.createSequentialGroup()
                            .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel27)
                                .addComponent(jLabel7))
                            .addGap(18, 18, 18)
                            .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(passportno, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                                .addComponent(exp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(idNo, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(Applicationpg1Layout.createSequentialGroup()
                            .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sur, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fname, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lname, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(Applicationpg1Layout.createSequentialGroup()
                        .addComponent(image)
                        .addGap(262, 262, 262)
                        .addComponent(nextpg, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(431, Short.MAX_VALUE))
        );
        Applicationpg1Layout.setVerticalGroup(
            Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Applicationpg1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newPass)
                    .addComponent(renewPass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(normalserv)
                    .addComponent(onedayserv))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(all)
                    .addComponent(me)
                    .addComponent(emc)
                    .addComponent(idc))
                .addGap(18, 18, 18)
                .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(passportno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Applicationpg1Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(22, 22, 22)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fname))
                        .addGap(18, 18, 18)
                        .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lname))
                        .addGap(17, 17, 17)
                        .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(district, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(Applicationpg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nextpg, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Applicationpg1Layout.createSequentialGroup()
                        .addComponent(exp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(idNo, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Applicationpg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(appointmentpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Applicationpg2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Applicationpg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(appointmentpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Applicationpg2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scroll.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
           
    private void ApplicationtabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ApplicationtabMouseClicked
        Applicationpg1.setVisible(true);
        Applicationpg2.setVisible(false);
        home.setVisible(false);
        appointmentpanel.setVisible(false);
    }//GEN-LAST:event_ApplicationtabMouseClicked

    private void AppointmentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AppointmentsMouseClicked
        appointmentpanel.setVisible(true);
        Applicationpg2.setVisible(false);
        Applicationpg1.setVisible(false);
        home.setVisible(false);
    }//GEN-LAST:event_AppointmentsMouseClicked

    private void HometabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HometabMouseClicked
        applicationstatus(idno.getText());
        home.setVisible(true);
        Applicationpg1.setVisible(false);
        Applicationpg2.setVisible(false);
        appointmentpanel.setVisible(false);
    }//GEN-LAST:event_HometabMouseClicked

    private void dualyesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dualyesActionPerformed
        dzn.setEnabled(true);
        fn.setEnabled(true);
        fpn.setEnabled(true);
        
    }//GEN-LAST:event_dualyesActionPerformed

    private void dualnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dualnoActionPerformed
        dzn.setEnabled(false);
        fn.setEnabled(false);
        fpn.setEnabled(false);
    }//GEN-LAST:event_dualnoActionPerformed

    private void normalservActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normalservActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_normalservActionPerformed

    private void idcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idcActionPerformed

    private void surActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_surActionPerformed

    private void addressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressActionPerformed

    private void districtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_districtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_districtActionPerformed

    private void nextpgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextpgActionPerformed
        Applicationpg2.setVisible(true);
        Applicationpg1.setVisible(false);
        appointmentpanel.setVisible(false);
        home.setVisible(false);
    }//GEN-LAST:event_nextpgActionPerformed

    private void prevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevActionPerformed
        // TODO add your handling code here:
        Applicationpg1.setVisible(true);
        Applicationpg2.setVisible(false);
        appointmentpanel.setVisible(false);
        home.setVisible(false);
    }//GEN-LAST:event_prevActionPerformed

    private void imageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageActionPerformed

        connect.connectdb(); // connect the connect class
        
        JFileChooser chooser = new JFileChooser(); //define a new file chooser object
        chooser.showOpenDialog(jPanel1); // call a file chooser into the screen
        File f = chooser.getSelectedFile(); // get the selected file from file chooser
        filename = f.getAbsolutePath(); // store the absolute path of the file in the filename
        //get an imageicon object to show the obtained image with the relavent defined scale
        ImageIcon imgicon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(theimage.getWidth(),
                theimage.getHeight(),Image.SCALE_SMOOTH));
        theimage.setIcon(imgicon);//set the label as image icon
    }//GEN-LAST:event_imageActionPerformed
    private void submitActionPerformed(java.awt.event.ActionEvent evt){                                       
        connect ob = new connect();
        connect.connectdb();
        
        String apptype = newPass.isSelected()? "New" : "Renew";
        String toS = normalserv.isSelected()? "Normal" : "One Day";
        String toTs;
        if(all.isSelected()){toTs = "All";}
        else if(me.isSelected()){toTs = "MiddleEast";}
        else if(emc.isSelected()){toTs = "Emergency Certificate";}
        else{toTs = "Identity Certificate";}
        byte[] img = ob.insertImg(filename);
        int tno = 0;
        String expire = null;
        if(renewPass.isSelected()){
            String travno = passportno.getText();
            tno = Integer.parseInt(travno);
            expire = expDateString;
        }
        
        String surname = sur.getText();
        String pa = address.getText();
        String dis = district.getText();
        String id = idNo.getText();
        String t = telno.getText();
        int tt = Integer.parseInt(t);
        String mail = email.getText();
        String finame = fname.getText();
        String laname = lname.getText();
        String date = dateString;
        String b = bcno.getText();
        int bc = Integer.parseInt(b);
        String bd = bDis.getText();
        String pl = bPlace.getText();
        String seX = male.isSelected()? "Male" : "Female";
        String prof = job.getText();
        int dno = 0;
        int fpassn = 0;
        String fnat = null;
        if(dualyes.isSelected()){
            String dual = dzn.getText();
            dno = Integer.parseInt(dual);
            fnat = fn.getText();
            String fp = fpn.getText();
            fpassn = Integer.parseInt(fp);
        }
        
       ob.submitData(id, img, toS, toTs, apptype, tno, expire, surname, finame, laname, pa, dis, date, bc, bd, pl, seX, prof, dno,fnat, fpassn,tt, mail );
       try{
            ob.GenerateReport(apptype,toTs,tno,expire,id,surname,finame,laname,pa,dis,seX,date,b,prof,dno,fpassn,fnat);
            ob.userMail(mail, id);
       }
       catch(IOException e){
           JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
       }
       catch(DocumentException e){
           JOptionPane.showMessageDialog(null, e.getMessage(), "Failed", JOptionPane.INFORMATION_MESSAGE);
       }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            String yx = idno.getText();
            idNo.setText(yx);
            updateProf uprf = new updateProf();
            uprf.setVisible(true);
            dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void newPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPassActionPerformed
        passportno.setEnabled(false);
        exp.setEnabled(false);
        appstatus.setText("New Applicant");
    }//GEN-LAST:event_newPassActionPerformed

    private void renewPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewPassActionPerformed
        passportno.setEnabled(true);
        exp.setEnabled(true);
        appstatus.setText("Renew Applicant");
    }//GEN-LAST:event_renewPassActionPerformed
/*
    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
   
    }//GEN-LAST:event_submitActionPerformed
 */ 
    private void confirmdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmdateActionPerformed
        connect ob = new connect();
        connect.connectdb();
        
        String appDate = DateString;
        String time = x;
        String Id = idno.getText();
        
        ob.appointment(Id, time, appDate);
        
    }//GEN-LAST:event_confirmdateActionPerformed

    private void pdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfActionPerformed
        connect ob = new connect();
        connect.connectdb();
        
        ob.tempPdf(idno.getText());
        ob.sendmail(idno.getText());//mailhere?
        
    }//GEN-LAST:event_pdfActionPerformed

    private void loadimageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadimageActionPerformed
        connect ob = new connect();
        connect.connectdb();
        
        ob.image(idno.getText(), theimage);
    }//GEN-LAST:event_loadimageActionPerformed

    private void updateDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDateActionPerformed
        connect ob = new connect();
        connect.connectdb();
        
        ob.updateAppointment(idno.getText(),DateString,x);
    }//GEN-LAST:event_updateDateActionPerformed
                                           
        
                                          
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(signedUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(signedUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(signedUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(signedUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new signedUp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Applicationpg1;
    private javax.swing.JPanel Applicationpg2;
    private javax.swing.JPanel Applicationtab;
    private javax.swing.JPanel Appointments;
    private javax.swing.JPanel Hometab;
    private javax.swing.JTextField address;
    private javax.swing.JRadioButton all;
    private javax.swing.ButtonGroup appType;
    private javax.swing.JPanel appointmentpanel;
    private javax.swing.JLabel appstatus;
    private javax.swing.JTextField bDis;
    private javax.swing.JTextField bPlace;
    private javax.swing.JTextField bcno;
    private javax.swing.JButton confirmdate;
    private javax.swing.JLabel datedisplay;
    private com.toedter.calendar.JDateChooser dateselcted;
    private javax.swing.JTextField district;
    private com.toedter.calendar.JDateChooser dob;
    private javax.swing.ButtonGroup dualcit;
    private javax.swing.JRadioButton dualno;
    private javax.swing.JRadioButton dualyes;
    private javax.swing.JPanel duelcitizen;
    private javax.swing.JTextField dzn;
    private javax.swing.JLabel email;
    private javax.swing.JRadioButton emc;
    private com.toedter.calendar.JDateChooser exp;
    private javax.swing.JRadioButton female;
    private javax.swing.JLabel first;
    private javax.swing.JTextField fn;
    private javax.swing.JLabel fname;
    private javax.swing.JTextField fpn;
    private javax.swing.JPanel home;
    private javax.swing.JLabel idNo;
    private javax.swing.JRadioButton idc;
    private javax.swing.JLabel idno;
    private javax.swing.JButton image;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField job;
    private javax.swing.JLabel lname;
    private javax.swing.JButton loadimage;
    private javax.swing.JLabel maill;
    private javax.swing.JRadioButton male;
    private javax.swing.JRadioButton me;
    private javax.swing.JPanel menu;
    private javax.swing.JLabel mobno;
    private javax.swing.JRadioButton newPass;
    private javax.swing.JButton nextpg;
    private javax.swing.JRadioButton normalserv;
    private javax.swing.JRadioButton onedayserv;
    private javax.swing.JTextField passportno;
    private javax.swing.JButton pdf;
    private javax.swing.JButton prev;
    private javax.swing.JRadioButton renewPass;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JLabel selctedtime;
    private javax.swing.ButtonGroup sex;
    private javax.swing.JLabel statuss;
    private javax.swing.JButton submit;
    private javax.swing.JTextField sur;
    private javax.swing.JLabel telno;
    private javax.swing.JLabel theimage;
    private com.raven.swing.TimePicker timePicker1;
    private javax.swing.ButtonGroup tos;
    private javax.swing.ButtonGroup totd;
    private javax.swing.JButton updateDate;
    private javax.swing.JPanel updatedate;
    // End of variables declaration//GEN-END:variables
}
