package com.bupt.wangfu.mgr.design;

import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Created by HanB on 2016/10/10 0010.
 */
public class FlowInfoShow {

    private String title; // 图标题  
    private String verticallyTitle;// 纵向坐标显示名称  
    private String horizontalTitle;// 横向坐标显示名称  
    private List<String> elem = new ArrayList<>();// 内容初始值 （每个类型显示文字）
    private List<Double> value = new ArrayList<>();// 内容初始值  (每个类型显示精度)
    private class PanelByHistogram extends JPanel implements ChangeListener {

        private JScrollBar scroller = null;
        private SlidingCategoryDataset dataset;// 数据集

        private CategoryDataset createDataset() throws Exception {
            DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();// 缺省类型数据设置

            if (elem.size() == 0) {
                JOptionPane.showMessageDialog(null, "数据未添加！", "",JOptionPane.INFORMATION_MESSAGE);
            }
            if (elem.size() != value.size()) {
                JOptionPane.showMessageDialog(null, "数据量不符合标题数目", "",JOptionPane.INFORMATION_MESSAGE);
                throw new Exception("标题和数据数量不符");
            }
            for (int i = 0; i < elem.size(); i++) {
                defaultcategorydataset.addValue(value.get(i), "PanelByHistogram", elem.get(i));
            }
            return defaultcategorydataset;
        }

        private JFreeChart createChart(CategoryDataset categorydataset) throws Exception {

            JFreeChart jfreechart = ChartFactory.createBarChart(title,
                    horizontalTitle, verticallyTitle, categorydataset,
                    PlotOrientation.VERTICAL, false, true, false);

            TextTitle t = jfreechart.getTitle();
            t.setFont(new Font("宋体", Font.BOLD, 30));// 标题文字

            CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();

            CategoryAxis categoryaxis = categoryplot.getDomainAxis();// X轴
            categoryaxis.setMaximumCategoryLabelWidthRatio(0.8F);
            categoryaxis.setLowerMargin(0.02D);
            categoryaxis.setUpperMargin(0.02D);
            categoryaxis.setLabelFont(new Font("宋体", Font.BOLD, 20)); // x轴标题文字
            categoryaxis.setTickLabelFont(new Font("黑体", Font.BOLD, 10)); // x轴坐标上文字
            categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // x轴坐标上文字倾斜45°

            NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis(); // Y轴
            numberaxis.setLabelFont(new Font("宋体", Font.BOLD, 20)); // y轴标题文字
            numberaxis.setTickLabelFont(new Font("黑体", Font.BOLD, 10)); // y轴坐标上文字
            numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            categoryplot.getRangeAxis().setUpperMargin(0.15); // 设置最高的柱与顶端距离

            BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
            barrenderer.setDrawBarOutline(false);

            barrenderer.setItemLabelAnchorOffset(2D);
            barrenderer.setBaseItemLabelsVisible(true);
            barrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

            GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F,Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
            barrenderer.setSeriesPaint(0, gradientpaint);
            return jfreechart;
        }

        public void stateChanged(ChangeEvent changeevent) {
            dataset.setFirstCategoryIndex(scroller.getValue());
        }

        private PanelByHistogram() throws Exception {
            setLayout(null);
            dataset = new SlidingCategoryDataset(createDataset(), 0, elem.size());
            JFreeChart jfreechart = createChart(dataset);
            ChartPanel chartpanel = new ChartPanel(jfreechart);
            chartpanel.setBounds(0,0,400,500);
            add(chartpanel);
        }
    }

    public void setTitle(String title) { this.title = title; }

    public void setVerticallyTitle(String verticallyTitle) { this.verticallyTitle = verticallyTitle; }

    public void setHorizontalTitle(String horizontalTitle) { this.horizontalTitle = horizontalTitle; }

    public void setElem(List<String> elem) { this.elem = elem; }

    public void setValue(List<Double> value) { this.value = value; }

    public JPanel createHistogramPanel() throws Exception {
        return new PanelByHistogram();
    }


    public static void main(String[] args) {
        FlowInfoShow FlowInfoShow = new FlowInfoShow();// 柱状图的panel
        FlowInfoShow.setTitle("柱状图");
        FlowInfoShow.setHorizontalTitle("序号");
        FlowInfoShow.setVerticallyTitle("数据量");

        ArrayList<String> elem = new ArrayList<>();
        ArrayList<Double> value = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            elem.add("elemelem" + i);
            value.add(123.0 * i);
        }
        FlowInfoShow.setElem(elem);
        FlowInfoShow.setValue(value);

        JFrame frame = new JFrame();
        frame.setBounds(0,0,725,480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        JPanel panel = new JPanel();
        JScrollPane scollpane = new JScrollPane(panel);
        frame.add(scollpane);
        try {
            scollpane.setViewportView(FlowInfoShow.createHistogramPanel());
            scollpane.updateUI();
        }catch (Exception e) { e.printStackTrace(); }
    }
}
