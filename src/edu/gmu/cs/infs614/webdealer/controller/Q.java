package edu.gmu.cs.infs614.webdealer.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javafx.scene.control.TextArea;

class Q {
	   int n;
	   boolean valueSet = false;
	   BufferedReader br;
	   TextArea ta;
	   String line;
	   
	   Q(BufferedReader br, TextArea ta) {
		   this.br = br;
		   this.ta = ta;
	   }
	   synchronized int get() {
	      if(!valueSet)
	      try {
	         br.wait();
	      } catch(InterruptedException e) {
	         System.out.println("InterruptedException caught");
	      }
	      
		try {
			line = br.readLine()+"\n";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	        
	      System.out.println("Got: " + n);
	      valueSet = false;
	      br.notify();
	      return n;
	   }

	   synchronized void put(int n) {
	      if(valueSet)
	      try {
	         ta.wait();
	      } catch(InterruptedException e) {
	         System.out.println("InterruptedException caught");
	      }
	      ta.appendText(line);
	      this.n = n;
	      valueSet = true;
	      System.out.println("Put: " + n);
	      ta.notify();
	   }
	}
