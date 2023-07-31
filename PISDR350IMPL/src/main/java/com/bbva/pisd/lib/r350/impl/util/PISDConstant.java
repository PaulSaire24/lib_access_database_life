package com.bbva.pisd.lib.r350.impl.util;

public class PISDConstant {
    public PISDConstant() {
    }

    public final class Operation {
        public static final String UPDATE = "UPDATE";
        public static final String SELECT = "SELECT";
        public static final String BATCH = "BATCH";

        private Operation() {
        }
    }

    public final class Tables {
        public static final String TABLE_T_PISD_INSURANCE_CTR_RECEIPTS = "T_PISD_INSURANCE_CTR_RECEIPTS";
        public static final String TABLE_T_PISD_INSURANCE_CONTRACT = "T_PISD_INSURANCE_CONTRACT";

        private Tables() {
        }
    }
}
