/*
 * Copyright 2009 Udai Gupta, Hemant Purohit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * XMLPanel.java
 *
 * Created on November 4, 2003, 8:23 AM
 */

package edu.lnmiit.wavd.ui.swing.editors;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.JLabel;

import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.InputSource;

import edu.lnmiit.wavd.util.DOMHandler;
import edu.lnmiit.wavd.util.swing.DOMTreeModel;
import edu.lnmiit.wavd.util.swing.MultiLineTreeCellRenderer;
import edu.lnmiit.wavd.util.swing.TreeUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLPanel.
 */
public class XMLPanel extends javax.swing.JPanel implements ByteArrayEditor {
    
    /** The _editable. */
    private boolean _editable = false;
    
    /** The _modified. */
    private boolean _modified = false;
    
    /** The _data. */
    private byte[] _data = new byte[0];
    
    /** The _search dialog. */
    private SearchDialog _searchDialog = null;
    
    /**
     * Instantiates a new xML panel.
     */
    public XMLPanel() {
        initComponents();
        setName("XML");
        xmlTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("No content")));
        xmlTree.setCellRenderer(new XMLTreeNodeRenderer());
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setEditable(boolean)
     */
    public void setEditable(boolean editable) {
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#setBytes(java.lang.String, byte[])
     */
    public void setBytes(String contentType, byte[] bytes) {
        _data = bytes;
        if (bytes != null) {
            InputStream is = new ByteArrayInputStream(bytes);
            Element rootElement = null;
            try {
                Document document = null;
                if (contentType.matches("text/xml.*")) {
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    builderFactory.setIgnoringElementContentWhitespace(true);
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    document = builder.parse(is);
                } else if (contentType.matches("text/html.*")) {
                    DOMHandler domHandler = new DOMHandler();
                    Parser parser = new Parser();
                    parser.setContentHandler(domHandler);
                    parser.parse(new InputSource(is));
                    document = domHandler.getDocument();
                }
                if (document != null) {
                    document.getDocumentElement().normalize();
                    rootElement = document.getDocumentElement();
                    TreeModel tm = new DOMTreeModel(rootElement);
                    xmlTree.setModel(tm);
                } else {
                    xmlTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Invalid content")));
                }
            } catch (Exception e) {
                xmlTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Error: " + e.getMessage())));
                e.printStackTrace();
            }
        } else {
            xmlTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("No elements")));
        }
        TreeUtil.expandAll(xmlTree, true);
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#isModified()
     */
    public boolean isModified() {
        return _editable && _modified;
    }
    
    /* (non-Javadoc)
     * @see edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor#getBytes()
     */
    public byte[] getBytes() {
        return _data;
    }
    
    /**
     * Gets the tree model.
     * 
     * @param bytes the bytes
     * 
     * @return the tree model
     */
    private TreeModel getTreeModel(byte[] bytes) {
        return null;
    }
    
    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        treeScrollPane = new javax.swing.JScrollPane();
        xmlTree = new javax.swing.JTree();

        setLayout(new java.awt.BorderLayout());

        treeScrollPane.setViewportView(xmlTree);

        add(treeScrollPane, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The tree scroll pane. */
    private javax.swing.JScrollPane treeScrollPane;
    
    /** The xml tree. */
    private javax.swing.JTree xmlTree;
    // End of variables declaration//GEN-END:variables
    
    /**
     * The Class XMLTreeNodeRenderer.
     */
    private class XMLTreeNodeRenderer extends MultiLineTreeCellRenderer {
        
        /**
         * Instantiates a new xML tree node renderer.
         */
        public XMLTreeNodeRenderer() {
        }
        
        /* (non-Javadoc)
         * @see edu.lnmiit.wavd.util.swing.MultiLineTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof Node) {
                Node node = (Node) value;
                String text = value.toString();
                int type = node.getNodeType();
//                System.out.println("Type: " + type + ": " + node.toString());
                switch (type) {
                    case Node.ATTRIBUTE_NODE: text = "ATTRIBUTE_NODE"; break;
                    case Node.CDATA_SECTION_NODE: text = "CDATA_SECTION_NODE"; break;
                    case Node.COMMENT_NODE: text = "COMMENT_NODE"; break;
                    case Node.ELEMENT_NODE:
                        text = "<" + node.getNodeName();
                        NamedNodeMap nnm = node.getAttributes();
                        if (nnm.getLength()>0) {
                            StringBuffer buff = new StringBuffer();
                            Node attr = nnm.item(0);
                            buff.append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append("\"");
                            for (int i=1; i<nnm.getLength();i++) {
                                attr = nnm.item(i);
                                buff.append(" ").append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append("\"");
                            }
                            text = text + " " + buff.toString();
                        }
                        text = text + ">";
                        break;
                    case Node.TEXT_NODE: text = node.getNodeValue(); break;
                    default: value = "Type: " + node.getNodeType() + node.toString();
                }
                value = text.trim();
            }
            return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
        
    }
    
    /**
     * The main method.
     * 
     * @param args the arguments
     * 
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        javax.swing.JFrame top = new javax.swing.JFrame("XML Editor");
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
        
        byte[] bytes;
        if (args.length > 0) {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            InputStream is = new java.io.FileInputStream(args[0]);
            byte[] buff = new byte[1024];
            int got;
            while ((got = is.read(buff)) > 0) {
                baos.write(buff,0,got);
            }
            bytes = baos.toByteArray();
        } else {
            bytes = ("<b>NOTE: For security reasons, using the administration webapp\n" +
            "        is restricted to users with role \"admin\". The manager webapp\n" + 
            "is restricted to users with role \"manager\".</b>").getBytes();
        }
        XMLPanel xp = new XMLPanel();
        top.getContentPane().add(xp);
        top.setBounds(100,100,600,400);
        try {
            xp.setBytes("text/xml",bytes);
            xp.setEditable(true);
            top.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
