package ru.stepup.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.stepup.entity.Product;
import ru.stepup.entity.ProductType;
import ru.stepup.entity.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Product save(Product product) {
        String sql = "INSERT INTO products (account,balance,product_type,user_id) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, product.getAccount(), product.getBalance(), product.getProductType(), product.getUserId());
        return product;
    }

    public Product findById(Long id) {
//    try {
        String sql = "SELECT * FROM products WHERE id = ?";
        Product product= jdbcTemplate.queryForObject(sql, new Object[]{id}, new ProductRowMapper());
        return product;
//    } catch (InvocationTargetException var4) {
//        Throwable cause = var4.getCause();
//        System.out.println("Cause of InvocationTargetException: " + cause);
//        return null;
//    }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    public List<Product> findByUserId(Long userId) {
        String sql = "SELECT * FROM products WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new ProductRowMapper());
    }

    class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setAccount(rs.getString("account"));
            product.setBalance(rs.getBigDecimal("balance"));
            ProductType productType= ProductType.getByCode(rs.getInt("product_type"));
            product.setProductType(productType);
            UserDao userDao = new UserDao(jdbcTemplate);
            User user = userDao.findById(rs.getLong("user_id"));
            product.setUserId(user);
            return product;
        }
    }
}