package com.suriya.proj;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class MyService implements ApplicationContextAware{
	
	@Autowired
	private Dao dao;
	
	private int id=0;
	
	private ApplicationContext context;
	
	public void getPropValue()
	{
		String lang="ensg",msg;
		System.out.println("inside getValue service");
		if("eng".equals(lang))
			msg=context.getMessage("hello",null,"testhelloeng", Locale.US);
		else
			msg=context.getMessage("hello",null,"testhellofra", Locale.FRANCE);
		
		System.out.println(msg);
	}
	
	public void setAbc(String name,Date dob)
	{
		id++;
		dao.setAbc(id,name,new java.sql.Date(dob.getTime()));
	}
	
	public void getAbc()
	{
		dao.getAbc();
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		System.out.println("inside myservice setAppcontext");
		this.context=context;
	}
	

}
