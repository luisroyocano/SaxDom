/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domsax;

import java.util.Scanner;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.FileOutputStream;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/*Las siguienes son necesarias para guardar los cambios en el fichero XML que
hagamos desde el DOM*/
import java.io.FileOutputStream;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author luisr
 */
public class DomSax {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            File f = new File("coches.xml");
            Document documento = builder.parse(f);
            
            Scanner sc = new Scanner(System.in);
            System.out.println("indique si quiere utilizar DOM(1), o utilizar SAX(2)\n");
            int menu = sc.nextInt();
        
            if(menu == 1){
                System.out.println("Usted ha seleccionado Dom /n");
                Scanner scd = new Scanner(System.in);
                 System.out.println("indique si quiere leer el documento entero(1), Buscar por matricula(2), Buscar por marca(3), actualizar el kilometraje(4)\n");
                int opcionDom = scd.nextInt();

                if(opcionDom == 1){
                    leerDocumento(documento);
                }else if(opcionDom == 2){
                    Scanner scmatricula = new Scanner(System.in);
                    System.out.println("Indique la matrícula del vehículo:");
                    String matricula = scmatricula.nextLine();
                    
                    
                    buscaPorMatricula(documento, matricula);
                }else if(opcionDom == 3){
                    Scanner scmarca = new Scanner(System.in);
                    System.out.println("Indique la marca del vehículo:");
                    String marca = scmarca.nextLine();
                    
                    
                    buscaPorMarca(documento, marca);
                }else if(opcionDom == 4){
                    Scanner scbuscar = new Scanner(System.in);
                    System.out.println("Indique la matrícula del vehículo al que quiere actualizar el kilometraje:");
                    String matriculacoche = scbuscar.nextLine();
                    
                    Scanner sckm = new Scanner(System.in);
                    System.out.println("Indique el kilometraje actualizado:");
                    String nuevokm = sckm.nextLine();
                    
                    actualizarKm(documento, matriculacoche, nuevokm);
                }
            }else if(menu == 2){
                mostrarPorSAX();
            }
        }catch (Exception e) {
        e.printStackTrace();
        }
        
        
    }
    public static void leerDocumento(Node nodo){ 
        if (nodo!=null){
            System.out.println(nodo.getNodeName()+": "+ nodo.getNodeValue()); 
            NodeList hijos = nodo.getChildNodes();
            for (int i=0;i < hijos.getLength();i++){    
                Node nodoNieto = hijos.item(i);    
                leerDocumento(nodoNieto);
            }              
        }
        
    }
    
    public static void buscaPorMatricula(Document documento, String matricula){
        try {
            boolean encontrado= false;
            NodeList list = documento.getElementsByTagName("matricula");
            int i=0;
            while(i<list.getLength()) {
                Node n = list.item(i); 
                if (n.getFirstChild().getNodeValue().equals(matricula)){  
                    Node Padre= n.getParentNode();
                    leerDocumento(Padre);
                    encontrado = true;
                   break;
                }
                i++;
            }
            if (encontrado==false){
                System.out.println("Esta matricula no se encuentra");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void buscaPorMarca(Document documento, String marca){
        try {
            boolean encontrado= false;
            NodeList list = documento.getElementsByTagName("marca");
            int i=0;
            while(i<list.getLength()) {
                Node n = list.item(i); 
                if (n.getFirstChild().getNodeValue().equals(marca)){  
                    Node Padre= n.getParentNode();
                    leerDocumento(Padre);
                    encontrado = true;
                  
                }
                i++;
            }
            if (encontrado==false){
                System.out.println("Esta marca no se encuentra");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void actualizarKm(Document documento, String matriculacoche, String kmActualizado){
        try {
            boolean encontrado= false;
            NodeList list = documento.getElementsByTagName("matricula");
            int i=0;
            while(i<list.getLength()) {
                Node n = list.item(i); 
                if (n.getFirstChild().getNodeValue().equals(matriculacoche)){  
                    Node Padre= n.getParentNode();
                    NodeList hijos = Padre.getChildNodes();
                    for (int x=0;x < hijos.getLength();x++){    
                        Node nodoNieto = hijos.item(x);    
                        if(nodoNieto.getNodeName().equals("km")){
                            nodoNieto.getFirstChild().setNodeValue(kmActualizado);
                        }
                    }
                    leerDocumento(Padre);
                    encontrado = true;
                    guardarDOMcomoFILE(documento);
                   break;
                }
                i++;
            }
            if (encontrado==false){
                System.out.println("Esta matricula no se encuentra");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
     public static void guardarDOMcomoFILE(Document doc){
                try{
                    File archivo_xml = new File("coches.xml");     
                    OutputFormat format = new OutputFormat(doc);                  
                    format.setIndenting(true);                   
                    XMLSerializer serializer = new XMLSerializer(new
                   FileOutputStream(archivo_xml), format);
                    serializer.serialize(doc);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
    }
     
     public static void mostrarPorSAX(){
         try {
            //Creamos el manejador SAX
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
            //cuando se llega al principio de un elemento imprimimos su nombre por

                public void startElement(String uri, String localName,
                String qName, Attributes attributes)
                throws SAXException {
                    System.out.println("Start Element :" + qName);
                }
                //cuando se llega al final del elemento lo indicamos por pantalla
                public void endElement(String uri, String localName,
                String qName)
                throws SAXException {
                    System.out.println("End Element :" + qName);
                }
                //cuando leemos el contenido de un elemento lo mostramos por pantalla
                public void characters(char ch[], int start, int length)
                throws SAXException {
                    System.out.println(new String(ch, start, length));
                }
            };
            //Encapsulamos el fichero xml a leer indicando que tiene formato utf-8
            File file = new File("coches.xml");
            InputStream inputStream= new FileInputStream(file);
            Reader reader = new InputStreamReader(inputStream,"UTF-8");
            InputSource is = new InputSource(reader);

            is.setEncoding("UTF-8");
            saxParser.parse(is, handler);
            
        
            } catch (Exception e) {
            e.printStackTrace();
            }
    }
}
