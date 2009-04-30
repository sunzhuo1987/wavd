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
package no.geosoft.cc.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JWindow;
import javax.swing.Timer;

// TODO: Auto-generated Javadoc
/**
 * The Class SplashScreen.
 */
public class SplashScreen extends JWindow {

    /**
     * 
     */
    private static final long serialVersionUID = 6755880409556259144L;

    /** The image_. */
    private Image image_;

    /** The height_. */
    private int x_, y_, width_, height_;

    /**
     * Instantiates a new splash screen.
     * 
     * @param imageFileName
     *            the image file name
     */
    public SplashScreen(String imageFileName) {
        super(new Frame());

        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();

            URL imageUrl = getClass().getResource(imageFileName);
            image_ = toolkit.getImage(imageUrl);

            MediaTracker mediaTracker = new MediaTracker(this);
            mediaTracker.addImage(image_, 0);
            mediaTracker.waitForID(0);

            width_ = image_.getWidth(this);
            height_ = image_.getHeight(this);

            Dimension screenSize = toolkit.getScreenSize();

            x_ = (screenSize.width - width_) / 2;
            y_ = (screenSize.height - height_) / 2;
        } catch (Exception exception) {
            exception.printStackTrace();
            image_ = null;
        }
    }

    /**
     * Open.
     * 
     * @param nMilliseconds
     *            the n milliseconds
     */
    public void open(int nMilliseconds) {
        if (image_ == null)
            return;

        Timer timer = new Timer(Integer.MAX_VALUE, new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ((Timer) event.getSource()).stop();
                close();
            };
        });

        timer.setInitialDelay(nMilliseconds);
        timer.start();

        setBounds(x_, y_, width_, height_);
        setVisible(true);
    }

    /**
     * Close.
     */
    public void close() {
        setVisible(false);
        dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Container#paint(java.awt.Graphics)
     */
    public void paint(Graphics graphics) {
        if (image_ == null)
            return;
        graphics.drawImage(image_, 0, 0, width_, height_, this);
    }
}
