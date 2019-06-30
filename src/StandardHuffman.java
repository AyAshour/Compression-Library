import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JLabel;
import java.awt.Window.Type;


public class StandardHuffman {
	

	private Standard_huffman obj = new Standard_huffman();
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
					StandardHuffman window = new StandardHuffman();
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
	public StandardHuffman() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLzProgram = new JFrame();
		frmLzProgram.setType(Type.POPUP);
		frmLzProgram.setTitle("StandardHuffman program");
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
				 String file_name=textField.getText();
				   //String content = readFile(file_name, StandardCharsets.UTF_8);
				   try {
					obj.decompress(file_name);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					   String file_name=textField.getText();
					  // String content = readFile(file_name, StandardCharsets.UTF_8);
					   obj.compress(file_name);
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
	protected void compress(String content) {
		// TODO Auto-generated method stub
		
	}
	protected void decompress(String file_name) {
		// TODO Auto-generated method stub
		
	}
}
