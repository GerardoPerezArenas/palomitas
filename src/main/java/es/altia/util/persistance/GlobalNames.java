package es.altia.util.persistance;

import es.altia.util.persistance.daocommands.SQLDAOCommandFactory;

public final class GlobalNames {
    private GlobalNames() {}
    public static final String DEFAULT_DATASOURCE = "CFGDATASOURCE";
	public static final String CFGDATASOURCE = "CFGDATASOURCE";
    public static final String CFG_DB_SERVER = SQLDAOCommandFactory.DB_SQLSERVER;
    public static final String MAIN_DB_SERVER = SQLDAOCommandFactory.DB_SQLSERVER;
}//class
