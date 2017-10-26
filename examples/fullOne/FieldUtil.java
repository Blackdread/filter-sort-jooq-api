package fullOne;

import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public final class FieldUtil {

    private FieldUtil(){}

    /**
     * TODO Check if jOOQ has similar feature in DSL class
     * @param field
     * @param format
     * @return
     */
    public static Field<String> dateFormat(Field<?> field, String format) {
        return DSL.field("date_format({0}, {1})", SQLDataType.VARCHAR,
            field, DSL.inline(format));
    }

}
