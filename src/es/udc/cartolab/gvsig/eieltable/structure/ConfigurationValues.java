package es.udc.cartolab.gvsig.eieltable.structure;

/**
 * Configuration Values.
 * 
 * Class used for storing configuration values data.
 */
public class ConfigurationValues {

	/**
	 * The configured DB.
	 */
	private String dataBase;
	/**
	 * The configured table.
	 */
	private String table;
	/**
	 * The configured layer.
	 */
	private String layer;

	/**
	 * DB getter.
	 * 
	 * @return The set DB.
	 */
	public String getDataBase() {
		return dataBase;
	}

	/**
	 * DB setter.
	 * 
	 * @param dataBase the DB we want to set.
	 */
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	/**
	 * Layer getter.
	 * 
	 * @return The set layer.
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * Layer setter.
	 * 
	 * @param layer the layer we want to set.
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * Table getter.
	 * 
	 * @return The set table.
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Table setter.
	 * 
	 * @param table the table we want to set.
	 */
	public void setTable(String table) {
		this.table = table;
	}

}
