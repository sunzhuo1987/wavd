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
package edu.lnmiit.wavd.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Diff.
 */
public class Diff {

	/** The Constant DELETE. */
	private static final CharSequence DELETE = new String();

	/**
	 * Instantiates a new diff.
	 */
	private Diff() {
	}

	/**
	 * Gets the edits.
	 * 
	 * @param src the src
	 * @param dst the dst
	 * 
	 * @return the edits
	 */
	public static List getEdits(CharSequence src, CharSequence dst) {
		return getEdits(0, src.length(), src, 0, dst.length(), dst);
	}

	/*
	 * Calculates the edits that will transform the src CharSequence to the
	 * destination.
	 */
	/**
	 * Gets the edits.
	 * 
	 * @param srcStart the src start
	 * @param srcEnd the src end
	 * @param src the src
	 * @param dstStart the dst start
	 * @param dstEnd the dst end
	 * @param dst the dst
	 * 
	 * @return the edits
	 */
	private static List getEdits(int srcStart, int srcEnd,
			CharSequence src, int dstStart, int dstEnd, CharSequence dst) {
		// System.out.println("Called with src (" + srcStart + "," + srcEnd +
		// ")'" + src.subSequence(srcStart, srcEnd) + "'");
		// System.out.println("Called with dst (" + dstStart + "," + dstEnd +
		// ")'" + dst.subSequence(dstStart, dstEnd) + "'");
		List edits = new LinkedList();

		// check for common prefix and suffix
		while (srcStart < srcEnd && dstStart < dstEnd
				&& src.charAt(srcStart) == dst.charAt(dstStart)) {
			srcStart++;
			dstStart++;
		}
		while (srcStart < srcEnd && dstStart < dstEnd
				&& src.charAt(srcEnd - 1) == dst.charAt(dstEnd - 1)) {
			srcEnd--;
			dstEnd--;
		}

		// check if the two texts are identical - unlikely
		if (srcStart == srcEnd && dstStart == dstEnd)
			return edits;

		// if the leader and trailer comprised the whole text of either src or
		// dst
		// then what remains is the difference
		if (srcStart == srcEnd) {
			edits.add(new Edit(srcStart, DELETE, dstStart, dst.subSequence(
					dstStart, dstEnd)));
			return edits;
		}
		if (dstStart == dstEnd) {
			edits.add(new Edit(srcStart, src.subSequence(srcStart, srcEnd),
					dstStart, DELETE));
			return edits;
		}

		// now divide the text into left and right parts, separated by the
		// longest common substring
		// and process the two parts recursively
		LCS lcs = lcs(srcStart, srcEnd, src, dstStart, dstEnd, dst);
		// System.out.println("LCS = " + lcs + ", '" +
		// src.subSequence(lcs.getSrcLocation(), lcs.getSrcLocation() +
		// lcs.getLength()) + "'");
		if (lcs.getLength() > 0) {
			edits.addAll(getEdits(srcStart, lcs.getSrcLocation(), src,
					dstStart, lcs.getDstLocation(), dst));
			srcStart = lcs.getSrcLocation() + lcs.getLength();
			dstStart = lcs.getDstLocation() + lcs.getLength();
			edits
					.addAll(getEdits(srcStart, srcEnd, src, dstStart, dstEnd,
							dst));
		} else {
			edits.add(new Edit(srcStart, src.subSequence(srcStart, srcEnd),
					dstStart, dst.subSequence(dstStart, dstEnd)));
		}
		return edits;
	}

	/**
	 * Split.
	 * 
	 * @param orig the orig
	 * @param boundary the boundary
	 * 
	 * @return the char sequence[]
	 */
	public static CharSequence[] split(CharSequence orig, char boundary) {
		List list = new LinkedList();
		int previous = 0, index = 0;
		while (index < orig.length()) {
			if (orig.charAt(index) == boundary) {
				list.add(orig.subSequence(previous, index+1));
				previous = index + 1;
			}
			index++;
		}
		if (index > previous)
			list.add(orig.subSequence(previous, index));
		return (CharSequence[]) list.toArray(new CharSequence[list.size()]);
	}
	
	/**
	 * Gets the edits.
	 * 
	 * @param src the src
	 * @param dst the dst
	 * @param boundary the boundary
	 * 
	 * @return the edits
	 */
	public static List getEdits(CharSequence src, CharSequence dst, char boundary) {
		CharSequence[] srcArray = split(src, boundary);
		CharSequence[] dstArray = split(dst, boundary);
		List edits = getEdits(srcArray, dstArray);
		return convertArrayToOriginal(srcArray, dstArray, edits);
	}
	
