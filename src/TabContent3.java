import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Vector;

class TabContent3 implements TabContent {
    private JTable table;
    private DefaultTableModel tableModel;
    private Map<Integer, List<String>> indexGroupsColumn2;
    private Map<Integer, List<String>> indexGroupsColumn3;
    private TabContent4 tabContent4;

    // Kolor dla oznaczenia komórek zielonym
    Color greenColor = new Color(0, 255, 0);  // Zielony kolor
    // Kolor dla oznaczenia komórek czerwonym
    Color redColor = new Color(255, 0, 0);  // Zielony kolor

    // Lista dla wszystkich kolumn
    private List<String> usedValuesColumn = new ArrayList<>();
    private List<String> unusedValuesColumn = new ArrayList<>();

    // Dodatkowe listy dla danych w Column 6 i Column 7
    private List<String> resultColumn6 = new ArrayList<>();
    private List<String> resultColumn7 = new ArrayList<>();

    public TabContent3(TabContent4 tabContent4) {
        tableModel = new DefaultTableModel(new Object[]{"Index Group", "Column 1", "Column 2", "Column 3", "Column 4", "Column 5", "Column 6", "Column 7"}, 0);
        table = new JTable(tableModel);
        indexGroupsColumn2 = new HashMap<>();
        indexGroupsColumn3 = new HashMap<>();
        this.tabContent4 = tabContent4;

        // Ustawienie niestandardowej czcionki i wyśrodkowania treści w komórkach
        Font font = new Font("Arial", Font.PLAIN, 24);
        table.setFont(font);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Zwiększenie wysokości komórek
        int rowHeight = 36; // Zmień na odpowiednią wysokość
        table.setRowHeight(rowHeight);

        // Ustawienie niestandardowej czcionki dla nagłówków kolumn
        JTableHeader tableHeader = table.getTableHeader();
        Font headerFont = new Font("Arial", Font.PLAIN, 24); // Niższa czcionka dla nagłówków
        tableHeader.setFont(headerFont);

        // Dodanie dwóch dodatkowych kolumn do modelu tabeli
        tableModel.addColumn("Column 6");
        tableModel.addColumn("Column 7");
    }

    @Override
    public Component getContent() {
        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }

