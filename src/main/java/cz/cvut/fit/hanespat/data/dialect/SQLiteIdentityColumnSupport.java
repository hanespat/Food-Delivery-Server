package cz.cvut.fit.hanespat.data.dialect;

import org.hibernate.MappingException;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

/**
 * We need to tell Hibernate how SQLite handles @Id columns
 * which we can do with a custom IdentityColumnSupport implementation.
 * https://www.baeldung.com/spring-boot-sqlite
 */
public class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {
    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    @Override
    public String getIdentitySelectString(String table, String column, int type) throws MappingException {
        return "select last_insert_rowid()";
    }

    @Override
    public String getIdentityColumnString(int type) throws MappingException {
        return "integer";
    }
}
