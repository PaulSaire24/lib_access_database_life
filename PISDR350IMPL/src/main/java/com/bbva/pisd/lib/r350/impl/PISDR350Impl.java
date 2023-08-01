package com.bbva.pisd.lib.r350.impl;

import com.bbva.apx.exception.business.BusinessException;

import com.bbva.apx.exception.db.DataIntegrityViolationException;
import com.bbva.apx.exception.db.DuplicateKeyException;
import com.bbva.apx.exception.db.IncorrectResultSizeException;
import com.bbva.apx.exception.db.TimeoutException;
import com.bbva.apx.exception.db.NoResultException;
import com.bbva.pisd.dto.invoice.OperationDTO;

import com.bbva.pisd.lib.r350.impl.util.PISDConstant;
import com.bbva.pisd.lib.r350.impl.util.PISDErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import static java.util.Objects.nonNull;

public class PISDR350Impl extends PISDR350Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(PISDR350Impl.class);

	@Override
	public int executeInsertSingleRow(String queryId, Map<String, Object> arguments, String... requiredParameters) {
		LOGGER.info("***** PISDR350Impl - insertSingleRow START *****");
		LOGGER.info("***** PISDR350Impl - insertSingleRow - EXECUTING {} QUERY ... *****", queryId);
		int affectedRows = 0;
		if(parametersEvaluation(arguments, requiredParameters)) {
			LOGGER.info("***** PISDR350Impl - insertSingleRow - PARAMETERS OK ... EXECUTING *****");
			try {
				affectedRows = this.jdbcUtils.update(queryId, arguments);
			} catch (NoResultException ex) {
				LOGGER.info("***** PISDR350Impl - {} database exception: {} *****", queryId, ex.getMessage());
				affectedRows = -1;
			}
		} else {
			LOGGER.info("insertSingleRow - MISSING MANDATORY PARAMETERS {}", queryId);
		}
		LOGGER.info("***** PISDR350Impl - insertSingleRow | Number of inserted rows: {} *****", affectedRows);
		LOGGER.info("***** PISDR350Impl - insertSingleRow END *****");
		return affectedRows;
	}

	@Override
	public Map<String, Object> executeGetASingleRow(String queryId, Map<String, Object> arguments) {
		LOGGER.info("***** PISDR350Impl - executeGetASingleRow START *****");
		LOGGER.info("***** PISDR350Impl - executeGetASingleRow | Executing {} QUERY", queryId);
		try {
			Map<String, Object> response = this.jdbcUtils.queryForMap(queryId, arguments);
			response.forEach((key, value) -> LOGGER.info("Column -> {} with value: {}", key,value));
			LOGGER.info("***** PISDR350Impl - executeGetASingleRow END *****");
			return response;
		} catch (NoResultException ex) {
			LOGGER.info("executeGetASingleRow - There wasn't no result in query {}. Reason -> {}", queryId, ex.getMessage());
			return null;
		}
	}

	@Override
	public int[] executeMultipleInsertionOrUpdate(String queryId, Map<String, Object>[] argumentsArray) {
		LOGGER.info("***** PISDR350Impl - executeMultipleInsertionOrUpdate START *****");
		LOGGER.info("***** PISDR350Impl - executeMultipleInsertionOrUpdate | Executing {} QUERY", queryId);
		int[] affectedRows = null;
		try {
			affectedRows = this.jdbcUtils.batchUpdate(queryId, argumentsArray);
		} catch (NoResultException ex) {
			LOGGER.info("***** PISDR350Impl - executeMultipleInsertionOrUpdate - Database exception: {} *****", ex.getMessage());
			affectedRows = new int[0];
		}
		LOGGER.info("***** PISDR350Impl - executeMultipleInsertionOrUpdate | Number of inserted rows: {} *****", nonNull(affectedRows) ? affectedRows.length : null);
		LOGGER.info("***** PISDR350Impl - executeMultipleInsertionOrUpdate END *****");
		return affectedRows;
	}

	@Override
	public Map<String, Object> executeGetListASingleRow(String queryId, Map<String, Object> arguments) {
		LOGGER.info("***** PISDR350Impl - executeGetListASingleRow START *****");
		LOGGER.info("***** PISDR350Impl - executeGetListASingleRow | Executing {} QUERY", queryId);
		try {
			List<Map<String, Object>> response = this.jdbcUtils.queryForList(queryId, arguments);
			response.stream().forEach(map -> map.
					forEach((key, value) -> LOGGER.info("[executeGetListASingleRow] Result -> Key {} with value: {}", key,value)));
			LOGGER.info("***** PISDR350Impl - executeGetListASingleRow END *****");
			return buildResult(response);
		} catch (NoResultException ex) {
			LOGGER.info("executeGetListASingleRow - There wasn't no result in query {}. Reason -> {}", queryId, ex.getMessage());
			return null;
		}
	}

	@Override
	public Object executeQuery(OperationDTO operation) {
		Object response = null;
		LOGGER.info("[PISDR350Impl] - start executeQuery() with Param OperationDTO :: {}", operation);
		try {
			if(operation.getTypeOperation().equals(PISDConstant.Operation.SELECT)){
				if(operation.isForListQuery()){
					response = this.jdbcUtils.queryForList(operation.getNameProp(), operation.getParams());
				}else {
					response = this.jdbcUtils.queryForMap(operation.getNameProp(), operation.getParams());
				}
			}else if(operation.getTypeOperation().equals(PISDConstant.Operation.UPDATE)){
				response = this.jdbcUtils.update(operation.getNameProp(), operation.getParams());
			}else if(operation.getTypeOperation().equals(PISDConstant.Operation.BATCH)){
				response = this.jdbcUtils.batchUpdate(operation.getNameProp(), operation.getBatchValues());
			}

		} catch(NoResultException ex) {
			LOGGER.info("[PISDR350Impl] - not found data, query Empty Result to {}, {}", operation.getNameProp(),PISDErrors.QUERY_EMPTY_RESULT.getMessage());
			this.addAdvice(PISDErrors.QUERY_EMPTY_RESULT.getAdviceCode());
		} catch(DuplicateKeyException ex) {
			this.addAdvice(PISDErrors.ERROR_DUPLICATE_KEY.getAdviceCode());
			throw new BusinessException(PISDErrors.ERROR_DUPLICATE_KEY.getAdviceCode(), PISDErrors.ERROR_DUPLICATE_KEY.isRollback(), ex.getMessage());
		} catch (TimeoutException ae){
			this.addAdvice(PISDErrors.ERROR_TIME_OUT.getAdviceCode());
			throw new BusinessException(PISDErrors.ERROR_TIME_OUT.getAdviceCode(), false, ae.getMessage());
		}catch (DataIntegrityViolationException ae){
			this.addAdvice(PISDErrors.ERROR_INTEGRITY_VIOLATION.getAdviceCode());
			throw new BusinessException(PISDErrors.ERROR_INTEGRITY_VIOLATION.getAdviceCode(), false, ae.getMessage());
		}catch (IncorrectResultSizeException ae){
			this.addAdvice(PISDErrors.ERROR_INCORRECT_RESULT.getAdviceCode());
			throw new BusinessException(PISDErrors.ERROR_INCORRECT_RESULT.getAdviceCode(), false, ae.getMessage());
		}

		LOGGER.info("[PISDR350Impl] - end executeQuery()");
		return response;
	}

	private boolean parametersEvaluation(Map<String, Object> arguments, String... keys) {
		return Arrays.stream(keys).allMatch(key -> nonNull(arguments.get(key)));
	}

	private Map<String, Object> buildResult(List<Map<String, Object>> response) {
		Map<String, Object> result = new HashMap<>();
		result.put("dtoInsurance", response);
		return result;
	}

}
