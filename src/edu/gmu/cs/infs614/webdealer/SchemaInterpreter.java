package edu.gmu.cs.infs614.webdealer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;


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
		
		
		
		List<String> contents = null;

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
			System.out.println("Formatted Statements: " + line);
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

