import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CompressionApp {

    private LZ77 obj = new LZ77 ();
    private JPanel panel1;
    private JRadioButton LZ77RadioButton;
    private JRadioButton adaptiveHuffmanRadioButton;
    private JRadioButton standardHuffmanRadioButton;
    private JRadioButton LZWRadioButton;
    private JRadioButton nonUnifromScalarQuantizerRadioButton;
    private JRadioButton feedForwardDPCMRadioButton;
    private JRadioButton vectorQuantizerRadioButton;
    private JButton compressButton;
    private JButton decompressButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton browseButton;
    private JRadioButton arithmeticCodingRadioButton;

    public CompressionApp() {
        LZ77RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String file_name=textField.getText();
                try {
                    obj.decompress(file_name);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.out.print(obj.substring);
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("CompressionTechniquesApp");
        frame.setContentPane(new CompressionApp().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
