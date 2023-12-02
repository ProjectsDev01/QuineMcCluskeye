import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TabContent1 implements TabContent {
    private JPanel mainPanel;
    private JTextField variableCountField;
    private CardLayout cardLayout;
    private TabContent2 tabContent2;


    public TabContent1(TabContent2 tabContent2) {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        this.tabContent2 = tabContent2;

        mainPanel.add(createMainInterface(), "main");
        cardLayout.show(mainPanel, "main");
    }

    @Override
    public Component getContent() {
        return mainPanel;
    }

    private JPanel createMainInterface() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel promptLabel = new JLabel("Enter the number of states:");
        variableCountField = new JTextField(3);
        variableCountField.setFont(new Font("Arial", Font.PLAIN, 24));
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String variableCountText = variableCountField.getText();
                if (variableCountText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter the variable count.");
                    return;
                }

                int variableCount = Integer.parseInt(variableCountText);
                tabContent2.processVariableCount(variableCount);
            }
        });

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(promptLabel, gbc);

        gbc.gridy = 1;
        inputPanel.add(variableCountField, gbc);

        gbc.gridy = 2;
        inputPanel.add(confirmButton, gbc);

        panel.add(inputPanel, BorderLayout.CENTER);

        return panel;
    }
}