	/**
	 * Convert array to original.
	 * 
	 * @param src the src
	 * @param dst the dst
	 * @param arrayEdits the array edits
	 * 
	 * @return the list
	 */
	public static List convertArrayToOriginal(CharSequence[] src, CharSequence[] dst, List arrayEdits) {
		List edits = new LinkedList();
		Iterator it = arrayEdits.iterator();
		int srcLast = 0, dstLast = 0;
		int srcOffset = 0, dstOffset = 0;
		while (it.hasNext()) {
			ArrayEdit edit = (ArrayEdit) it.next();
			// catch up things in between edits
			while(srcLast<edit.getSrcStart())
				srcOffset += src[srcLast++].length();
			while (dstLast<edit.getDstStart())
				dstOffset += dst[dstLast++].length();
			int srcStart = srcOffset;
			int dstStart = dstOffset;
			StringBuffer srcEdit = new StringBuffer();
			StringBuffer dstEdit = new StringBuffer();
			while (srcLast<edit.getSrcEnd())
				srcEdit.append(src[srcLast++]);
			while (dstLast<edit.getDstEnd())
				dstEdit.append(dst[dstLast++]);
			srcOffset += srcEdit.length();
			dstOffset += dstEdit.length();
			edits.add(new Edit(srcStart, srcEdit.toString(), dstStart, dstEdit.toString()));
		}
		return edits;
	}
	
	/**
	 * Gets the edits.
	 * 
	 * @param src the src
	 * @param dst the dst
	 * 
	 * @return the edits
	 */
	public static List getEdits(CharSequence src[], CharSequence dst[]) {
		return getEdits(0, src.length, src, 0, dst.length, dst);
	}

	/*
	 * Calculates the edits that will transform the src CharSequence to the
	 * destination.
	 */
	/*
	 * Calculates the edits that will transform the src CharSequence to the
	 * destination.
	 */
	/**
	 * Gets the edits.
	 * 
	 * @param srcStart the src start
	 * @param srcEnd the src end
	 * @param src the src
	 * @param dstStart the dst start
	 * @param dstEnd the dst end
	 * @param dst the dst
	 * 
	 * @return the edits
	 */
	private static List getEdits(int srcStart, int srcEnd,
			CharSequence[] src, int dstStart, int dstEnd, CharSequence[] dst) {
		List edits = new LinkedList();

		// check for common prefix and suffix
		while (srcStart < srcEnd && dstStart < dstEnd
				&& src[srcStart].equals(dst[dstStart])) {
			srcStart++;
			dstStart++;
		}
		while (srcStart < srcEnd && dstStart < dstEnd
				&& src[srcEnd - 1].equals(dst[dstEnd - 1])) {
			srcEnd--;
			dstEnd--;
		}

		// check if the two texts are identical - unlikely
		if (srcStart == srcEnd && dstStart == dstEnd)
			return edits;

		// if the leader and trailer comprised the whole text of either src or
		// dst
		// then what remains is the difference
		if (srcStart == srcEnd || dstStart == dstEnd) {
			edits.add(new ArrayEdit(srcStart, srcEnd, dstStart, dstEnd));
			return edits;
		}

		// now divide the text into left and right parts, separated by the
		// longest common substring
		// and process the two parts recursively
		LCS lcs = lcs(srcStart, srcEnd, src, dstStart, dstEnd, dst);
		// System.out.println("LCS = " + lcs + ", '" +
		// src.subSequence(lcs.getSrcLocation(), lcs.getSrcLocation() +
		// lcs.getLength()) + "'");
		if (lcs.getLength() > 0) {
			edits.addAll(getEdits(srcStart, lcs.getSrcLocation(), src,
					dstStart, lcs.getDstLocation(), dst));
			srcStart = lcs.getSrcLocation() + lcs.getLength();
			dstStart = lcs.getDstLocation() + lcs.getLength();
			edits
					.addAll(getEdits(srcStart, srcEnd, src, dstStart, dstEnd,
							dst));
		} else {
			edits.add(new ArrayEdit(srcStart, srcEnd, dstStart, dstEnd));
		}
		return edits;
	}

	/**
	 * Refine.
	 * 
	 * @param src the src
	 * @param dst the dst
	 * @param edits the edits
	 * 
	 * @return the list
	 */
	public static List refine(CharSequence src, CharSequence dst, List edits) {
		List refined = new LinkedList();
		Iterator it = edits.iterator();
		while(it.hasNext()) {
			Edit edit = (Edit) it.next();
			int srcStart = edit.getSrcLocation();
			int srcEnd = srcStart + edit.getSrc().length();
			int dstStart = edit.getDstLocation();
			int dstEnd = dstStart + edit.getDst().length();
			refined.addAll(getEdits(srcStart, srcEnd, src, dstStart, dstEnd, dst));
		}
		return refined;
	}
	
