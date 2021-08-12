package com.yyzq.springbootcondition;

import com.yyzq.springbootcondition.domain.Book;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class XmlAnalyze {

    @Test
    void contextLoads() {
    }

    @Test
    void xmlDomAnalyze(){
            //创建一个DocumentBuilderFactory的对象
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //创建一个DocumentBuilder的对象
            try {
                //创建DocumentBuilder对象
                DocumentBuilder db = dbf.newDocumentBuilder();
                //通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
                Document document = db.parse("./src/main/resources/static/book.xml");
                //获取所有book节点的集合
                NodeList bookList = document.getElementsByTagName("book");
                //通过nodelist的getLength()方法可以获取bookList的长度
                System.out.println("一共有" + bookList.getLength() + "本书");
                //遍历每一个book节点
                for (int i = 0; i < bookList.getLength(); i++) {
                    System.out.println("=================下面开始遍历第" + (i + 1) + "本书的内容=================");
                    //通过 item(i)方法 获取一个book节点，nodelist的索引值从0开始
                    Node book = bookList.item(i);
                    //获取book节点的所有属性集合
                    NamedNodeMap attrs = book.getAttributes();
                    System.out.println("第 " + (i + 1) + "本书共有" + attrs.getLength() + "个属性");
                    //遍历book的属性
                    for (int j = 0; j < attrs.getLength(); j++) {
                        //通过item(index)方法获取book节点的某一个属性
                        Node attr = attrs.item(j);
                        //获取属性名
                        System.out.print("属性名：" + attr.getNodeName());
                        //获取属性值
                        System.out.println("--属性值" + attr.getNodeValue());
                    }
                    //解析book节点的子节点
                    NodeList childNodes = book.getChildNodes();
                    //遍历childNodes获取每个节点的节点名和节点值
                    System.out.println("第" + (i+1) + "本书共有" +
                            childNodes.getLength() + "个子节点");
                    for (int k = 0; k < childNodes.getLength(); k++) {
                        //区分出text类型的node以及element类型的node
                        if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                            //获取了element类型节点的节点名
                            System.out.print("第" + (k + 1) + "个节点的节点名："
                                    + childNodes.item(k).getNodeName());
                            //获取了element类型节点的节点值
                            System.out.println("--节点值是：" + childNodes.item(k).getFirstChild().getNodeValue());
                            //System.out.println("--节点值是：" + childNodes.item(k).getTextContent());
                        }
                    }
                    System.out.println("======================结束遍历第" + (i + 1) + "本书的内容=================");
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private static ArrayList<Book> booksList = new ArrayList<Book>();
        @Test
        void xmlJdomAnalyze() {
            // 进行对books.xml文件的JDOM解析
            // 准备工作
            // 1.创建一个SAXBuilder的对象
            SAXBuilder saxBuilder = new SAXBuilder();
            InputStream in;
            try {
                // 2.创建一个输入流，将xml文件加载到输入流中
                in = new FileInputStream("./src/main/resources/static/book.xml");
                InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                // 3.通过saxBuilder的build方法，将输入流加载到saxBuilder中
                org.jdom2.Document document = saxBuilder.build(isr);
                // 4.通过document对象获取xml文件的根节点
                Element rootElement = document.getRootElement();
                // 5.获取根节点下的子节点的List集合
                List<Element> bookList = rootElement.getChildren();
                // 继续进行解析
                for (Element book : bookList) {
                    Book bookEntity = new Book();
                    System.out.println("======开始解析第" + (bookList.indexOf(book) + 1)
                            + "书======");
                    // 解析book的属性集合
                    List<Attribute> attrList = book.getAttributes();
                    // //知道节点下属性名称时，获取节点值
                    // book.getAttributeValue("id");
                    // 遍历attrList(针对不清楚book节点下属性的名字及数量)
                    for (Attribute attr : attrList) {
                        // 获取属性名
                        String attrName = attr.getName();
                        // 获取属性值
                        String attrValue = attr.getValue();
                        System.out.println("属性名：" + attrName + "----属性值："
                                + attrValue);
                        if (attrName.equals("id")) {
                            bookEntity.setId(attrValue);
                        }
                    }
                    // 对book节点的子节点的节点名以及节点值的遍历
                    List<Element> bookChilds = book.getChildren();
                    for (Element child : bookChilds) {
                        System.out.println("节点名：" + child.getName() + "----节点值："
                                + child.getValue());
                        if (child.getName().equals("name")) {
                            bookEntity.setName(child.getValue());
                        } else if (child.getName().equals("author")) {
                            bookEntity.setAuthor(child.getValue());
                        } else if (child.getName().equals("year")) {
                            bookEntity.setYear(child.getValue());
                        } else if (child.getName().equals("price")) {
                            bookEntity.setPrice(child.getValue());
                        } else if (child.getName().equals("language")) {
                            bookEntity.setLanguage(child.getValue());
                        }
                    }
                    System.out.println("======结束解析第" + (bookList.indexOf(book) + 1)
                            + "书======");
                    booksList.add(bookEntity);
                    bookEntity = null;
                    System.out.println(booksList.size());
                    System.out.println(booksList.get(0).getId());
                    System.out.println(booksList.get(0).getName());

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 文件复制
     */
        @Test
        public  void copy(){
            long l = System.currentTimeMillis();
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(new File("D:\\PlantsVsZombiesSetup.zip"));
                fos = new FileOutputStream(new File("D:\\PlantsVsZombiesSetup1.zip"));
                int len = 0;
                byte[] bytes = new byte[1024];
                while((len = fis.read(bytes)) != -1){
                    fos.write(bytes, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            long l1 = System.currentTimeMillis();
            System.out.println("节点流消耗时间：" + (l1 - l) + " 毫秒");
    }
}

