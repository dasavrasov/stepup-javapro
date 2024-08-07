package ru.stepup.service;

import org.springframework.stereotype.Service;
import ru.stepup.dao.ProductDao;
import ru.stepup.entity.Product;

import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(ProductDao userDao) {
        this.productDao = userDao;
    }

    public Product save(Product user) {
        return productDao.save(user);
    }

    public void deleteById(Long id) {
        productDao.deleteById(id);
    }

    public Product findById(Long id) {
        return productDao.findById(id);
    }
    public List<Product> findByUserId(Long userId) {
        return productDao.findByUserId(userId);
    }
}
