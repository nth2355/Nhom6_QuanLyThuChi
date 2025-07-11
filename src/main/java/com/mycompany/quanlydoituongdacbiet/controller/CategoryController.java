package com.mycompany.quanlydoituongdacbiet.controller;

import com.mycompany.quanlydoituongdacbiet.entity.Category;
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

public class CategoryController {
    private static final String FILE_PATH = "categories.xml";

    // Đọc tất cả danh mục từ file
    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            // Nếu file chưa tồn tại, tạo danh sách mặc định và lưu lại
            return createDefaultCategories();
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("category");

            for (int i = 0; i < list.getLength(); i++) {
                Element e = (Element) list.item(i);
                String name = e.getTextContent();
                String type = e.getAttribute("type");
                categories.add(new Category(name, type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Lưu một danh sách danh mục vào file
    public static void saveCategories(List<Category> categories) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element root = doc.createElement("categories");
            doc.appendChild(root);

            for (Category cat : categories) {
                Element categoryElement = doc.createElement("category");
                categoryElement.setAttribute("type", cat.getType());
                categoryElement.appendChild(doc.createTextNode(cat.getName()));
                root.appendChild(categoryElement);
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
    
    // Thêm một danh mục mới
    public static void addCategory(Category newCategory) {
        List<Category> categories = getAllCategories(); // Lấy danh sách danh mục ra trước
        
        // Kiểm tra để không thêm trùng
        for(Category c : categories) {
            if(c.getName().equalsIgnoreCase(newCategory.getName()) && c.getType().equals(newCategory.getType())) {
                return; // Đã tồn tại, không làm gì cả
            }
        }
        
        // Thêm mục mới vào danh sách và lưu lại
        categories.add(newCategory);
        saveCategories(categories);
    }
    
    // Tạo danh sách danh mục mặc định cho lần chạy đầu tiên
    private static List<Category> createDefaultCategories() {
        List<Category> defaults = new ArrayList<>();
        // Danh mục thu
        defaults.add(new Category("Lương", "income"));
        defaults.add(new Category("Thưởng", "income"));
        defaults.add(new Category("Bán hàng", "income"));
        // Danh mục chi
        defaults.add(new Category("Ăn uống", "expense"));
        defaults.add(new Category("Di chuyển", "expense"));
        defaults.add(new Category("Hóa đơn", "expense"));
        
        saveCategories(defaults);
        return defaults;
    }
}