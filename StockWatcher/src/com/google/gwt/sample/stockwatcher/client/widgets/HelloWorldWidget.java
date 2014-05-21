package com.google.gwt.sample.stockwatcher.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class HelloWorldWidget extends Composite{
	interface MyUiBinder extends UiBinder<Widget, HelloWorldWidget> {}
	
	  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	  @UiField ListBox listBox;

	  public HelloWorldWidget(String... names) {
	    // sets listBox
	    initWidget(uiBinder.createAndBindUi(this));
	    for (String name : names) {
	      listBox.addItem(name);
	    }
	  }
	}