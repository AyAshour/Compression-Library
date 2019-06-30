import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.JLabel;
import java.awt.Window.Type;


public class NUSQ_GUI {


    private feedForwardDPCM obj = new feedForwardDPCM();
    private JFrame frmLzProgram;
    private JTextField textField;
    private JLabel lblEnterFileName;
    public static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NUSQ_GUI window = new NUSQ_GUI();
                    window.frmLzProgram.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public NUSQ_GUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmLzProgram = new JFrame();
        frmLzProgram.setType(Type.POPUP);
        frmLzProgram.setTitle("compression program");
        frmLzProgram.setBounds(100, 100, 450, 300);
        frmLzProgram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmLzProgram.getContentPane().setLayout(null);

        textField = new JTextField();
        textField.setBounds(134, 62, 226, 26);
        frmLzProgram.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnDecompress = new JButton("Decompress");
        btnDecompress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try{
                    String file_name=textField.getText();
                    File dec=new File(file_name);
                    //String content = readFile(file_name, StandardCharsets.UTF_8);
                    obj.decompress(dec);

                } catch(IOException ie) {
                    ie.printStackTrace();
                }
            }
        });
        btnDecompress.setBackground(SystemColor.activeCaption);
        btnDecompress.setBounds(238, 171, 122, 34);
        frmLzProgram.getContentPane().add(btnDecompress);

        JButton btnCompress = new JButton("Compress");
        btnCompress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try{
                    File compress=new File(textField.getText());
                  //  String content = readFile(file_name, StandardCharsets.UTF_8);
                    obj.compress(textField.getText(),7);
                } catch(IOException ie) {
                    ie.printStackTrace();
                }
            }
        });
        btnCompress.setBackground(SystemColor.activeCaption);
        btnCompress.setBounds(67, 171, 122, 34);
        frmLzProgram.getContentPane().add(btnCompress);

        lblEnterFileName = new JLabel("Enter file name :");
        lblEnterFileName.setBounds(29, 50, 200, 50);
        frmLzProgram.getContentPane().add(lblEnterFileName);
    }
}
