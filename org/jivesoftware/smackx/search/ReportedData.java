package org.jivesoftware.smackx.search;

import android.support.v7.appcompat.BuildConfig;
import java.util.ArrayList;
import java.util.List;

public final class ReportedData {
    List<Column> columns;
    List<Row> rows;
    private String title;

    public static class Column {
        private String label;
        private String type;
        String variable;

        public Column(String label, String variable, String type) {
            this.label = label;
            this.variable = variable;
            this.type = type;
        }
    }

    public static class Field {
        private List<String> values;
        private String variable;

        public Field(String variable, List<String> values) {
            this.variable = variable;
            this.values = values;
        }
    }

    public static class Row {
        private List<Field> fields;

        public Row(List<Field> fields) {
            this.fields = new ArrayList();
            this.fields = fields;
        }
    }

    public ReportedData() {
        this.columns = new ArrayList();
        this.rows = new ArrayList();
        this.title = BuildConfig.VERSION_NAME;
    }

    public final void addColumn(Column column) {
        this.columns.add(column);
    }
}