	/**
	 * Gets the distance.
	 * 
	 * @param edits the edits
	 * 
	 * @return the distance
	 */
	public static int getDistance(List edits) {
		int distance = 0;
		for (int i = 0; i < edits.size(); i++) {
			Edit edit = (Edit) edits.get(i);
			distance += edit.getSrc().length() + edit.getDst().length();
		}
		return distance;
	}

	/*
	 * This method is useful for ensuring that the edits are properly calculated
	 */
	/**
	 * Apply.
	 * 
	 * @param src the src
	 * @param edits the edits
	 * 
	 * @return the string
	 */
	public static String apply(CharSequence src, List edits) {
		Iterator it = edits.iterator();
		StringBuffer buff = new StringBuffer();
		int last = 0;
		while (it.hasNext()) {
			Edit edit = (Edit) it.next();
			// System.out.println(edit);
			if (edit.getSrcLocation() > last) {
				// catch up things in between edits
				buff.append(src.subSequence(last, edit.getSrcLocation()));
			}
			if (edit.getDst().length() > 0) {
				buff.append(edit.getDst());
			}
			last = edit.getSrcLocation() + edit.getSrc().length();
		}
		if (last < src.length()) {
			buff.append(src.subSequence(last, src.length()));
		}
		return buff.toString();
	}

	/*
	 * This method is useful for ensuring that the edits are properly calculated
	 */
	/**
	 * Revert.
	 * 
	 * @param dst the dst
	 * @param edits the edits
	 * 
	 * @return the string
	 */
	public static String revert(CharSequence dst, List edits) {
		Iterator it = edits.iterator();
		StringBuffer buff = new StringBuffer();
		int last = 0;
		while (it.hasNext()) {
			Edit edit = (Edit) it.next();
			if (edit.getDstLocation() > last) {
				// catch up things in between edits
				buff.append(dst.subSequence(last, edit.getDstLocation()));
			}
			if (edit.getSrc().length() > 0) {
				buff.append(edit.getSrc());
			}
			last = edit.getDstLocation() + edit.getDst().length();
		}
		if (last < dst.length()) {
			buff.append(dst.subSequence(last, dst.length()));
		}
		return buff.toString();
	}

	/**
	 * Lcs.
	 * 
	 * @param srcStart the src start
	 * @param srcEnd the src end
	 * @param src the src
	 * @param dstStart the dst start
	 * @param dstEnd the dst end
	 * @param dst the dst
	 * 
	 * @return the lCS
	 */
	private static LCS lcs(int srcStart, int srcEnd, CharSequence src,
			int dstStart, int dstEnd, CharSequence dst) {
		LCS lcs = new LCS(0, 0, 0);

		for (int i = srcStart; i < srcEnd; i++) {
			for (int j = dstStart; j < dstEnd; j++) {
				// System.out.println("I = " + i + " J = " + j);

				int len = 0;
				int max = Math.min(srcEnd - i, dstEnd - j);

				while (len < max) {
					if (src.charAt(i + len) == dst.charAt(j + len)) {
						len++;
					} else {
						break;
					}
				}
				// got a longer match, so erase bestMatch and replace it.
				if (len > lcs.getLength()) {
					/* replace bestMatch with our current match, which is longer */
					lcs = new LCS(i, j, len);
				}
			}
		}
		return lcs;
	}

	/**
	 * Lcs.
	 * 
	 * @param srcStart the src start
	 * @param srcEnd the src end
	 * @param src the src
	 * @param dstStart the dst start
	 * @param dstEnd the dst end
	 * @param dst the dst
	 * 
	 * @return the lCS
	 */
	private static LCS lcs(int srcStart, int srcEnd, CharSequence[] src,
			int dstStart, int dstEnd, CharSequence[] dst) {
		LCS lcs = new LCS(0, 0, 0);

		for (int i = srcStart; i < srcEnd; i++) {
			for (int j = dstStart; j < dstEnd; j++) {

				int len = 0;
				int max = Math.min(srcEnd - i, dstEnd - j);

				while (len < max) {
					if (src[i + len].equals(dst[j + len])) {
						len++;
					} else {
						break;
					}
				}
				// got a longer match, so erase bestMatch and replace it.
				if (len > lcs.getLength()) {
					/* replace bestMatch with our current match, which is longer */
					lcs = new LCS(i, j, len);
				}
			}
		}
		return lcs;
	}

	/**
	 * The Class LCS.
	 */
	private static class LCS {
		
		/** The src location. */
		private int srcLocation;

		/** The dst location. */
		private int dstLocation;

		/** The length. */
		private int length;

