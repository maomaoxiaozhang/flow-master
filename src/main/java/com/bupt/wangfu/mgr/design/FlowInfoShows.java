package com.bupt.wangfu.mgr.design;

import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Tang L on 2017/4/15.
 */
public class FlowInfoShows {
    private String controllerAddr,switchId,port;//控制器地址，交换机，端口号
    private JFrame frame;// 主窗口
    public static Dimension screenSize;
    private JTabbedPane flowInfoTabbedPane;
    private JPanel managePane;// 流量总数面板
    private JLabel maclabel,bytelabel,upperlabel,servelabel,speedlabel,losslabel;
    private JTextField mac,bt,upper,sp,loss;
    private JComboBox serve;
    private JButton submit;


    public FlowInfoShows(){
        //this.controllerAddr = controllerAddr;
        //this.switchId = switchId;
        //this .port = port;
        JFrame.setDefaultLookAndFeelDecorated(true);
        /**
         * com.jtattoo.plaf.aluminium.AluminiumLookAndFeel 椭圆按钮+翠绿色按钮背景+金属质感
         *
         */
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        open();
    }

    public void open(){
        frame = new JFrame();// 主窗口
        Toolkit kit = Toolkit.getDefaultToolkit();
        screenSize = kit.getScreenSize();
        frame.setIconImage(kit.getImage("./img/INT25.png"));
        frame.setResizable(false);
        frame.setTitle("流量信息及管理");
        frame.setBounds(screenSize.width / 2 - 400, screenSize.height / 2 - 300, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        flowInfoTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        flowInfoTabbedPane.setBounds(0, 0, 500, 500);
        flowInfoTabbedPane.setPreferredSize(frame.getSize());
        frame.getContentPane().add(flowInfoTabbedPane);

        managePane = new JPanel();
        managePane.setBounds(500, 0, 300, 500);
        managePane.setLayout(null);
        frame.getContentPane().add(managePane);

       // JPanel bytes = new JPanel();
       // bytes.setLayout(null);
        //flowInfoTabbedPane.addTab("流量总数", bytes);
        FlowInfoShow bytesShow = new FlowInfoShow();// 柱状图的panel
        bytesShow.setTitle("柱状图");
        bytesShow.setHorizontalTitle("序号");
        bytesShow.setVerticallyTitle("数据量");

        ArrayList<String> elem = new ArrayList<>();
        ArrayList<Double> value = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            elem.add("elemelem" + i);
            value.add(123.0 * i);
        }
        bytesShow.setElem(elem);
        bytesShow.setValue(value);
        //JScrollPane scollpane = new JScrollPane(bytes);
        try {
            flowInfoTabbedPane.addTab("流量总数", bytesShow.createHistogramPanel());
            //scollpane.updateUI();
        }catch (Exception e) { e.printStackTrace(); }


        FlowInfoShow speedShow = new FlowInfoShow();// 柱状图的panel
        speedShow.setTitle("柱状图");
        speedShow.setHorizontalTitle("序号");
        speedShow.setVerticallyTitle("数据量");

        ArrayList<String> elem1 = new ArrayList<>();
        ArrayList<Double> value1 = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            elem.add("elemelem" + i);
            value.add(123.0 * i);
        }
        speedShow.setElem(elem);
        speedShow.setValue(value);
        //JScrollPane scollpane = new JScrollPane(bytes);
        try {
            flowInfoTabbedPane.addTab("平均速率", speedShow.createHistogramPanel());
            //scollpane.updateUI();
        }catch (Exception e) { e.printStackTrace(); }

        FlowInfoShow lossShow = new FlowInfoShow();// 柱状图的panel
        lossShow.setTitle("柱状图");
        lossShow.setHorizontalTitle("序号");
        lossShow.setVerticallyTitle("数据量");

        ArrayList<String> elem2 = new ArrayList<>();
        ArrayList<Double> value2 = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            elem.add("elemelem" + i);
            value.add(123.0 * i);
        }
        lossShow.setElem(elem);
        lossShow.setValue(value);
        //JScrollPane scollpane = new JScrollPane(bytes);
        try {
            flowInfoTabbedPane.addTab("平均速率", lossShow.createHistogramPanel());
            //scollpane.updateUI();
        }catch (Exception e) { e.printStackTrace(); }


        maclabel = new JLabel("mac地址:");
        maclabel.setBounds(20,50,100,23);
        mac = new JTextField("admin");
        mac.setBounds(150,50,100,23);
        bytelabel = new JLabel("当前流量:");
        bytelabel.setBounds(20,100,100,23);
        bt = new JTextField("admin");
        bt.setBounds(150,100,100,23);
        servelabel = new JLabel("服务等级");
        servelabel.setBounds(20,150,100,23);
        String list[] = {"等级1","等级2","等级3"};
        serve = new JComboBox(list);
        serve.setBounds(150,150,100,23);
        upperlabel = new JLabel("流量上限:");
        upperlabel.setBounds(20,200,100,23);
        upper = new JTextField();
        upper.setBounds(150,200,100,23);
        speedlabel = new JLabel("速度上限:");
        speedlabel.setBounds(20,250,100,23);
        sp = new JTextField();
        sp.setBounds(150,250,100,23);
        losslabel = new JLabel("丢包率上限:");
        losslabel.setBounds(20,300,100,23);
        loss = new JTextField();
        loss.setBounds(150,300,100,23);
        submit = new JButton("保存");
        submit.setBounds(150,350,100,23);
        managePane.add(maclabel);
        managePane.add(mac);
        managePane.add(bytelabel);
        managePane.add(bt);
        managePane.add(servelabel);
        managePane.add(serve);
        managePane.add(upperlabel);
        managePane.add(upper);
        managePane.add(losslabel);
        managePane.add(loss);
        managePane.add(speedlabel);
        managePane.add(sp);
        managePane.add(submit);

    }

    public static void main(String[] args) {
        FlowInfoShows FlowInfoShows = new FlowInfoShows();
    }



}
