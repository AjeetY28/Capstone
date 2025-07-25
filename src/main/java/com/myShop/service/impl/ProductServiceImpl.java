package com.myShop.service.impl;

import com.myShop.entity.Category;
import com.myShop.entity.Product;
import com.myShop.entity.Seller;
import com.myShop.exceptions.ProductException;
import com.myShop.repository.CategoryRepository;
import com.myShop.repository.ProductRepository;
import com.myShop.request.CreateProductRequest;
import com.myShop.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public Product createProduct(CreateProductRequest req,
                                 Seller seller) {

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Category category1=categoryRepository.findByCategoryId(req.getCategory());
        if (category1 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(1);
            category.setName(req.getCategory().replace("_"," "));
            category1=categoryRepository.save(category);
        }
        Category category2=categoryRepository.findByCategoryId(req.getCategory2());
        if (category2 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category.setName(req.getCategory2().replace("_"," "));
            category2=categoryRepository.save(category);
        }
        Category category3=categoryRepository.findByCategoryId(req.getCategory3());
        if (category3 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category.setName(req.getCategory2().replace("_"," "));
            category3=categoryRepository.save(category);
        }

//        int discountPercentage= calculateDiscountPercentage(req.getMrpPrice(),req.getSellingPrice());


        Product product = new Product();

        product.setSeller(seller);
        product.setCategory(category3);
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountPercent(discountPercentage);



        return productRepository.save(product);

    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
       if (mrpPrice<=0)
       {
           throw new IllegalArgumentException("Actual price must be greater then zero");
       }
       double discount=mrpPrice-sellingPrice;
       double discountPercentage=(discount/mrpPrice)*100;
       return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product=findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        this.findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product updateProductStock(Long productId) throws ProductException {
        Product product = this.findProductById(productId);
        product.setIn_stock(!product.isIn_stock());
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(()->
                new ProductException("Product not found with the id....."+productId));

    }

    @Override
    public List<Product> searchProduct(String query) {

        return productRepository.searchProduct(query);

    }

    @Override
    public Page<Product> getAllProduct(String category,
                                       String brand,
                                       String color,
                                       String sizes,
                                       Integer minPrice,
                                       Integer maxPrice,
                                       Integer minDiscount,
                                       String sort,
                                       String stock,
                                       Integer pageNumber) {
        Specification<Product> spec=(root, query, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();

            if(category!=null)
            {
                Join<Product,Category> categoryJoin= root.join("category");
                Predicate categoryPredicate = criteriaBuilder.or(
                        criteriaBuilder.equal(categoryJoin.get("categoryId"), category),  // Match categoryId
                        criteriaBuilder.equal(categoryJoin.get("parentCategory").get("categoryId"), category)  // Match parentCategory.categoryId
                );
                predicates.add(categoryPredicate);
            }

            if(color!=null && !color.isEmpty())
            {
//                System.out.println("Color "+colors);
                predicates.add(criteriaBuilder.equal(root.get("color"),color));
            }
            if(sizes != null && !sizes.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("sizes"),sizes));
            }
            if(minPrice !=null)
            {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"),minPrice));
            }
            if(maxPrice!=null)
            {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"),maxPrice));
            }
            if(minDiscount!=null)
            {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"),minDiscount));
            }

            if(stock!=null)
            {
                predicates.add(criteriaBuilder.equal(root.get("stock"),stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        if(sort!=null && !sort.isEmpty())
        {
            pageable = switch (sort) {
                case "price_low" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };
        }else {
            pageable=PageRequest.of(pageNumber!=null?pageNumber:0,10,Sort.unsorted());
        }

        return productRepository.findAll(spec,pageable);
    }

    @Override
    public List<Product> recentlyAddedProduct() {
        return List.of();
    }


    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {

        return productRepository.findBySellerId(sellerId);
    }
}
