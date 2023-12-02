import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quine-McCluskeye");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            JTabbedPane tabbedPane = new JTabbedPane();

            TabContent5 tabContent5 = new TabContent5();
            TabContent4 tabContent4 = new TabContent4(tabContent5);
            TabContent3 tabContent3 = new TabContent3(tabContent4);
            TabContent2 tabContent2 = new TabContent2(tabContent3);
            TabContent1 tabContent1 = new TabContent1(tabContent2);

            tabbedPane.addTab("Number Of States", tabContent1.getContent());
            tabbedPane.addTab("TruthTable", tabContent2.getContent());
            tabbedPane.addTab("ConnectingTogether", tabContent3.getContent());
            tabbedPane.addTab("ImplicantTab", tabContent4.getContent());
            tabbedPane.addTab("Solution", tabContent5.getContent());

            frame.add(tabbedPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton prevButton = new JButton("Previous");
            JButton nextButton = new JButton("Next");

            prevButton.addActionListener(e -> {
                int currentIndex = tabbedPane.getSelectedIndex();
                int newIndex = (currentIndex - 1 + tabbedPane.getTabCount()) % tabbedPane.getTabCount();
                tabbedPane.setSelectedIndex(newIndex);
            });

            nextButton.addActionListener(e -> {
                int currentIndex = tabbedPane.getSelectedIndex();
                int newIndex = (currentIndex + 1) % tabbedPane.getTabCount();
                tabbedPane.setSelectedIndex(newIndex);
            });

            // Ustawiamy LayoutManager dla buttonPanel
            buttonPanel.setLayout(new BorderLayout());

            // Dodajemy przyciski do panelu
            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.add(prevButton);
            centerPanel.add(nextButton);
            buttonPanel.add(centerPanel, BorderLayout.CENTER);

            // Dodajemy napis "Made by..." po prawej stronie na dole
            JLabel labelField = new JLabel("  Made by Bartosz Golis");
            buttonPanel.add(labelField, BorderLayout.SOUTH);

            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }
}
