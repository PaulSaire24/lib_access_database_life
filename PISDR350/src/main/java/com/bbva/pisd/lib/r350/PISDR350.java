package com.bbva.pisd.lib.r350;

import java.util.Map;

/**
 * The  interface PISDR350 class...
 */
public interface PISDR350 {

	/**
	 * @author P029820
	 * @param queryId query identificator from sql properties sheet
	 * @param arguments parameters to replace in query
	 * @param requiredParameters parameters whose should not be null values (if there aren't any parameters to evaluate, pass an empty array)
	 * @return rows number inserted, deleted or updated (it always must be 1 if everything went ok)
	 */
	int executeInsertSingleRow(String queryId, Map<String, Object> arguments, String... requiredParameters);

	/**
	 * @author P029820
	 * @param queryId query identificator from sql properties sheet
	 * @param arguments parameters to replace in query
	 * @return a row with its columns
	 */
	Map<String, Object> executeGetASingleRow(String queryId, Map<String, Object> arguments);

	/**
	 * @author P029820
	 * @param queryId query identificator from sql properties sheet
	 * @param argumentsArray parameters array to replace in each insert or update
	 * @return rows number inserted or updated (it should be more than 1 if everything went ok)
	 */
	int[] executeMultipleInsertionOrUpdate(String queryId, Map<String, Object>[] argumentsArray);

	/**
	 * @author P029820
	 * @param queryId query identificator from sql properties sheet
	 * @param arguments parameters to replace in query
	 * @return a list with its columns
	 */
	Map<String, Object> executeGetListASingleRow(String queryId, Map<String, Object> arguments);


}
