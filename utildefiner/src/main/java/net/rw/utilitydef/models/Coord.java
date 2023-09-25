package net.rw.utilitydef.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Coord {

	private double x;
	private double y;
	
	
	
	public Coord(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Coord() {
		super();
	}
	

	
	@Override
	public String toString() {
		return "Coords [x=" + x + ", y=" + y + "]";
	}

	
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	
}
