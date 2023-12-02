import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

class TabContent2 implements TabContent {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField valueField;
    private JTextField binaryRecordField;
    private JButton addButton;
    private TabContent3 tabContent3;

    private int onesCount; // Liczba wartości "1"
    private int variableCount; // Liczba zmiennych

    public TabContent2(TabContent3 tabContent3) {
        Font font = new Font("Arial", Font.PLAIN, 20);
        this.tabContent3 = tabContent3;

        // Inicjalizacja pól tekstowych i przycisku
        valueField = new JTextField(10);  // Zwiększenie szerokości pola tekstowego
        binaryRecordField = new JTextField(20);  // Zwiększenie szerokości pola tekstowego
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDataFromUserInput();
            }
        });

        // Inicjalizacja modelu tabeli
        tableModel = new DefaultTableModel(new Object[]{"Output", "Input", "Number of ones", "Delete"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Integer.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        // Inicjalizacja tabeli
        table = new JTable(tableModel);
        table.setFont(font);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        // Dodanie przycisku "delete" do każdego wiersza
        addDeleteButtonToTable();

        // Zwiększenie wysokości komórek
        int rowHeight = 40; // Zwiększenie wysokości komórki
        table.setRowHeight(rowHeight);

        // Ustawienie niestandardowej czcionki dla nagłówków kolumn
        JTableHeader tableHeader = table.getTableHeader();
        Font headerFont = new Font("Arial", Font.PLAIN, 24); // Niższa czcionka dla nagłówków
        tableHeader.setFont(headerFont);
    }

    @Override
    public Component getContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Dodanie pól tekstowych i przycisku do panelu
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Output: "));
        inputPanel.add(valueField);
        inputPanel.add(new JLabel("Input: "));
        inputPanel.add(binaryRecordField);
        inputPanel.add(addButton);

        // Dodanie tabeli do panelu
        JScrollPane scrollPane = new JScrollPane(table);

        // Dodanie paneli do głównego panelu
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void processVariableCount(int variableCount) {
        this.variableCount = variableCount;
        // Dodatkowe ustawienia, jeśli potrzebne
    }

    private void addDataFromUserInput() {
        String value = valueField.getText();
        String binaryRecord = binaryRecordField.getText();

        // Walidacja wprowadzonych danych (możesz dostosować warunki)
        if (value.isEmpty() || binaryRecord.isEmpty() || binaryRecord.length() != variableCount) {
            JOptionPane.showMessageDialog(null, "Please enter both value and a binary record of the correct length.");
            return;
        }

        // Sprawdzenie, czy istnieje już wartość w drugiej kolumnie
        if (!isBinaryRecordUnique(binaryRecord)) {
            JOptionPane.showMessageDialog(null, "Binary record already exists in the table. Cannot add a new row.");
            return;
        }

        // Dodanie danych do modelu tabeli
        Vector<Object> row = new Vector<>();
        row.add(value);
        row.add(binaryRecord);
        row.add(countOnes(binaryRecord));
        row.add("Delete");

        tableModel.addRow(row);

        // Przekazanie danych do TabContent3
        tabContent3.processTabContent23Data(getDataFromTableModel(), 0);
    }

    private boolean isBinaryRecordUnique(String binaryRecord) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (binaryRecord.equals(tableModel.getValueAt(i, 1))) {
                return false;
            }
        }
        return true;
    }

    private Vector<Vector<Object>> getDataFromTableModel() {
        Vector<Vector<Object>> data = new Vector<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Vector<Object> row = new Vector<>();
            row.add(tableModel.getValueAt(i, 0));
            row.add(tableModel.getValueAt(i, 1));
            row.add(tableModel.getValueAt(i, 2));
            data.add(row);
        }
        return data;
    }

    private int countOnes(String binaryRecord) {
        return (int) binaryRecord.chars().filter(c -> c == '1').count();
    }

    private void addDeleteButtonToTable() {
        TableColumn column = table.getColumnModel().getColumn(3); // Dodanie nowej kolumny dla przycisku "delete"

        // Dodanie przycisku do każdego wiersza
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Delete");
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Delete");
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    // Usuń wiersz po naciśnięciu przycisku "Delete"
                    tableModel.removeRow(table.getSelectedRow());
                    tabContent3.processTabContent23Data(getDataFromTableModel(), variableCount);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }
}
