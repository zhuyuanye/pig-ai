package com.pig4cloud.pig.monitor.config;

/**
 * 数据碎片常量类
 */
public class FragmentSQLConstants {

    /**
     * MySQL 查询表碎片 SQL
     */
    public static final String MYSQL_FRAGMENT_SQL =
            "SELECT\n" +
                "    table_schema AS schemaName,\n" +
                "    table_name AS tableName,\n" +
                "    data_free AS fragmentBytes,\n" +
                "    ROUND(\n" +
                "        (data_free / NULLIF(data_length + index_length + data_free, 0)) * 100,\n" +
                "        2\n" +
                "    ) AS fragmentPercent\n" +
                "FROM information_schema.tables\n" +
                "WHERE data_free > 0\n" +
                "  AND table_schema NOT IN ('information_schema', 'performance_schema', 'mysql', 'sys')\n" +
                "ORDER BY fragmentBytes DESC LIMIT ";

    /**
     * PostgreSQL 查询表碎片 SQL
     */
    public static final String PGSQL_FRAGMENT_SQL =
            "SELECT\n" +
                    "    schemaname AS schemaName,\n" +
                    "    relname AS tableName,\n" +
                    "    ROUND(100 * dead_tuple_len / table_len, 2) AS fragmentPercent,\n" +
                    "    dead_tuple_len AS fragmentBytes\n" +
                    "FROM (\n" +
                    "    SELECT\n" +
                    "        schemaname,\n" +
                    "        relname,\n" +
                    "        (pgstattuple(schemaname || '.' || relname)).table_len AS table_len,\n" +
                    "        (pgstattuple(schemaname || '.' || relname)).dead_tuple_len AS dead_tuple_len\n" +
                    "    FROM pg_stat_user_tables\n" +
                    ") t\n" +
                    "WHERE table_len > 0\n" +
                    "ORDER BY fragmentPercent DESC\n" +
                    "LIMIT";

    /**
     * 瀚高数据库 查询表碎片 SQL
     */
    public static final String HGDB_FRAGMENT_SQL =
            "SELECT\n" +
                    "    schemaname AS schemaName,\n" +
                    "    relname AS tableName,\n" +
                    "    ROUND(100 * dead_tuple_len / table_len, 2) AS fragmentPercent,\n" +
                    "    dead_tuple_len AS fragmentBytes\n" +
                    "FROM (\n" +
                    "    SELECT\n" +
                    "        schemaname,\n" +
                    "        relname,\n" +
                    "        (pgstattuple(schemaname || '.' || relname)).table_len AS table_len,\n" +
                    "        (pgstattuple(schemaname || '.' || relname)).dead_tuple_len AS dead_tuple_len\n" +
                    "    FROM pg_stat_user_tables\n" +
                    ") t\n" +
                    "WHERE table_len > 0\n" +
                    "ORDER BY fragmentPercent DESC\n" +
                    "LIMIT";

    private FragmentSQLConstants() {
        // 禁止实例化
    }
}
