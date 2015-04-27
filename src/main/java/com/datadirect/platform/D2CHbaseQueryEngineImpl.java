package com.datadirect.platform;

import java.util.Map;

import javax.naming.Context;

import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.hbase.HBaseExecutionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import bitronix.tm.resource.jdbc.PoolingDataSource;


public class D2CHbaseQueryEngineImpl extends D2CQueryEngineImpl {
	
	public static final String TRANSLATOR_HBASE = "translator-hbase";

	public D2CHbaseQueryEngineImpl(String localhost, int port, String username,
			String password) {
		super(localhost, port, username, password);
	}
	
	public D2CHbaseQueryEngineImpl(String username, String password) {
		super(username, password);
	}
	
	@Override
	protected String registerTranslatorFor(String dstype)
			throws TranslatorException {
		String translator;
		switch(dstype){
			case "hbase" :
				ExecutionFactory factory = new HBaseExecutionFactory();
                factory.start();
                m_server.addTranslator(TRANSLATOR_HBASE, factory);
                return TRANSLATOR_HBASE;
			default:
				translator = super.registerTranslatorFor(dstype);
		}
		return translator;
	}
	
	@Override
	protected Element createVDBModel(Document vdbDoc,
			Map<String, String> translators, DataSource ds)
			throws TranslatorException {
		String name = ds.getName();
		if("hbase".equals(name)){
			
		}
		return super.createVDBModel(vdbDoc, translators, ds);
	}
	
	@Override
	protected String registerD2CDataSource(String name) {
		String datasourceName;
		if (name.equals("hbase")) {
			PoolingDataSource pds = new PoolingDataSource();
	        datasourceName = "java:/QSPhoenixDS";
			pds.setUniqueName(datasourceName);
	        pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
	        pds.setMaxPoolSize(5);
	        pds.setAllowLocalTransactions(true);
	        pds.getDriverProperties().put("user", "");
	        pds.getDriverProperties().put("password", "");
	        pds.getDriverProperties().put("url", "jdbc:phoenix:172.21.64.87:2181");
	        pds.getDriverProperties().put("driverClassName", "org.apache.phoenix.jdbc.PhoenixDriver");
	        pds.init();
	        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	        return datasourceName;
		}
		return super.registerD2CDataSource(name);
	}

}
