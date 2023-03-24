package Homework3.OrgStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class OrgStructureParserImpl implements OrgStructureParser {

    private TreeMap<Long, Employee> structure;

    public OrgStructureParserImpl() {
        structure = new TreeMap<>();
    }

    public TreeMap<Long, Employee> getStructure() {
        return structure;
    }

    @Override
    public Employee parseStructure(File csvFile) {
        try (Scanner scanner = new Scanner(csvFile)) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                parseEmployee(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
        for (Map.Entry<Long, Employee> item : structure.entrySet()) {
            if (item.getValue().getBossId() == null) {
                return item.getValue();
            }
        }
        return null;
    }

    private void parseEmployee(String s) {
        String[] stringEmployee;
        try {
            stringEmployee = s.split(";");
            if (stringEmployee.length != 4) {
                throw new InvalidStringException("Строка содержит ошибку");
            }
        } catch (InvalidStringException e) {
            System.out.println(e.getMessage());
            return;
        }
        Employee employee = new Employee();
        assignInfo(employee, stringEmployee);
    }

    private void assignInfo(Employee employee, String[] stringEmployee) {
        employee.setId(Long.valueOf(stringEmployee[0]));
        employee.setBossId(stringEmployee[1].length() == 0 ? null : Long.valueOf(stringEmployee[1]));
        employee.setName(stringEmployee[2]);
        employee.setPosition(stringEmployee[3]);
        structure.put(employee.getId(), employee);
        assignRelationship();
    }

    private void assignRelationship() {
        for (Long key : structure.keySet()) {
            Employee currentEmployee = structure.get(key);
            if (currentEmployee.getBossId() != null && structure.containsKey(currentEmployee.getBossId())) {
                Employee boss = structure.get(currentEmployee.getBossId());
                if (!checkifAlreadyAssigned(currentEmployee, boss)) {
                    boss.getSubordinate().add(currentEmployee);
                    currentEmployee.setBoss(boss);
                }

            }
        }
    }

    private boolean checkifAlreadyAssigned(Employee employee, Employee boss) {
        return boss.getSubordinate().contains(employee);
    }
}