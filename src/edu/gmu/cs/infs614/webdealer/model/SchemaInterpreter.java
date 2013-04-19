package edu.gmu.cs.infs614.webdealer.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import edu.gmu.cs.infs614.webdealer.AppUtil;


public class SchemaInterpreter implements Iterable<String> // Create a new class to encapsulate the program
{
	private static final String RESOURCE_DIR = "/resources/";
	
	private ArrayList<String> m_formattedStatements;

	public SchemaInterpreter(String sql_file) {
		ingestSchema(RESOURCE_DIR+sql_file);
	}
	
	
	@Override
	public Iterator<String> iterator() {
		Iterator<String> fs = m_formattedStatements.iterator();
        return fs;
	}
	
	private void ingestSchema(String sql_file) {
		String resource = sql_file;
		String line;
		InputStreamReader isr;
		BufferedReader sql;
		String statement = "";


		isr = new InputStreamReader(getClass().getResourceAsStream(resource));
		sql = new BufferedReader(isr);

		m_formattedStatements = new ArrayList<String>();

		try {
			while ((line = sql.readLine()) != null) {
						
				if(line.contains("//")) continue;
				if(!line.contains(";")) {
					statement = statement+" "+line;
				}
				else {
					line = line.replace(";", "");
					m_formattedStatements.add((statement+" "+line).trim());
					statement = "";
				}

			  	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void printSchema () {
		
		for (String line : m_formattedStatements) {
			AppUtil.console("Formatted Statements: " + line);
		}
	}
	
	public static void main(String args[]) {
		String sql_file = "schema.sql";
		SchemaInterpreter si = new SchemaInterpreter(sql_file);
		si.printSchema();
		
		for(String line : si) {
			System.out.println("out: "+line);
		}
	}


}
