package com.mycompany.quanlydoituongdacbiet.controller;

import com.mycompany.quanlydoituongdacbiet.entity.Expense;
import com.mycompany.quanlydoituongdacbiet.entity.Income;
import com.mycompany.quanlydoituongdacbiet.entity.Transaction;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller chịu trách nhiệm xử lý các hoạt động đọc/ghi
 * cho TẤT CẢ các loại giao dịch (Transaction) với file XML.
 * @author Admin
 */
public class TransactionController {

    private static final String FILE_PATH = "transactions.xml";


    public static void saveTransaction(Transaction transaction) {
        try {
            File file = new File(FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;
            Element root;

            if (file.exists()) {
                doc = dBuilder.parse(file);
                root = doc.getDocumentElement();
            } else {
                doc = dBuilder.newDocument();
                root = doc.createElement("transactions"); // Root element là transactions
                doc.appendChild(root);
            }

            Element transactionElement = doc.createElement("transaction");

            // Lấy ID tự động tăng
            int nextId = getNextId(doc);
            transaction.setId(nextId);
            transactionElement.setAttribute("id", String.valueOf(nextId));

            // QUAN TRỌNG: Lưu loại của đối tượng để biết cách đọc ra sau này
            String transactionType = "";
            if (transaction instanceof Income) {
                transactionType = "income";
            } else if (transaction instanceof Expense) {
                transactionType = "expense";
            }
            transactionElement.setAttribute("type", transactionType);

            // Tạo các element con
            createElement(doc, transactionElement, "date", transaction.getDate().toString()); // Chuyển LocalDate sang String
            createElement(doc, transactionElement, "category", transaction.getCategory());
            createElement(doc, transactionElement, "description", transaction.getDescription());
            createElement(doc, transactionElement, "amount", String.valueOf(transaction.getAmount()));

            root.appendChild(transactionElement);

            // Ghi nội dung đã cập nhật vào file XML
            transformAndSave(doc, FILE_PATH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void updateTransaction(Transaction updatedTransaction) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            NodeList list = doc.getElementsByTagName("transaction");
            for (int i = 0; i < list.getLength(); i++) {
                Element e = (Element) list.item(i);
                int id = Integer.parseInt(e.getAttribute("id"));
                if (id == updatedTransaction.getId()) {
                    // Cập nhật các thuộc tính
                    e.getElementsByTagName("date").item(0).setTextContent(updatedTransaction.getDate().toString());
                    e.getElementsByTagName("category").item(0).setTextContent(updatedTransaction.getCategory());
                    e.getElementsByTagName("description").item(0).setTextContent(updatedTransaction.getDescription());
                    e.getElementsByTagName("amount").item(0).setTextContent(String.valueOf(updatedTransaction.getAmount()));
                    break;
                }
            }

            transformAndSave(doc, FILE_PATH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void deleteTransaction(int transactionId) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            NodeList list = doc.getElementsByTagName("transaction");
            for (int i = 0; i < list.getLength(); i++) {
                Element e = (Element) list.item(i);
                int id = Integer.parseInt(e.getAttribute("id"));
                if (id == transactionId) {
                    e.getParentNode().removeChild(e);
                    break;
                }
            }

            transformAndSave(doc, FILE_PATH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return transactions; // Trả về danh sách rỗng nếu file không tồn tại
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("transaction");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;

                    // Đọc các thuộc tính chung
                    int id = Integer.parseInt(e.getAttribute("id"));
                    String type = e.getAttribute("type"); // Lấy ra loại giao dịch
                    LocalDate date = LocalDate.parse(e.getElementsByTagName("date").item(0).getTextContent()); // Chuyển String sang LocalDate
                    String category = e.getElementsByTagName("category").item(0).getTextContent();
                    String description = e.getElementsByTagName("description").item(0).getTextContent();
                    double amount = Double.parseDouble(e.getElementsByTagName("amount").item(0).getTextContent());

                    if ("income".equals(type)) {
                        transactions.add(new Income(id, date, category, description, amount));
                    } else if ("expense".equals(type)) {
                        transactions.add(new Expense(id, date, category, description, amount));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }


    private static int getNextId(Document doc) {
        NodeList list = doc.getElementsByTagName("transaction");
        int maxId = 0;
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);
            int id = Integer.parseInt(e.getAttribute("id"));
            if (id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }


    private static void createElement(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textContent));
        parent.appendChild(element);
    }


    private static void transformAndSave(Document doc, String filePath) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Định dạng file XML cho đẹp
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(filePath));
        transformer.transform(source, result);
    }
    
    
    public static List<Transaction> searchTransactions(String keyword, String type, double min, double max, LocalDate fromDate, LocalDate toDate) {
    List<Transaction> all = getAllTransactions();
    List<Transaction> filtered = new ArrayList<>();

    for (Transaction t : all) {
        boolean matchType = type.equals("Tất cả") || t.getType().equalsIgnoreCase(type);
        boolean matchKeyword = t.getDescription().toLowerCase().contains(keyword.toLowerCase());
        boolean matchAmount = t.getAmount() >= min && t.getAmount() <= max;

        if (matchType && matchKeyword && matchAmount) {
            filtered.add(t);
        }
    }
    return filtered;
    }

}