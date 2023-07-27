package com.bbva.pisd.lib.r350;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.db.*;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import javax.annotation.Resource;

import com.bbva.elara.utility.jdbc.JdbcUtils;
import com.bbva.pisd.dto.invoice.OperationDTO;
import com.bbva.pisd.dto.invoice.constants.PISDConstant;
import com.bbva.pisd.lib.r350.impl.PISDR350Impl;
import com.bbva.pisd.lib.r350.impl.util.PISDErrors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/PISDR350-app.xml",
		"classpath:/META-INF/spring/PISDR350-app-test.xml",
		"classpath:/META-INF/spring/PISDR350-arc.xml",
		"classpath:/META-INF/spring/PISDR350-arc-test.xml" })
public class PISDR350Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(PISDR350Test.class);

	private static final String CODE = "adviceCode";
	private static final String MESSAGE = "No se encontró data";

	private PISDR350Impl pisdR350 = new PISDR350Impl();

	private JdbcUtils jdbcUtils;

	@Spy
	private Context context;

	@Mock
	private Map<String, Object> insertSingleRowArguments;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		context = new Context();
		ThreadContext.set(context);

		jdbcUtils = mock(JdbcUtils.class);
		pisdR350.setJdbcUtils(jdbcUtils);
	}

	@Test
	public void executeInsertSingleRow_OK() {
		LOGGER.info("PISDR350Test - Executing executeInsertSingleRow_OK...");

		when(insertSingleRowArguments.get("firstKey")).thenReturn("anyValue");

		when(jdbcUtils.update("anyQueryId", insertSingleRowArguments)).thenReturn(1);

		int validation = pisdR350.executeInsertSingleRow("anyQueryId", insertSingleRowArguments, "firstKey");

		assertEquals(1, validation);
	}

	@Test
	public void executeInsertSingleRow_WithNoResultException() {
		LOGGER.info("PISDR350Test - Executing executeInsertSingleRow_WithNoResultException...");

		when(insertSingleRowArguments.get("firstKey")).thenReturn("anyValue");

		when(jdbcUtils.update("anyQueryId", insertSingleRowArguments)).thenThrow(new NoResultException("adviceCode", "errorMessage"));

		int validation = pisdR350.executeInsertSingleRow("anyQueryId", insertSingleRowArguments, "firstKey");

		assertEquals(-1, validation);
	}

	@Test
	public void executeInsertSingleRow_WithMissingMandatoryParameters() {
		LOGGER.info("PISDR350Test - Executing executeInsertSingleRow_WithMissingMandatoryParameters...");

		int validation = pisdR350.executeInsertSingleRow("anyQueryId", insertSingleRowArguments, "firstKey");

		assertEquals(0, validation);
	}

	@Test
	public void executeGetASingleRow_OK() {
		LOGGER.info("PISDR350Test - Executing executeGetASingleRow_OK...");

		Map<String, Object> response = new HashMap<>();
		response.put("key", "someValue");

		when(jdbcUtils.queryForMap("anyQueryId", new HashMap<>())).thenReturn(response);

		Map<String, Object> validation = pisdR350.executeGetASingleRow("anyQueryId", new HashMap<>());

		assertNotNull(validation);
	}

	@Test
	public void executeGetASingleRow_WithNoResultException() {
		LOGGER.info("PISDR350Test - Executing executeGetASingleRow_WithNoResultException...");

		when(jdbcUtils.queryForMap("anyQueryId", new HashMap<>())).thenThrow(new NoResultException("adviceCode", "errorMessage"));

		Map<String, Object> validation = pisdR350.executeGetASingleRow("anyQueryId", new HashMap<>());

		assertNull(validation);
	}

	@Test
	public void executeMultipleInsertionOrUpdate_OK() {
		LOGGER.info("PISDR350Test - Executing executeMultipleInsertionOrUpdate_OK...");

		when(pisdR350.executeMultipleInsertionOrUpdate(anyString(), any())).thenReturn(new int[2]);

		int[] validation = this.pisdR350.executeMultipleInsertionOrUpdate("queryId", new Map[2]);

		assertEquals(2, validation.length);
	}

	@Test
	public void executeMultipleInsertionOrUpdate_WithNoResultException() {
		LOGGER.info("PISDR350Test - Executing executeMultipleInsertionOrUpdate_WithNoResultException...");

		when(pisdR350.executeMultipleInsertionOrUpdate(anyString(), any())).thenThrow(new NoResultException("adviceCode", "errorMessage"));

		int[] validation = this.pisdR350.executeMultipleInsertionOrUpdate("queryId", new Map[2]);

		assertEquals(0, validation.length);
	}

	@Test
	public void executeGetListASingleRow_OK() {
		LOGGER.info("PISDR350Test - Executing executeGetListASingleRow_OK...");

		List<Map<String, Object>> listResponse = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();
		response.put("key", "someValue");
		listResponse.add(response);

		when(jdbcUtils.queryForList("anyQueryId", new HashMap<>())).thenReturn(listResponse);

		Map<String, Object> validation = pisdR350.executeGetListASingleRow("anyQueryId", new HashMap<>());

		assertNotNull(validation);
	}

	@Test
	public void executeGetListASingleRow_WithNoResultException() {
		LOGGER.info("PISDR350Test - Executing executeGetListASingleRow_WithNoResultException...");

		when(jdbcUtils.queryForList("anyQueryId", new HashMap<>())).thenThrow(new NoResultException("adviceCode", "errorMessage"));

		Map<String, Object> validation = pisdR350.executeGetListASingleRow("anyQueryId", new HashMap<>());

		assertNull(validation);
	}

	private Map<String, Object> PrepareParamsToContract(){
		Map<String, Object> params = new HashMap<>();
		// set values
		params.put("SETTLE_PENDING_PREMIUM_AMOUNT","2500");
		params.put("CONTRACT_REGIS_CST_AGE_NUMBER","2");
		params.put("ISSUED_RECEIPT_NUMBER","02");
		params.put("PERIOD_NEXT_PAYMENT_DATE","2021-04-20");
		params.put("NET_PREM_PLCY_ORIG_CURR_AMOUNT","750");
		params.put("ACCUM_OUTSTANDING_DEBT_AMOUNT","150");
		params.put("PREV_PEND_BILL_RCPTS_NUMBER","850");

		// Where condition
		params.put("INSURANCE_CONTRACT_ENTITY_ID","0011");
		params.put("INSURANCE_CONTRACT_BRANCH_ID","0142");
		params.put("INSRC_CONTRACT_INT_ACCOUNT_ID","4000317312");

		return params;
	}

	private Map<String, Object> PrepareParamsSelectConditions() {
		Map<String, Object> params = new HashMap<>();
		params.put("PERIOD_NEXT_PAYMENT_DATE","ICTD");

		return params;
	}

	@Test
	public void executeQuerySelectTest(){
		LOGGER.info("Executing the executeQuerySelectTest...");
		OperationDTO opSelect = new OperationDTO();
		opSelect.setQuery("cnxPISD;select SETTLE_PENDING_PREMIUM_AMOUNT FROM T_PISD_INSURANCE_CONTRACT");
		opSelect.setTypeOperation(PISDConstant.Operation.SELECT);
		opSelect.setParams(PrepareParamsSelectConditions());
		opSelect.setForListQuery(false);

		Map<String, Object> resultExpected = new HashMap<>();
		resultExpected.put("INSURANCE_CONTRACT_ENTITY_ID","0011");
		resultExpected.put("INSURANCE_CONTRACT_BRANCH_ID","0486");
		resultExpected.put("INSRC_CONTRACT_INT_ACCOUNT_ID","1222");
		resultExpected.put("SETTLE_PENDING_VAR_PREM_AMOUNT","2500");

		when(jdbcUtils.queryForMap(Mockito.anyString(), Mockito.anyMap())).thenReturn(resultExpected);

		Object resultObject = pisdR350.executeQuery(opSelect);
		Map<String, Object> result = (Map<String, Object>) resultObject;
		Assert.assertEquals(resultExpected, resultObject);
		Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
				.queryForMap(Mockito.anyString(),
						Mockito.anyMap());
	}

	@Test
	public void executeQueryUpdateTest(){
		LOGGER.info("Executing the executeQueryUpdateTest...");
		OperationDTO opUpdated = new OperationDTO();
		opUpdated.setQuery("cnxPISD;UPDATE T_PISD_INSURANCE_CONTRACT " +
				" SET SETTLE_PENDING_PREMIUM_AMOUNT= :SETTLE_PENDING_PREMIUM_AMOUNT " +
				" WHERE INSURANCE_CONTRACT_ENTITY_ID= :INSURANCE_CONTRACT_ENTITY_ID " +
				"  AND INSURANCE_CONTRACT_BRANCH_ID= :INSURANCE_CONTRACT_BRANCH_ID " +
				"  AND INSRC_CONTRACT_INT_ACCOUNT_ID= :INSRC_CONTRACT_INT_ACCOUNT_ID ");
		opUpdated.setTypeOperation(PISDConstant.Operation.UPDATE);
		opUpdated.setParams(PrepareParamsToContract());

		when(jdbcUtils.update(Mockito.anyString(), Mockito.anyMap())).thenReturn(1);

		Object resultObject = pisdR350.executeQuery(opUpdated);

		int result = (int) resultObject;

		Assert.assertEquals(1, result);
		Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
				.update(Mockito.anyString(),
						Mockito.anyMap());
	}

	@Test
	public void executeQueryForListOKTest(){
		LOGGER.info("Executing the executeQueryForListOKTest...");
		OperationDTO opSelect = new OperationDTO();
		opSelect.setQuery("cnxPISD;select SETTLE_PENDING_PREMIUM_AMOUNT FROM T_PISD_INSURANCE_CONTRACT");
		opSelect.setTypeOperation(PISDConstant.Operation.SELECT);
		opSelect.setParams(PrepareParamsSelectConditions());
		opSelect.setForListQuery(true);

		Map<String, Object> resultExpected = new HashMap<>();
		resultExpected.put("INSURANCE_CONTRACT_ENTITY_ID","0011");
		resultExpected.put("INSURANCE_CONTRACT_BRANCH_ID","0486");
		resultExpected.put("INSRC_CONTRACT_INT_ACCOUNT_ID","1222");
		resultExpected.put("SETTLE_PENDING_VAR_PREM_AMOUNT","2500");

		Map<String, Object> resultExpected01 = new HashMap<>();
		resultExpected01.put("INSURANCE_CONTRACT_ENTITY_ID","0011");
		resultExpected01.put("INSURANCE_CONTRACT_BRANCH_ID","0242");
		resultExpected01.put("INSRC_CONTRACT_INT_ACCOUNT_ID","111");
		resultExpected01.put("SETTLE_PENDING_VAR_PREM_AMOUNT","4588");

		List<Map<String,Object>> responseExpected = new ArrayList<>();
		responseExpected.add(resultExpected);
		responseExpected.add(resultExpected01);
		when(jdbcUtils.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(responseExpected);

		Object resultObject= pisdR350.executeQuery(opSelect);
		List<Map<String,Object>> result = (List<Map<String,Object>>) resultObject;

		Assert.assertNotNull( resultObject);

		Assert.assertEquals(2,result.size() );
		Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
				.queryForList(Mockito.anyString(),
						Mockito.anyMap());

	}


	@Test
	public void executeQueryNoResultExceptionTest(){
		LOGGER.info("Executing the executeQueryUpdateTest...");
		OperationDTO opUpdated = new OperationDTO();
		opUpdated.setTable("UPDATE T_PISD_INSURANCE_CONTRACT");
		opUpdated.setTypeOperation(PISDConstant.Operation.SELECT);
		opUpdated.setParams(PrepareParamsToContract());
		opUpdated.setForListQuery(false);

		when(jdbcUtils.queryForMap(Mockito.anyString(), Mockito.anyMap())).thenThrow(NoResultException.class);

		Object resultObject = null;

		try {
			resultObject= pisdR350.executeQuery(opUpdated);
		}catch (NoResultException e){
			Assert.assertEquals(null, resultObject);
			Assert.assertEquals(PISDErrors.ERROR_DUPLICATE_KEY.getAdviceCode(), this.context.getAdviceList().get(0).getCode());
			Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
					.queryForMap(Mockito.anyString(),
							Mockito.anyMap());
		}

	}

	@Test
	public void executeQuerySelectTimeoutExceptionTest(){
		LOGGER.info("Executing the executeQuerySelectAPXExceptionTest...");
		OperationDTO opSelect = new OperationDTO();
		opSelect.setQuery("cnxPISD;select SETTLE_PENDING_PREMIUM_AMOUNT FROM T_PISD_INSURANCE_CONTRACT");
		opSelect.setTypeOperation(PISDConstant.Operation.SELECT);
		opSelect.setParams(PrepareParamsSelectConditions());

		when(jdbcUtils.queryForMap(Mockito.anyString(), Mockito.anyMap())).thenThrow(TimeoutException.class);

		Object resultObject = null;

		try {
			resultObject= pisdR350.executeQuery(opSelect);
			Assert.fail();
		}catch (BusinessException e){
			Assert.assertEquals(null, resultObject);
			Assert.assertEquals(PISDErrors.ERROR_TIME_OUT.getAdviceCode(), e.getAdviceCode());
			Assert.assertEquals(PISDErrors.ERROR_TIME_OUT.getAdviceCode(), this.context.getAdviceList().get(0).getCode());
			Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
					.queryForMap(Mockito.anyString(),
							Mockito.anyMap());
		}
	}


	@Test
	public void executeQueryDuplicateKeyExceptionTest(){
		LOGGER.info("Executing the executeQueryDuplicateKeyExceptionTest...");
		OperationDTO opSelect = new OperationDTO();
		opSelect.setTypeOperation(PISDConstant.Operation.SELECT);
		opSelect.setParams(PrepareParamsSelectConditions());
		opSelect.setForListQuery(false);

		when(jdbcUtils.queryForMap(Mockito.anyString(), Mockito.anyMap())).thenThrow(DuplicateKeyException.class);

		Object resultObject = null;

		try {
			resultObject= pisdR350.executeQuery(opSelect);
			Assert.fail();
		}catch (BusinessException e){
			Assert.assertEquals(null, resultObject);
			Assert.assertEquals(PISDErrors.ERROR_DUPLICATE_KEY.getAdviceCode(), e.getAdviceCode());
			Assert.assertEquals(PISDErrors.ERROR_DUPLICATE_KEY.getAdviceCode(), this.context.getAdviceList().get(0).getCode());
			Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
					.queryForMap(Mockito.anyString(),
							Mockito.anyMap());
		}
	}

	@Test
	public void executeQueryDataIntegrityViolationExceptionTest(){
		LOGGER.info("Executing the executeQueryDataIntegrityViolationExceptionTest...");
		OperationDTO opSelect = new OperationDTO();
		opSelect.setTypeOperation(PISDConstant.Operation.UPDATE);
		opSelect.setQuery("cnxPISD;UPDATE T_PISD_INSURANCE_CONTRACT " +
				" SET SETTLE_PENDING_PREMIUM_AMOUNT= :SETTLE_PENDING_PREMIUM_AMOUNT " +
				" WHERE INSURANCE_CONTRACT_ENTITY_ID= :INSURANCE_CONTRACT_ENTITY_ID " +
				"  AND INSURANCE_CONTRACT_BRANCH_ID= :INSURANCE_CONTRACT_BRANCH_ID " +
				"  AND INSRC_CONTRACT_INT_ACCOUNT_ID= :INSRC_CONTRACT_INT_ACCOUNT_ID ");
		opSelect.setParams(PrepareParamsSelectConditions());

		when(jdbcUtils.update(Mockito.anyString(), Mockito.anyMap())).thenThrow(DataIntegrityViolationException.class);

		Object resultObject = null;

		try {
			resultObject= pisdR350.executeQuery(opSelect);
			Assert.fail();
		}catch (BusinessException e){
			Assert.assertEquals(null, resultObject);
			Assert.assertEquals(PISDErrors.ERROR_INTEGRITY_VIOLATION.getAdviceCode(), e.getAdviceCode());
			Assert.assertEquals(PISDErrors.ERROR_INTEGRITY_VIOLATION.getAdviceCode(), this.context.getAdviceList().get(0).getCode());
			Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
					.update(Mockito.anyString(),
							Mockito.anyMap());
		}
	}

	@Test
	public void executeQueryIncorrectResultSizeExceptionTest(){
		LOGGER.info("Executing the executeQueryIncorrectResultSizeExceptionTest...");
		OperationDTO opSelect = new OperationDTO();
		opSelect.setTypeOperation(PISDConstant.Operation.SELECT);
		opSelect.setParams(PrepareParamsSelectConditions());
		opSelect.setForListQuery(false);
		when(jdbcUtils.queryForMap(Mockito.anyString(), Mockito.anyMap())).thenThrow(IncorrectResultSizeException.class);

		Object resultObject = null;

		try {
			resultObject= pisdR350.executeQuery(opSelect);
			Assert.fail();
		}catch (BusinessException e){
			Assert.assertEquals(null, resultObject);
			Assert.assertEquals(PISDErrors.ERROR_INCORRECT_RESULT.getAdviceCode(), e.getAdviceCode());
			Assert.assertEquals(PISDErrors.ERROR_INCORRECT_RESULT.getAdviceCode(), this.context.getAdviceList().get(0).getCode());
			Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
					.queryForMap(Mockito.anyString(),
							Mockito.anyMap());
		}
	}

	@Test
	public void executeBatchQueryTest(){
		LOGGER.info("Executing the executeBatchQueryTest...");
		OperationDTO opSelect = new OperationDTO();
		opSelect.setNameProp("UPDATED_CONTRACT");
		opSelect.setTypeOperation(PISDConstant.Operation.BATCH);
		Map<String, Object> [] params = new Map[]{ PrepareParamsToContract(), PrepareParamsToContract()};
		opSelect.setBatchValues(params);

		int [] resultExpected = new int[2];
		when(jdbcUtils.batchUpdate(Mockito.anyString(), Mockito.any(params.getClass()))).thenReturn(resultExpected);

		Object resultObject = pisdR350.executeQuery(opSelect);

		int [] result = (int []) resultObject;
		Assert.assertEquals(result.length, resultExpected.length);
		Mockito.verify(jdbcUtils, Mockito.atLeastOnce())
				.batchUpdate(Mockito.anyString(),
						Mockito.any(params.getClass()));
	}

	
}
