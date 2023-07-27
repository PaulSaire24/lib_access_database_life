package com.bbva.pisd.lib.r350.impl.util;

public enum PISDErrors {

    QUERY_EMPTY_RESULT("PISD20100001", false, "No hay resultados para la consulta."),
    ERROR_QUERY_FILTERS("PISD20100002", false, "No se enviaron correctamente los valores de los filtros."),
    ERROR_TO_CONNECT_DB("PISD20100003", false, "Error al consultar a la base de datos."),
    ERROR_DUPLICATE_KEY("PISD20100004", false, "Error al intentar duplicar la información con llaves únicas."),
    ERROR_INCORRECT_RESULT("PISD20100005", false, "Error en el tamaño del resultado."),
    ERROR_TIME_OUT("PISD20100006", false, "Error en el tiempo de espera."),
    ERROR_INTEGRITY_VIOLATION("PISD20100007", false, "Error por infracción de la integridad de los datos.");

    private final String adviceCode;
    private final boolean rollback;
    private final String message;

    PISDErrors(String adviceCode, boolean rollback, String message) {
        this.adviceCode = adviceCode;
        this.rollback = rollback;
        this.message = message;
    }

    public String getAdviceCode() { return adviceCode; }
    public boolean isRollback() { return rollback; }
    public String getMessage() { return message; }
}