    public void processTabContent23Data(Vector<Vector<Object>> dataFromTabContent2, int numberOfColumn) {
        // Tworzymy mapę grup indeksowych dla Kolumny 1
        Map<Integer, List<String>> indexGroupsColumn1 = new HashMap<>();

        // Tworzymy osobną mapę grup indeksowych dla Kolumny 2
        Map<Integer, List<String>> indexGroupsColumn2 = new HashMap<>();

        for (Vector<Object> row : dataFromTabContent2) {
            String binaryRepresentation = (String) row.get(1);
            Integer onesCount1 = (Integer) row.get(2);
            Integer onesCount2 = countOnes(binaryRepresentation);

            // Tworzymy klucz grupy na podstawie liczby jedynek w Kolumnie 1
            if (!indexGroupsColumn1.containsKey(onesCount1)) {
                indexGroupsColumn1.put(onesCount1, new ArrayList<>());
            }
            // Dodajemy wartość zero-jedynkową do odpowiedniej grupy w Kolumnie 1
            List<String> groupColumn1 = indexGroupsColumn1.get(onesCount1);
            groupColumn1.add(binaryRepresentation);

            // Tworzymy klucz grupy na podstawie liczby jedynek w Kolumnie 2
            if (!indexGroupsColumn2.containsKey(onesCount2)) {
                indexGroupsColumn2.put(onesCount2, new ArrayList<>());
            }
            // Dodajemy wartość zero-jedynkową do odpowiedniej grupy w Kolumnie 2
            List<String> groupColumn2 = indexGroupsColumn2.get(onesCount2);
            groupColumn2.add(binaryRepresentation);
            // Tworzymy klucz grupy na podstawie liczby jedynek w Kolumnie 3
            if (!indexGroupsColumn3.containsKey(onesCount2)) {
                indexGroupsColumn3.put(onesCount2, new ArrayList<>());
            }
            // Dodajemy wartość zero-jedynkową do odpowiedniej grupy w Kolumnie 3
            List<String> groupColumn3 = indexGroupsColumn3.get(onesCount2);
            groupColumn3.add(binaryRepresentation);
        }

        // Sortujemy grupy indeksowe po liczbie jedynek w Kolumnie 1
        List<Integer> sortedKeysColumn1 = new ArrayList<>(indexGroupsColumn1.keySet());
        Collections.sort(sortedKeysColumn1);

        // Sortujemy grupy indeksowe po liczbie jedynek w Kolumnie 2
        List<Integer> sortedKeysColumn2 = new ArrayList<>(indexGroupsColumn2.keySet());
        Collections.sort(sortedKeysColumn2);

        // Sortujemy grupy indeksowe po liczbie jedynek (kluczu) w Kolumnie 3
        List<Integer> sortedKeysColumn3 = new ArrayList<>(indexGroupsColumn3.keySet());
        Collections.sort(sortedKeysColumn3);

        // Wyczyść istniejące dane w modelu tabeli w Kolumnie 3
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
        // Wyczyść tablicę usedValuesColumn i unusedValuesColumn
        usedValuesColumn.clear();
        unusedValuesColumn.clear();

        // Wypełniamy tabelę danymi w Kolumnie 2
        List<String> resultColumn2 = new ArrayList<>();

        for (int i = 0; i < sortedKeysColumn2.size() - 1; i++) {
            List<String> group1 = indexGroupsColumn2.get(sortedKeysColumn2.get(i));
            List<String> group2 = indexGroupsColumn2.get(sortedKeysColumn2.get(i + 1));

            if (Math.abs(sortedKeysColumn2.get(i) - sortedKeysColumn2.get(i + 1)) <= 1) {
                for (String value1 : group1) {
                    for (String value2 : group2) {
                        if (!hasSamePositionHyphens(value1, value2)) {
                            String result = performMergeWithSamePosition(value1, value2);
                            if (!result.isEmpty() && !resultColumn2.contains(result)) {
                                resultColumn2.add(result);
                            }
                        }
                    }
                }
            }
        }

        // Wypełniamy tabelę danymi w Kolumnie 3
        List<String> resultColumn3 = new ArrayList<>();

        for (int i = 0; i < resultColumn2.size() - 1; i++) {
            String value1 = resultColumn2.get(i); // Kolumna 2
            for (int j = i + 1; j < resultColumn2.size(); j++) {
                String value2 = resultColumn2.get(j); // Kolumna 2
                if (hasSamePositionHyphens(value1, value2)) {
                    String result = performMergeWithSamePosition(value1, value2);
                    if (!result.isEmpty() && !resultColumn3.contains(result)) {
                        resultColumn3.add(result);
                    }
                }
            }
        }

        // Wypełniamy tabelę danymi w Kolumnie 4
        List<String> resultColumn4 = new ArrayList<>();

        for (int i = 0; i < resultColumn3.size() - 1; i++) {
            String value1 = resultColumn3.get(i); // Kolumna 3
            for (int j = i + 1; j < resultColumn3.size(); j++) {
                String value2 = resultColumn3.get(j); // Kolumna 3
                if (hasSamePositionHyphens(value1, value2)) {
                    String result = performMergeWithSamePosition(value1, value2);
                    if (!result.isEmpty() && !resultColumn4.contains(result)) {
                        resultColumn4.add(result);
                    }
                }
            }
        }

        // Wypełniamy tabelę danymi w Kolumnie 5
        List<String> resultColumn5 = new ArrayList<>();

        for (int i = 0; i < resultColumn4.size() - 1; i++) {
            String value1 = resultColumn4.get(i); // Kolumna 4
            for (int j = i + 1; j < resultColumn4.size(); j++) {
                String value2 = resultColumn4.get(j); // Kolumna 4
                if (hasSamePositionHyphens(value1, value2)) {
                    String result = performMergeWithSamePosition(value1, value2);
                    if (!result.isEmpty() && !resultColumn5.contains(result)) {
                        resultColumn5.add(result);
                    }
                }
            }
        }

        // Aktualizujemy Grupa indeksowa, Kolumna 1
        int rowIndex = 0;
        for (Integer onesCount : sortedKeysColumn1) {
            List<String> groupColumn1 = indexGroupsColumn1.get(onesCount);
            for (String value : groupColumn1) {
                if (tableModel.getRowCount() <= rowIndex) {
                    tableModel.addRow(new Object[tableModel.getColumnCount()]);

                }
                tableModel.setValueAt(onesCount, rowIndex, 0);  // Grupa Indeksowa
                tableModel.setValueAt(value, rowIndex, 1);  // Kolumna 1

                // Dodajemy wartość do listy użytych lub nieużytych
                if (usedValuesColumn.contains(value)) {
                    tableModel.setValueAt(value + "√", rowIndex, 1);  // Oznaczamy wartość "√"
                } else {
                    tableModel.setValueAt(value + "*", rowIndex, 1);  // Oznaczamy wartość "*"
                    // Sprawdź, czy wartość już istnieje w liście
                    if (!usedValuesColumn.contains(value) && !unusedValuesColumn.contains(value)) {
                        unusedValuesColumn.add(value);
                    }
                }
                rowIndex++;
            }
        }

        // Dodajemy wyniki do Kolumny 2
        rowIndex = 0;
        for (String result : resultColumn2) {
            if (tableModel.getRowCount() <= rowIndex) {
                tableModel.addRow(new Object[tableModel.getColumnCount()]);

            }
            tableModel.setValueAt(result, rowIndex, 2); // Kolumna 2

            // Dodajemy wartość do listy użytych lub nieużytych
            if (usedValuesColumn.contains(result)) {
                tableModel.setValueAt(result + "√", rowIndex, 2);  // Oznaczamy wartość "*"
            }
            else{
                tableModel.setValueAt(result + "*", rowIndex, 2);  // Oznaczamy wartość "*"
                // Sprawdź, czy wartość już istnieje w liście
                if (!usedValuesColumn.contains(result) && !unusedValuesColumn.contains(result)) {
                    unusedValuesColumn.add(result);
                }
            }
            rowIndex++;
        }

        // Wypełniamy tabelę danymi w Kolumni 3
        rowIndex = 0;
        for (String result : resultColumn3) {
            if (tableModel.getRowCount() <= rowIndex) {
                tableModel.addRow(new Object[tableModel.getColumnCount()]);

            }
            tableModel.setValueAt(result, rowIndex, 3); // Kolumna 3

            // Dodajemy wartość do listy użytych lub nieużytych
            if (usedValuesColumn.contains(result)) {
                tableModel.setValueAt(result + "√", rowIndex, 3);  // Oznaczamy wartość "*"
            }
            else{
                tableModel.setValueAt(result + "*", rowIndex, 3);  // Oznaczamy wartość "*"
                if (!usedValuesColumn.contains(result) && !unusedValuesColumn.contains(result)) {
                    unusedValuesColumn.add(result);
                }
            }
            rowIndex++;
        }

        // Wypełniamy tabelę danymi w Kolumni 4
        rowIndex = 0;
        for (String result : resultColumn4) {
            if (tableModel.getRowCount() <= rowIndex) {
                tableModel.addRow(new Object[tableModel.getColumnCount()]);

            }
            tableModel.setValueAt(result, rowIndex, 4); // Kolumna 4

            // Dodajemy wartość do listy użytych lub nieużytych
            if (usedValuesColumn.contains(result)) {
                tableModel.setValueAt(result + "√", rowIndex, 4);  // Oznaczamy wartość "*"
            }
            else{
                tableModel.setValueAt(result + "*", rowIndex, 4);  // Oznaczamy wartość "*"
                if (!usedValuesColumn.contains(result) && !unusedValuesColumn.contains(result)) {
                    unusedValuesColumn.add(result);
                }
            }
            rowIndex++;
        }

        // Wypełniamy tabelę danymi w Kolumni 5
        rowIndex = 0;
        for (String result : resultColumn5) {
            if (tableModel.getRowCount() <= rowIndex) {
                tableModel.addRow(new Object[tableModel.getColumnCount()]);

            }
            tableModel.setValueAt(result, rowIndex, 5); // Kolumna 5

            // Dodajemy wartość do listy użytych lub nieużytych
            if (usedValuesColumn.contains(result)) {
                tableModel.setValueAt(result + "√", rowIndex, 5);  // Oznaczamy wartość "*"
            }
            else{
                tableModel.setValueAt(result + "*", rowIndex, 5);  // Oznaczamy wartość "*"
                if (!usedValuesColumn.contains(result) && !unusedValuesColumn.contains(result)) {
                    unusedValuesColumn.add(result);
                }
            }
            rowIndex++;
        }
        // Wypełnianie tabeli danymi w Column 6
        rowIndex = 0;
        for (String result : resultColumn6) {
            if (tableModel.getRowCount() <= rowIndex) {
                tableModel.addRow(new Object[tableModel.getColumnCount()]);
            }
            tableModel.setValueAt(result, rowIndex, 6); // Column 6

            // Dodajemy wartość do listy użytych lub nieużytych
            if (usedValuesColumn.contains(result)) {
                tableModel.setValueAt(result + "√", rowIndex, 6);  // Oznaczamy wartość "*"
            } else {
                tableModel.setValueAt(result + "*", rowIndex, 6);  // Oznaczamy wartość "*"
                if (!usedValuesColumn.contains(result) && !unusedValuesColumn.contains(result)) {
                    unusedValuesColumn.add(result);
                }
            }
            rowIndex++;
        }

        // Wypełnianie tabeli danymi w Column 7
        rowIndex = 0;
        for (String result : resultColumn7) {
            if (tableModel.getRowCount() <= rowIndex) {
                tableModel.addRow(new Object[tableModel.getColumnCount()]);
            }
            tableModel.setValueAt(result, rowIndex, 7); // Column 7

            // Dodajemy wartość do listy użytych lub nieużytych
            if (usedValuesColumn.contains(result)) {
                tableModel.setValueAt(result + "√", rowIndex, 7);  // Oznaczamy wartość "*"
            } else {
                tableModel.setValueAt(result + "*", rowIndex, 7);  // Oznaczamy wartość "*"
                if (!usedValuesColumn.contains(result) && !unusedValuesColumn.contains(result)) {
                    unusedValuesColumn.add(result);
                }
            }
            rowIndex++;
        }
        tabContent4.processTabContent24Data(dataFromTabContent2, unusedValuesColumn);


    }

