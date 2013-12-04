package com.mhosio.rowpainter;

import java.util.HashMap;

import com.mhosio.rowpainter.client.RowPainterState;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.GridLayout;

/**
 * Extension component for painting backgrounds of (virtual) rows in Vaadin
 * gridlayout. Based on a canvas that is added as background for the layout.
 * <b>NOTE:</b> Relies heavily on internal workings of the grid layout, so may
 * not be compatible with future Vaadin versions.
 * 
 * @author mjhosio
 * 
 */
public class RowPainter extends AbstractExtension {

	private static final long serialVersionUID = 7573800482330252035L;

	/**
	 * Creates a new instance of row painter
	 * 
	 */
	public RowPainter() {
		getState().rowColors = new HashMap<Integer, String>();
	}

	/**
	 * Creates a new instance of row painter
	 * 
	 * @param layout
	 *            the layout for painting
	 */
	public RowPainter(GridLayout layout) {
		super.extend(layout);
		getState().rowColors = new HashMap<Integer, String>();
	}

	/**
	 * Extends the specified grid layout to show row colors
	 * 
	 * @param layout
	 *            the grid layout to paint rows on
	 */
	public void extend(GridLayout layout) {
		super.extend(layout);
	}

	@Override
	protected RowPainterState getState() {
		return (RowPainterState) super.getState();
	}

	/**
	 * Sets the color used to paint even rows in the layout. Can be null, in
	 * which case the rows are not painted by default.
	 * 
	 * @param evenRowColor
	 *            the css color for the rows. Example #FFFFFF
	 */
	public void setEvenRowColor(String evenRowColor) {
		getState().evenRowColor = evenRowColor;
	}

	/**
	 * Sets the color used to paint odd rows in the layout. Can be null, in
	 * which case the rows are not painted by default.
	 * 
	 * @param oddRowColor
	 *            the css color for the rows. Example #DDDDDD
	 */
	public void setOddRowColor(String oddRowColor) {
		getState().oddRowColor = oddRowColor;
	}

	/**
	 * Gets the color used to paint even rows in the layout, or null if not
	 * specified
	 * 
	 * @return the color used to paint even rows. Example #FFFFFF
	 */
	public String getEvenRowColor() {
		return getState().evenRowColor;
	}

	/**
	 * Gets the color used to paint odd rows in the layout, or null if not
	 * specified
	 * 
	 * @return the color used to paint odd rows. Example #DDDDDD
	 */
	public String getOddRowColor() {
		return getState().oddRowColor;
	}

	/**
	 * Sets the color for an individual row
	 * 
	 * @param row
	 *            the row number starting from 0
	 * @param color
	 *            the color for the row. Example #0000FF
	 */
	public void setRowColor(int row, String color) {
		getState().rowColors.put(row, color);
	}

	/**
	 * Sets the color for an individual row
	 * 
	 * @param row
	 *            the row number starting from 0
	 * @return the color used to paint the specified row.
	 */
	public String getRowColor(int row) {
		return getState().rowColors.get(row);
	}

	/**
	 * Returns true if the specified row has individually specified color. Even
	 * and odd row colors are not taken into account.
	 * 
	 * @param row
	 *            the row number starting from 0
	 * @return true if the row has individually specified color
	 */
	public boolean hasRowColor(int row) {
		return getState().rowColors.containsKey(row);
	}

	/**
	 * Clears the individually set color from the specified row
	 * 
	 * @param row
	 *            the row number starting from 0
	 */
	public void resetRowColor(int row) {
		getState().rowColors.remove(row);
	}

	/**
	 * Clears all individually set row colors
	 */
	public void resetRowColors() {
		getState().rowColors.clear();
	}

}
