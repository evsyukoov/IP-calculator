import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class GUI {
    JFrame jFrame;
    private int height;
    private int width;
    private JTextField ipAdressField;
    private JTextField maskField;
    private JPanel jPanel;
    JButton jButton;
    JButton calculateButton;
    private JTextArea broadcast;
    private JTextArea interval;
    private JTextArea networkNumber;
    private JTextArea numberofHosts;
    JButton repeatButton;
    private String ip;
    private String mask;

    public void setWindowSize(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        height = (int) toolkit.getScreenSize().getHeight();
        width =  (int) toolkit.getScreenSize().getWidth();
    }

    public void setFrame(){
        jFrame = new JFrame("IP-calculator");
        jPanel = new JPanel();
        setWindowSize();
        jFrame.repaint();
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(width/4 - width/8,  height/2 - height/4,  width/4, height/3);
        setIpAdress();
        setMaskField();
        broadcast = new JTextArea("Broadcast");
        interval = new JTextArea("Interval");
        networkNumber = new JTextArea("NetworkNumber");
        numberofHosts = new JTextArea("NumberOfHosts");
        broadcast.setColumns(30);
        interval.setColumns(30);
        networkNumber.setColumns(30);
        numberofHosts.setColumns(30);
        setCalculateButton();
        setRepeatButton();
        jPanel.add(broadcast);
        jPanel.add(interval);
        jPanel.add(networkNumber);
        jPanel.add(numberofHosts);
        jPanel.add(repeatButton);
        jFrame.add(jPanel);
        jFrame.setVisible(true);
        jPanel.revalidate();
        jPanel.repaint();
    }

    public void setCalculateButton()
    {
        calculateButton = new JButton("Считать");
        calculateButton.setBounds(200, 200, 100, 100);
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NetWhatHelper nwh = new NetWhatHelper(mask, ip);
                nwh.run();
                broadcast.setText(String.format("Broadcast :      %s",nwh.getBroadcast()));
                interval.setText(String.format("Interval :          %s", nwh.getInterval()));
                networkNumber.setText(String.format("Network :        %s",nwh.getNetWorknumber()));
                numberofHosts.setText(String.format("Number of free hosts :           %s",nwh.getNumberOfHosts()));
            }
        });
        jPanel.add(calculateButton);
    }

    public void setMaskField()
    {
        maskField = new JTextField();
        maskField.setText("Введите маску");
        maskField.setColumns(30);
        maskField.setVisible(true);
        maskField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (maskField.isEnabled())
                    maskField.setText("");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (maskField.isEnabled()) maskField.setText("Введите маску. Нажмите Enter");
            }
        });
        maskField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maskField.setEnabled(false);
                mask = maskField.getText();
            }
        });
        jPanel.add(maskField);
        jPanel.revalidate();
    }

    public void setIpAdress() {
        ipAdressField = new JTextField();
        ipAdressField.setText("Введите ip-aдрес. Нажмите Enter");
        ipAdressField.setColumns(30);
        ipAdressField.setVisible(true);
        ipAdressField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ipAdressField.isEnabled())
                    ipAdressField.setText("");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (ipAdressField.isEnabled()) ipAdressField.setText("Введите ip-aдрес. Нажмите Enter");
            }
        });
        ipAdressField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ipAdressField.setEnabled(false);
                ip = ipAdressField.getText();
            }
        });
        jPanel.add(ipAdressField);
        jPanel.revalidate();
    }

    public void setRepeatButton(){
        repeatButton = new JButton("Заново");
        repeatButton.setBounds(300,300,300,300);
        repeatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                setFrame();
            }
        });
    }
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setFrame();
    }
}