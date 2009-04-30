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
package edu.lnmiit.wavd.util.swing;

/**
 * @author rdawes
 * 
 */

import javax.swing.text.*;

// TODO: Auto-generated Javadoc
/**
 * The Class NoWrapEditorKit.
 */
public class NoWrapEditorKit extends StyledEditorKit {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2867130121374027370L;

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledEditorKit#getViewFactory()
	 */
	public ViewFactory getViewFactory() {
		return new StyledViewFactory();
	}

	/**
	 * A factory for creating StyledView objects.
	 */
	static class StyledViewFactory implements ViewFactory {
		
		/* (non-Javadoc)
		 * @see javax.swing.text.ViewFactory#create(javax.swing.text.Element)
		 */
		public View create(Element elem) {
			String kind = elem.getName();

			if (kind != null) {
				if (kind.equals(AbstractDocument.ContentElementName)) {
					return new MyLabelView(elem);
				} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
					return new ParagraphView(elem);
				} else if (kind.equals(AbstractDocument.SectionElementName)) {
					return new NoWrapBoxView(elem, View.Y_AXIS);
				} else if (kind.equals(StyleConstants.ComponentElementName)) {
					return new ComponentView(elem);
				} else if (kind.equals(StyleConstants.IconElementName)) {
					return new IconView(elem);
				}
			}

			return new LabelView(elem);
		}
	}

	/**
	 * The Class NoWrapBoxView.
	 */
	static class NoWrapBoxView extends BoxView {
		
		/**
		 * Instantiates a new no wrap box view.
		 * 
		 * @param elem the elem
		 * @param axis the axis
		 */
		public NoWrapBoxView(Element elem, int axis) {
			super(elem, axis);
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.BoxView#layout(int, int)
		 */
		public void layout(int width, int height) {
			super.layout(32768, height);
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.BoxView#getMinimumSpan(int)
		 */
		public float getMinimumSpan(int axis) {
			return super.getPreferredSpan(axis);
		}
	}

	/**
	 * The Class MyLabelView.
	 */
	static class MyLabelView extends LabelView {
		
		/**
		 * Instantiates a new my label view.
		 * 
		 * @param elem the elem
		 */
		public MyLabelView(Element elem) {
			super(elem);
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.GlyphView#getPreferredSpan(int)
		 */
		public float getPreferredSpan(int axis) {
			float span = 0;
			if (axis == View.X_AXIS) {
				int p0 = getStartOffset();
				int p1 = getEndOffset();
				checkPainter();
				TabExpander ex = getTabExpander();
				if (ex == null) {
					// paragraph implements TabExpander
					ex = (TabExpander) this.getParent().getParent();
				}
				span = getGlyphPainter().getSpan(this, p0, p1, ex, 0);
				return Math.max(span, 1);
			} else {
				span = super.getPreferredSpan(axis);
			}
			return span;
		}
	}

}
