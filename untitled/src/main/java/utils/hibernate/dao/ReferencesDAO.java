package utils.hibernate.dao;

import entity.references.ReferenceItem;

import java.sql.Timestamp;
import java.util.List;

public interface ReferencesDAO {
    int[] getAllId();
    int[] getIdAfter(Timestamp t);
}
