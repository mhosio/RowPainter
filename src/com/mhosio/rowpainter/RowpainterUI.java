package com.mhosio.rowpainter;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("rowpainter")
public class RowpainterUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = RowpainterUI.class, widgetset = "com.mhosio.rowpainter.RowpainterWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		setContent(layout);
		final GridLayout testGrid = new GridLayout(5, 10);
		testGrid.setSizeFull();
		layout.addComponent(testGrid);
		final RowPainter rp = new RowPainter(testGrid);
		rp.setEvenRowColor("#FFFFFF");
		rp.setOddRowColor("#DDDDDD");
		for (int i = 0; i < 10; i++) {
			Button b = new Button("toggle row color");
			final int rowIndex = i;
			b.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					if (!rp.hasRowColor(rowIndex)) {
						switch (rowIndex % 3) {
						case 0:
							rp.setRowColor(rowIndex, "#0000FF");
							break;
						case 1:
							rp.setRowColor(rowIndex, "#00FF00");
							break;
						default:
							rp.setRowColor(rowIndex, "#FF0000");
							break;
						}
					} else {
						rp.resetRowColor(rowIndex);
					}
				}
			});
			testGrid.addComponent(b, 0, i);
		}
	}

}