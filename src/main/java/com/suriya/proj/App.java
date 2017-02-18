package com.suriya.proj;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
	@Autowired
	private MyService myService;
	
	private List<String> name;
	public void initz()
	{
		System.out.println("inside intit");
	}
	public void destrz()
	{
		System.out.println("inside dest");
	}
	public void print()
	{
		System.out.println("print");
		myService.getPropValue();
		
	//	myService.setAbc("surya",new Date());
	//	myService.setAbc("raja",new Date());
	//	myService.setAbc("chandra",new Date());
		
		myService.getAbc();
		
	}
    public static void main( String[] args )
    {
    	ApplicationContext context=new ClassPathXmlApplicationContext("context.xml");
    	App app=(App)context.getBean("app");
    	String msg=context.getMessage("welcome",null,"test", null);
    	System.out.println(msg);
    	String msg1=context.getMessage("hello",null,"testhelloeng", Locale.US);
    	System.out.println(msg1);
    	String msg2=context.getMessage("hello",null,"testhellofra", Locale.FRANCE);
    	System.out.println(msg2);
    	app.print();
    }
}
