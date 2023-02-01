package lk.ijse.asms.dao;

import lk.ijse.asms.entity.SuperEntity;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO <T,pk> {
    String getNextId() throws SQLException, ClassNotFoundException;
    boolean save(T entity) throws SQLException, ClassNotFoundException;
    boolean update(T entity) throws SQLException, ClassNotFoundException;
    ArrayList<T> getAll() throws SQLException, ClassNotFoundException;
    T geByPk(pk pk)throws SQLException, ClassNotFoundException;
}