    private int countOnes(String binaryValue) {
        // Liczenie liczby jedynek w kluczu (reprezentacji wiersza + kolumny)
        return (int) binaryValue.chars().filter(c -> c == '1').count();
    }

    private boolean hasSamePositionHyphens(String binaryValue1, String binaryValue2) {
        for (int i = 0; i < binaryValue1.length(); i++) {

            if ((binaryValue1.charAt(i) == binaryValue2.charAt(i) && (binaryValue1.charAt(i) == '-'))) {
                return true;
            }
        }
        return false;
    }

    private String performMergeWithSamePosition(String binaryValue1, String binaryValue2) {
        StringBuilder result = new StringBuilder(binaryValue1.length());
        int differencesCount = 0;
        for (int i = 0; i < binaryValue1.length(); i++) {
            if (binaryValue1.charAt(i) == binaryValue2.charAt(i)) {
                result.append(binaryValue1.charAt(i));
            } else {
                result.append("-");
                differencesCount++;
            }
        }
        // Sprawdź, czy jest dokładnie jedna różnica w pozostałych znakach
        if (differencesCount == 1) {
            // Jeśli jest więcej niż jedna różnica, przywróć oryginalne znaki
            usedValuesColumn.add(binaryValue1);
            usedValuesColumn.add(binaryValue2);
            return result.toString();
        }
        return "";
    }


}
