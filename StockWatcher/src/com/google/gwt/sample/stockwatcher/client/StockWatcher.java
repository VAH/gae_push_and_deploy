package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {

	
	private ArrayList<String> stocks = new ArrayList<String>();
	private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);
																						
	private static final int REFRESH_INTERVAL = 5000; // ms
	private VerticalPanel mainPanel = new VerticalPanel();  
	private FlexTable stocksFlexTable = new FlexTable();  
	private HorizontalPanel addPanel = new HorizontalPanel(); 
	private TextBox newSymbolTextBox = new TextBox();  
	private Button addStockButton = new Button("Add"); 

	private Label lastUpdatedLabel = new Label();
	private Label errorMsgLabel = new Label();

	/**  Entry point method.  */ 
	public void onModuleLoad() {  
		
	RootPanel.get("header").setStyleName("header");
		
	
	
	//Experimant 
	 
	//&1
		//HelloWorldWidget helloWorld =   new HelloWorldWidget("able", "baker", "charlie");	
		//mainPanel.add(helloWorld);
	//&2
		//UserProfileDO upDO = new UserProfileDO("Project manager at Epam Systems.", 
		      //  "alexander.arendar@gmail.com", "/images/me.jpg", "+380952600399", 
		      //  "Alexander Arendar", "aarendar.wordpress.com");
		      //  UserProfile userProfile= new UserProfile(upDO);
		      //  RootPanel.get().add(userProfile);
	
	
	//Create table for stock data. 
		stocksFlexTable.setText(0, 0, "Symbol");
		stocksFlexTable.setText(0, 1, "Price");
		stocksFlexTable.setText(0, 2, "Change");
		stocksFlexTable.setText(0 ,3, "Remove");
		

	//Add styles to elements in the stock list table
		stocksFlexTable.addStyleName("watchList");
		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericalColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericalColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
		
	//Add styles to elements in the stock list table.
		stocksFlexTable.setCellPadding(6);	
		 
	//Assemble Add Stock panel. 
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton); 
		addPanel.addStyleName("addPanel");
	
	//Add errorLabel
		errorMsgLabel.setStyleName("errorMessage"); 
		errorMsgLabel.setVisible(false);
		
	//Assemble Main panel.  
		mainPanel.add(errorMsgLabel);
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);
		
	//Associate the Main panel with the HTML host page.  
		RootPanel.get("stockList").add(mainPanel);
		
	//Move cursor focus to the input box.
		newSymbolTextBox.setFocus(true);
		
	//Setup timer to refresh list automatically.
		Timer refreshTimer = new Timer(){
			public void run(){
				refreshWatchList();	
			}
		};	
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);		
	
	//Listen for mouse events on the App button
		addStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addStock();				
			}
		});
		
	//Listen the kayboard events in the input box.
		newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {
			
			public void onKeyDown(KeyDownEvent event) {	
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					addStock();
				}				
			}
		});
		
		

	}
	
	protected void addStock() {
		
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
		newSymbolTextBox.setFocus(true);
		
	// If the symbol is not a valid
		if(!symbol.matches("^[0-9A-Z\\.]{1,10}$")){
			Window.alert("'" + symbol + "'" + "is not a valid symbol");
			newSymbolTextBox.selectAll();
			return;
		}
			
	// If the stocks is already contains a such symbol
		if(stocks.contains(symbol)){		
			return;
				
		}
			
	//Add the stock to the table
		int row = stocksFlexTable.getRowCount();
		stocks.add(symbol);
		stocksFlexTable.setText(row, 0, symbol);
		stocksFlexTable.setWidget(row, 2, new Label());
		stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");
	    
		Button removeStockButton = new Button("x");
		removeStockButton.addStyleDependentName("remove");
		 
	//Add remove Handler
		removeStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removeIndex = stocks.indexOf(symbol);
				stocks.remove(removeIndex);
				stocksFlexTable.removeRow(removeIndex + 1);
			}
		});
		
	//Add remove Button 
		stocksFlexTable.setWidget(row, 3, removeStockButton);
		
	//Get the stock price
		refreshWatchList();
		
	//Clearing the text field
		newSymbolTextBox.setText("");	
	
	}
	
	private void refreshWatchList() {
		// Initialize the service proxy.
	    if (stockPriceSvc == null) {
	      stockPriceSvc = GWT.create(StockPriceService.class);
	    }

	    // Set up the callback object.
	    AsyncCallback <StockPrice[]>  callback = new AsyncCallback <StockPrice[]> () {
	      public void onFailure(Throwable caught) {
	    	  String details = caught.getMessage();
	    	  
	          if (caught instanceof DelistedException) {
	            details = "Company '" + ((DelistedException)caught).getSymbol() + "' was delisted";
	          }

	          errorMsgLabel.setText("Error: " + details);
	          errorMsgLabel.setVisible(true);
	      }

	      public void onSuccess(StockPrice[] result) {
	        updateTable(result);
	      }
	    };

	    // Make the call to the stock price service.
	    stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);
	  }
	
	
	protected void displayError(String error) {
		errorMsgLabel.setText("Error:" + error);
		errorMsgLabel.setVisible(true);		
	}

	@SuppressWarnings("deprecation")
	private void updateTable(StockPrice[] prices) {
		for (int i = 0; i < prices.length; i++) {
	      updateTable(prices[i]);
	    }

	//Display timestamp showing last refresh. 
		lastUpdatedLabel.setText("Last update :" + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
		
	// Clear any errors.  
		errorMsgLabel.setVisible(false);
		
	}

	private void updateTable(StockPrice price) {
		    // Make sure the stock is still in the stock table.
		    if (!stocks.contains(price.getSymbol())) {
		      return;
		    }

		    int row = stocks.indexOf(price.getSymbol()) + 1;

		    // Format the data in the Price and Change fields.
		    String priceText = NumberFormat.getFormat("#,##0.00").format(
		        price.getPrice());
		    NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
		    String changeText = changeFormat.format(price.getChange());
		    String changePercentText = changeFormat.format(price.getChangePercent());

		    // Populate the Price and Change fields with new data.
		    stocksFlexTable.setText(row, 1, priceText);
		  
			Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);	
			changeWidget.setText(changeText + "(" + changePercentText + "%)");
			
			// Change the color of text in the Change field based on its value.
		    String changeStyleName = "noChange";
		    if (price.getChangePercent() < -0.1f) {
		      changeStyleName = "negativeChange";
		    }
		    else if (price.getChangePercent() > 0.1f) {
		      changeStyleName = "positiveChange";
		    }

		    changeWidget.setStyleName(changeStyleName);
			
	}
}






