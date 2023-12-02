import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class TabContent5 implements TabContent {
    private JTable resultTable;
    private JLabel functionLabel;

    public TabContent5() {
        resultTable = new JTable();
        functionLabel = new JLabel("Content for Tab 5", SwingConstants.CENTER);
    }

    @Override
    public Component getContent() {
        JPanel panel = new JPanel(new BorderLayout());
        Font font = new Font("Arial", Font.PLAIN, 24);

        // Add the table to the panel
        panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        resultTable.setFont(font);
        int rowHeight = 36;
        resultTable.setRowHeight(rowHeight);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        resultTable.setDefaultRenderer(Object.class, centerRenderer);
        JTableHeader tableHeader = resultTable.getTableHeader();
        Font headerFont = new Font("Arial", Font.PLAIN, 24);
        tableHeader.setFont(headerFont);

        // Add the label to the panel
        panel.add(functionLabel, BorderLayout.SOUTH);
        functionLabel.setFont(font);

        return panel;
    }

    public void finalResult(List<String> finalValues) {
        // Create a data model for the table
        DefaultTableModel tableModel = new DefaultTableModel();

        // Set column names based on finalValues
        if (!finalValues.isEmpty()) {
            String firstValue = finalValues.get(0);
            int numVariables = firstValue.length();

            for (int i = 0; i < finalValues.size(); i++) {
                tableModel.addColumn(getHeader(numVariables, i));
            }

            // Add the row with finalValues
            Object[] valuesArray = finalValues.toArray();
            tableModel.addRow(valuesArray);

            // Generate values for the next row
            Object[] nextRowValues = generateNextRowValues(finalValues, numVariables);
            tableModel.addRow(nextRowValues);

            // Set the data model for the table
            resultTable.setModel(tableModel);

            // Set preferred column width
            TableColumnModel columnModel = resultTable.getColumnModel();
            for (int i = 0; i < numVariables; i++) {
                if (i < finalValues.size()) {
                    columnModel.getColumn(i).setPreferredWidth(100); // You can adjust the column width
                }
            }

            // Display values from the second row in the JLabel
            displayFunction(finalValues);
        }
    }

    // Helper method to display the values from the second row
    private void displayFunction(List<String> finalValues) {
        DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
        Object[] valuesArray = tableModel.getDataVector().elementAt(1).toArray();

        StringBuilder functionText = new StringBuilder("y = ");

        // Check if the second row contains at least one non-empty value
        boolean hasNonEmptyValue = false;
        for (Object value : valuesArray) {
            if (value != null && !value.toString().trim().isEmpty()) {
                hasNonEmptyValue = true;
                break;
            }
        }

        if (hasNonEmptyValue) {
            for (int i = 0; i < valuesArray.length; i++) {
                if (valuesArray[i] != null && !valuesArray[i].toString().trim().isEmpty()) {
                    functionText.append(valuesArray[i]);
                    if (i < valuesArray.length - 1) {
                        functionText.append(" + ");
                    }
                }
            }
        } else {
            functionText.append("1");
        }

        // Update the text of the existing JLabel
        functionLabel.setText(functionText.toString());
    }


    // Helper method to generate column headers like "x3 x2 x1 x0"
    private String getHeader(int numVariables, int columnIndex) {
        StringBuilder header = new StringBuilder();
        for (int i = numVariables - 1; i >= 0; i--) {
            header.append("x").append(i).append(" ");
        }
        return header.toString().trim();
    }

    // Helper method to generate values for the next row
    private Object[] generateNextRowValues(List<String> finalValues, int numVariables) {
        Object[] nextRowValues = new Object[finalValues.size()];

        for (int columnIndex = 0; columnIndex < finalValues.size(); columnIndex++) {
            String header = getHeader(numVariables, columnIndex);
            String value = finalValues.get(columnIndex);

            StringBuilder nextRowValue = new StringBuilder();

            for (int i = 0; i < numVariables; i++) {
                char valueChar = value.charAt(i);
                int variableIndex = numVariables - 1 - i; // Ustalenie indeksu zmiennej zgodnie z kolejnością nagłówka

                if (valueChar == '1') {
                    // Przepisuj tylko wartości 1, pomijając '-'
                    nextRowValue.append("x").append(variableIndex).append(" ");
                } else if (valueChar == '0') {
                    nextRowValue.append("x̅").append(variableIndex).append(" ");
                }
            }

            nextRowValues[columnIndex] = nextRowValue.toString().trim();
        }

        return nextRowValues;
    }
}
