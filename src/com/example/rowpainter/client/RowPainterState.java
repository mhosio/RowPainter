package com.example.rowpainter.client;

import java.util.Map;

import com.vaadin.shared.communication.SharedState;

public class RowPainterState extends SharedState {
	private static final long serialVersionUID = 3656835095682303274L;

	public String evenRowColor;
	public String oddRowColor;
	public Map<Integer, String> rowColors;
}