		/**
		 * Instantiates a new lCS.
		 * 
		 * @param srcLocation the src location
		 * @param dstLocation the dst location
		 * @param len the len
		 */
		public LCS(int srcLocation, int dstLocation, int len) {
			this.srcLocation = srcLocation;
			this.dstLocation = dstLocation;
			this.length = len;
		}

		/**
		 * Gets the dst location.
		 * 
		 * @return the dst location
		 */
		public int getDstLocation() {
			return this.dstLocation;
		}

		/**
		 * Gets the length.
		 * 
		 * @return the length
		 */
		public int getLength() {
			return this.length;
		}

		/**
		 * Gets the src location.
		 * 
		 * @return the src location
		 */
		public int getSrcLocation() {
			return this.srcLocation;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "(" + srcLocation + "," + dstLocation + "," + length + ")";
		}
	}

	/**
	 * The Class Edit.
	 */
	public static class Edit {

		/** The dst location. */
		private int srcLocation, dstLocation;

		/** The dst. */
		private CharSequence src, dst;

		/**
		 * Instantiates a new edits the.
		 * 
		 * @param srcLocation the src location
		 * @param src the src
		 * @param dstLocation the dst location
		 * @param dst the dst
		 */
		public Edit(int srcLocation, CharSequence src, int dstLocation,
				CharSequence dst) {
			if (srcLocation < 0)
				throw new IllegalArgumentException(
						"Src Start may not be negative! " + srcLocation);
			if (dstLocation < 0)
				throw new IllegalArgumentException(
						"Dst Start may not be negative! " + dstLocation);

			this.srcLocation = srcLocation;
			this.src = src;
			this.dstLocation = dstLocation;
			this.dst = dst;
		}

		/**
		 * Gets the src.
		 * 
		 * @return the src
		 */
		public CharSequence getSrc() {
			return this.src;
		}

		/**
		 * Gets the src location.
		 * 
		 * @return the src location
		 */
		public int getSrcLocation() {
			return this.srcLocation;
		}

		/**
		 * Gets the dst.
		 * 
		 * @return the dst
		 */
		public CharSequence getDst() {
			return this.dst;
		}

		/**
		 * Gets the dst location.
		 * 
		 * @return the dst location
		 */
		public int getDstLocation() {
			return this.dstLocation;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return srcLocation + "- '" + src + "', " + dstLocation + "- '"
					+ dst + "'";
		}
	}

	/**
	 * The Class ArrayEdit.
	 */
	public static class ArrayEdit {

		/** The dst end. */
		private int srcStart, srcEnd, dstStart, dstEnd;

		/**
		 * Instantiates a new array edit.
		 * 
		 * @param srcStart the src start
		 * @param srcEnd the src end
		 * @param dstStart the dst start
		 * @param dstEnd the dst end
		 */
		public ArrayEdit(int srcStart, int srcEnd, int dstStart, int dstEnd) {
			if (srcStart < 0)
				throw new IllegalArgumentException(
						"Src Start may not be negative! " + srcStart);
			if (dstStart < 0)
				throw new IllegalArgumentException(
						"Dst Start may not be negative! " + dstStart);

			this.srcStart = srcStart;
			this.srcEnd = srcEnd;
			this.dstStart = dstStart;
			this.dstEnd = dstEnd;
		}

		/**
		 * Gets the dst end.
		 * 
		 * @return the dst end
		 */
		public int getDstEnd() {
			return this.dstEnd;
		}

		/**
		 * Gets the dst start.
		 * 
		 * @return the dst start
		 */
		public int getDstStart() {
			return this.dstStart;
		}

		/**
		 * Gets the src end.
		 * 
		 * @return the src end
		 */
		public int getSrcEnd() {
			return this.srcEnd;
		}

		/**
		 * Gets the src start.
		 * 
		 * @return the src start
		 */
		public int getSrcStart() {
			return this.srcStart;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return srcStart + "-" + srcEnd + ", " + dstStart + "-" + dstEnd;
		}
	}

	/**
	 * Test.
	 * 
	 * @param src the src
	 * @param dst the dst
	 */
	private static void test(String src, String dst) {
		List edits = getEdits(src, dst, ' ');
		String result = apply(src, edits);
		if (!result.equals(dst)) {
			System.err.println("Failed applying edits! '" + result + "' != '"
					+ dst + "'");
		} else {
			System.err.println("Success applying!!");
		}
		result = revert(dst, edits);
		if (!result.equals(src)) {
			System.err.println("Failed reverting edits! '" + result + "' != '"
					+ src + "'");
		} else {
			System.err.println("Success reverting!!");
		}

	}

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		test("cith", "ttttcithbbbb");
		test("quicklyquicish", "quincequickish");
		test("the cat in the hat box", "cat in the hat");
	}
}
