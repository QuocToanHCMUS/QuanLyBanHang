/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ql.model.CenterManager;
import ql.model.Staff;
import ql.model.Store;
import ql.model.StoreManager;
import ql.model.User;

/**
 *
 * @author ADMIN
 */
public class UserDao extends BaseDao {

    public User getUserByUsername(String username) {
        Connection connection = null;
        PreparedStatement ps = null;
        User user = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT a.*, c.*, d.*, e.*, f.*, g.id as storeId, g.name as storeName, g.address as storeAddress FROM users a ");
            sql.append("INNER JOIN user_role b ON a.id = b.user_id ");
            sql.append("INNER JOIN role c ON b.role_id = c.id ");
            sql.append("INNER JOIN store g ON a.store_id = g.id ");
            sql.append("LEFT JOIN center_manager d ON a.id = d.user_id ");
            sql.append("LEFT JOIN store_manager e ON a.id = e.user_id ");
            sql.append("LEFT JOIN staff f ON a.id = f.user_id ");
            sql.append("WHERE a.username = ? ");
            ps = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                List<String> roleList = new ArrayList<>();
                while(rs.next()) {
                    String role = rs.getString("role");
                    roleList.add(role);
                    if (rs.isLast()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        Date birthday = rs.getDate("birthday");
                        String address = rs.getString("address");
                        String loginName = rs.getString("username");
                        String password = rs.getString("password");
                        String storeId = rs.getString("storeId");
                        String storeName = rs.getString("storeName");
                        String storeAddress = rs.getString("storeAddress");
                        Store store = new Store(storeId, storeName, storeAddress);
                        if (roleList.contains("QUANLY_TRUNGTAM")) {
                            user = new CenterManager(id, name, birthday, address, loginName, password, store);
                        } else if (roleList.contains("QUANLY_CUAHANG") && roleList.contains("NHAN_VIEN")) {
                            user = new StoreManager(id, name, birthday, address, loginName, password, store);
                        } else if (roleList.contains("NHAN_VIEN")) {
                            String staffNo = rs.getString("staff_no");
                            int rank = rs.getInt("rank");
                            user = new Staff(staffNo, rank, id, name, birthday, address, loginName, password, store);
                        }
                        user.setRoleList(roleList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return user;
    }
    
    public List<User> getAllStaff(String storeId, String name) {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT f.*, a.*, g.id as storeId, g.name as storeName, g.address as storeAddress FROM users a ");
            sql.append("INNER JOIN user_role b ON a.id = b.user_id ");
            sql.append("INNER JOIN store g ON a.store_id = g.id ");
            sql.append("INNER JOIN staff f ON a.id = f.user_id ");
            sql.append("WHERE g.id = ? ");
            if (name != null && name != "") {
                sql.append("AND a.name LIKE ?");
            }
            ps = connection.prepareStatement(sql.toString());
            int index = 1;
            ps.setString(index++, storeId);
            if (name != null && name != "") {
                ps.setString(index++, "%" + name + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while(rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("name");
                    Date birthday = rs.getDate("birthday");
                    String address = rs.getString("address");
                    String loginName = rs.getString("username");
                    String password = rs.getString("password");
                    
                    String stId = rs.getString("storeId");
                    String storeName = rs.getString("storeName");
                    String storeAddress = rs.getString("storeAddress");
                    Store store = new Store(storeId, storeName, storeAddress);
                    
                    String staffNo = rs.getString("staff_no");
                    int rank = rs.getInt("rank");
                    
                    User user = new Staff(staffNo, rank, id, username, birthday, address, loginName, password, store);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return users;
    }
    
    public String getLastStaffId() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT TOP 1 staff_no FROM staff ORDER BY CAST(STUFF(staff_no, 1, 2, '') AS int) DESC ");
            ps = connection.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("staff_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return null;
    }

    public boolean add(User user) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            StringBuilder sql = new StringBuilder("INSERT INTO users (store_id, name, birthday, address, username, password) VALUES (?, ?, ?, ?, ?, ?)");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, user.getStore().getId());
            ps.setString(2, user.getName());
            ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getUsername());
            ps.setString(6, user.getPassword());
            ps.executeUpdate();
            
            sql = new StringBuilder("SELECT TOP 1 id FROM users ORDER BY id DESC ");
            ps = connection.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            int nextUserId = 0;
            if (rs.next()) {
                nextUserId = rs.getInt("id");
            }
            if (nextUserId > 0) {
                Staff staff = (Staff) user;
                sql = new StringBuilder("INSERT INTO staff(user_id, staff_no, rank) VALUES(?, ?, ?)");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, nextUserId);
                ps.setString(2, staff.getStaffNo());
                ps.setInt(3, staff.getRank());
                ps.executeUpdate(); 
                
                sql = new StringBuilder("INSERT INTO user_role(role_id, user_id) VALUES(?, ?)");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, 3);
                ps.setInt(2, nextUserId);
                ps.executeUpdate();
                
                connection.commit();
                return true;
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    public boolean update(User user) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            Staff staff = (Staff) user;
            
            StringBuilder sql = new StringBuilder("SELECT a.id FROM users a INNER JOIN staff b ON a.id = b.user_id WHERE b.staff_no = ? ");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, staff.getStaffNo());
            ResultSet rs = ps.executeQuery();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt("id");
            }
            if (userId > 0) {
                sql = new StringBuilder("UPDATE users SET name = ?, birthday = ?, address = ? WHERE id = ? AND store_id = ? ");
                ps = connection.prepareStatement(sql.toString());
                ps.setString(1, staff.getName());
                ps.setDate(2, new java.sql.Date(staff.getBirthday().getTime()));
                ps.setString(3, staff.getAddress());
                ps.setInt(4, userId);
                ps.setString(5, staff.getStore().getId());
                ps.executeUpdate();

                sql = new StringBuilder("UPDATE staff SET rank = ? WHERE user_id = ? AND staff_no = ? ");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, staff.getRank());
                ps.setInt(2, userId);
                ps.setString(3, staff.getStaffNo());
                ps.executeUpdate();

                connection.commit();
                return true;
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    public boolean remove(String staffNo) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            
            StringBuilder sql = new StringBuilder("SELECT a.id FROM users a INNER JOIN staff b ON a.id = b.user_id WHERE b.staff_no = ? ");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, staffNo);
            ResultSet rs = ps.executeQuery();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt("id");
            }
            if (userId > 0) {
                sql = new StringBuilder("DELETE FROM staff WHERE user_id = ? AND staff_no = ? ");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, userId);
                ps.setString(2, staffNo);
                ps.executeUpdate();
                
                sql = new StringBuilder("DELETE FROM user_role WHERE user_id = ?");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, userId);
                ps.executeUpdate();
                
                sql = new StringBuilder("DELETE FROM users WHERE id = ?");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, userId);
                ps.executeUpdate();
                
                connection.commit();
                return true;
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }
    
    public List<User> getAllStoreManagerBy(String storeId, String name) {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT a.name, a.birthday, a.address, a.username, c.name as storeName FROM users a ");
            sql.append("INNER JOIN store_manager b ON a.id = b.user_id ");
            sql.append("INNER JOIN store c ON a.store_id = c.id ");
            sql.append("INNER JOIN user_role d ON d.user_id = a.id ");
            sql.append("WHERE 1 = 1 ");
            if (storeId != null && storeId != "") {
                sql.append("AND c.id = ? ");
            }
            if (name != null && name != "") {
                sql.append("AND a.name LIKE ? ");
            }
            sql.append("GROUP BY a.name, a.birthday, a.address, a.username, c.name ");
            ps = connection.prepareStatement(sql.toString());
            int index = 1;
            if (storeId != null && storeId != "") {
                ps.setString(index++, storeId);
            }
            if (name != null && name != "") {
                ps.setString(index++, "%" + name + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while(rs.next()) {
                    String username = rs.getString("name");
                    Date birthday = rs.getDate("birthday");
                    String address = rs.getString("address");
                    String loginName = rs.getString("username");
                    
                    String storeName = rs.getString("storeName");
                    Store store = new Store();
                    store.setName(storeName);
                    User user = new StoreManager(username, birthday, address, loginName, store);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return users;
    }

    public boolean addStoreManager(User user) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            StringBuilder sql = new StringBuilder("INSERT INTO users (store_id, name, birthday, address, username, password) VALUES (?, ?, ?, ?, ?, ?)");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, user.getStore().getId());
            ps.setString(2, user.getName());
            ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getUsername());
            ps.setString(6, user.getPassword());
            ps.executeUpdate();
            
            sql = new StringBuilder("SELECT TOP 1 id FROM users ORDER BY id DESC ");
            ps = connection.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            int nextUserId = 0;
            if (rs.next()) {
                nextUserId = rs.getInt("id");
            }
            if (nextUserId > 0) {
                StoreManager storeManager = (StoreManager) user;
                sql = new StringBuilder("INSERT INTO store_manager(user_id) VALUES(?)");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, nextUserId);
                ps.executeUpdate(); 
                
                sql = new StringBuilder("INSERT INTO user_role(role_id, user_id) VALUES(?, ?)");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, 2);
                ps.setInt(2, nextUserId);
                ps.executeUpdate();
                
                connection.commit();
                return true;
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    public boolean updateStoreManager(User user) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            StringBuilder sql = new StringBuilder("UPDATE users SET store_id = ?, name = ?, birthday = ?, address = ? WHERE username = ?");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, user.getStore().getId());
            ps.setString(2, user.getName());
            ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getUsername());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    public boolean removeStoreManager(String username) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            
            StringBuilder sql = new StringBuilder("SELECT a.id FROM users a INNER JOIN store_manager b ON a.id = b.user_id WHERE a.username = ? ");
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt("id");
            }
            if (userId > 0) {
                sql = new StringBuilder("DELETE FROM store_manager WHERE user_id = ?");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, userId);
                ps.executeUpdate();
                
                sql = new StringBuilder("DELETE FROM user_role WHERE user_id = ?");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, userId);
                ps.executeUpdate();
                
                sql = new StringBuilder("DELETE FROM users WHERE id = ?");
                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, userId);
                ps.executeUpdate();
                
                connection.commit();
                return true;
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }
}
