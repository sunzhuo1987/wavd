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
 * ContentPanel.java
 *
 * Created on November 4, 2003, 8:06 AM
 */

package edu.lnmiit.wavd.ui.swing;

import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.lnmiit.wavd.ui.swing.editors.ByteArrayEditor;
import edu.lnmiit.wavd.ui.swing.editors.EditorFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class ContentPanel.
 */
public class ContentPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5716702920663186833L;

    /** The _content type. */
    private String _contentType = null;

    /** The _editable. */
    private boolean _editable = false;

    /** The _modified. */
    private boolean _modified = false;

    /** The _data. */
    private byte[] _data = null;

    /** The _editors. */
    private ByteArrayEditor[] _editors = null;

    /** The _selected. */
    private int _selected = -1;

    /** The _up to date. */
    private boolean[] _upToDate = new boolean[] { false };

    /** The _logger. */
    // private Logger _logger = Logger.getLogger(getClass().getName());
    // This list is sorted in increasing order of preference
    /** The _preferred. */
    private static List _preferred = new ArrayList();

    /** The _creating panels. */
    private boolean _creatingPanels = false;

    /**
     * Instantiates a new content panel.
     */
    public ContentPanel() {
        initComponents();
        viewTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (_creatingPanels)
                    return;
                // update our view of the data, after (possible) modifications
                // in the previously selected editor
                updateData(_selected);
                _selected = viewTabbedPane.getSelectedIndex();
                if (_selected > -1) {
                    updatePanel(_selected);
                    String name = _editors[_selected].getName();
                    _preferred.remove(name);
                    _preferred.add(name);
                }
            }
        });
    }

    /**
     * Sets the editable.
     * 
     * @param editable
     *            the new editable
     */
    public void setEditable(boolean editable) {
        _editable = editable;
        if (_editors != null) {
            for (int i = 0; i < _editors.length; i++) {
                _editors[i].setEditable(editable);
            }
        }
    }

    /**
     * Sets the content type.
     * 
     * @param contentType
     *            the new content type
     */
    public void setContentType(String contentType) {
        if (_contentType == null || !_contentType.equals(contentType)) {
            _contentType = contentType;
            createPanels(_contentType);
        }
    }

    /**
     * Creates the panels.
     * 
     * @param contentType
     *            the content type
     */
    private void createPanels(final String contentType) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        createPanels(contentType);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            _creatingPanels = true;
            viewTabbedPane.removeAll();
            _editors = EditorFactory.getEditors(contentType);
            for (int i = 0; i < _editors.length; i++) {
                _editors[i].setEditable(_editable);
                viewTabbedPane.add((Component) _editors[i]);
            }
            int preferred = -1;
            for (int i = 0; i < _preferred.size(); i++) {
                for (int e = 0; e < _editors.length; e++) {
                    if (_editors[e].getName().equals(_preferred.get(i))) {
                        preferred = e;
                        break;
                    }
                }
            }
            invalidateEditors();
            revalidate();
            if (preferred > -1) {
                viewTabbedPane.setSelectedIndex(preferred);
            }
            _creatingPanels = false;
        }
    }

    /**
     * Invalidate editors.
     */
    private void invalidateEditors() {
        _upToDate = new boolean[_editors.length];
        for (int i = 0; i < _upToDate.length; i++)
            _upToDate[i] = false;
    }

    /**
     * Sets the content.
     * 
     * @param content
     *            the new content
     */
    public void setContent(byte[] content) {
        _modified = false;
        if (content == null) {
            _data = null;
        } else {
            _data = new byte[content.length];
            System.arraycopy(content, 0, _data, 0, content.length);
        }

        if (_editors == null || _editors.length == 0) {
            return;
        }
        invalidateEditors();

        _selected = viewTabbedPane.getSelectedIndex();
        if (_selected < 0) {
            _selected = 0;
            viewTabbedPane.setSelectedIndex(_selected);
        }
        updatePanel(_selected);
    }

    /**
     * Checks if is modified.
     * 
     * @return true, if is modified
     */
    public boolean isModified() {
        if (!_editable)
            return false;
        int selected = viewTabbedPane.getSelectedIndex();
        if (selected < 0)
            return false;
        return _modified || _editors[selected].isModified();
    }

    /**
     * Gets the content.
     * 
     * @return the content
     */
    public byte[] getContent() {
        if (isModified()) {
            int selected = viewTabbedPane.getSelectedIndex();
            _data = _editors[selected].getBytes();
            _modified = false;
        }
        return _data;
    }

    /**
     * Update panel.
     * 
     * @param panel
     *            the panel
     */
    private void updatePanel(int panel) {
        if (panel < 0 || _upToDate.length == 0) {
            return;
        } else if (panel >= _upToDate.length) {
            panel = 0;
        }
        if (!_upToDate[panel]) {
            _editors[panel].setBytes(_contentType, _data);
            _upToDate[panel] = true;
        }
    }

    /**
     * Update data.
     * 
     * @param panel
     *            the panel
     */
    private void updateData(int panel) {
        if (_editable && panel >= 0) {
            ByteArrayEditor ed = (ByteArrayEditor) viewTabbedPane.getComponentAt(panel);
            if (ed.isModified()) {
                _modified = true;
                _data = ed.getBytes();
                invalidateEditors();
                _upToDate[panel] = true;
            }
        }
    }

    /**
     * Inits the components.
     */
    // <editor-fold defaultstate="collapsed"
    // desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        viewTabbedPane = new javax.swing.JTabbedPane();

        setLayout(new java.awt.GridBagLayout());

        viewTabbedPane.setPreferredSize(new java.awt.Dimension(300, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(viewTabbedPane, gridBagConstraints);

    }

    // </editor-fold>//GEN-END:initComponents

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        edu.lnmiit.wavd.model.Response response = new edu.lnmiit.wavd.model.Response();
        try {
            new ByteArrayOutputStream();
            /*
             * FileInputStream fis = new
             * FileInputStream("/home/rdawes/exodus/HowTo.html"); byte[] buff =
             * new byte[1024]; int got = 0; while ((got = fis.read(buff)) > 0) {
             * baos.write(buff, 0, got); } content = baos.toByteArray();
             */
            String filename = "/home/rogan/workspace/webscarab/test/data/index-resp";
            if (args.length == 1) {
                filename = args[0];
            }
            java.io.FileInputStream fis = new java.io.FileInputStream(filename);
            response.read(fis);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        javax.swing.JFrame top = new javax.swing.JFrame("Content Pane");
        top.getContentPane().setLayout(new java.awt.BorderLayout());
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        javax.swing.JButton button = new javax.swing.JButton("GET");
        final ContentPanel cp = new ContentPanel();
        top.getContentPane().add(cp);
        top.getContentPane().add(button, java.awt.BorderLayout.SOUTH);
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println(new String(cp.getContent()));
            }
        });
        top.pack();
        // top.setSize(cp.getPreferredSize());
        // top.setBounds(100,100,600,400);
        top.setVisible(true);
        try {
            cp.setContentType(response.getHeader("Content-Type"));
            cp.setEditable(false);
            cp.setContent(response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** The view tabbed pane. */
    private javax.swing.JTabbedPane viewTabbedPane;
    // End of variables declaration//GEN-END:variables

}
