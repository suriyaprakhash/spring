package com.suriya.proj;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class Dao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	public Dao() {
		// TODO Auto-generated constructor stub
	}

	public void setAbc(int id, String name, Date dob) {
		TransactionDefinition def=new DefaultTransactionDefinition();
		TransactionStatus status=transactionManager.getTransaction(def);
		try{
			String query = "insert into abc values('" + id + "','" + name + "','"
					+ dob + "')";
			jdbcTemplate.update(query);
			transactionManager.commit(status);
		}
		catch(Exception e)
		{
			System.out.println(e);
			transactionManager.rollback(status);
		}
	}

	public void getAbc() {
		String query = "select * from abc order by id desc";
		List<Map> rows = jdbcTemplate.queryForList(query);
		for (Map row : rows) {
			System.out.println("--------------------------");
			System.out.println(row.get("id"));
			System.out.println(row.get("name"));
			System.out.println(row.get("dob"));
			System.out.println("--------------------------");
		}
	}
}
