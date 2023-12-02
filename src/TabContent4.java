import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TabContent4 implements TabContent {
    private TabContent5 tabContent5;
    private JTable dynamicTable;
    private DefaultTableModel dynamicTableModel;
    private List<String> finalValues = new ArrayList<>();
    private JPanel combinedPanel; // Added declaration

    public TabContent4(TabContent5 tabContent5) {
        this.tabContent5 = tabContent5;
        dynamicTableModel = new CustomTableModel();
        dynamicTable = new JTable(dynamicTableModel);

        Font font = new Font("Arial", Font.PLAIN, 24);
        dynamicTable.setFont(font);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        dynamicTable.setDefaultRenderer(Object.class, centerRenderer);

        int rowHeight = 36;
        dynamicTable.setRowHeight(rowHeight);

        JTableHeader tableHeader = dynamicTable.getTableHeader();
        Font headerFont = new Font("Arial", Font.PLAIN, 24);
        tableHeader.setFont(headerFont);

        dynamicTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == dynamicTable.getColumnCount() - 1) {
                    String updatedValue = (String) dynamicTable.getValueAt(row, column);
                    finalValues.add(updatedValue);
                }
            }
        });

        // Initialize combinedPanel
        combinedPanel = new JPanel(new BorderLayout());

        // Dodaj przycisk "Apply"
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyChanges();
            }
        });

        // Dodaj przycisk do panelu
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);
        combinedPanel.add(buttonPanel, BorderLayout.EAST);
    }

    @Override
    public Component getContent() {
        JPanel rowHeaderPanel = createRowHeaderPanel();
        JScrollPane scrollPane = new JScrollPane(dynamicTable);
        combinedPanel.add(rowHeaderPanel, BorderLayout.WEST);
        combinedPanel.add(scrollPane, BorderLayout.CENTER);

        return combinedPanel;
    }

    private JPanel createRowHeaderPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        int numRows = dynamicTableModel.getRowCount();
        Font rowHeaderFont = new Font("Arial", Font.PLAIN, 24);
        return panel;
    }

    public void processTabContent24Data(Vector<Vector<Object>> binaryValues, List<String> unusedValues) {
        Vector<Object> columnIdentifiers = new Vector<>();
        columnIdentifiers.add(" ");

        for (Vector<Object> row : binaryValues) {
            String firstColumnValue = row.get(0).toString();
            String secondColumnValue = row.get(1).toString();
            if ("1".equals(firstColumnValue)) {
                columnIdentifiers.add(secondColumnValue);
            }
        }

        columnIdentifiers.add("  ");

        DefaultTableModel newTableModel = new DefaultTableModel(columnIdentifiers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };

        for (String unusedValue : unusedValues) {
            Vector<Object> row = new Vector<>();
            row.add(unusedValue);

            for (int i = 1; i < columnIdentifiers.size() - 1; i++) {
                String columnHeader = columnIdentifiers.get(i).toString();
                String firstColumnValue = unusedValue;

                if (isOneCharDifference(firstColumnValue, columnHeader)) {
                    row.add("X");
                } else {
                    row.add("");
                }
            }

            newTableModel.addRow(row);
        }

        dynamicTable.setModel(newTableModel);

        for (int i = 1; i < columnIdentifiers.size() - 1; i++) {
            int foundX = 0;
            int rowIndex = 0;

            for (int j = 0; j < dynamicTable.getRowCount(); j++) {
                Object cellValue = dynamicTable.getValueAt(j, i);
                if ("X".equals(cellValue.toString())) {
                    foundX++;
                    rowIndex = j;
                }
            }

            if (foundX == 1) {
                newTableModel.setValueAt("*", rowIndex, columnIdentifiers.size() - 1);
            }
        }
        finalValues.clear();

        for (int i = 0; i < dynamicTable.getRowCount(); i++) {
            String valueFinal = (String) newTableModel.getValueAt(i, columnIdentifiers.size() - 1);
            if (valueFinal != null && valueFinal.equals("*")) {
                finalValues.add((String) dynamicTable.getValueAt(i, 0));
            }
        }

        System.out.println(finalValues);
        tabContent5.finalResult(finalValues);
    }
    private void applyChanges() {
        finalValues.clear();

        for (int i = 0; i < dynamicTable.getRowCount(); i++) {
            String valueFinal = (String) dynamicTable.getValueAt(i, dynamicTable.getColumnCount() - 1);
            if (valueFinal != null && valueFinal.equals("*")) {
                finalValues.add((String) dynamicTable.getValueAt(i, 0));
            }
        }

        System.out.println(finalValues);
        tabContent5.finalResult(finalValues);
    }




    private boolean isOneCharDifference(String str1, String str2) {
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != '-' && str1.charAt(i) != str2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private class CustomTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    }
}
