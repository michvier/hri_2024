package com.utildefiner.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

@Tag("paper-slider")
@NpmPackage(value = "@polymer/paper-slider", version = "3.0.1")
@JsModule("@polymer/paper-slider/paper-slider.js")
public class PaperSlider extends AbstractSinglePropertyField<PaperSlider, Integer> {
	 @DomEvent(value = "immediate-value-change")
	    public static class ImmediateValueChangeEvent extends ComponentEvent<PaperSlider> {
	        private int value;

	        public ImmediateValueChangeEvent(PaperSlider source,
	                                         boolean fromClient,
	                                         @EventData("element.immediateValue") Integer immediateValue) {
	            super(source, fromClient);
	            this.value = immediateValue;
	        }

	        public int getValue() {
	            return value;
	        }

	    }
	    public PaperSlider() {
	    	super("value", 0, false); 
	        //Field.initSingleProperty(this, 0, "value");
	    }

	    public void setMax(int max) {
	        this.getElement().setProperty("max", max);
	    }

	    public Registration addClickListener(ComponentEventListener<ImmediateValueChangeEvent> listener) {
	        return addListener(ImmediateValueChangeEvent.class, listener);
	    }
}
