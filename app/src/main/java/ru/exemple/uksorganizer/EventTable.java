package ru.exemple.uksorganizer;

public class EventTable {

    public static final String TABLE = "events";

    public static class COLUMN {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CATEGORY = "category";
        public static final String DESCRIPTION = "description";
    }

    public static final String CREATE_SCRIPT =
            String.format("create table %s ("
                            + "%s integer primary key autoincrement,"
                            + "%s text,"
                            + "%s text" + ");",
                    TABLE, COLUMN.ID, COLUMN.NAME, COLUMN.CATEGORY, COLUMN.DESCRIPTION);
}
