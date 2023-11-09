package com.example.e_backend.service;

import com.example.e_backend.Model.Category;
import com.example.e_backend.Model.Product;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.repository.CategoryRepository;
import com.example.e_backend.repository.ProductRepository;
import com.example.e_backend.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService{

    private ProductRepository productRepository;
    private UserService userService;
    private CategoryRepository categoryRepository;

    public ProductServiceImplementation(ProductRepository productRepository,
                                        UserService userService,
                                        CategoryRepository categoryRepository){
        this.productRepository= productRepository;
        this.userService=userService;
        this.categoryRepository=categoryRepository;
    }

    @Override
    public Product createProduct(CreateProductRequest req) {
        Category topLevel = categoryRepository.findByName(req.getTopLavelCategory());
        if (topLevel==null)
        {
            Category topLavelCategory = new Category();
            topLavelCategory.setName(req.getTopLavelCategory());
            topLavelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLavelCategory);

        }

        Category secondLevel=categoryRepository.findByNameAndParant(req.getSecondLavelCategory(),topLevel.getName());
        if (secondLevel==null){
            Category secondLevelCategory=new Category();
            secondLevelCategory.setName(req.getSecondLavelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel= categoryRepository.findByNameAndParant(req.getThirdLavelCategory(),secondLevel.getName());
        if (thirdLevel==null)
        {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLavelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);

            thirdLevel= categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPersent(req.getDiscountPersent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSize());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreateAt(LocalDateTime.now());

        Product saveProduct =productRepository.save(product);


        return saveProduct;
    }

    @Override
    public String deleteProduct(Long productld) throws ProductException {

        Product product=findProductById(productld);
        product.getSizes().clear();

        productRepository.delete(product);
        return "Product deleted Successfully";
    }

    @Override
    public Product updateProduct(Long productld, Product req) throws ProductException {

        Product product=findProductById(productld);

        if (req.getQuantity()!=0){
            product.setQuantity(req.getQuantity());
        }



        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt= productRepository.findById(id);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new ProductException("Product not found with id"+id);
//        return null;
    }

    @Override
    public List<Product> findProductByCategory(String category) {

        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        List<Product> products=productRepository.filterProducts(category,minPrice,maxPrice,minDiscount,sort);
        if (!colors.isEmpty()){
            products=products.stream().filter(p->colors.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }

        if (stock!=null){
            if(stock.equals("in_stock")){
                products=products.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
            }
            else if (stock.equals("out_of_stock")){
                products=products.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());
            }

        }
        int stratIndex =(int )pageable.getOffset();
        int endIndex = Math.min(stratIndex+pageable.getPageSize(),products.size());

        List<Product> pageContent= products.subList(stratIndex,endIndex);

        Page<Product> filteredProduct= new PageImpl<>(pageContent,pageable,products.size());

        return filteredProduct;
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }
}
