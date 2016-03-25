package view;

import java.util.ArrayList;
import java.util.HashMap;

public class QueryBuilder {
	private String mainTable;
	private ArrayList<String> grouping, from, select;
	private HashMap<String, String> where;
	
	public QueryBuilder(){
		this.select = new ArrayList();
		this.from = new ArrayList();
		this.where = new HashMap();
		this.grouping = new ArrayList();
	}
	
	public String build(){
		String query = "";
		for(int i=0; i<select.size(); i++){
			if(i == 0){
				query = query + "select " + select.get(i);
			}else query = query + ", " + select.get(i);
		}
		for(int i=0; i<from.size(); i++){
			if(i == 0){
				query = query + "\nfrom " + from.get(i);
				this.mainTable = from.get(i);
			}else query = query + "\ninner join " + from.get(i) 
							+ " on (" + mainTable + "." + from.get(i) + "_id = " + from.get(i) +".id) ";
		}
		for(String key: where.keySet()){
			if(query.contains("where")){
				query = query + " and " + where.get(key);
			}else{
				query = query + "\nwhere " + where.get(key);
			}
		}
		for(int i=0; i<grouping.size(); i++){
			if(i==0){
				query = query + "\ngroup by " + grouping.get(i);
			}else{
				query = query + ", " + grouping.get(i);
			}
		}
		return query;
	}
	
	public QueryBuilder addSelect(String variable){
		if(!this.select.contains(variable))
			this.select.add(variable);
		return this;
	}
	
	public QueryBuilder removeSelect(String variable){
		if(this.select.contains(variable))
			this.select.remove(variable);
		return this;
	}
	
	public QueryBuilder addFrom(String table){
		if(!this.from.contains(table))
			this.from.add(table);
		return this;
	}
	
	public QueryBuilder removeFrom(String table){
		if(this.from.contains(table))
			this.from.remove(table);
		return this;
	}
	
	public QueryBuilder addCondition(String field, String value){
		String condition = field;
//		
		if(value.equals("null")){
			condition = condition + " is null";
		}else{
			condition = condition + " = " + value;
		}
		
		if(!where.containsKey(field))
			where.put(field, condition);
		else where.replace(field, condition);
		return this;
	}
	
	public QueryBuilder removeCondition(String field){
		if(where.containsKey(field))
			where.remove(field);
		return this;
	}
	
	public QueryBuilder addGrouping(String field){
		if(!this.grouping.contains(field))
			this.grouping.add(field);
		return this;
	}
	
	public QueryBuilder removeGrouping(String field){
		if(this.grouping.contains(field))
			this.grouping.remove(field);
		for(String group: this.grouping){
			System.out.println(group + " " + field);
		}
		return this;
	}
}
