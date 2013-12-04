package com.mhosio.rowpainter.client;

import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Element;
import com.mhosio.rowpainter.RowPainter;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler;
import com.vaadin.client.LayoutManager;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.communication.StateChangeEvent.StateChangeHandler;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.client.ui.gridlayout.GridLayoutConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;

@Connect(RowPainter.class)
public class RowPainterConnector extends AbstractExtensionConnector implements
		ElementResizeListener, StateChangeHandler {

	private static final long serialVersionUID = 8971781697146077249L;

	private GridLayoutConnector layoutConnector;
	Canvas canvas;

	public RowPainterConnector() {

	}

	@Override
	protected void extend(ServerConnector target) {
		layoutConnector = (GridLayoutConnector) target;
		canvas = Canvas.createIfSupported();
		layoutConnector.getWidget().getElement()
				.appendChild(canvas.getElement());
		layoutConnector.addStateChangeHandler(this);
		layoutConnector
				.addConnectorHierarchyChangeHandler(new ConnectorHierarchyChangeHandler() {

					private static final long serialVersionUID = -8584673426204483260L;

					@Override
					public void onConnectorHierarchyChange(
							ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
						drawRows();
					}
				});
		LayoutManager.get(getConnection()).addElementResizeListener(
				layoutConnector.getWidget().getElement(), this);
	}

	protected String getEvenRowColor() {
		return getState().evenRowColor;
	}

	protected String getOddRowColor() {
		return getState().oddRowColor;
	}

	protected Map<Integer, String> getRowColorMap() {
		return getState().rowColors;
	}

	public RowPainterState getState() {
		return (RowPainterState) super.getState();
	}

	// draw only once if multiple scheduled redraws
	private boolean dirty = false;

	protected void drawRows() {
		dirty = true;
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				if (!dirty) {
					return;
				}
				if (layoutConnector.getParent() == null) {
					return;
				}
				VGridLayout layout = layoutConnector.getWidget();
				int[] rowHeights = layout.rowHeights;
				if (rowHeights == null) {
					return;
				}
				// get padding info
				LayoutManager lm = LayoutManager.get(getConnection());
				Element e = layout.getElement();
				int paddingTop = lm.getPaddingTop(e);
				// component spacing
				int spacing = LayoutManager.get(getConnection())
						.getOuterHeight(layout.spacingMeasureElement);

				int width = layoutConnector.getWidget().getOffsetWidth();
				int height = layoutConnector.getWidget().getOffsetHeight();
				canvas.setCoordinateSpaceWidth(width);
				canvas.setCoordinateSpaceHeight(height);
				canvas.setWidth("100%");
				canvas.setHeight("100%");
				Context2d context = canvas.getContext2d();
				int y = 0;
				String evenRowColor = getEvenRowColor();
				String oddRowColor = getOddRowColor();
				Map<Integer, String> rowColors = getRowColorMap();
				for (int i = 0; i < rowHeights.length; i++) {
					boolean doNotPaint = false;
					if (rowColors.containsKey(i)) {
						context.setFillStyle(rowColors.get(i));
					} else if (i % 2 == 0 && evenRowColor != null) {
						context.setFillStyle(evenRowColor);
					} else if (i % 2 == 1 && oddRowColor != null) {
						context.setFillStyle(oddRowColor);
					} else {
						// no color definition for the row found
						doNotPaint = true;
					}
					int lineHeight = rowHeights[i] + spacing;
					if (i == 0) {
						lineHeight += paddingTop;
					}
					if (!doNotPaint) {
						context.fillRect(0, y, width, lineHeight);
					}
					y += lineHeight;
				}
				dirty = false;
			}
		});
	}

	@Override
	public void onUnregister() {
		super.onUnregister();
		LayoutManager.get(getConnection()).removeElementResizeListener(
				layoutConnector.getWidget().getElement(), this);
	}

	@Override
	public void onElementResize(ElementResizeEvent e) {
		drawRows();
	}

	@Override
	public void onStateChanged(StateChangeEvent e) {
		super.onStateChanged(e);
		drawRows();
	}

}
