package com.mycompany.quanlydoituongdacbiet.controller;

import com.mycompany.quanlydoituongdacbiet.entity.Budget;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BudgetController {
    private static final String FILE_PATH = "budgets.xml";

    public static List<Budget> getAllBudgets() {
        List<Budget> budgets = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return budgets;
        }
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("budget");
            for (int i = 0; i < list.getLength(); i++) {
                Element e = (Element) list.item(i);
                String category = e.getElementsByTagName("category").item(0).getTextContent();
                double amount = Double.parseDouble(e.getElementsByTagName("amount").item(0).getTextContent());
                int month = Integer.parseInt(e.getElementsByTagName("month").item(0).getTextContent());
                int year = Integer.parseInt(e.getElementsByTagName("year").item(0).getTextContent());
                budgets.add(new Budget(category, amount, month, year));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return budgets;
    }

    public static void saveBudgets(List<Budget> budgets) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element root = doc.createElement("budgets");
            doc.appendChild(root);

            for (Budget b : budgets) {
                Element budgetElement = doc.createElement("budget");
                
                Element category = doc.createElement("category");
                category.appendChild(doc.createTextNode(b.getCategory()));
                budgetElement.appendChild(category);

                Element amount = doc.createElement("amount");
                amount.appendChild(doc.createTextNode(String.valueOf(b.getAmount())));
                budgetElement.appendChild(amount);

                Element month = doc.createElement("month");
                month.appendChild(doc.createTextNode(String.valueOf(b.getMonth())));
                budgetElement.appendChild(month);

                Element year = doc.createElement("year");
                year.appendChild(doc.createTextNode(String.valueOf(b.getYear())));
                budgetElement.appendChild(year);
                
                root.appendChild(budgetElement);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(FILE_PATH));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveOrUpdateBudget(Budget budgetToSave) {
        List<Budget> budgets = getAllBudgets();
        Optional<Budget> existingBudget = budgets.stream()
                .filter(b -> b.getCategory().equals(budgetToSave.getCategory()) &&
                             b.getMonth() == budgetToSave.getMonth() &&
                             b.getYear() == budgetToSave.getYear())
                .findFirst();

        if (existingBudget.isPresent()) {
            existingBudget.get().setAmount(budgetToSave.getAmount());
        } else {
            budgets.add(budgetToSave);
        }
        saveBudgets(budgets);
    }
    
    public static void deleteBudget(Budget budgetToDelete) {
        List<Budget> budgets = getAllBudgets();
        budgets.removeIf(b -> b.getCategory().equals(budgetToDelete.getCategory()) &&
                               b.getMonth() == budgetToDelete.getMonth() &&
                               b.getYear() == budgetToDelete.getYear());
        saveBudgets(budgets);
    }
}