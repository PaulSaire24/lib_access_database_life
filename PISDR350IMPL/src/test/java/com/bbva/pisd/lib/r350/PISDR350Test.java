package com.bbva.pisd.lib.r350;

import com.bbva.apx.exception.db.NoResultException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import javax.annotation.Resource;

import com.bbva.elara.utility.jdbc.JdbcUtils;
import com.bbva.pisd.lib.r350.impl.PISDR350Impl;
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

	@Mock
	private Map<String, Object> insertSingleRowArguments;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ThreadContext.set(new Context());

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

	
}
