package com.google.gwt.sample.stockwatcher.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;


public class UserProfile extends Composite {

	interface UserProfileUiBinder extends UiBinder<FlowPanel,UserProfile>{};
	private static UserProfileUiBinder uiBinder = GWT.create(UserProfileUiBinder.class);
	
	 @UiField Image photo;
	 @UiField Label name;
	 @UiField Label mobile;
	 @UiField Label email;
	 @UiField Label web;
	 @UiField Label bio;
	
	
	 public UserProfile(UserProfileDO data)
	    {
	        super();
	        initWidget(uiBinder.createAndBindUi(this));
	        photo.setUrl(data.getImageURL());
	        name.setText(data.getName());
	        email.setText(data.getEmail());
	        mobile.setText(data.getMobile());
	        web.setText(data.getMobile());
	        bio.setText(data.getBio());
	    }

}
